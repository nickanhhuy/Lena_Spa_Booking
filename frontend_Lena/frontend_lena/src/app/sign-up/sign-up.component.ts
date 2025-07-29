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
})
export class SignUpComponent {
  user = { username: '', password: '' };
  message = '';

  constructor(private authService: AuthService, private router: Router) {}

  register() {
    this.authService.register(this.user).subscribe({
      next: (res) => {
        this.message = res;
        this.router.navigate(['/app-login']); // redirect to login after signup
      },
      error: (err) => {
        this.message = 'Registration failed: ' + (err.error || err.statusText);
      },
    });
  }
}
