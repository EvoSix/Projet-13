import { InjectionToken } from '@angular/core';
import { WsInterceptor } from './ws-interceptor';

export const WS_INTERCEPTORS = new InjectionToken<WsInterceptor[]>(
  'WS_INTERCEPTORS',
  { factory: () => [] }
);