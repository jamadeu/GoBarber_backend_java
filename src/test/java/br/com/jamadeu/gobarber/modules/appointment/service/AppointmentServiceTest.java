package br.com.jamadeu.gobarber.modules.appointment.service;

import br.com.jamadeu.gobarber.modules.appointment.domain.Appointment;
import br.com.jamadeu.gobarber.modules.appointment.repository.AppointmentRepository;
import br.com.jamadeu.gobarber.modules.appointment.requests.NewAppointmentRequest;
import br.com.jamadeu.gobarber.modules.appointment.requests.ReplaceAppointmentRequest;
import br.com.jamadeu.gobarber.modules.user.domain.GoBarberProvider;
import br.com.jamadeu.gobarber.modules.user.domain.GoBarberUser;
import br.com.jamadeu.gobarber.modules.user.repository.ProviderRepository;
import br.com.jamadeu.gobarber.modules.user.repository.UserRepository;
import br.com.jamadeu.gobarber.shared.exception.BadRequestException;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.DateTimeException;
import java.time.LocalDateTime;
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
        BDDMockito.when(providerRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(UserCreator.createValidProvider()));
        BDDMockito.when(appointmentRepositoryMock.findByProvider(
                ArgumentMatchers.any(GoBarberProvider.class), ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(appointmentPage);
        BDDMockito.when(appointmentRepositoryMock.save(ArgumentMatchers.any(Appointment.class)))
                .thenReturn(AppointmentCreator.createValidAppointment());
        BDDMockito.doNothing().when(appointmentRepositoryMock).delete(ArgumentMatchers.any(Appointment.class));
        BDDMockito.when(appointmentRepositoryMock.findByProvider(ArgumentMatchers.any(GoBarberProvider.class)))
                .thenReturn(List.of(AppointmentCreator.createValidAppointment()));
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

    @Test
    @DisplayName("findByProviderId returns list of appointments inside page object when successful")
    void findByProviderId_ReturnsListOfAppointmentsInsidePageObject_WhenSuccessful() {
        GoBarberProvider provider = UserCreator.createValidProvider();
        Page<Appointment> appointmentPage = appointmentService.findByProviderId(provider.getId());

        Assertions.assertThat(appointmentPage).isNotNull();
        Assertions.assertThat(appointmentPage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(appointmentPage.toList().get(0).getProvider())
                .isEqualTo(provider);
    }

    @Test
    @DisplayName("findByProviderId throws BadRequestException when user is not found")
    void findByProviderId_ThrowsBadRequestException_WhenUserIsNotFound() {
        BDDMockito.when(providerRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> appointmentService.findByProviderId(1L));
    }

    @Test
    @DisplayName("create returns an appointment when successful")
    void create_ReturnsAppointment_WhenSuccessful() {
        NewAppointmentRequest request = NewAppointmentRequestCreator.createNewAppointmentRequest();
        Appointment appointment = appointmentService.create(request);

        Assertions.assertThat(appointment).isNotNull();
        Assertions.assertThat(appointment.getId()).isNotNull();
        Assertions.assertThat(appointment.getUser()).isEqualTo(request.getUser());
        Assertions.assertThat(appointment.getProvider()).isEqualTo(request.getProvider());
    }

    @Test
    @DisplayName("create throws BadRequestException when user is not found")
    void create_ThrowsBadRequestException_WhenUserIsNotFound() {
        BDDMockito.when(userRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());
        NewAppointmentRequest request = NewAppointmentRequestCreator.createNewAppointmentRequest();

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> appointmentService.create(request));
    }

    @Test
    @DisplayName("create throws BadRequestException when provider is not found")
    void create_ThrowsBadRequestException_WhenProviderIsNotFound() {
        BDDMockito.when(providerRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());
        NewAppointmentRequest request = NewAppointmentRequestCreator.createNewAppointmentRequest();

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> appointmentService.create(request));
    }

    @Test
    @DisplayName("delete deletes appointment when successful")
    void delete_DeletesUser_WhenSuccessful() {
        Assertions.assertThatCode(() -> appointmentService.delete(1L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("replace update appointment when successful")
    void replace_UpdateAppointment_WhenSuccessful() {
        ReplaceAppointmentRequest request = ReplaceAppointmentRequestCreator.createReplaceAppointmentRequest();
        Assertions.assertThatCode(() -> appointmentService.replace(request))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("replace throws BadRequestException when user is not found")
    void replace_ThrowsBadRequestException_WhenUserIsNotFound() {
        BDDMockito.when(userRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());
        ReplaceAppointmentRequest request = ReplaceAppointmentRequestCreator.createReplaceAppointmentRequest();
        GoBarberUser user = UserCreator.createValidUser();
        user.setUsername("another_user");
        request.setUser(user);

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> appointmentService.replace(request));
    }

    @Test
    @DisplayName("replace throws BadRequestException when provider is not found")
    void replace_ThrowsBadRequestException_WhenProviderIsNotFound() {
        BDDMockito.when(providerRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());
        ReplaceAppointmentRequest request = ReplaceAppointmentRequestCreator.createReplaceAppointmentRequest();
        GoBarberProvider provider = UserCreator.createValidProvider();
        provider.setUsername("another_provider");
        request.setProvider(provider);

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> appointmentService.replace(request));
    }

    @Test
    @DisplayName("listAllProvidersAppointmentsByMonth returns list of appointments inside page object when successful")
    void listAllProvidersAppointmentsByMonth_ReturnsPageOfAppointments_WhenSuccessful() {
        int month = LocalDateTime.now().getMonth().getValue();
        Page<Appointment> appointments = appointmentService.listAllProvidersAppointmentsByMonth(1L, month);

        Assertions.assertThat(appointments).isNotNull();
        Assertions.assertThat(appointments.toList().get(0))
                .isEqualTo(AppointmentCreator.createValidAppointment());
    }

    @Test
    @DisplayName("listAllProvidersAppointmentsByMonth throws BadRequestException when provider is not found")
    void listAllProvidersAppointmentsByMonth_ThrowsBadRequestException_WhenProviderIsNotFound() {
        BDDMockito.when(providerRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());
        int month = LocalDateTime.now().getMonth().getValue();

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> appointmentService.listAllProvidersAppointmentsByMonth(1L, month));
    }

    @Test
    @DisplayName("listAllProvidersAppointmentsByMonth throws DateTimeException when month is invalid")
    void listAllProvidersAppointmentsByMonth_ThrowsDateTimeException_WhenMonthIsInvalid() {
        Assertions.assertThatExceptionOfType(DateTimeException.class)
                .isThrownBy(() -> appointmentService.listAllProvidersAppointmentsByMonth(1L, 13));
    }

}