import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { tap, map } from 'rxjs';
import { AuthService } from '../services/auth.service';

export const loggedGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  return authService.isLoggedIn$.pipe(
    tap((isLoggedin: boolean) => {
      if (isLoggedin) {
        router.navigateByUrl('/chats');
      }
    }),
    map(() => true)
  );
};
