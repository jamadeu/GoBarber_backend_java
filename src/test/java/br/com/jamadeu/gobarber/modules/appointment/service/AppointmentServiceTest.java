package br.com.jamadeu.gobarber.modules.appointment.service;

import br.com.jamadeu.gobarber.modules.appointment.domain.Appointment;
import br.com.jamadeu.gobarber.modules.appointment.repository.AppointmentRepository;
import br.com.jamadeu.gobarber.modules.user.domain.GoBarberUser;
import br.com.jamadeu.gobarber.modules.user.repository.ProviderRepository;
import br.com.jamadeu.gobarber.modules.user.repository.UserRepository;
import br.com.jamadeu.gobarber.shared.exception.BadRequestException;
import br.com.jamadeu.gobarber.util.AppointmentCreator;
import br.com.jamadeu.gobarber.util.UserCreator;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
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
        PageImpl<Appointment> appointmentPage = new PageImpl<>(List.of(AppointmentCreator.createValidAppointment()));
        BDDMockito.when(appointmentRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(AppointmentCreator.createValidAppointment()));
        BDDMockito.when(userRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(UserCreator.createValidUser()));
        BDDMockito.when(appointmentRepositoryMock.findByUser(
                ArgumentMatchers.any(GoBarberUser.class), ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(appointmentPage);
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

    @Test
    @DisplayName("findByIdOrThrowBadRequestException throws BadRequestException when appointment is not found")
    void findByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenAppointmentIsNotFound() {
        BDDMockito.when(appointmentRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> appointmentService.findByIdOrThrowBadRequestException(1));
    }

    @Test
    @DisplayName("findByUserId returns list of appointments inside page object when successful")
    void findByUserId_ReturnsListOfAppointmentsInsidePageObject_WhenSuccessful() {
        GoBarberUser user = UserCreator.createValidUser();
        Page<Appointment> appointmentPage = appointmentService.findByUserId(user.getId());

        Assertions.assertThat(appointmentPage).isNotNull();
        Assertions.assertThat(appointmentPage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(appointmentPage.toList().get(0).getUser())
                .isEqualTo(user);
    }

    @Test
    @DisplayName("findByUserId throws BadRequestException when user is not found")
    void findByUserId_ThrowsBadRequestException_WhenUserIsNotFound() {
        BDDMockito.when(userRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> appointmentService.findByUserId(1L));
    }

}