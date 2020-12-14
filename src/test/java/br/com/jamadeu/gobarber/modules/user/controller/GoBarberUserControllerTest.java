package br.com.jamadeu.gobarber.modules.user.controller;

import br.com.jamadeu.gobarber.modules.user.domain.GoBarberUser;
import br.com.jamadeu.gobarber.modules.user.requests.NewUserRequest;
import br.com.jamadeu.gobarber.modules.user.requests.ReplaceUserRequest;
import br.com.jamadeu.gobarber.modules.user.service.UserService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
class GoBarberUserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userServiceMock;

    @BeforeEach
    void setup() {
        PageImpl<GoBarberUser> userPage = new PageImpl<>(List.of(UserCreator.createValidUser()));
        BDDMockito.when(userServiceMock.listAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(userPage);
        BDDMockito.when(userServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(UserCreator.createValidUser());
        BDDMockito.when(userServiceMock.save(ArgumentMatchers.any(NewUserRequest.class)))
                .thenReturn(UserCreator.createValidUser());
        BDDMockito.doNothing().when(userServiceMock).replace(ArgumentMatchers.any(ReplaceUserRequest.class));
        BDDMockito.doNothing().when(userServiceMock).delete(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("listAll returns list of users inside page object when successful")
    void listAll_ReturnsListOfUsersInsidePageObject_WhenSuccessful() {
        String expectedName = UserCreator.createValidUser().getName();
        Page<GoBarberUser> userPage = userController.listAll(PageRequest.of(1, 1)).getBody();

        Assertions.assertThat(userPage).isNotNull();
        Assertions.assertThat(userPage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(userPage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns user when successful")
    void findById_ReturnsUser_WhenSuccessful() {
        String expectedName = UserCreator.createValidUser().getName();
        GoBarberUser user = userController.findById(1).getBody();

        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getId()).isNotNull().isEqualTo(1);
        Assertions.assertThat(user.getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("save returns user when successful")
    void save_ReturnsUser_WhenSuccessful() {
        GoBarberUser user = userController.save(NewUserRequestCreator.createNewUserRequest()).getBody();

        Assertions.assertThat(user)
                .isNotNull()
                .isEqualTo(UserCreator.createValidUser());
    }

    @Test
    @DisplayName("replace updates user when successful")
    void replace_UpdatesUser_WhenSuccessful() {
        ResponseEntity<GoBarberUser> responseEntity = userController.replace(
                ReplaceUserRequestCreator.createReplaceUserRequest());

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete deletes user when successful")
    void delete_DeletesUser_WhenSuccessful() {
        Assertions.assertThatCode(() -> userController.delete(1L))
                .doesNotThrowAnyException();
        ResponseEntity<Void> responseEntity = userController.delete(1L);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("resetPassword updates user password when successful")
    void resetPassword_UpdatesUserPassword_WhenSuccessful() {
        Assertions.assertThatCode(() -> userController.resetPassword(ResetPasswordRequestCreator.createUserResetPasswordRequest()))
                .doesNotThrowAnyException();
        ResponseEntity<Void> responseEntity = userController.resetPassword(ResetPasswordRequestCreator.createUserResetPasswordRequest());

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }


}