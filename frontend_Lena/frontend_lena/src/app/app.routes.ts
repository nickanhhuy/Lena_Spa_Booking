import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { BookingFormComponent } from './booking-form/booking-form.component';
import { BookingListComponent } from './booking-list/booking-list.component';
import { LoginComponent } from './login/login.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { ProfileComponent } from './profile/profile.component';
import { AdminComponent } from './admin/admin.component';
import { authGuard } from './auth.guard';
import { adminGuard } from './admin.guard';

export const routes: Routes = [
    {path: 'app-home', component: HomeComponent},
    {path: 'app-booking-form', component: BookingFormComponent, canActivate: [authGuard]},
    {path: 'app-booking-list', component: BookingListComponent, canActivate: [authGuard]},
    {path: 'app-profile', component: ProfileComponent, canActivate: [authGuard]},
    {path: 'app-admin', component: AdminComponent, canActivate: [adminGuard]},
    {path: 'app-login', component: LoginComponent},
    {path: 'app-signup', component: SignUpComponent},
    {path: '', redirectTo: '/app-home', pathMatch: 'full'},
];
