package br.com.jamadeu.gobarber.modules.user.controller;

import br.com.jamadeu.gobarber.modules.user.domain.GoBarberProvider;
import br.com.jamadeu.gobarber.modules.user.requests.NewProviderRequest;
import br.com.jamadeu.gobarber.modules.user.requests.ReplaceProviderRequest;
import br.com.jamadeu.gobarber.modules.user.service.ProviderService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
class GoBarberProviderControllerTest {
    @InjectMocks
    private ProviderController providerController;

    @Mock
    private ProviderService providerServiceMock;

    @BeforeEach
    void setup() {
        PageImpl<GoBarberProvider> providerPage = new PageImpl<>(List.of(UserCreator.createValidProvider()));
        BDDMockito.when(providerServiceMock.listAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(providerPage);
        BDDMockito.when(providerServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(UserCreator.createValidProvider());
        BDDMockito.when(providerServiceMock.save(ArgumentMatchers.any(NewProviderRequest.class)))
                .thenReturn(UserCreator.createValidProvider());
        BDDMockito.doNothing().when(providerServiceMock).replace(ArgumentMatchers.any(ReplaceProviderRequest.class));
        BDDMockito.doNothing().when(providerServiceMock).delete(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("listAll returns list of providers inside page object when successful")
    void listAll_ReturnsListOfProvidersInsidePageObject_WhenSuccessful() {
        String expectedName = UserCreator.createValidProvider().getName();
        Page<GoBarberProvider> providerPage = providerController.listAll(PageRequest.of(1, 1)).getBody();

        Assertions.assertThat(providerPage).isNotNull();
        Assertions.assertThat(providerPage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(providerPage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns provider when successful")
    void findById_ReturnsProvider_WhenSuccessful() {
        String expectedName = UserCreator.createValidProvider().getName();
        GoBarberProvider provider = providerController.findById(1).getBody();

        Assertions.assertThat(provider).isNotNull();
        Assertions.assertThat(provider.getId()).isNotNull().isEqualTo(1);
        Assertions.assertThat(provider.getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("save returns provider when successful")
    void save_ReturnsProvider_WhenSuccessful() {
        GoBarberProvider provider = providerController.save(NewProviderRequestCreator.createNewProviderRequest()).getBody();

        Assertions.assertThat(provider)
                .isNotNull()
                .isEqualTo(UserCreator.createValidProvider());
    }

    @Test
    @DisplayName("replace updates provider when successful")
    void replace_UpdatesProvider_WhenSuccessful() {
        ResponseEntity<GoBarberProvider> responseEntity = providerController.replace(
                ReplaceProviderRequestCreator.createReplaceProviderRequest());

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete deletes provider when successful")
    void delete_DeletesProvider_WhenSuccessful() {
        Assertions.assertThatCode(() -> providerController.delete(1L))
                .doesNotThrowAnyException();
        ResponseEntity<Void> responseEntity = providerController.delete(1L);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("resetPassword updates provider password when successful")
    void resetPassword_UpdatesProviderPassword_WhenSuccessful() {
        Assertions.assertThatCode(() -> providerController.resetPassword(ResetPasswordRequestCreator.createProviderResetPasswordRequest()))
                .doesNotThrowAnyException();
        ResponseEntity<Void> responseEntity = providerController.resetPassword(ResetPasswordRequestCreator.createProviderResetPasswordRequest());

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }


}