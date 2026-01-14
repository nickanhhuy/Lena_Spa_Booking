export interface Booking {
  id?: number;
  name: string;
  email: string;
  phone: string;
  service: string;
  bookingDate : string;
  createdBy?: string;
  status?: string;
  cancellationReason?: string;
  cancelledAt?: string;
}