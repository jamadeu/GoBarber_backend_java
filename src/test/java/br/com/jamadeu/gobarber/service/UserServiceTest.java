package br.com.jamadeu.gobarber.service;

import br.com.jamadeu.gobarber.domain.User;
import br.com.jamadeu.gobarber.exception.BadRequestException;
import br.com.jamadeu.gobarber.repository.UserRepository;
import br.com.jamadeu.gobarber.util.NewUserRequestCreator;
import br.com.jamadeu.gobarber.util.ReplaceUserRequestCreator;
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
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepositoryMock;

    @BeforeEach
    void setup() {
        PageImpl<User> userPage = new PageImpl<>(List.of(UserCreator.createValidUser()));
        PageImpl<User> providerPage = new PageImpl<>(List.of(UserCreator.createValidProvider()));
        BDDMockito.when(userRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(userPage);
        BDDMockito.when(userRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(UserCreator.createValidUser()));
        BDDMockito.when(userRepositoryMock.findByIsProviderTrue(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(providerPage);
        BDDMockito.when(userRepositoryMock.save(ArgumentMatchers.any(User.class)))
                .thenReturn(UserCreator.createValidUser());
        BDDMockito.doNothing().when(userRepositoryMock).delete(ArgumentMatchers.any(User.class));
        BDDMockito.when(userRepositoryMock.findByUsername(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(UserCreator.createValidUser()));
    }

    @Test
    @DisplayName("listAll returns list of users inside page object when successful")
    void listAll_ReturnsListOfUsersInsidePageObject_WhenSuccessful() {
        String expectedName = UserCreator.createValidUser().getName();
        Page<User> userPage = userService.listAll(PageRequest.of(1, 1));

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
        User user = userService.findByIdOrThrowBadRequestException(1);

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
    @DisplayName("listAllProviders returns list of users who isProvider is true inside page object when successful")
    void listAllProviders_ReturnsListOfUsersWhoIsProviderIsTrueInsidePageObject_WhenSuccessful() {
        String expectedName = UserCreator.createValidProvider().getName();
        Page<User> providerPage = userService.listAllProviders(PageRequest.of(1, 1));

        Assertions.assertThat(providerPage).isNotNull();
        Assertions.assertThat(providerPage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(providerPage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("save returns user when successful")
    void save_ReturnsAnimUser_WhenSuccessful() {
        BDDMockito.when(userRepositoryMock.findByUsername(ArgumentMatchers.anyString())).
                thenReturn(Optional.empty());
        User user = userService.save(NewUserRequestCreator.createNewUserRequest());

        Assertions.assertThat(user)
                .isNotNull()
                .isEqualTo(UserCreator.createValidUser());
    }

    @Test
    @DisplayName("replace updates user when successful")
    void replace_UpdatesUser_WhenSuccessful() {
        Assertions.assertThatCode(() -> userService.replace(ReplaceUserRequestCreator.createReplaceUserRequest()))
                .doesNotThrowAnyException();
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


}