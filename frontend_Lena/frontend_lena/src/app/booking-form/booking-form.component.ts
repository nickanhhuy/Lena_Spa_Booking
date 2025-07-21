import { Component } from '@angular/core';
import { Booking } from '../../booking'
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { BookingService } from '../bookingservice.service';

@Component({
  selector: 'app-booking-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './booking-form.component.html',
  styleUrl: './booking-form.component.css'
})
export class BookingFormComponent {
  booking: Booking = {
    name: '',
    email: '',
    phone: '',
    service: '',
    bookingDate: ''
  };
  
  successMessage: string = '';

  constructor(private bookingService : BookingService) {}

  submitBooking() {
    this.bookingService.addNewBooking(this.booking).subscribe({
      next: data => {
        console.log('Booking submitted:', data);
        this.successMessage = 'Successfully Booked!';
        this.booking = {
          name: '',
          email: '',
          phone: '',
          service: '',
          bookingDate: ''
        };
      },
      error: err => {
        console.error('Booking failed:', err);
        this.successMessage = 'Something went wrong. Please try again.';
      }
    });
  }
}
