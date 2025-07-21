import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { BookingFormComponent } from './booking-form/booking-form.component';
import { BookingListComponent } from './booking-list/booking-list.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, BookingFormComponent, BookingListComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend_lena';
}
