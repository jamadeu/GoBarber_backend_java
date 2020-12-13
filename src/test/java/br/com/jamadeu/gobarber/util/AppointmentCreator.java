package br.com.jamadeu.gobarber.util;

import br.com.jamadeu.gobarber.modules.appointment.domain.Appointment;
import br.com.jamadeu.gobarber.modules.user.domain.GoBarberProvider;
import br.com.jamadeu.gobarber.modules.user.domain.GoBarberUser;

import java.time.LocalDateTime;

public class AppointmentCreator {
    public static Appointment createAppointmentToBeSaved(GoBarberUser user, GoBarberProvider provider) {
        return Appointment.builder()
                .user(user)
                .provider(provider)
                .date(LocalDateTime.now())
                .build();
    }
}
