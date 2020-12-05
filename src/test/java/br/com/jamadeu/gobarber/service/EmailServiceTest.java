package br.com.jamadeu.gobarber.service;

import br.com.jamadeu.gobarber.exception.BadRequestException;
import br.com.jamadeu.gobarber.repository.UserRepository;
import br.com.jamadeu.gobarber.util.SendForgotPasswordEmailRequestCreator;
import br.com.jamadeu.gobarber.util.UserCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class EmailServiceTest {
    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSenderImpl javaMailSenderMock;

    @Mock
    private UserRepository userRepositoryMock;

    @BeforeEach
    void setup() {
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        BDDMockito.when(userRepositoryMock.findByUsername(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(UserCreator.createValidUser()));
        BDDMockito.doNothing().when(javaMailSenderMock).send(ArgumentMatchers.any(MimeMessage.class));
        BDDMockito.when(javaMailSenderMock.createMimeMessage())
                .thenReturn(mimeMessage);
    }

    @Test
    @DisplayName("sendForgotPasswordEmail sends email when successful")
    void sendForgotPasswordEmail_SendsEmail_WhenSuccessful() throws MessagingException {
        String res = emailService.sendForgotPasswordEmail(
                SendForgotPasswordEmailRequestCreator.createSendForgotPasswordEmailRequest()
        );

        Assertions.assertThat(res)
                .isNotNull()
                .isEqualTo("Email sent, please check your email box");
    }

    @Test
    @DisplayName("sendForgotPasswordEmail returns status code 400 when user is not found")
    void sendForgotPasswordEmail_ReturnsStatusCode400BadRequest_WhenUserIsNotFound() {
        BDDMockito.when(userRepositoryMock.findByUsername(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> emailService.sendForgotPasswordEmail(
                        SendForgotPasswordEmailRequestCreator.createSendForgotPasswordEmailRequest()
                ));
    }
}