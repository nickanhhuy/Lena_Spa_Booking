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
  minDate: string = '';
  selectedDate: string = '';
  selectedTime: string = '';
  availableSlots: string[] = [];
  loadingSlots: boolean = false;

  constructor(private bookingService : BookingService) {
    // Set minimum date to today
    const today = new Date();
    this.minDate = today.toISOString().split('T')[0];
  }

  onDateChange() {
    if (this.selectedDate) {
      this.selectedTime = '';
      this.loadingSlots = true;
      this.bookingService.getAvailableSlots(this.selectedDate).subscribe({
        next: (slots) => {
          this.availableSlots = slots;
          this.loadingSlots = false;
        },
        error: (err) => {
          console.error('Failed to load slots:', err);
          this.availableSlots = [];
          this.loadingSlots = false;
        }
      });
    }
  }

  selectTimeSlot(time: string) {
    this.selectedTime = time;
    // Combine date and time for booking
    this.booking.bookingDate = `${this.selectedDate}T${time}:00`;
  }

  submitBooking() {
    this.bookingService.addNewBooking(this.booking).subscribe({
      next: data => {
        console.log('Booking submitted:', data);
        this.successMessage = 'Successfully Booked! We will contact you soon.';
        this.booking = {
          name: '',
          email: '',
          phone: '',
          service: '',
          bookingDate: ''
        };
        this.selectedDate = '';
        this.selectedTime = '';
        this.availableSlots = [];
        // Clear success message after 5 seconds
        setTimeout(() => {
          this.successMessage = '';
        }, 5000);
      },
      error: err => {
        console.error('Booking failed:', err);
        this.successMessage = 'Something went wrong. Please try again.';
      }
    });
  }
}
