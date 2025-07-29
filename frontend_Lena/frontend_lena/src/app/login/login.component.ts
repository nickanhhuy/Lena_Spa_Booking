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
})
export class LoginComponent {
  credentials = { username: '', password: '' };
  message = '';

  constructor(private authService: AuthService, private router: Router) {}

  login() {
  this.authService.login(this.credentials).subscribe({
    next: () => {
      localStorage.setItem('loggedInUser', this.credentials.username); 
      this.router.navigate(['/app-home']);
    },
    error: () => {
      this.message = 'Login failed: Invalid credentials';
    },
  });
}
}

