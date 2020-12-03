package br.com.jamadeu.gobarber.repository;

import br.com.jamadeu.gobarber.domain.User;
import br.com.jamadeu.gobarber.util.UserCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

@DataJpaTest
@DisplayName("Tests for UserRepository")
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("save persists user when successful")
    void save_PersistUser_WhenSuccessful() {
        User userToBeSaved = UserCreator.createUserToBeSaved();
        User userSaved = this.userRepository.save(userToBeSaved);

        Assertions.assertThat(userSaved).isNotNull();
        Assertions.assertThat(userSaved.getId()).isNotNull();
        Assertions.assertThat(userSaved.getName()).isEqualTo(userToBeSaved.getName());
        Assertions.assertThat(userSaved.getEmail()).isEqualTo(userToBeSaved.getEmail());
        Assertions.assertThat(userSaved.getPassword()).isEqualTo(userToBeSaved.getPassword());
    }

    @Test
    @DisplayName("save updates user when successful")
    void save_UpdateUser_WhenSuccessful() {
        User userToBeSaved = UserCreator.createUserToBeSaved();
        User userSaved = this.userRepository.save(userToBeSaved);
        userToBeSaved.setName("Updated User");
        User updatedUser = this.userRepository.save(userToBeSaved);

        Assertions.assertThat(updatedUser).isNotNull();
        Assertions.assertThat(updatedUser.getId())
                .isNotNull()
                .isEqualTo(userSaved.getId());
        Assertions.assertThat(updatedUser.getName()).isEqualTo(userSaved.getName());
        Assertions.assertThat(updatedUser.getEmail()).isEqualTo(userToBeSaved.getEmail());
        Assertions.assertThat(updatedUser.getPassword()).isEqualTo(userToBeSaved.getPassword());
    }

    @Test
    @DisplayName("delete deletes user when successful")
    void delete_DeleteUser_WhenSuccessful() {
        User userToBeSaved = UserCreator.createUserToBeSaved();
        User userSaved = this.userRepository.save(userToBeSaved);
        this.userRepository.delete(userSaved);
        Optional<User> userOptional = this.userRepository.findById(userSaved.getId());

        Assertions.assertThat(userOptional).isEmpty();
    }

    @Test
    @DisplayName("findByIsProviderTrue returns list of user who isProvider is true inside page object when successful")
    void findByIsProviderTrue_ReturnsListOfUserWhoIsProviderIsTrueInsidePageObject_WhenSuccessful() {
        User providerToBeSaved = UserCreator.createProviderToBeSaved();
        User userToBeSaved = UserCreator.createUserToBeSaved();
        this.userRepository.save(providerToBeSaved);
        this.userRepository.save(userToBeSaved);
        Page<User> userPage = this.userRepository.findByIsProviderTrue(PageRequest.of(0, 1));

        Assertions.assertThat(userPage).isNotNull();
        Assertions.assertThat(userPage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(userPage.toList().get(0).getName()).isEqualTo(providerToBeSaved.getName());
    }

    @Test
    @DisplayName("findByEmail returns optional of user when successful")
    void findByEmail_ReturnsOptionalOfUser_WhenSuccessful() {
        User userToBeSaved = UserCreator.createUserToBeSaved();
        User userSaved = this.userRepository.save(userToBeSaved);
        Optional<User> userOptional = this.userRepository.findByEmail(userSaved.getEmail());

        Assertions.assertThat(userOptional)
                .isNotNull()
                .isPresent();
        Assertions.assertThat(userOptional.get().getId()).isEqualTo(userSaved.getId());
    }

    @Test
    @DisplayName("findByEmail returns optional empty when user is not found")
    void findByEmail_ReturnsOptionalOfUser_WhenUserIsNotFound() {
        Optional<User> userOptional = this.userRepository.findByEmail("notfound@gobarber.com");

        Assertions.assertThat(userOptional)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save throw ConstraintViolationException when name is empty")
    void save_ThrowsConstraintViolationException_WhenNameIsEmpty() {
        User user = User.builder()
                .email("email@gobarber.com")
                .password("password")
                .build();

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> this.userRepository.save(user))
                .withMessageContaining("The user name can not be empty");
    }

    @Test
    @DisplayName("save throw ConstraintViolationException when email is empty")
    void save_ThrowsConstraintViolationException_WhenEmailIsEmpty() {
        User user = User.builder()
                .name("User")
                .password("password")
                .build();

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> this.userRepository.save(user))
                .withMessageContaining("The user email can not be empty");
    }

    @Test
    @DisplayName("save throw ConstraintViolationException when email is not in the correct format")
    void save_ThrowsConstraintViolationException_WhenEmailIsNotFormatted() {
        User user = User.builder()
                .name("User")
                .email("emailNotFormatted")
                .password("password")
                .build();

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> this.userRepository.save(user))
                .withMessageContaining("The user email must be in a valid email format");
    }

    @Test
    @DisplayName("save throw ConstraintViolationException when password is empty")
    void save_ThrowsConstraintViolationException_WhenPasswordIsEmpty() {
        User user = User.builder()
                .name("User")
                .email("email@gobarber.com")
                .build();

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> this.userRepository.save(user))
                .withMessageContaining("The user password can not be empty");
    }

    @Test
    @DisplayName("save throw ConstraintViolationException when password is less than 6 characters")
    void save_ThrowsConstraintViolationException_WhenPasswordIsLessThan6Characters() {
        User user = User.builder()
                .name("User")
                .email("email@gobarber.com")
                .password("123")
                .build();

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> this.userRepository.save(user))
                .withMessageContaining("The user password must be at least 6 characters");
    }

    @Test
    @DisplayName("save persists user isProvider is false by default when successful")
    void save_PersistUserIsProviderFalseByDefault_WhenSuccessful() {
        User userToBeSaved = UserCreator.createUserToBeSaved();
        User userSaved = this.userRepository.save(userToBeSaved);

        Assertions.assertThat(userSaved).isNotNull();
        Assertions.assertThat(userSaved.getId()).isNotNull();
        Assertions.assertThat(userSaved.getName()).isEqualTo(userToBeSaved.getName());
        Assertions.assertThat(userSaved.getEmail()).isEqualTo(userToBeSaved.getEmail());
        Assertions.assertThat(userSaved.getPassword()).isEqualTo(userToBeSaved.getPassword());
        Assertions.assertThat(userSaved.isProvider()).isFalse();
    }

    @Test
    @DisplayName("findByUsername returns optional of user when successful")
    void findByUsername_ReturnsOptionalOfUser_WhenSuccessful() {
        User userToBeSaved = UserCreator.createUserToBeSaved();
        User userSaved = this.userRepository.save(userToBeSaved);
        Optional<User> userOptional = this.userRepository.findByUsername(userSaved.getUsername());

        Assertions.assertThat(userOptional)
                .isNotNull()
                .isPresent();
        Assertions.assertThat(userOptional.get().getId()).isEqualTo(userSaved.getId());
    }

    @Test
    @DisplayName("findByUsername returns optional empty when user is not found")
    void findByUsername_ReturnsOptionalOfUser_WhenUserIsNotFound() {
        Optional<User> userOptional = this.userRepository.findByUsername("usernameNotExists");

        Assertions.assertThat(userOptional)
                .isNotNull()
                .isEmpty();
    }


}