import { CommonModule } from '@angular/common';
import { Component, ElementRef, OnDestroy, OnInit, ViewChild, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatListModule } from '@angular/material/list';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Observable, Subscription } from 'rxjs';

import { HeaderComponent } from 'src/app/components/Layout/header/header.component';
import { Chats } from 'src/app/interfaces/Chats';
import { WsChatService } from 'src/app/services/ws-chat.service';
import { ChatService } from 'src/app/services/chat.service';
import { IMessage } from '@stomp/stompjs';

@Component({
  selector: 'app-chats-ws',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatListModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
    HeaderComponent
  ],
  templateUrl: './chats-ws.component.html',
  styleUrls: ['./chats-ws.component.scss']
})
export class ChatsWsComponent implements OnInit, OnDestroy {
   private readonly chatService = inject(ChatService);
  private readonly ws = inject(WsChatService);
  private readonly snack = inject(MatSnackBar);

  @ViewChild('endOfList') endOfList?: ElementRef<HTMLDivElement>;
  items = signal<Chats[]>([]);
  chats$!: Observable<Chats[]>;


  loading = signal(true);
  creating = signal(false);
  connected = signal(false);
 private subs: Subscription[] = [];
  constructor(private fb: FormBuilder) {}

  form = this.fb.group({
    content: ['', [Validators.required, Validators.maxLength(2000)]],
  });
  get contentCtrl() { return this.form.controls.content; }

   ngOnInit() {
    // 1) Historique une seule fois via REST
    const s1 = this.chatService.getAll().subscribe(list => {
      this.items.set(this.sorted(list));
      this.loading.set(false);
    });
    this.subs.push(s1);

    // 2) Connexion + abonnement temps réel
    this.ws.connect();
    this.ws.subscribe('/topic/chats', (msg: IMessage) => {
      try {
        const chat: Chats = JSON.parse(msg.body);
        this.mergeChat(chat);         // ⬅️ MAJ locale sans re-fetch
      } catch {}
    });
  }
   ngOnDestroy() {
    this.subs.forEach(s => s.unsubscribe());
    this.ws.disconnect();
  }

    private mergeChat(incoming: Chats) {
    const current = this.items();
    const idx = current.findIndex(c => c.id === incoming.id);
    const next = idx === -1
      ? [...current, incoming]
      : Object.assign([...current], { [idx]: incoming });
    this.items.set(this.sorted(next));
      this.scrollToBottomSoon();
  }

  private sorted(list: Chats[]) {
    return [...list].sort((a, b) => (a.created_at || '').localeCompare(b.created_at || ''));
  }

  createMessage() {
    const content = (this.form.value.content ?? '').trim();
    if (!content) { this.form.markAllAsTouched(); return; }
    this.creating.set(true);


    this.ws.sendCreate({ content });

    this.creating.set(false);
    this.contentCtrl.reset('');
  
  }

  trackById(_i: number, m: Chats) { return m.id; }

  private scrollToBottomSoon() {
    queueMicrotask(() => this.endOfList?.nativeElement.scrollIntoView({ behavior: 'smooth' }));
  }
}
