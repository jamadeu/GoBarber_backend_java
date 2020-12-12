package br.com.jamadeu.gobarber.util;

import br.com.jamadeu.gobarber.modules.appointment.domain.Appointment;

import java.time.LocalDateTime;

public class AppointmentCreator {
    public static Appointment createAppointmentToBeSaved() {
        return Appointment.builder()
                .user(UserCreator.createValidUser())
                .provider(UserCreator.createValidProvider())
                .date(LocalDateTime.now())
                .build();
    }
}
