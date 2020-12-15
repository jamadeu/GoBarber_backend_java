package br.com.jamadeu.gobarber.modules.appointment.controller;

import br.com.jamadeu.gobarber.modules.appointment.domain.Appointment;
import br.com.jamadeu.gobarber.modules.appointment.requests.NewAppointmentRequest;
import br.com.jamadeu.gobarber.modules.appointment.requests.ReplaceAppointmentRequest;
import br.com.jamadeu.gobarber.modules.appointment.service.AppointmentService;
import br.com.jamadeu.gobarber.modules.user.domain.GoBarberUser;
import br.com.jamadeu.gobarber.util.appointment.AppointmentCreator;
import br.com.jamadeu.gobarber.util.appointment.NewAppointmentRequestCreator;
import br.com.jamadeu.gobarber.util.appointment.ReplaceAppointmentRequestCreator;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
class AppointmentControllerTest {
    @InjectMocks
    private AppointmentController appointmentController;

    @Mock
    private AppointmentService appointmentServiceMock;

    @BeforeEach
    void setup() {
        Page<Appointment> appointmentPage = new PageImpl<>(List.of(AppointmentCreator.createValidAppointment()));
        BDDMockito.when(appointmentServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(AppointmentCreator.createValidAppointment());
        BDDMockito.when(appointmentServiceMock.findByUserId(ArgumentMatchers.anyLong()))
                .thenReturn(appointmentPage);
        BDDMockito.when(appointmentServiceMock.findByProviderId(ArgumentMatchers.anyLong()))
                .thenReturn(appointmentPage);
        BDDMockito.when(appointmentServiceMock.create(ArgumentMatchers.any(NewAppointmentRequest.class)))
                .thenReturn(AppointmentCreator.createValidAppointment());
        BDDMockito.doNothing().when(appointmentServiceMock).delete(ArgumentMatchers.anyLong());
        BDDMockito.doNothing().when(appointmentServiceMock).replace(ArgumentMatchers.any(ReplaceAppointmentRequest.class));
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

    @Test
    @DisplayName("findByUserId returns list of appointments inside page object when successful")
    void findByUserId_ReturnsListOfAppointmentsInsidePageObject_WhenSuccessful() {
        Appointment appointment = AppointmentCreator.createValidAppointment();
        ResponseEntity<Page<Appointment>> response = appointmentController.findByUserId(1L);
        Page<Appointment> appointmentPage = response.getBody();


        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(appointmentPage).isNotNull().hasSize(1);
        Assertions.assertThat(appointmentPage.toList().get(0)).isNotNull().isEqualTo(appointment);
    }

    @Test
    @DisplayName("findByProviderId returns list of appointments inside page object when successful")
    void findByProviderId_ReturnsListOfAppointmentsInsidePageObject_WhenSuccessful() {
        Appointment appointment = AppointmentCreator.createValidAppointment();
        ResponseEntity<Page<Appointment>> response = appointmentController.findByProviderId(1L);
        Page<Appointment> appointmentPage = response.getBody();


        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(appointmentPage).isNotNull().hasSize(1);
        Assertions.assertThat(appointmentPage.toList().get(0)).isNotNull().isEqualTo(appointment);
    }

    @Test
    @DisplayName("create returns an appointment when successful")
    void create_ReturnsAppointment_WhenSuccessful() {
        ResponseEntity<Appointment> response = appointmentController.create(
                NewAppointmentRequestCreator.createNewAppointmentRequest());
        Appointment appointment = response.getBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(appointment).isNotNull();
        Assertions.assertThat(appointment.getId()).isNotNull();
    }

    @Test
    @DisplayName("delete deletes appointment when successful")
    void delete_DeletesUser_WhenSuccessful() {
        ResponseEntity<Void> response = appointmentController.delete(1L);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("replace updates appointment when successful")
    void replace_UpdatesAppointment_WhenSuccessful() {
        ReplaceAppointmentRequest request = ReplaceAppointmentRequestCreator.createReplaceAppointmentRequest();
        ResponseEntity<Void> response = appointmentController.replace(request);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}