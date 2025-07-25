import { CommonModule} from '@angular/common';
import { Component} from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { Booking } from '../../booking';
import { BookingService } from '../bookingservice.service';
import { FormsModule } from '@angular/forms';
@Component({
  selector: 'app-booking-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './booking-list.component.html',
  styleUrl: './booking-list.component.css'
})
export class BookingListComponent  {
  bookings: Booking[] = [];
  selectedBooking: Booking | null = null; // list of selected bookings

  constructor(private bookingService: BookingService) {
    this.getBookings();
  }

 getBookings() {
    this.bookingService.getAllBooking().subscribe({
      next: data => {
      console.log('Loaded bookings:', data);  //testing console log whether data is successfully loaded
      this.bookings = data;
    },
    error: err => console.error('Failed to load bookings', err)
  });
  }

  editBooking(booking: Booking): void {
    this.selectedBooking = { ...booking }; // shallow copy to avoid direct mutation
  }

  updateBooking(): void {
    if (!this.selectedBooking || this.selectedBooking.id === undefined) {
      console.error('Booking ID is missing.');
      return;
    }

    this.bookingService.updateBooking(this.selectedBooking.id, this.selectedBooking).subscribe({
      next: (updated) => {
        console.log('Booking updated:', updated); //testing console log update booking
        this.selectedBooking = null;
        this.getBookings(); //call the getBookings method again to refresh the list
      },
      error: (err) => {
        console.error('Failed to update booking:', err);
      }
    });
  }

  cancelEdit(): void {
    this.selectedBooking = null;
  }
  cancelBooking(id: number) {
    if (confirm('Are you sure you want to cancel this booking?')) {
      this.bookingService.cancelBooking(id).subscribe(() => {
        this.getBookings(); 
      });
    }
  }

}
