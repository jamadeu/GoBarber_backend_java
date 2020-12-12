package br.com.jamadeu.gobarber.modules.appointment.repository;

import br.com.jamadeu.gobarber.modules.appointment.domain.Appointment;
import br.com.jamadeu.gobarber.modules.user.domain.GoBarberProvider;
import br.com.jamadeu.gobarber.modules.user.domain.GoBarberUser;
import br.com.jamadeu.gobarber.modules.user.repository.ProviderRepository;
import br.com.jamadeu.gobarber.modules.user.repository.UserRepository;
import br.com.jamadeu.gobarber.util.UserCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
        Appointment appointmentToBeSaved = Appointment.builder()
                .user(user)
                .provider(provider)
                .date(LocalDateTime.now())
                .build();
        Appointment appointmentSaved = appointmentRepository.save(appointmentToBeSaved);

        Assertions.assertThat(appointmentSaved).isNotNull();
        Assertions.assertThat(appointmentSaved.getId()).isNotNull();
        Assertions.assertThat(appointmentSaved.getUser()).isEqualTo(appointmentToBeSaved.getUser());
        Assertions.assertThat(appointmentSaved.getProvider()).isEqualTo(appointmentToBeSaved.getProvider());
        Assertions.assertThat(appointmentSaved.getDate()).isBefore(LocalDateTime.now());
    }

}