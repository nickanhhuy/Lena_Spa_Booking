import { Component } from '@angular/core';
import { Booking } from '../../booking'
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-booking-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './booking-form.component.html',
  styleUrl: './booking-form.component.css'
})
export class BookingFormComponent {
  booking: Booking = {
    id: 0,
    name: '',
    email: '',
    phone: '',
    service: '',
    dateTime: ''
  };
  
  constructor(private router: Router) {}

  submitBooking() {
    // Logic to handle booking submission
    console.log('Booking submitted:', this.booking);
    this.router.navigate(['/confirmation']);
  }
}
