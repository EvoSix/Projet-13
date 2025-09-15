// src/app/ws/interceptors/ws-auth.interceptor.ts
import { Injectable } from '@angular/core';
import { WsHeaders, WsInterceptor, WsOutgoingFrame } from './ws-interceptor';

@Injectable()
export class WsAuthInterceptor implements WsInterceptor {
  private get token(): string | null {
    return localStorage.getItem('token'); // même source que ton HttpInterceptor
  }

  onConnectHeaders(headers: WsHeaders): WsHeaders {
    const t = this.token;
    console.log(" onConnectHeaders",t);
    if (!t) return headers;
    // On met les deux variantes, certains serveurs sont sensibles à la casse
    return {
      ...headers,
      Authorization: `Bearer ${t}`,
      authorization: `Bearer ${t}`,
    };
  }

  beforeSend(frame: WsOutgoingFrame): WsOutgoingFrame {
    const t = this.token;
    if (!t) return frame;
    return {
      ...frame,
      headers: {
        ...(frame.headers ?? {}),
        Authorization: `Bearer ${t}`,
        authorization: `Bearer ${t}`,
        'content-type': frame.headers?.['content-type'] ?? 'application/json',
      },
    };
  }
}
