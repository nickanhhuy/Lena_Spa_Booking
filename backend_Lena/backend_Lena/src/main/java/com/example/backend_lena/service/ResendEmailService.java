package com.example.backend_lena.service;

import com.example.backend_lena.model.BookInfo;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ResendEmailService {

    @Value("${resend.api.key}")
    private String apiKey;

    @Value("${resend.from.email}")
    private String fromEmail;

    @Value("${admin.notification.email}")
    private String adminEmail;

    public void sendBookingConfirmation(String to, BookInfo booking) {
        String subject = "Appointment Confirmation - Lena Spa";
        String htmlContent = buildBookingEmailHtml(booking);
        sendEmail(to, subject, htmlContent);
    }

    public void sendTestEmail(String to) {
        String subject = "Hello World";
        String htmlContent = "<p>Congrats on sending your <strong>first email</strong>!</p>";
        sendEmail(to, subject, htmlContent);
    }

    public void sendAdminBookingNotification(BookInfo booking) {
        String subject = "New Booking - Lena Spa";
        String htmlContent = buildAdminBookingNotificationHtml(booking);
        sendEmail(adminEmail, subject, htmlContent);
    }

    public void sendAdminRegistrationNotification(String username, String email, String phone) {
        String subject = "New User Registration - Lena Spa";
        String htmlContent = buildAdminRegistrationNotificationHtml(username, email, phone);
        sendEmail(adminEmail, subject, htmlContent);
    }

    public void sendWelcomeEmail(String to, String username) {
        String subject = "Welcome to Lena Spa!";
        String htmlContent = buildWelcomeEmailHtml(username);
        sendEmail(to, subject, htmlContent);
    }

    public void sendEmailVerification(String to, String username, String verificationToken) {
        String subject = "Verify Your Email - Lena Spa";
        String htmlContent = buildEmailVerificationHtml(username, verificationToken);
        sendEmail(to, subject, htmlContent);
    }

    public void sendPasswordResetEmail(String to, String username, String resetToken) {
        String subject = "Reset Your Password - Lena Spa";
        String htmlContent = buildPasswordResetEmailHtml(username, resetToken);
        sendEmail(to, subject, htmlContent);
    }

    public void sendBookingCancellationEmail(String to, BookInfo booking) {
        String subject = "Booking Cancelled - Lena Spa";
        String htmlContent = buildBookingCancellationEmailHtml(booking);
        sendEmail(to, subject, htmlContent);
    }

    public void sendBookingRescheduleEmail(String to, BookInfo booking, LocalDateTime oldDate) {
        String subject = "Booking Rescheduled - Lena Spa";
        String htmlContent = buildBookingRescheduleEmailHtml(booking, oldDate);
        sendEmail(to, subject, htmlContent);
    }

    private void sendEmail(String to, String subject, String htmlContent) {
        try {
            Resend resend = new Resend(apiKey);
            
            CreateEmailOptions params = CreateEmailOptions.builder()
                .from("Lena Spa <" + fromEmail + ">")
                .to(to)
                .replyTo("huynguyen.study3054@gmail.com")
                .subject(subject)
                .html(htmlContent)
                .build();
            
            CreateEmailResponse data = resend.emails().send(params);
            
            System.out.println("✅ Email sent successfully!");
            System.out.println("   Email ID: " + data.getId());
            System.out.println("   To: " + to);
            
        } catch (ResendException e) {
            System.err.println("❌ Failed to send email to " + to + ": " + e.getMessage());
            // Note: In testing mode, Resend only allows sending to verified email addresses
            // To send to other recipients, verify a domain at resend.com/domains
        } catch (Exception e) {
            System.err.println("❌ Unexpected error sending email: " + e.getMessage());
        }
    }

    private String buildBookingEmailHtml(BookInfo booking) {
        return String.format("""
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                <h2 style="color: #639a3e;">Booking Confirmation</h2>
                <p>Hi <strong>%s</strong>,</p>
                <p>Your appointment has been successfully booked at Lena Spa!</p>
                
                <div style="background-color: #f8f9fa; padding: 20px; border-radius: 8px; margin: 20px 0;">
                    <h3 style="margin-top: 0;">Appointment Details:</h3>
                    <p><strong>Service:</strong> %s</p>
                    <p><strong>Date & Time:</strong> %s</p>
                    <p><strong>Phone:</strong> %s</p>
                </div>
                
                <p>Thank you for choosing Lena Spa!</p>
            </div>
            """,
            booking.getName(),
            booking.getService(),
            booking.getBookingDate(),
            booking.getPhone()
        );
    }

    private String buildAdminBookingNotificationHtml(BookInfo booking) {
        return String.format("""
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                <h2 style="color: #639a3e;">New Booking Alert</h2>
                <p>A new appointment has been booked at Lena Spa.</p>
                
                <div style="background-color: #f8f9fa; padding: 20px; border-radius: 8px; margin: 20px 0;">
                    <h3 style="margin-top: 0;">Booking Details:</h3>
                    <p><strong>Customer Name:</strong> %s</p>
                    <p><strong>Service:</strong> %s</p>
                    <p><strong>Date & Time:</strong> %s</p>
                    <p><strong>Phone:</strong> %s</p>
                    <p><strong>Booked By:</strong> %s</p>
                </div>
                
                <p style="color: #666; font-size: 12px;">This is an automated notification from Lena Spa booking system.</p>
            </div>
            """,
            booking.getName(),
            booking.getService(),
            booking.getBookingDate(),
            booking.getPhone(),
            booking.getCreatedBy()
        );
    }

    private String buildAdminRegistrationNotificationHtml(String username, String email, String phone) {
        return String.format("""
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                <h2 style="color: #639a3e;">New User Registration</h2>
                <p>A new user has registered on Lena Spa.</p>
                
                <div style="background-color: #f8f9fa; padding: 20px; border-radius: 8px; margin: 20px 0;">
                    <h3 style="margin-top: 0;">User Details:</h3>
                    <p><strong>Username:</strong> %s</p>
                    <p><strong>Email:</strong> %s</p>
                    <p><strong>Phone:</strong> %s</p>
                </div>
                
                <p style="color: #666; font-size: 12px;">This is an automated notification from Lena Spa booking system.</p>
            </div>
            """,
            username,
            email != null ? email : "Not provided",
            phone != null ? phone : "Not provided"
        );
    }

    private String buildWelcomeEmailHtml(String username) {
        return String.format("""
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                <h2 style="color: #639a3e;">Welcome to Lena Spa!</h2>
                <p>Hi <strong>%s</strong>,</p>
                <p>Thank you for registering with Lena Spa! We're excited to have you as part of our community.</p>
                
                <div style="background-color: #f8f9fa; padding: 20px; border-radius: 8px; margin: 20px 0;">
                    <h3 style="margin-top: 0;">What's Next?</h3>
                    <p>✨ Browse our spa services</p>
                    <p>📅 Book your first appointment</p>
                    <p>👤 Update your profile with your contact information</p>
                </div>
                
                <p>If you have any questions, feel free to reach out to us at <strong>huynguyen.study3054@gmail.com</strong></p>
                
                <p>We look forward to serving you!</p>
                <p style="color: #639a3e;"><strong>The Lena Spa Team</strong></p>
            </div>
            """,
            username
        );
    }

    private String buildEmailVerificationHtml(String username, String verificationToken) {
        String verificationUrl = "https://www.lenaspabooking.site/verify-email?token=" + verificationToken;
        
        return String.format("""
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                <h2 style="color: #639a3e;">Verify Your Email Address</h2>
                <p>Hi <strong>%s</strong>,</p>
                <p>Thank you for registering with Lena Spa! To complete your registration and start booking appointments, please verify your email address.</p>
                
                <div style="text-align: center; margin: 30px 0;">
                    <a href="%s" style="background-color: #639a3e; color: white; padding: 15px 30px; text-decoration: none; border-radius: 5px; font-weight: bold; display: inline-block;">
                        Verify Email Address
                    </a>
                </div>
                
                <p>Or copy and paste this link into your browser:</p>
                <p style="background-color: #f8f9fa; padding: 10px; border-radius: 5px; word-break: break-all; font-size: 12px;">
                    %s
                </p>
                
                <p style="color: #666; font-size: 12px; margin-top: 30px;">
                    This verification link will expire in 24 hours. If you didn't create an account with Lena Spa, please ignore this email.
                </p>
                
                <p>Welcome to Lena Spa!</p>
                <p style="color: #639a3e;"><strong>The Lena Spa Team</strong></p>
            </div>
            """,
            username,
            verificationUrl,
            verificationUrl
        );
    }

    private String buildPasswordResetEmailHtml(String username, String resetToken) {
        String resetUrl = "https://www.lenaspabooking.site/reset-password?token=" + resetToken;
        
        return String.format("""
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                <h2 style="color: #639a3e;">Reset Your Password</h2>
                <p>Hi <strong>%s</strong>,</p>
                <p>We received a request to reset your password for your Lena Spa account. Click the button below to create a new password.</p>
                
                <div style="text-align: center; margin: 30px 0;">
                    <a href="%s" style="background-color: #639a3e; color: white; padding: 15px 30px; text-decoration: none; border-radius: 5px; font-weight: bold; display: inline-block;">
                        Reset Password
                    </a>
                </div>
                
                <p>Or copy and paste this link into your browser:</p>
                <p style="background-color: #f8f9fa; padding: 10px; border-radius: 5px; word-break: break-all; font-size: 12px;">
                    %s
                </p>
                
                <p style="color: #666; font-size: 12px; margin-top: 30px;">
                    <strong>Important:</strong> This password reset link will expire in 1 hour for security reasons.
                </p>
                
                <p style="color: #dc3545; font-size: 12px;">
                    If you didn't request a password reset, please ignore this email or contact us if you have concerns about your account security.
                </p>
                
                <p>Best regards,</p>
                <p style="color: #639a3e;"><strong>The Lena Spa Team</strong></p>
            </div>
            """,
            username,
            resetUrl,
            resetUrl
        );
    }

    private String buildBookingCancellationEmailHtml(BookInfo booking) {
        return String.format("""
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                <h2 style="color: #dc3545;">Booking Cancelled</h2>
                <p>Hi <strong>%s</strong>,</p>
                <p>Your appointment at Lena Spa has been cancelled as requested.</p>
                
                <div style="background-color: #f8f9fa; padding: 20px; border-radius: 8px; margin: 20px 0;">
                    <h3 style="margin-top: 0;">Cancelled Appointment Details:</h3>
                    <p><strong>Service:</strong> %s</p>
                    <p><strong>Original Date & Time:</strong> %s</p>
                    <p><strong>Phone:</strong> %s</p>
                    %s
                </div>
                
                <p>We're sorry to see you cancel your appointment. If you'd like to book again in the future, we'd love to serve you!</p>
                
                <div style="text-align: center; margin: 30px 0;">
                    <a href="https://www.lenaspabooking.site/app-booking-form" style="background-color: #639a3e; color: white; padding: 15px 30px; text-decoration: none; border-radius: 5px; font-weight: bold; display: inline-block;">
                        Book Another Appointment
                    </a>
                </div>
                
                <p>If you have any questions, please contact us.</p>
                <p style="color: #639a3e;"><strong>The Lena Spa Team</strong></p>
            </div>
            """,
            booking.getName(),
            booking.getService(),
            booking.getBookingDate(),
            booking.getPhone(),
            booking.getCancellationReason() != null 
                ? "<p><strong>Reason:</strong> " + booking.getCancellationReason() + "</p>" 
                : ""
        );
    }

    private String buildBookingRescheduleEmailHtml(BookInfo booking, java.time.LocalDateTime oldDate) {
        return String.format("""
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                <h2 style="color: #639a3e;">Booking Rescheduled</h2>
                <p>Hi <strong>%s</strong>,</p>
                <p>Your appointment at Lena Spa has been successfully rescheduled!</p>
                
                <div style="background-color: #fff3cd; padding: 20px; border-radius: 8px; margin: 20px 0; border-left: 4px solid #ffc107;">
                    <h3 style="margin-top: 0;">Previous Appointment:</h3>
                    <p><strong>Date & Time:</strong> %s</p>
                </div>
                
                <div style="background-color: #d4edda; padding: 20px; border-radius: 8px; margin: 20px 0; border-left: 4px solid #28a745;">
                    <h3 style="margin-top: 0;">New Appointment Details:</h3>
                    <p><strong>Service:</strong> %s</p>
                    <p><strong>New Date & Time:</strong> %s</p>
                    <p><strong>Phone:</strong> %s</p>
                </div>
                
                <p>We look forward to seeing you at your new appointment time!</p>
                
                <p>If you need to make any changes, please contact us or visit our website.</p>
                <p style="color: #639a3e;"><strong>The Lena Spa Team</strong></p>
            </div>
            """,
            booking.getName(),
            oldDate,
            booking.getService(),
            booking.getBookingDate(),
            booking.getPhone()
        );
    }
}
