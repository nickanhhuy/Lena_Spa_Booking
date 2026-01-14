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
  showCancelModal: boolean = false;
  showRescheduleModal: boolean = false;
  bookingToCancel: Booking | null = null;
  bookingToReschedule: Booking | null = null;
  cancellationReason: string = '';
  newBookingDate: string = '';
  newBookingTime: string = '';

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

  openCancelModal(booking: Booking) {
    this.bookingToCancel = booking;
    this.cancellationReason = '';
    this.showCancelModal = true;
  }

  closeCancelModal() {
    this.showCancelModal = false;
    this.bookingToCancel = null;
    this.cancellationReason = '';
  }

  confirmCancellation() {
    if (!this.bookingToCancel || !this.bookingToCancel.id) return;

    this.bookingService.cancelBookingWithReason(this.bookingToCancel.id, this.cancellationReason).subscribe({
      next: () => {
        alert('Booking cancelled successfully! You will receive a confirmation email.');
        this.closeCancelModal();
        this.getBookings();
      },
      error: (err) => {
        console.error('Failed to cancel booking:', err);
        alert('Failed to cancel booking. Please try again.');
      }
    });
  }

  openRescheduleModal(booking: Booking) {
    this.bookingToReschedule = booking;
    this.newBookingDate = '';
    this.newBookingTime = '';
    this.showRescheduleModal = true;
  }

  closeRescheduleModal() {
    this.showRescheduleModal = false;
    this.bookingToReschedule = null;
    this.newBookingDate = '';
    this.newBookingTime = '';
  }

  confirmReschedule() {
    if (!this.bookingToReschedule || !this.bookingToReschedule.id) return;
    if (!this.newBookingDate || !this.newBookingTime) {
      alert('Please select both date and time');
      return;
    }

    const newDateTime = `${this.newBookingDate}T${this.newBookingTime}:00`;
    
    this.bookingService.rescheduleBooking(this.bookingToReschedule.id, newDateTime).subscribe({
      next: () => {
        alert('Booking rescheduled successfully! You will receive a confirmation email.');
        this.closeRescheduleModal();
        this.getBookings();
      },
      error: (err) => {
        console.error('Failed to reschedule booking:', err);
        alert('Failed to reschedule booking. Please try again.');
      }
    });
  }

  canCancelOrReschedule(booking: Booking): boolean {
    return booking.status !== 'CANCELLED';
  }

  getTodayDate(): string {
    return new Date().toISOString().split('T')[0];
  }

}
