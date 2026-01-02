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
}
