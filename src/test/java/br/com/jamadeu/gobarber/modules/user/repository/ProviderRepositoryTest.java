package br.com.jamadeu.gobarber.modules.user.repository;

import br.com.jamadeu.gobarber.modules.user.domain.GoBarberProvider;
import br.com.jamadeu.gobarber.util.UserCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

@DataJpaTest
@DisplayName("Tests for UserRepository")
class ProviderRepositoryTest {
    @Autowired
    private ProviderRepository providerRepository;

    @Test
    @DisplayName("save persists provider when successful")
    void save_PersistProvider_WhenSuccessful() {
        GoBarberProvider providerToBeSaved = UserCreator.createProviderToBeSaved();
        GoBarberProvider providerSaved = providerRepository.save(providerToBeSaved);

        Assertions.assertThat(providerSaved).isNotNull();
        Assertions.assertThat(providerSaved.getId()).isNotNull();
        Assertions.assertThat(providerSaved.getName()).isEqualTo(providerToBeSaved.getName());
        Assertions.assertThat(providerSaved.getEmail()).isEqualTo(providerToBeSaved.getEmail());
        Assertions.assertThat(providerSaved.getPassword()).isEqualTo(providerToBeSaved.getPassword());
    }

    @Test
    @DisplayName("save updates provider when successful")
    void save_UpdateProvider_WhenSuccessful() {
        GoBarberProvider providerToBeSaved = UserCreator.createProviderToBeSaved();
        GoBarberProvider providerSaved = providerRepository.save(providerToBeSaved);
        providerToBeSaved.setName("Updated User");
        GoBarberProvider updatedProvider = providerRepository.save(providerToBeSaved);

        Assertions.assertThat(updatedProvider).isNotNull();
        Assertions.assertThat(updatedProvider.getId())
                .isNotNull()
                .isEqualTo(providerSaved.getId());
        Assertions.assertThat(updatedProvider.getName()).isEqualTo(providerSaved.getName());
        Assertions.assertThat(updatedProvider.getEmail()).isEqualTo(providerToBeSaved.getEmail());
        Assertions.assertThat(updatedProvider.getPassword()).isEqualTo(providerToBeSaved.getPassword());
    }

    @Test
    @DisplayName("delete deletes provider when successful")
    void delete_DeleteProvider_WhenSuccessful() {
        GoBarberProvider providerToBeSaved = UserCreator.createProviderToBeSaved();
        GoBarberProvider providerSaved = providerRepository.save(providerToBeSaved);
        providerRepository.delete(providerSaved);
        Optional<GoBarberProvider> providerOptional = providerRepository.findById(providerSaved.getId());

        Assertions.assertThat(providerOptional).isEmpty();
    }

    @Test
    @DisplayName("findByEmail returns optional of provider when successful")
    void findByEmail_ReturnsOptionalOfProvider_WhenSuccessful() {
        GoBarberProvider providerToBeSaved = UserCreator.createProviderToBeSaved();
        GoBarberProvider providerSaved = providerRepository.save(providerToBeSaved);
        Optional<GoBarberProvider> providerOptional = providerRepository.findByEmail(providerSaved.getEmail());

        Assertions.assertThat(providerOptional)
                .isNotNull()
                .isPresent();
        Assertions.assertThat(providerOptional.get().getId()).isEqualTo(providerSaved.getId());
    }

    @Test
    @DisplayName("findByEmail returns optional empty when provider is not found")
    void findByEmail_ReturnsOptionalOfUser_WhenProviderIsNotFound() {
        Optional<GoBarberProvider> providerOptional = providerRepository.findByEmail("notfound@gobarber.com");

        Assertions.assertThat(providerOptional)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save throw ConstraintViolationException when name is empty")
    void save_ThrowsConstraintViolationException_WhenNameIsEmpty() {
        GoBarberProvider provider = GoBarberProvider.builder()
                .email("email@gobarber.com")
                .password("password")
                .username("username")
                .build();

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> providerRepository.save(provider))
                .withMessageContaining("The user name can not be empty");
    }

    @Test
    @DisplayName("save throw ConstraintViolationException when email is empty")
    void save_ThrowsConstraintViolationException_WhenEmailIsEmpty() {
        GoBarberProvider provider = GoBarberProvider.builder()
                .name("Provider")
                .password("password")
                .build();

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> providerRepository.save(provider))
                .withMessageContaining("The user email can not be empty");
    }

    @Test
    @DisplayName("save throw ConstraintViolationException when email is not in the correct format")
    void save_ThrowsConstraintViolationException_WhenEmailIsNotFormatted() {
        GoBarberProvider provider = GoBarberProvider.builder()
                .name("Provider")
                .email("emailNotFormatted")
                .username("username")
                .password("password")
                .build();

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> providerRepository.save(provider))
                .withMessageContaining("The user email must be in a valid email format");
    }

    @Test
    @DisplayName("save throw ConstraintViolationException when password is empty")
    void save_ThrowsConstraintViolationException_WhenPasswordIsEmpty() {
        GoBarberProvider provider = GoBarberProvider.builder()
                .name("Provider")
                .username("username")
                .email("email@gobarber.com")
                .build();

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> providerRepository.save(provider))
                .withMessageContaining("The user password can not be empty");
    }

    @Test
    @DisplayName("save throw ConstraintViolationException when password is less than 6 characters")
    void save_ThrowsConstraintViolationException_WhenPasswordIsLessThan6Characters() {
        GoBarberProvider provider = GoBarberProvider.builder()
                .name("Provider")
                .username("username")
                .email("email@gobarber.com")
                .password("123")
                .build();

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> providerRepository.save(provider))
                .withMessageContaining("The user password must be at least 6 characters");
    }

    @Test
    @DisplayName("save throw ConstraintViolationException when username is empty")
    void save_ThrowsConstraintViolationException_WhenUsernameIsEmpty() {
        GoBarberProvider provider = GoBarberProvider.builder()
                .name("Provider")
                .email("email@gobarber.com")
                .password("123123")
                .build();

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> providerRepository.save(provider))
                .withMessageContaining("The user username can not be empty");
    }

    @Test
    @DisplayName("findByUsername returns optional of provider when successful")
    void findByUsername_ReturnsOptionalOfProvider_WhenSuccessful() {
        GoBarberProvider providerToBeSaved = UserCreator.createProviderToBeSaved();
        GoBarberProvider providerSaved = providerRepository.save(providerToBeSaved);
        Optional<GoBarberProvider> providerOptional = providerRepository.findByUsername(providerSaved.getUsername());

        Assertions.assertThat(providerOptional)
                .isNotNull()
                .isPresent();
        Assertions.assertThat(providerOptional.get().getId()).isEqualTo(providerSaved.getId());
    }

    @Test
    @DisplayName("findByUsername returns optional empty when provider is not found")
    void findByUsername_ReturnsOptionalOfProvider_WhenUserIsNotFound() {
        Optional<GoBarberProvider> providerOptional = providerRepository.findByUsername("usernameNotExists");

        Assertions.assertThat(providerOptional)
                .isNotNull()
                .isEmpty();
    }


}