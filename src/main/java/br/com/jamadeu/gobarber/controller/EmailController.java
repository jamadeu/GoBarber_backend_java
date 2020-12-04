package br.com.jamadeu.gobarber.controller;

import br.com.jamadeu.gobarber.requests.SendForgotPasswordEmailRequest;
import br.com.jamadeu.gobarber.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<String> sendForgotPasswordEmail(
            @RequestBody @Valid SendForgotPasswordEmailRequest sendForgotPasswordEmailRequest) {
        return new ResponseEntity<>(
                emailService.sendForgotPasswordEmail(sendForgotPasswordEmailRequest),
                HttpStatus.OK
        );
    }
}
