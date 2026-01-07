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
  credentials = { email: '', password: '' };
  message = '';
  showResendVerification = false;
  resendEmail = '';

  constructor(private authService: AuthService, private router: Router) {}

  login() {
    if (!this.credentials.email || !this.credentials.password) {
      this.message = 'Email and password are required.';
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
      error: (error) => {
        const errorMessage = error.error || 'Login failed: Invalid credentials';
        this.message = errorMessage;
        
        // Show resend verification option if email verification is required
        if (errorMessage.includes('verify your email')) {
          this.showResendVerification = true;
          this.resendEmail = this.credentials.email; // Pre-fill with login email
        }
      }
    });
  }

  resendVerificationEmail() {
    if (!this.resendEmail.trim()) {
      alert('Please enter your email address');
      return;
    }

    this.authService.resendVerificationEmail(this.resendEmail).subscribe({
      next: (response) => {
        alert('Verification email sent! Please check your inbox.');
        this.showResendVerification = false;
      },
      error: (error) => {
        alert(error.error || 'Failed to send verification email');
      }
    });
  }

}

