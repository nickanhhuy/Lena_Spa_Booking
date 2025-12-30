import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../auth.service';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-profile',
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit {
  profile = {
    username: '',
    email: '',
    phone: '',
    avatarUrl: '',
    role: ''
  };

  passwordChange = {
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  };

  message = '';
  messageType: 'success' | 'error' = 'success';
  activeTab: 'info' | 'password' = 'info';
  uploadingAvatar = false;

  constructor(
    private authService: AuthService,
    private http: HttpClient
  ) {}

  ngOnInit() {
    this.loadProfile();
  }

  loadProfile() {
    this.authService.getProfile().subscribe({
      next: (data) => {
        this.profile = data;
        // If avatar URL is relative, make it absolute
        if (this.profile.avatarUrl && !this.profile.avatarUrl.startsWith('http')) {
          this.profile.avatarUrl = environment.apiUrl.replace('/api', '') + this.profile.avatarUrl;
        }
      },
      error: (err) => {
        console.error('Failed to load profile:', err);
        this.showMessage('Failed to load profile', 'error');
      }
    });
  }

  updateProfile() {
    this.authService.updateProfile(this.profile).subscribe({
      next: () => {
        this.showMessage('Profile updated successfully!', 'success');
      },
      error: (err) => {
        console.error('Failed to update profile:', err);
        this.showMessage('Failed to update profile', 'error');
      }
    });
  }

  changePassword() {
    if (this.passwordChange.newPassword !== this.passwordChange.confirmPassword) {
      this.showMessage('New passwords do not match', 'error');
      return;
    }

    if (this.passwordChange.newPassword.length < 6) {
      this.showMessage('Password must be at least 6 characters', 'error');
      return;
    }

    this.authService.changePassword(
      this.passwordChange.currentPassword,
      this.passwordChange.newPassword
    ).subscribe({
      next: () => {
        this.showMessage('Password changed successfully!', 'success');
        this.passwordChange = {
          currentPassword: '',
          newPassword: '',
          confirmPassword: ''
        };
      },
      error: (err) => {
        console.error('Failed to change password:', err);
        this.showMessage(err.error || 'Failed to change password', 'error');
      }
    });
  }

  onAvatarChange(event: any) {
    const file = event.target.files[0];
    if (!file) return;

    // Validate file type
    if (!file.type.startsWith('image/')) {
      this.showMessage('Please select an image file', 'error');
      return;
    }

    // Validate file size (2MB)
    if (file.size > 2 * 1024 * 1024) {
      this.showMessage('Image must be less than 2MB', 'error');
      return;
    }

    // Upload the file
    this.uploadingAvatar = true;
    const formData = new FormData();
    formData.append('file', file);

    const token = localStorage.getItem('jwtToken');
    this.http.post<string>(`${environment.apiUrl}/upload/avatar`, formData, {
      headers: { 'Authorization': `Bearer ${token}` },
      responseType: 'text' as 'json'
    }).subscribe({
      next: (fileUrl) => {
        // Update profile with new avatar URL
        this.profile.avatarUrl = environment.apiUrl.replace('/api', '') + fileUrl;
        
        // Automatically save the profile
        this.authService.updateProfile({ avatarUrl: fileUrl }).subscribe({
          next: () => {
            this.uploadingAvatar = false;
            this.showMessage('Avatar updated successfully!', 'success');
          },
          error: (err) => {
            this.uploadingAvatar = false;
            console.error('Failed to save avatar:', err);
            this.showMessage('Failed to save avatar', 'error');
          }
        });
      },
      error: (err) => {
        this.uploadingAvatar = false;
        console.error('Failed to upload avatar:', err);
        this.showMessage(err.error || 'Failed to upload avatar', 'error');
      }
    });
  }

  showMessage(msg: string, type: 'success' | 'error') {
    this.message = msg;
    this.messageType = type;
    setTimeout(() => {
      this.message = '';
    }, 5000);
  }

  switchTab(tab: 'info' | 'password') {
    this.activeTab = tab;
    this.message = '';
  }
}
