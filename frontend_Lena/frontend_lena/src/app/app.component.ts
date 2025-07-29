import { Component, inject } from '@angular/core';
import { Router, RouterModule, RouterOutlet } from '@angular/router';
import { BookingFormComponent } from './booking-form/booking-form.component';
import { BookingListComponent } from './booking-list/booking-list.component';
import { NgIf } from '@angular/common';
import { AuthService } from './auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, BookingFormComponent, BookingListComponent, RouterModule , NgIf],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})

export class AppComponent {
  title = 'Lena Beauty Spa';
  constructor(private router: Router, private authService : AuthService ) {}
 

  isLoggedIn(): boolean {
    return !!localStorage.getItem('loggedInUser');
  }

  getUsername(): string | null {
    return localStorage.getItem('loggedInUser');
  }

  logout(): void {
  fetch('http://localhost:8080/api/auth/logout', {
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
