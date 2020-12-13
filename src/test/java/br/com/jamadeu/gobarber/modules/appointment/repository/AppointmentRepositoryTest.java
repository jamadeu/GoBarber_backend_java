package br.com.jamadeu.gobarber.modules.appointment.repository;

import br.com.jamadeu.gobarber.modules.appointment.domain.Appointment;
import br.com.jamadeu.gobarber.modules.user.domain.GoBarberProvider;
import br.com.jamadeu.gobarber.modules.user.domain.GoBarberUser;
import br.com.jamadeu.gobarber.modules.user.repository.ProviderRepository;
import br.com.jamadeu.gobarber.modules.user.repository.UserRepository;
import br.com.jamadeu.gobarber.util.AppointmentCreator;
import br.com.jamadeu.gobarber.util.UserCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

@DataJpaTest
@DisplayName("Tests for AppointmentRepository")
class AppointmentRepositoryTest {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private UserRepository userRepository;
    private GoBarberUser user;
    @Autowired
    private ProviderRepository providerRepository;
    private GoBarberProvider provider;

    @BeforeEach
    void setup() {
        user = userRepository.save(UserCreator.createUserToBeSavedWithPasswordEncoded());
        provider = providerRepository.save(UserCreator.createProviderToBeSavedWithPasswordEncoded());
    }

    @Test
    @DisplayName("save persists appointment when successful")
    void save_PersistsAppointment_WhenSuccessful() {
        Appointment appointmentToBeSaved = AppointmentCreator.createAppointmentToBeSaved(user, provider);
        Appointment appointmentSaved = appointmentRepository.save(appointmentToBeSaved);

        Assertions.assertThat(appointmentSaved).isNotNull();
        Assertions.assertThat(appointmentSaved.getId()).isNotNull();
        Assertions.assertThat(appointmentSaved.getUser()).isEqualTo(appointmentToBeSaved.getUser());
        Assertions.assertThat(appointmentSaved.getProvider()).isEqualTo(appointmentToBeSaved.getProvider());
        Assertions.assertThat(appointmentSaved.getDate()).isBefore(LocalDateTime.now());
    }

    @Test
    @DisplayName("save updates appointment when successful")
    void save_UpdateAppointment_WhenSuccessful() {
        Appointment appointmentSaved = appointmentRepository.save(
                AppointmentCreator.createAppointmentToBeSaved(user, provider)
        );
        appointmentSaved.setDate(LocalDateTime.of(2050, 12, 12, 12, 12));
        Appointment updatedAppointment = appointmentRepository.save(appointmentSaved);

        Assertions.assertThat(updatedAppointment).isNotNull();
        Assertions.assertThat(updatedAppointment.getId())
                .isNotNull()
                .isEqualTo(appointmentSaved.getId());
        Assertions.assertThat(updatedAppointment.getUser()).isEqualTo(appointmentSaved.getUser());
        Assertions.assertThat(appointmentSaved.getProvider()).isEqualTo(appointmentSaved.getProvider());
        Assertions.assertThat(appointmentSaved.getDate()).isAfter(LocalDateTime.now());
    }

    @Test
    @DisplayName("delete deletes appointment when successful")
    void delete_DeleteAppointment_WhenSuccessful() {
        Appointment appointmentSaved = appointmentRepository.save(
                AppointmentCreator.createAppointmentToBeSaved(user, provider)
        );
        appointmentRepository.delete(appointmentSaved);
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(appointmentSaved.getId());

        Assertions.assertThat(appointmentOptional).isEmpty();
    }

}