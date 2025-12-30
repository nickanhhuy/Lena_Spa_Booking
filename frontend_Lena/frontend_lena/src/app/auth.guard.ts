import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { CanActivateFn } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const isLoggedIn = !!localStorage.getItem('loggedInUser');

  if (!isLoggedIn) {
    router.navigate(['/app-login']);
    return false;
  }

  return true;
};
