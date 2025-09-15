// src/app/ws/interceptors/ws-logger.interceptor.ts
import { Injectable } from '@angular/core';
import { WsInterceptor, WsOutgoingFrame } from './ws-interceptor';
import { IMessage } from '@stomp/stompjs';

@Injectable()
export class WsLoggerInterceptor implements WsInterceptor {
  beforeSend(frame: WsOutgoingFrame): WsOutgoingFrame {
    // console.debug('[WS SEND]', frame.destination, frame.headers);
    return frame;
  }
  afterReceive(message: IMessage): IMessage {
    // console.debug('[WS RECV]', message.headers.destination);
    return message;
  }
  onError(err: unknown): void {
    console.error('[WS ERROR]', err);
  }
}
