import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../auth.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [FormsModule, RouterModule, CommonModule],
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent {
  email: string = '';
  message: string = '';
  isLoading: boolean = false;
  isSuccess: boolean = false;

  constructor(private authService: AuthService, private router: Router) {}

  requestPasswordReset() {
    if (!this.email.trim()) {
      this.message = 'Please enter your email address';
      this.isSuccess = false;
      return;
    }

    this.isLoading = true;
    this.authService.forgotPassword(this.email).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.isSuccess = true;
        this.message = response;
      },
      error: (error) => {
        this.isLoading = false;
        this.isSuccess = false;
        this.message = error.error || 'Failed to send password reset email';
      }
    });
  }

  goToLogin() {
    this.router.navigate(['/app-login']);
  }
}
