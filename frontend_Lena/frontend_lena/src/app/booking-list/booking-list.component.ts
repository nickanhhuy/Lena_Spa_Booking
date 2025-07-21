import { CommonModule} from '@angular/common';
import { Component} from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { Booking } from '../../booking';
import { BookingService } from '../bookingservice.service';
@Component({
  selector: 'app-booking-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './booking-list.component.html',
  styleUrl: './booking-list.component.css'
})
export class BookingListComponent  {
  bookings: Booking[] = [];

  constructor(private bookingService: BookingService) {
    this.getBookings();
  }

 getBookings() {
    this.bookingService.getAllBooking().subscribe({
      next: data => {
      console.log('Loaded bookings:', data);  // Check here if every booking has a valid id
      this.bookings = data;
    },
    error: err => console.error('Failed to load bookings', err)
  });
  }

  cancelBooking(id: number) {
    if (confirm('Are you sure you want to cancel this appointment?')) {
      this.bookingService.cancelBooking(id).subscribe(() => {
        this.getBookings(); 
      });
    }
  }

}
