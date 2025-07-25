import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Booking } from '../booking'; 
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BookingService {
  private apiUrl = 'http://localhost:8080/api'; //connect to the backend API
  http: HttpClient;
  constructor(http: HttpClient) {
    this.http = http;
  }

  getAllBooking(): Observable<Booking[]> {
    return this.http.get<Booking[]>(`${this.apiUrl}/bookings`);
  }

  getByEmail(email: string): Observable<Booking[]> {
    return this.http.get<Booking[]>(`${this.apiUrl}/user?email=${email}`);
  }

  addNewBooking(booking: Booking): Observable<Booking> {
    return this.http.post<Booking>(`${this.apiUrl}/bookings/addbooking`, booking);
  }

  updateBooking(id: number, booking: Booking): Observable<Booking> {
    return this.http.put<Booking>(`${this.apiUrl}/bookings/${id}/update`, booking);
  }

  cancelBooking(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/bookings/${id}`);
  }
}
