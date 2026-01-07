import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-email-verification',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './email-verification.component.html',
  styleUrls: ['./email-verification.component.css']
})
export class EmailVerificationComponent implements OnInit {
  verificationStatus: 'loading' | 'success' | 'error' = 'loading';
  message: string = '';
  email: string = '';
  isResending: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit() {
    const token = this.route.snapshot.queryParams['token'];
    
    if (token) {
      this.verifyEmail(token);
    } else {
      this.verificationStatus = 'error';
      this.message = 'Invalid verification link. Please check your email for the correct link.';
    }
  }

  verifyEmail(token: string) {
    this.authService.verifyEmail(token).subscribe({
      next: (response) => {
        this.verificationStatus = 'success';
        this.message = response;
        
        // Redirect to login after 3 seconds
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 3000);
      },
      error: (error) => {
        this.verificationStatus = 'error';
        this.message = error.error || 'Email verification failed. Please try again.';
      }
    });
  }

  resendVerificationEmail() {
    if (!this.email.trim()) {
      alert('Please enter your email address');
      return;
    }

    this.isResending = true;
    this.authService.resendVerificationEmail(this.email).subscribe({
      next: (response) => {
        this.isResending = false;
        alert('Verification email sent! Please check your inbox.');
      },
      error: (error) => {
        this.isResending = false;
        alert(error.error || 'Failed to send verification email');
      }
    });
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }
}