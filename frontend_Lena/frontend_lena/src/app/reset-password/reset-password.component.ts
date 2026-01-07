import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {
  token: string = '';
  newPassword: string = '';
  confirmPassword: string = '';
  message: string = '';
  isLoading: boolean = false;
  isSuccess: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.token = this.route.snapshot.queryParams['token'] || '';
    
    if (!this.token) {
      this.message = 'Invalid reset link. Please request a new password reset.';
    }
  }

  resetPassword() {
    if (!this.newPassword || !this.confirmPassword) {
      this.message = 'Please fill in all fields';
      return;
    }

    if (this.newPassword !== this.confirmPassword) {
      this.message = 'Passwords do not match';
      return;
    }

    if (this.newPassword.length < 6) {
      this.message = 'Password must be at least 6 characters long';
      return;
    }

    this.isLoading = true;
    this.authService.resetPassword(this.token, this.newPassword).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.isSuccess = true;
        this.message = response;
        
        // Redirect to login after 3 seconds
        setTimeout(() => {
          this.router.navigate(['/app-login']);
        }, 3000);
      },
      error: (error) => {
        this.isLoading = false;
        this.isSuccess = false;
        this.message = error.error || 'Failed to reset password';
      }
    });
  }

  goToLogin() {
    this.router.navigate(['/app-login']);
  }
}
