import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private baseUrl = 'http://lena-spa-alb-1246212692.us-east-1.elb.amazonaws.com/api/auth';

  constructor(private http: HttpClient, private router: Router) {}

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
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http.get(`${this.baseUrl}/current-user`, {
      headers,
      responseType: 'text',
    });
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



