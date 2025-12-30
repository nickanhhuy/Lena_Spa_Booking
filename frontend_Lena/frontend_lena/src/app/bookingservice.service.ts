import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Booking } from '../booking'; 
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BookingService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwtToken'); // Make sure token is saved after login
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  getAllBooking(): Observable<Booking[]> {
    return this.http.get<Booking[]>(`${this.apiUrl}/bookings`, {
      headers: this.getAuthHeaders()
    });
  }

  getByEmail(email: string): Observable<Booking[]> {
    return this.http.get<Booking[]>(`${this.apiUrl}/user?email=${email}`, {
      headers: this.getAuthHeaders()
    });
  }

  getAvailableSlots(date: string): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/bookings/available-slots?date=${date}`, {
      headers: this.getAuthHeaders()
    });
  }

  addNewBooking(booking: Booking): Observable<Booking> {
    return this.http.post<Booking>(`${this.apiUrl}/bookings/addbooking`, booking, {
      headers: this.getAuthHeaders()
    });
  }

  updateBooking(id: number, booking: Booking): Observable<Booking> {
    return this.http.put<Booking>(`${this.apiUrl}/bookings/${id}/update`, booking, {
      headers: this.getAuthHeaders()
    });
  }

  cancelBooking(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/bookings/${id}`, {
      headers: this.getAuthHeaders()
    });
  }
}

