
import { IMessage } from '@stomp/stompjs';

export type WsHeaders = Record<string, string>;

export interface WsOutgoingFrame {
  destination: string;
  body?: string;
  headers?: WsHeaders;
}

export interface WsInterceptor {

  onConnectHeaders?(headers: WsHeaders): WsHeaders;


  beforeSend?(frame: WsOutgoingFrame): WsOutgoingFrame;


  afterReceive?(message: IMessage): IMessage | void;

  onError?(err: unknown): void;
}
