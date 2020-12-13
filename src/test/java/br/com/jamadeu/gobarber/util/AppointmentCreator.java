package br.com.jamadeu.gobarber.util;

import br.com.jamadeu.gobarber.modules.appointment.domain.Appointment;
import br.com.jamadeu.gobarber.modules.user.domain.GoBarberProvider;
import br.com.jamadeu.gobarber.modules.user.domain.GoBarberUser;

import java.time.LocalDateTime;

public class AppointmentCreator {
    public static Appointment createAppointmentToBeSaved() {
        return Appointment.builder()
                .user(UserCreator.createValidUser())
                .provider(UserCreator.createValidProvider())
                .date(LocalDateTime.now())
                .build();
    }

    public static Appointment createAppointmentToBeSaved(GoBarberUser user, GoBarberProvider provider) {
        return Appointment.builder()
                .user(user)
                .provider(provider)
                .date(LocalDateTime.now())
                .build();
    }

    public static Appointment createValidAppointment() {
        return Appointment.builder()
                .id(1L)
                .user(UserCreator.createValidUser())
                .provider(UserCreator.createValidProvider())
                .date(LocalDateTime.now())
                .build();
    }
}
