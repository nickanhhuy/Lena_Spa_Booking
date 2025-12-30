package com.example.backend_lena.emails;

import com.example.backend_lena.model.BookInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl {

    @Autowired
    private JavaMailSender emailSender;

    public void sendBookingConfirmation(String to, BookInfo book_information) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("xuanhoang2434@gmail.com");
        message.setTo(to);
        message.setSubject("Appointment Confirmation");
        message.setText(buildAppointmentMessage(book_information));
        emailSender.send(message);
    }
    private String buildAppointmentMessage(BookInfo book_information) {
        return "Hi,\n\n" +
                "Your appointment has been successfully booked.\n\n" +
                "Appointment Details:\n" +
                "Name: " + book_information.getName() + "\n" +
                "Email: " + book_information.getEmail() + "\n" +
                "Service: " + book_information.getService() + "\n" +
                "Date & Time: " + book_information.getBookingDate() + "\n" +
                "Phone Number: " + book_information.getPhone() + "\n\n" +
                "Thank you for choosing our service!\n\n" +
                "Best regards";
    }

}

