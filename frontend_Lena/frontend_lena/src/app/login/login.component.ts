import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../auth.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  credentials = { username: '', password: '' };
  message = '';

  constructor(private authService: AuthService, private router: Router) {}

  login() {
  if (!this.credentials.username || !this.credentials.password) {
    this.message = 'Username and password are required.';
    return;
  }

  this.authService.login(this.credentials).subscribe({
    next: (token) => {
      console.log('Login successful, token received:', token ? 'YES' : 'NO');
      console.log('Token stored in localStorage:', localStorage.getItem('jwtToken') ? 'YES' : 'NO');
      this.authService.getCurrentUser().subscribe({
        next: (username) => {
          localStorage.setItem('loggedInUser', username);
          console.log("Logged in as:", localStorage.getItem('loggedInUser'));
          this.router.navigate(['/app-home']);
        },
        error: () => this.router.navigate(['/app-home'])
      });
    },
    error: () => {
      this.message = 'Login failed: Invalid credentials';
    }
  });
}

}

