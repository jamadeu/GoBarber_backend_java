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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Optional;

@DataJpaTest
@DisplayName("Tests for AppointmentRepository")
class AppointmentRepositoryTest {
    @Autowired
    private AppointmentRepository appointmentRepository;
    private Appointment appointment;
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
        appointment = appointmentRepository.save(
                AppointmentCreator.createAppointmentToBeSaved(user, provider)
        );
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
    @DisplayName("save throw ConstraintViolationException when user is null")
    void save_ThrowsConstraintViolationException_WhenUserIsNull() {
        Appointment appointment = Appointment.builder()
                .provider(provider)
                .date(LocalDateTime.now())
                .build();

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> appointmentRepository.save(appointment))
                .withMessageContaining("User can not be empty");
    }

    @Test
    @DisplayName("save throw ConstraintViolationException when provider is null")
    void save_ThrowsConstraintViolationException_WhenProviderIsNull() {
        Appointment appointment = Appointment.builder()
                .user(user)
                .date(LocalDateTime.now())
                .build();

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> appointmentRepository.save(appointment))
                .withMessageContaining("Provider can not be empty");
    }

    @Test
    @DisplayName("save throw ConstraintViolationException when date is null")
    void save_ThrowsConstraintViolationException_WhenDateIsNull() {
        Appointment appointment = Appointment.builder()
                .user(user)
                .provider(provider)
                .build();

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> appointmentRepository.save(appointment))
                .withMessageContaining("Appointment date can not be null");
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

    @Test
    @DisplayName("findByUser returns a list of appointments inside a page object when successful")
    void findByUser_ReturnsPageableAppointments_WhenSuccessful() {
        Page<Appointment> pageUser = appointmentRepository.findByUser(user, PageRequest.of(0, 1));

        Assertions.assertThat(pageUser)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(pageUser.toList().get(0)).isEqualTo(appointment);
    }

}