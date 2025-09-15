import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { map, take } from 'rxjs';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  
  return authService.validateToken().pipe(
    take(1),
    map(isValid => {
      if (isValid) {
        return true;
      } else {
        return router.createUrlTree(['/']);
      }
    })
  );
  

};
