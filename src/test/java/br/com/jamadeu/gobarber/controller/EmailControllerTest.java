package br.com.jamadeu.gobarber.controller;

import br.com.jamadeu.gobarber.requests.SendForgotPasswordEmailRequest;
import br.com.jamadeu.gobarber.service.EmailService;
import br.com.jamadeu.gobarber.util.SendForgotPasswordEmailRequestCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class EmailControllerTest {
    @InjectMocks
    private EmailController emailController;

    @Mock
    private EmailService emailServiceMock;

    @Test
    @DisplayName("sendForgotPasswordEmail return a message and status code ok when successful")
    void sendForgotPasswordEmail_ReturnAMessageAndStatusCodeOk_WhenSuccessful() {
        BDDMockito.when(emailServiceMock.sendForgotPasswordEmail(
                ArgumentMatchers.any(SendForgotPasswordEmailRequest.class)))
                .thenReturn("Email sent, please check your email box");
        ResponseEntity<String> responseEntity = emailController.sendForgotPasswordEmail(
                SendForgotPasswordEmailRequestCreator.createSendForgotPasswordEmailRequest());

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo("Email sent, please check your email box");
    }
}