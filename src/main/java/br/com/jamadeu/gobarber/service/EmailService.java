package br.com.jamadeu.gobarber.service;

import br.com.jamadeu.gobarber.domain.GoBarberUser;
import br.com.jamadeu.gobarber.repository.UserRepository;
import br.com.jamadeu.gobarber.requests.SendForgotPasswordEmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final UserRepository userRepository;
    private final JavaMailSenderImpl emailSender;

    public String sendForgotPasswordEmail(SendForgotPasswordEmailRequest sendForgotPasswordEmailRequest) {
        GoBarberUser user = sendForgotPasswordEmailRequest.toUser(userRepository);
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(user.getEmail());
            helper.setFrom("noreply@gobarber.com");
            helper.setSubject("Reset password");
            helper.setText("Link to reset password" +
                    "http://localhost:8080/users/forgot-password/" + user.getId());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        emailSender.send(message);
        return "Email sent, please check your email box";
    }


}
