import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BookingService } from '../bookingservice.service';
import { Booking } from '../../booking';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
})
export class AdminComponent implements OnInit {
  activeTab: string = 'dashboard';
  
  // Dashboard stats
  totalBookings: number = 0;
  todayBookings: number = 0;
  upcomingBookings: number = 0;
  completedBookings: number = 0;
  
  // Bookings data
  allBookings: Booking[] = [];
  filteredBookings: Booking[] = [];
  searchTerm: string = '';
  
  // Services
  services = [
    { id: 'basic-skincare', name: 'Chăm sóc da cơ bản', price: 300000, duration: 60 },
    { id: 'deep-skincare', name: 'Chăm sóc da chuyên sâu', price: 500000, duration: 90 },
    { id: 'acne-treatment', name: 'Chăm sóc da mụn', price: 450000, duration: 75 },
    { id: 'detox-treatment', name: 'Chăm sóc Thải Độc Da', price: 550000, duration: 90 },
    { id: 'nano-whitening', name: 'Cấy trắng Nano', price: 800000, duration: 120 },
    { id: 'skin-tightening', name: 'Căng bóng', price: 700000, duration: 90 },
    { id: 'advanced-treatment', name: 'Cấy trắng và căng bóng chuyên sâu', price: 1200000, duration: 150 },
    { id: 'ageloc-massage', name: 'Chăm sóc Massage Nâng cơ Ageloc', price: 600000, duration: 90 }
  ];
  
  // Time slots
  timeSlots = [
    '09:00', '10:00', '11:00', '12:00', '13:00', 
    '14:00', '15:00', '16:00', '17:00'
  ];

  constructor(private bookingService: BookingService) {}

  ngOnInit() {
    this.loadBookings();
  }

  loadBookings() {
    this.bookingService.getAllBooking().subscribe({
      next: (bookings) => {
        this.allBookings = bookings;
        this.filteredBookings = bookings;
        this.calculateStats();
      },
      error: (err) => console.error('Failed to load bookings:', err)
    });
  }

  calculateStats() {
    this.totalBookings = this.allBookings.length;
    
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    
    this.todayBookings = this.allBookings.filter(b => {
      const bookingDate = new Date(b.bookingDate);
      bookingDate.setHours(0, 0, 0, 0);
      return bookingDate.getTime() === today.getTime();
    }).length;
    
    this.upcomingBookings = this.allBookings.filter(b => {
      return new Date(b.bookingDate) > new Date();
    }).length;
    
    this.completedBookings = this.allBookings.filter(b => {
      return new Date(b.bookingDate) < new Date();
    }).length;
  }

  switchTab(tab: string) {
    this.activeTab = tab;
  }

  searchBookings() {
    if (!this.searchTerm) {
      this.filteredBookings = this.allBookings;
      return;
    }
    
    const term = this.searchTerm.toLowerCase();
    this.filteredBookings = this.allBookings.filter(b => 
      b.name.toLowerCase().includes(term) ||
      b.email.toLowerCase().includes(term) ||
      b.phone.includes(term) ||
      b.service.toLowerCase().includes(term)
    );
  }

  deleteBooking(id: number | undefined) {
    if (!id) return;
    
    if (confirm('Are you sure you want to delete this booking?')) {
      this.bookingService.cancelBooking(id).subscribe({
        next: () => {
          this.loadBookings();
          alert('Booking deleted successfully');
        },
        error: (err) => {
          console.error('Failed to delete booking:', err);
          alert('Failed to delete booking');
        }
      });
    }
  }

  formatDate(dateStr: string): string {
    const date = new Date(dateStr);
    return date.toLocaleString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  getServiceName(serviceId: string): string {
    const service = this.services.find(s => s.id === serviceId);
    return service ? service.name : serviceId;
  }

  isUpcoming(dateStr: string): boolean {
    return new Date(dateStr) > new Date();
  }

  isCompleted(dateStr: string): boolean {
    return new Date(dateStr) < new Date();
  }

  getStatus(dateStr: string): string {
    return this.isUpcoming(dateStr) ? 'Upcoming' : 'Completed';
  }
}
