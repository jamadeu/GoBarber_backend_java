package br.com.jamadeu.gobarber.util.appointment;

import br.com.jamadeu.gobarber.modules.appointment.requests.NewAppointmentRequest;
import br.com.jamadeu.gobarber.util.user.UserCreator;

import java.time.LocalDateTime;

public class NewAppointmentRequestCreator {
    public static NewAppointmentRequest createNewAppointmentRequest() {
        return NewAppointmentRequest.builder()
                .user(UserCreator.createValidUser())
                .provider(UserCreator.createValidProvider())
                .date(LocalDateTime.now())
                .build();
    }
}
