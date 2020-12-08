package br.com.jamadeu.gobarber.user.service;

import br.com.jamadeu.gobarber.shared.exception.BadRequestException;
import br.com.jamadeu.gobarber.user.domain.GoBarberProvider;
import br.com.jamadeu.gobarber.user.repository.ProviderRepository;
import br.com.jamadeu.gobarber.user.requests.ReplaceProviderRequest;
import br.com.jamadeu.gobarber.user.requests.ResetPasswordRequest;
import br.com.jamadeu.gobarber.util.NewProviderRequestCreator;
import br.com.jamadeu.gobarber.util.ReplaceProviderRequestCreator;
import br.com.jamadeu.gobarber.util.ResetPasswordRequestCreator;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class ProviderServiceTest {
    @InjectMocks
    private ProviderService providerService;

    @Mock
    private ProviderRepository providerRepositoryMock;

    @BeforeEach
    void setup() {
        PageImpl<GoBarberProvider> providerPage = new PageImpl<>(List.of(UserCreator.createValidProvider()));
        BDDMockito.when(providerRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(providerPage);
        BDDMockito.when(providerRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(UserCreator.createValidProvider()));
        BDDMockito.when(providerRepositoryMock.save(ArgumentMatchers.any(GoBarberProvider.class)))
                .thenReturn(UserCreator.createValidProvider());
        BDDMockito.doNothing().when(providerRepositoryMock).delete(ArgumentMatchers.any(GoBarberProvider.class));
        BDDMockito.when(providerRepositoryMock.findByUsername(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(UserCreator.createValidProvider()));
        BDDMockito.when(providerRepositoryMock.findByEmail(ArgumentMatchers.anyString())).
                thenReturn(Optional.empty());
    }

    @Test
    @DisplayName("listAll returns list of providers inside page object when successful")
    void listAll_ReturnsListOfProvidersInsidePageObject_WhenSuccessful() {
        String expectedName = UserCreator.createValidProvider().getName();
        Page<GoBarberProvider> providerPage = providerService.listAll(PageRequest.of(1, 1));

        Assertions.assertThat(providerPage).isNotNull();
        Assertions.assertThat(providerPage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(providerPage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException returns provider when successful")
    void findByIdOrThrowBadRequestException_ReturnsProvider_WhenSuccessful() {
        Long expectedId = UserCreator.createValidProvider().getId();
        GoBarberProvider provider = providerService.findByIdOrThrowBadRequestException(1);

        Assertions.assertThat(provider).isNotNull();
        Assertions.assertThat(provider.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException throws BadRequestException when provider is not found")
    void findByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenProviderIsNotFound() {
        BDDMockito.when(providerRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> providerService.findByIdOrThrowBadRequestException(1));
    }

    @Test
    @DisplayName("save returns provider when successful")
    void save_ReturnsProvider_WhenSuccessful() {
        BDDMockito.when(providerRepositoryMock.findByUsername(ArgumentMatchers.anyString())).
                thenReturn(Optional.empty());
        GoBarberProvider provider = providerService.save(NewProviderRequestCreator.createNewProviderRequest());

        Assertions.assertThat(provider)
                .isNotNull()
                .isEqualTo(UserCreator.createValidProvider());
    }

    @Test
    @DisplayName("save returns status code 400 bad request when email is already in use")
    void save_ReturnsStatusCode400BadRequest_WhenEmailIsAlreadyInUse() {
        BDDMockito.when(providerRepositoryMock.findByUsername(ArgumentMatchers.anyString())).
                thenReturn(Optional.empty());
        BDDMockito.when(providerRepositoryMock.findByEmail(ArgumentMatchers.anyString())).
                thenReturn(Optional.of(UserCreator.createValidProvider()));

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> providerService.save(NewProviderRequestCreator.createNewProviderRequest()));
    }

    @Test
    @DisplayName("save returns status code 400 bad request when username is already in use")
    void save_ReturnsStatusCode400BadRequest_WhenUsernameIsAlreadyInUse() {
        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> providerService.save(NewProviderRequestCreator.createNewProviderRequest()));
    }

    @Test
    @DisplayName("replace updates provider when successful")
    void replace_UpdatesProvider_WhenSuccessful() {
        Assertions.assertThatCode(() -> providerService.replace(ReplaceProviderRequestCreator.createReplaceProviderRequest()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("replace returns status code 400 bad request when provider is not found")
    void replace_ReturnsStatusCode400BadRequest_WhenProviderIsNotFound() {
        BDDMockito.when(providerRepositoryMock.findById(ArgumentMatchers.anyLong())).
                thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> providerService.replace(ReplaceProviderRequestCreator.createReplaceProviderRequest()));
    }

    @Test
    @DisplayName("replace returns status code 400 bad request when email is already in use")
    void replace_ReturnsStatusCode400BadRequest_WhenEmailIsAlreadyInUse() {
        BDDMockito.when(providerRepositoryMock.findByEmail(ArgumentMatchers.anyString())).
                thenReturn(Optional.of(UserCreator.createValidProvider()));
        ReplaceProviderRequest replaceProviderRequest = ReplaceProviderRequestCreator.createReplaceProviderRequest();
        replaceProviderRequest.setEmail("email-already-in-use@test.com");

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> providerService.replace(replaceProviderRequest))
                .withMessageContaining("This email is already in use: email-already-in-use@test.com");
    }

    @Test
    @DisplayName("replace returns status code 400 bad request when username is already in use")
    void replace_ReturnsStatusCode400BadRequest_WhenUsernameIsAlreadyInUse() {
        BDDMockito.when(providerRepositoryMock.findByUsername(ArgumentMatchers.anyString())).
                thenReturn(Optional.of(UserCreator.createValidProvider()));
        ReplaceProviderRequest replaceProviderRequest = ReplaceProviderRequestCreator.createReplaceProviderRequest();
        replaceProviderRequest.setUsername("username-already-in-use");

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> providerService.replace(replaceProviderRequest))
                .withMessageContaining("This username is already in use: username-already-in-use");
    }

    @Test
    @DisplayName("delete deletes user when successful")
    void delete_DeletesUser_WhenSuccessful() {
        Assertions.assertThatCode(() -> providerService.delete(1L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("loadUserByUsername returns provider when successful")
    void loadUserByUsername_ReturnsProvider_WhenSuccessful() {
        String username = UserCreator.createValidProvider().getUsername();
        UserDetails userDetails = providerService.loadUserByUsername(username);

        Assertions.assertThat(userDetails).isNotNull();
        Assertions.assertThat(userDetails.getUsername())
                .isNotNull()
                .isEqualTo(username);
    }

    @Test
    @DisplayName("loadUserByUsername throws BadRequestException when provider is not found")
    void loadUserByUsername_ThrowsBadRequestException_WhenProviderIsNotFound() {
        BDDMockito.when(providerRepositoryMock.findByUsername(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(UsernameNotFoundException.class)
                .isThrownBy(() -> providerService.loadUserByUsername("userNameNotExists"));
    }

    @Test
    @DisplayName("resetPassword updates provider's password when successful")
    void resetPassword_UpdatesProviderPassword_WhenSuccessful() {
        Assertions.assertThatCode(() -> providerService.resetPassword(ResetPasswordRequestCreator.createResetPasswordRequest()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("resetPassword returns status code 400 bad request when password is wrong")
    void resetPassword_ReturnsStatusCode400BadRequest_WhenPasswordIsWrong() {
        ResetPasswordRequest resetPasswordRequest = ResetPasswordRequestCreator.createResetPasswordRequest();
        resetPasswordRequest.setOldPassword("wrong password");

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> providerService.resetPassword(resetPasswordRequest));
    }
}