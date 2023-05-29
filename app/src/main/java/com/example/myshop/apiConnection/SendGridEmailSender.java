package com.example.myshop.apiConnection;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import java.io.IOException;

public class SendGridEmailSender {

    public void sendEmail(String toEmail, String subject, String content) {
        Email from = new Email("your-email@gmail.com");
        Email to = new Email(toEmail);
        Content mailContent = new Content("text/plain", content);
        Mail mail = new Mail(from, subject, to, mailContent);

        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}