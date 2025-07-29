import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private baseUrl = 'http://localhost:8080/api/auth';

  http: HttpClient;
  constructor(http: HttpClient) {
    this.http = http;
  }

  register(user: { username: string; password: string }) {
    return this.http.post(`${this.baseUrl}/register`, user, { responseType: 'text' });
  }

  login(credentials: { username: string; password: string }) {
    return this.http.post(`${this.baseUrl}/login`, credentials, { responseType: 'text', withCredentials: true });
  }

  getCurrentUser() {
    return this.http.get(`${this.baseUrl}/current-user`, { responseType: 'text', withCredentials: true });
  }

  logout() {
    return this.http.post(`${this.baseUrl}/logout`, {}, { responseType: 'text', withCredentials: true });
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('loggedInUser');
  }
}


