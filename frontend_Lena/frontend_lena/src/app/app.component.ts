import { Component, inject } from '@angular/core';
import { Router, RouterModule, RouterOutlet } from '@angular/router';
import { BookingFormComponent } from './booking-form/booking-form.component';
import { BookingListComponent } from './booking-list/booking-list.component';
import { NgIf } from '@angular/common';
import { AuthService } from './auth.service';
import { environment } from '../environments/environment';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, BookingFormComponent, BookingListComponent, RouterModule, NgIf],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})

export class AppComponent {
  title = 'Lena Beauty Spa';

  constructor(
    public router: Router, 
    private authService: AuthService
  ) {}
 
  shouldShowHeader(): boolean {
    const currentUrl = this.router.url;
    return !currentUrl.includes('/app-login') && !currentUrl.includes('/app-signup');
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('loggedInUser');
  }

  getUsername(): string | null {
    return localStorage.getItem('loggedInUser');
  }

  logout(): void {
    fetch(`${environment.apiUrl}/auth/logout`, {
      method: 'POST',
      credentials: 'include'
    }).then(() => {
      console.log("Before clearing: ", localStorage.getItem('loggedInUser'));
      localStorage.removeItem('loggedInUser');
      console.log("After logout: ", localStorage.getItem('loggedInUser'));
      this.router.navigate(['/app-login']);
    });
  }
}
