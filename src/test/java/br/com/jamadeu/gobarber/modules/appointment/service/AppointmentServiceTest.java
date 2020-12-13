package br.com.jamadeu.gobarber.modules.appointment.service;

import br.com.jamadeu.gobarber.modules.appointment.domain.Appointment;
import br.com.jamadeu.gobarber.modules.appointment.repository.AppointmentRepository;
import br.com.jamadeu.gobarber.modules.user.repository.ProviderRepository;
import br.com.jamadeu.gobarber.modules.user.repository.UserRepository;
import br.com.jamadeu.gobarber.util.AppointmentCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
class AppointmentServiceTest {
    @InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private AppointmentRepository appointmentRepositoryMock;
    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private ProviderRepository providerRepositoryMock;

    @BeforeEach
    void setup() {
        BDDMockito.when(appointmentRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(AppointmentCreator.createValidAppointment()));
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException returns appointment when successful")
    void findByIdOrThrowBadRequestException_ReturnsAppointment_WhenSuccessful() {
        Long expectId = AppointmentCreator.createValidAppointment().getId();
        Appointment appointment = appointmentService.findByIdOrThrowBadRequestException(expectId);

        Assertions.assertThat(appointment).isNotNull();
        Assertions.assertThat(appointment.getId())
                .isNotNull()
                .isEqualTo(expectId);
    }


}