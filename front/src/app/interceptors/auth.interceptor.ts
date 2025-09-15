import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpInterceptorFn,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, Observable, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { ToastService } from '../services/toast.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(
    private router: Router,
    private authService: AuthService,
    private toastService: ToastService
  ) {}

  intercept(
    req: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    const token = localStorage.getItem('token');

    const authReq = token
      ? req.clone({
          headers: req.headers.set('Authorization', `Bearer ${token}`),
        })
      : req;

    return next.handle(authReq).pipe(
      catchError((err) => {
        if (err.status === 401) {
          this.authService.logout();
          this.toastService.show(
            'Un problème est survenue veuillez vous reconnecter'
          );

          this.router.navigate(['/login']);
        }
        if (err.status === 500 && err.status === 503) {
          this.authService.logout();
          this.toastService.show(err.error.message);

          this.router.navigate(['/login']);
        } else this.toastService.show(err.error.message);
        return throwError(() => err);
      })
    );
  }
}
