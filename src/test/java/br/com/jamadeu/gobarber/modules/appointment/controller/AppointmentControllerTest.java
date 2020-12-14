package br.com.jamadeu.gobarber.modules.appointment.controller;

import br.com.jamadeu.gobarber.modules.appointment.domain.Appointment;
import br.com.jamadeu.gobarber.modules.appointment.service.AppointmentService;
import br.com.jamadeu.gobarber.modules.user.domain.GoBarberUser;
import br.com.jamadeu.gobarber.util.appointment.AppointmentCreator;
import br.com.jamadeu.gobarber.util.user.UserCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class AppointmentControllerTest {
    @InjectMocks
    private AppointmentController appointmentController;

    @Mock
    private AppointmentService appointmentServiceMock;

    @BeforeEach
    void setup() {
        BDDMockito.when(appointmentServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(AppointmentCreator.createValidAppointment());
    }

    @Test
    @DisplayName("findById returns appointment when successful")
    void findById_ReturnsAppointment_WhenSuccessful() {
        GoBarberUser user = UserCreator.createValidUser();
        ResponseEntity<Appointment> response = appointmentController.findById(1L);
        Appointment appointment = response.getBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(appointment).isNotNull();
        Assertions.assertThat(appointment.getId()).isNotNull();
        Assertions.assertThat(appointment.getUser()).isEqualTo(user);
    }

}