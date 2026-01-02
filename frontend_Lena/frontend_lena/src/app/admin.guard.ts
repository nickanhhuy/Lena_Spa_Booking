import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

export const adminGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  
  // Check if user is logged in
  const token = localStorage.getItem('jwtToken');
  if (!token) {
    router.navigate(['/app-login']);
    return false;
  }
  
  // Check if user is admin
  const username = localStorage.getItem('loggedInUser');
  if (username === 'ahu' || username === 'admin') {
    return true;
  }
  
  // Not admin, redirect to home
  alert('Access denied. Admin privileges required.');
  router.navigate(['/app-home']);
  return false;
};
