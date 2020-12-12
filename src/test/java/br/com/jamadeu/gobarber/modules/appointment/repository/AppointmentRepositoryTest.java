package br.com.jamadeu.gobarber.modules.appointment.repository;

import br.com.jamadeu.gobarber.modules.appointment.domain.Appointment;
import br.com.jamadeu.gobarber.util.AppointmentCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

@DataJpaTest
@DisplayName("Tests for AppointmentRepository")
class AppointmentRepositoryTest {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Test
    @DisplayName("save persists appointment when successful")
    void save_PersistsAppointment_WhenSuccessful() {
        Appointment appointmentToBeSaved = AppointmentCreator.createAppointmentToBeSaved();
        Appointment appointmentSaved = appointmentRepository.save(appointmentToBeSaved);

        Assertions.assertThat(appointmentSaved).isNotNull();
        Assertions.assertThat(appointmentSaved.getId()).isNotNull();
        Assertions.assertThat(appointmentSaved.getUser()).isEqualTo(appointmentToBeSaved.getUser());
        Assertions.assertThat(appointmentSaved.getProvider()).isEqualTo(appointmentToBeSaved.getProvider());
        Assertions.assertThat(appointmentSaved.getDate()).isAfterOrEqualTo(LocalDateTime.now());
    }

}