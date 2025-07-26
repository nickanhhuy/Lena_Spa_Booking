import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { BookingFormComponent } from './booking-form/booking-form.component';
import { BookingListComponent } from './booking-list/booking-list.component';
export const routes: Routes = [
    {path: 'app-home', component: HomeComponent},
    {path: 'app-booking-form', component: BookingFormComponent},
    {path: 'app-booking-list', component: BookingListComponent},
];
