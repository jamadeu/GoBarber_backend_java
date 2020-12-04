package br.com.jamadeu.gobarber.service;

import br.com.jamadeu.gobarber.domain.User;
import br.com.jamadeu.gobarber.exception.BadRequestException;
import br.com.jamadeu.gobarber.repository.UserRepository;
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

    public String sendForgotPasswordEmail(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("User not found"));
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(user.getEmail());
            helper.setText("Thank you for ordering!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        emailSender.send(message);
        return "Email sent, please check your email box";
    }


}
