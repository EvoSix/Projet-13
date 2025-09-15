import { CommonModule } from '@angular/common';
import { Component, ElementRef, inject, signal, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Observable, switchMap } from 'rxjs';
import { Chats } from 'src/app/interfaces/Chats';
import { ChatService } from 'src/app/services/chat.service';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import { HeaderComponent } from 'src/app/components/Layout/header/header.component';
@Component({
  selector: 'app-chats',
  imports: [   CommonModule,
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
  templateUrl: './chats.component.html',
  styleUrl: './chats.component.scss'
})
export class ChatsComponent {
  private readonly chatService = inject(ChatService);
  private readonly snack = inject(MatSnackBar);
    @ViewChild('endOfList') endOfList?: ElementRef<HTMLDivElement>;
  
     chats$: Observable<Chats[]> = this.chatService.chats$;
     
  loading = signal(true);
  creating = signal(false);
 constructor(private fb: FormBuilder) {
     this.chatService.getAll().subscribe({
      next: () => { this.loading.set(false); this.scrollToBottom(); },
      error: () => { this.loading.set(false); this.snack.open('Impossible de charger les messages.', 'Fermer', { duration: 3000 }); }
    });
  
    
  }
  form = this.fb.group({
  content: ['', [Validators.required, Validators.maxLength(2000)]],
});
get contentCtrl() { return this.form.controls.content; }

  createMessage() {
 const value = (this.form.value.content ?? '').trim();
  if (!value) { this.form.markAllAsTouched(); return; }
  this.creating.set(true);
  this.chatService.createChat({ content: value}).pipe(
    // 🔁 Recharger la liste juste après le POST réussi
    switchMap(() => this.chatService.getAll())
  ).subscribe({
    next: () => { this.creating.set(false); this.contentCtrl.reset(''); this.scrollToBottom(); },
    error: () => { this.creating.set(false); this.snack.open('Échec de l’envoi', 'Fermer', { duration: 3000 }); }
  });
  }
  trackById(_i: number, m: Chats) { return m.id; }

  private scrollToBottom() {
    queueMicrotask(() => this.endOfList?.nativeElement.scrollIntoView({ behavior: 'smooth' }));
  }


}

