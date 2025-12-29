import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private baseUrl = 'http://localhost:5000/api/auth';

  constructor(private http: HttpClient, private router: Router) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwtToken');
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  register(user: { username: string; password: string }): Observable<string> {
    return this.http.post(`${this.baseUrl}/register`, user, {
      responseType: 'text',
    });
  }

  login(credentials: { username: string; password: string }): Observable<string> {
    return new Observable<string>((observer) => {
      this.http.post(`${this.baseUrl}/login`, credentials, { responseType: 'text' }).subscribe({
        next: (token: string) => {
          localStorage.setItem('jwtToken', token);
          observer.next(token);
        },
        error: (err) => {
          observer.error(err);
        },
      });
    });
  }

  getCurrentUser(): Observable<string> {
    return this.http.get(`${this.baseUrl}/current-user`, {
      headers: this.getAuthHeaders(),
      responseType: 'text',
    });
  }

  getProfile(): Observable<any> {
    return this.http.get(`${this.baseUrl}/profile`, {
      headers: this.getAuthHeaders()
    });
  }

  updateProfile(profile: any): Observable<string> {
    return this.http.put(`${this.baseUrl}/profile`, profile, {
      headers: this.getAuthHeaders(),
      responseType: 'text'
    });
  }

  changePassword(currentPassword: string, newPassword: string): Observable<string> {
    return this.http.post(`${this.baseUrl}/change-password`, 
      { currentPassword, newPassword },
      {
        headers: this.getAuthHeaders(),
        responseType: 'text'
      }
    );
  }

  logout(): void {
    localStorage.removeItem('jwtToken');
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('jwtToken');
  }

  getToken(): string | null {
    return localStorage.getItem('jwtToken');
  }
}



