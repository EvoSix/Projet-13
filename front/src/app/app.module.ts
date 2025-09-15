import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { WsAuthInterceptor } from './interceptors/webSocket/ws-auth.interceptor';
import { WS_INTERCEPTORS } from './interceptors/webSocket/ws-interceptor.token';
import { WsLoggerInterceptor } from './interceptors/webSocket/ws-logger.interceptor';

@NgModule({
  imports: [
    BrowserModule,

    BrowserAnimationsModule,
    MatButtonModule,
    CommonModule,
  ],
  providers: [
    provideHttpClient(),
    { provide: WS_INTERCEPTORS, useClass: WsAuthInterceptor, multi: true },
    { provide: WS_INTERCEPTORS, useClass: WsLoggerInterceptor, multi: true },
  ],
})
export class AppModule {}
