package br.com.jamadeu.gobarber.modules.user.service;

import br.com.jamadeu.gobarber.modules.user.domain.GoBarberUser;
import br.com.jamadeu.gobarber.modules.user.repository.UserRepository;
import br.com.jamadeu.gobarber.modules.user.requests.NewUserRequest;
import br.com.jamadeu.gobarber.modules.user.requests.ReplaceUserRequest;
import br.com.jamadeu.gobarber.modules.user.requests.ResetPasswordRequest;
import br.com.jamadeu.gobarber.shared.exception.BadRequestException;
import br.com.jamadeu.gobarber.util.user.NewUserRequestCreator;
import br.com.jamadeu.gobarber.util.user.ReplaceUserRequestCreator;
import br.com.jamadeu.gobarber.util.user.ResetPasswordRequestCreator;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepositoryMock;

    @BeforeEach
    void setup() {
        PageImpl<GoBarberUser> userPage = new PageImpl<>(List.of(UserCreator.createValidUser()));
        BDDMockito.when(userRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(userPage);
        BDDMockito.when(userRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(UserCreator.createValidUser()));
        BDDMockito.when(userRepositoryMock.save(ArgumentMatchers.any(GoBarberUser.class)))
                .thenReturn(UserCreator.createValidUser());
        BDDMockito.doNothing().when(userRepositoryMock).delete(ArgumentMatchers.any(GoBarberUser.class));
        BDDMockito.when(userRepositoryMock.findByUsername(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(UserCreator.createValidUser()));
        BDDMockito.when(userRepositoryMock.findByEmail(ArgumentMatchers.anyString())).
                thenReturn(Optional.empty());
    }

    @Test
    @DisplayName("listAll returns list of users inside page object when successful")
    void listAll_ReturnsListOfUsersInsidePageObject_WhenSuccessful() {
        String expectedName = UserCreator.createValidUser().getName();
        Page<GoBarberUser> userPage = userService.listAll(PageRequest.of(1, 1));

        Assertions.assertThat(userPage).isNotNull();
        Assertions.assertThat(userPage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(userPage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException returns user when successful")
    void findByIdOrThrowBadRequestException_ReturnsUser_WhenSuccessful() {
        Long expectedId = UserCreator.createValidUser().getId();
        GoBarberUser user = userService.findByIdOrThrowBadRequestException(1);

        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException throws BadRequestException when user is not found")
    void findByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenUserIsNotFound() {
        BDDMockito.when(userRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> userService.findByIdOrThrowBadRequestException(1));
    }

    @Test
    @DisplayName("save returns user when successful")
    void save_ReturnsUser_WhenSuccessful() {
        BDDMockito.when(userRepositoryMock.findByUsername(ArgumentMatchers.anyString())).
                thenReturn(Optional.empty());
        GoBarberUser user = userService.save(NewUserRequestCreator.createNewUserRequest());

        Assertions.assertThat(user)
                .isNotNull()
                .isEqualTo(UserCreator.createValidUser());
    }

    @Test
    @DisplayName("save returns status code 400 bad request when email is already in use")
    void save_ReturnsStatusCode400BadRequest_WhenEmailIsAlreadyInUse() {
        BDDMockito.when(userRepositoryMock.findByUsername(ArgumentMatchers.anyString())).
                thenReturn(Optional.empty());
        BDDMockito.when(userRepositoryMock.findByEmail(ArgumentMatchers.anyString())).
                thenReturn(Optional.of(UserCreator.createValidUser()));
        NewUserRequest newUserRequest = NewUserRequestCreator.createNewUserRequest();

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> userService.save(newUserRequest));
    }

    @Test
    @DisplayName("save returns status code 400 bad request when username is already in use")
    void save_ReturnsStatusCode400BadRequest_WhenUsernameIsAlreadyInUse() {
        NewUserRequest newUserRequest = NewUserRequestCreator.createNewUserRequest();

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> userService.save(newUserRequest));
    }

    @Test
    @DisplayName("replace updates user when successful")
    void replace_UpdatesUser_WhenSuccessful() {
        Assertions.assertThatCode(() -> userService.replace(ReplaceUserRequestCreator.createReplaceUserRequest()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("replace returns status code 400 bad request when user is not found")
    void replace_ReturnsStatusCode400BadRequest_WhenUserIsNotFound() {
        BDDMockito.when(userRepositoryMock.findById(ArgumentMatchers.anyLong())).
                thenReturn(Optional.empty());
        ReplaceUserRequest replaceUserRequest = ReplaceUserRequestCreator.createReplaceUserRequest();

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> userService.replace(replaceUserRequest));
    }

    @Test
    @DisplayName("replace returns status code 400 bad request when email is already in use")
    void replace_ReturnsStatusCode400BadRequest_WhenEmailIsAlreadyInUse() {
        BDDMockito.when(userRepositoryMock.findByEmail(ArgumentMatchers.anyString())).
                thenReturn(Optional.of(UserCreator.createValidUser()));
        ReplaceUserRequest replaceUserRequest = ReplaceUserRequestCreator.createReplaceUserRequest();
        replaceUserRequest.setEmail("email-already-in-use@test.com");

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> userService.replace(replaceUserRequest))
                .withMessageContaining("This email is already in use: email-already-in-use@test.com");
    }

    @Test
    @DisplayName("replace returns status code 400 bad request when username is already in use")
    void replace_ReturnsStatusCode400BadRequest_WhenUsernameIsAlreadyInUse() {
        BDDMockito.when(userRepositoryMock.findByUsername(ArgumentMatchers.anyString())).
                thenReturn(Optional.of(UserCreator.createValidUser()));
        ReplaceUserRequest replaceUserRequest = ReplaceUserRequestCreator.createReplaceUserRequest();
        replaceUserRequest.setUsername("username-already-in-use");

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> userService.replace(replaceUserRequest))
                .withMessageContaining("This username is already in use: username-already-in-use");
    }

    @Test
    @DisplayName("delete deletes user when successful")
    void delete_DeletesUser_WhenSuccessful() {
        Assertions.assertThatCode(() -> userService.delete(1L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("loadUserByUsername returns user when successful")
    void loadUserByUsername_ReturnsUser_WhenSuccessful() {
        String username = UserCreator.createValidUser().getUsername();
        UserDetails userDetails = userService.loadUserByUsername(username);

        Assertions.assertThat(userDetails).isNotNull();
        Assertions.assertThat(userDetails.getUsername())
                .isNotNull()
                .isEqualTo(username);
    }

    @Test
    @DisplayName("loadUserByUsername throws BadRequestException when user is not found")
    void loadUserByUsername_ThrowsBadRequestException_WhenUserIsNotFound() {
        BDDMockito.when(userRepositoryMock.findByUsername(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(UsernameNotFoundException.class)
                .isThrownBy(() -> userService.loadUserByUsername("userNameNotExists"));
    }

    @Test
    @DisplayName("resetPassword updates user's password when successful")
    void resetPassword_UpdatesUserPassword_WhenSuccessful() {
        Assertions.assertThatCode(() -> userService.resetPassword(ResetPasswordRequestCreator.createUserResetPasswordRequest()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("resetPassword returns status code 400 bad request when password is wrong")
    void resetPassword_ReturnsStatusCode400BadRequest_WhenPasswordIsWrong() {
        ResetPasswordRequest resetPasswordRequest = ResetPasswordRequestCreator.createUserResetPasswordRequest();
        resetPasswordRequest.setOldPassword("wrong password");

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> userService.resetPassword(resetPasswordRequest));
    }
}