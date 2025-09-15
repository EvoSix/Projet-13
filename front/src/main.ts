import { enableProdMode, importProvidersFrom, inject } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from './app/app.module';
import { environment } from './environments/environment';
import { bootstrapApplication } from '@angular/platform-browser';
import { MatButtonModule } from '@angular/material/button';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';
import { AppComponent } from './app/app.component';
import { routes } from './app/app.routes';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { AuthInterceptor } from './app/interceptors/auth.interceptor';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { HeaderComponent } from './app/components/Layout/header/header.component';
import { WsAuthInterceptor } from './app/interceptors/webSocket/ws-auth.interceptor';
import { WS_INTERCEPTORS } from './app/interceptors/webSocket/ws-interceptor.token';
import { WsLoggerInterceptor } from './app/interceptors/webSocket/ws-logger.interceptor';

if (environment.production) {
  enableProdMode();
}
bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes),
    provideAnimations(),
     provideHttpClient(  withInterceptorsFromDi(), ) ,
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
     { provide: WS_INTERCEPTORS, useClass: WsAuthInterceptor, multi: true },
        { provide: WS_INTERCEPTORS, useClass: WsLoggerInterceptor, multi: true },
  ],
});

platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => console.error(err));
