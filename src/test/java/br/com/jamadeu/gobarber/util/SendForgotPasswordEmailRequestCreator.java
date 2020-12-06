package br.com.jamadeu.gobarber.util;

import br.com.jamadeu.gobarber.shared.email.requests.SendForgotPasswordEmailRequest;

public class SendForgotPasswordEmailRequestCreator {
    public static SendForgotPasswordEmailRequest createSendForgotPasswordEmailRequest() {
        return SendForgotPasswordEmailRequest.builder()
                .username("username")
                .build();
    }
}
