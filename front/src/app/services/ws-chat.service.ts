import { Inject, Injectable, NgZone, Optional } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Client, IMessage } from '@stomp/stompjs';
import * as SockJS from 'sockjs-client'; // évite "global is not defined"
import { WsHeaders, WsInterceptor, WsOutgoingFrame } from '../interceptors/webSocket/ws-interceptor';
import { WS_INTERCEPTORS } from '../interceptors/webSocket/ws-interceptor.token';

@Injectable({ providedIn: 'root' })
export class WsChatService  {
  private readonly _messages$ = new BehaviorSubject<IMessage | null>(null);
  readonly messages$ = this._messages$.asObservable();
  private client?: Client;
  private wsUrl =  'http://localhost:3001/ws'
  private interceptors: WsInterceptor[] = [];
  private connected = false;

  // --- NEW: ready + queues ---
  private readyResolve?: () => void;
  private readyPromise: Promise<void> = new Promise(res => this.readyResolve = res);

  private pendingSubs: Array<{ destination: string; cb: (msg: IMessage) => void }> = [];
  private pendingSends: Array<{ destination: string; body?: unknown; headers?: WsHeaders }> = [];

  constructor(
    private zone: NgZone,
    @Optional() @Inject(WS_INTERCEPTORS) interceptors: WsInterceptor[] | null
  ) {
    this.interceptors = interceptors ?? [];
    // console.log('[WsChatService] interceptors:', this.interceptors.map(i => i.constructor?.name));
  }

  // --- util ---
  private hasToken(): boolean { return !!localStorage.getItem('token'); }

  async waitUntilReady() { await this.readyPromise; }

  connect( baseHeaders: WsHeaders = {}) {
    // pas de connexion tant qu’on n’a pas de token
    if (!this.hasToken()) { return; }
    if (this.connected) { return; }

    const connectHeaders = this.applyOnConnectHeaders({ ...baseHeaders });
    // Blindage: fallback si intercepteurs absents mais token présent
    const t = localStorage.getItem('token');
    if (t && !connectHeaders['Authorization'] && !connectHeaders['authorization']) {
      connectHeaders['Authorization'] = `Bearer ${t}`;
      connectHeaders['authorization'] = `Bearer ${t}`;
      // console.debug('[WS] Fallback auth header applied');
    }
    // console.log('ConnectHeaders', connectHeaders);

    this.client = new Client({
      reconnectDelay: 3000,
      webSocketFactory: () => new SockJS(this.wsUrl),
      connectHeaders,
      debug: (msg) => { console.log('[WS DEBUG STOMP]', msg); },
    });

    this.client.onConnect = () => {
      this.connected = true;

      // lever "ready"
      this.readyResolve?.();

      // rejouer les souscriptions en attente
      const subs = [...this.pendingSubs];
      this.pendingSubs.length = 0;
      for (const s of subs) this._doSubscribe(s.destination, s.cb);

      // rejouer les envois en attente
      const sends = [...this.pendingSends];
      this.pendingSends.length = 0;
      for (const p of sends) this._doSend(p.destination, p.body, p.headers);

      // (facultatif) abo génériques
      this.client?.subscribe('/user/queue/acks', (msg) => this.handleIncoming(msg));
    };

    this.client.onStompError = (frame) => this.notifyError(frame);
    this.client.onWebSocketError = (err) => this.notifyError(err);
    this.client.onWebSocketClose = () => { this.connected = false; };

    this.client.activate();
  }

  disconnect() {
    this.client?.deactivate();
    this.connected = false;
    // NOTE: on garde la readyPromise résolue une fois connectée (stompjs gère reconnect)
  }

  // --- publish ---
  async send(destination: string, body?: unknown, headers: WsHeaders = {}) {
    if (this.connected && this.client) {
      this._doSend(destination, body, headers);
    } else {
      this.pendingSends.push({ destination, body, headers });
    }
  }

  private _doSend(destination: string, body?: unknown, headers: WsHeaders = {}) {
    const frame: WsOutgoingFrame = {
      destination,
      headers,
      body: typeof body === 'string' ? body : JSON.stringify(body ?? {}),
    };
    const finalFrame = this.applyBeforeSend(frame);
    this.client!.publish({
      destination: finalFrame.destination,
      headers: finalFrame.headers,
      body: finalFrame.body ?? '',
    });
  }

  // --- subscribe ---
  subscribe(destination: string, cb: (msg: IMessage) => void) {
    if (this.connected && this.client) {
      return this._doSubscribe(destination, cb);
    }
    this.pendingSubs.push({ destination, cb });
    return { unsubscribe() {} };
  }

  private _doSubscribe(destination: string, cb: (msg: IMessage) => void) {
    return this.client!.subscribe(destination, (msg) => {
      const processed = this.applyAfterReceive(msg);
      this.zone.run(() => cb(processed ?? msg));
    });
  }
    subscribeTopicChats(cb: (msg: IMessage) => void) {
    this.subscribe('/topic/chats', cb);
  }

sendCreate(body: { content: string }) {
    this.send('/app/chats.create', body);
  }

  // --- interceptors pipeline ---
  private applyOnConnectHeaders(headers: WsHeaders): WsHeaders {
    return (this.interceptors ?? []).reduce(
      (acc, itc) => (itc.onConnectHeaders ? (itc.onConnectHeaders(acc) ?? acc) : acc),
      headers
    );
  }

  private applyBeforeSend(frame: WsOutgoingFrame): WsOutgoingFrame {
    return (this.interceptors ?? []).reduce(
      (acc, itc) => (itc.beforeSend ? (itc.beforeSend(acc) ?? acc) : acc),
      frame
    );
  }

  private applyAfterReceive(message: IMessage): IMessage | void {
    return (this.interceptors ?? []).reduce(
      (acc, itc) => (itc.afterReceive ? (acc ? itc.afterReceive(acc) ?? acc : acc) : acc),
      message
    );
  }

  // --- incoming ---
  private handleIncoming(message: IMessage) {
    const processed = this.applyAfterReceive(message) ?? message;
    this.zone.run(() => this._messages$.next(processed));
  }

  // --- error notify ---
  private notifyError(err: unknown) {
    for (const i of this.interceptors) i.onError?.(err);
  }
}
