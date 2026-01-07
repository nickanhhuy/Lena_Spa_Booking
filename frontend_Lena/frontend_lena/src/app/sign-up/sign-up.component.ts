import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../auth.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [FormsModule, RouterModule, CommonModule],
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent {
  user = { username: '', email: '', password: '' };
  message = '';

  constructor(private authService: AuthService, private router: Router) {}

  register() {
    if (!this.user.email || !this.user.email.trim()) {
      this.message = 'Email is required for registration';
      return;
    }

    this.authService.register(this.user).subscribe({
      next: (res) => {
        this.message = res;
        // Show success message and redirect after delay
        setTimeout(() => {
          this.router.navigate(['/app-login']);
        }, 3000);
      },
      error: (err) => {
        this.message = 'Registration failed: ' + (err.error || err.statusText);
      },
    });
  }
}
