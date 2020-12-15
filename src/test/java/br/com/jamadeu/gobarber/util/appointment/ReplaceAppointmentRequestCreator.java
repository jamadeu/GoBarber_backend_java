package br.com.jamadeu.gobarber.util.appointment;

import br.com.jamadeu.gobarber.modules.appointment.requests.ReplaceAppointmentRequest;
import br.com.jamadeu.gobarber.util.user.UserCreator;

import java.time.LocalDateTime;

public class ReplaceAppointmentRequestCreator {
    public static ReplaceAppointmentRequest createReplaceAppointmentRequest() {
        return ReplaceAppointmentRequest.builder()
                .id(1L)
                .user(UserCreator.createValidUser())
                .provider(UserCreator.createValidProvider())
                .date(LocalDateTime.of(2099, 12, 12, 12, 12))
                .build();
    }
}
