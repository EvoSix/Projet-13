import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, catchError, map, Observable, of, tap } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ChatRequest } from '../interfaces/ChatRequest';
import {ApiChatListResponse, Chats} from '../interfaces/Chats'
import { IMessage } from '@stomp/stompjs';
import { WsChatService } from './ws-chat.service';

@Injectable({
  providedIn: 'root',
})

export class ChatService {
   private readonly _chats$ = new BehaviorSubject<Chats[]>([]);
  readonly chats$ = this._chats$.asObservable();

  constructor(private http: HttpClient,private ws: WsChatService) {
    
  }
  private apiUrl = environment.baseUrl;
  getAll() {
    return this.http.get<ApiChatListResponse>(`${this.apiUrl}chats`).pipe(  
         map(res => res.content ?? []),
      // tri ancien → récent (mets .reverse() si tu veux l'inverse)
      map(list => list.sort((a, b) => a.created_at.localeCompare(b.created_at))),
      tap(list => this._chats$.next(list)),
      catchError(err => { console.error(err); this._chats$.next([]); return of([]); })
    );
  }

  createChat(data: ChatRequest): Observable<Chats> {
    console.log(data);
    return this.http.post<Chats>(`${this.apiUrl}chats`, data);
  }


 

  private applyIncoming(msg: IMessage) {
    try {
      const chat: Chats = JSON.parse(msg.body);
      const current = this._chats$.value.slice();
      const idx = current.findIndex(c => c.id === chat.id);
      if (idx === -1) current.push(chat); else current[idx] = chat;
      this._chats$.next(current);
    } catch { /* noop */ }
  }

}