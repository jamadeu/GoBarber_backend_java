package br.com.jamadeu.gobarber.modules.user.integration;

import br.com.jamadeu.gobarber.modules.user.domain.GoBarberUser;
import br.com.jamadeu.gobarber.modules.user.repository.UserRepository;
import br.com.jamadeu.gobarber.modules.user.requests.NewUserRequest;
import br.com.jamadeu.gobarber.modules.user.requests.ReplaceUserRequest;
import br.com.jamadeu.gobarber.modules.user.requests.ResetPasswordRequest;
import br.com.jamadeu.gobarber.shared.wrapper.PageableResponse;
import br.com.jamadeu.gobarber.util.NewUserRequestCreator;
import br.com.jamadeu.gobarber.util.ReplaceUserRequestCreator;
import br.com.jamadeu.gobarber.util.ResetPasswordRequestCreator;
import br.com.jamadeu.gobarber.util.UserCreator;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Log4j2
class UserControllerIT {
    public static final GoBarberUser USER = GoBarberUser.builder()
            .name("admin")
            .password("{bcrypt}$2a$10$w80PLxkKSJMlJR2//Y7zcekwzFK1k2tIuo/hf.7toZ5y2rEu0302i")
            .username("admin")
            .email("admin@gobarber.com")
            .authorities("ROLE_USER")
            .build();
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("listAll returns list of users inside page object when successful")
    void listAll_ReturnsListOfUsersInsidePageObject_WhenSuccessful() {
        String expectedName = userRepository.save(UserCreator.createUserToBeSaved()).getName();
        userRepository.save(USER);
        PageableResponse<GoBarberUser> userPage = testRestTemplate.exchange(
                "/users",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageableResponse<GoBarberUser>>() {
                }).getBody();

        Assertions.assertThat(userPage).isNotNull();
        Assertions.assertThat(userPage.toList())
                .isNotEmpty()
                .hasSize(2);
        Assertions.assertThat(userPage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns user when successful")
    void findById_ReturnsUser_WhenSuccessful() {
        GoBarberUser savedUser = userRepository.save(UserCreator.createUserToBeSaved());
        userRepository.save(USER);
        Long expectedId = savedUser.getId();
        GoBarberUser user = testRestTemplate.getForObject(
                "/users/{id}",
                GoBarberUser.class,
                expectedId
        );

        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findById returns status code 400 BadRequest when user is not found")
    void findById_ReturnsStatusCode400BadRequest_WhenUserIsNotFound() {
        Long id = userRepository.save(USER).getId();
        id += 1L;
        ResponseEntity<GoBarberUser> responseEntity = testRestTemplate.exchange(
                "/users/{id}",
                HttpMethod.GET,
                null,
                GoBarberUser.class,
                id
        );

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("save returns user when successful")
    void save_ReturnsUser_WhenSuccessful() {
        userRepository.save(USER);
        NewUserRequest newUserRequest = NewUserRequestCreator.createNewUserRequest();
        ResponseEntity<GoBarberUser> responseEntity = testRestTemplate.postForEntity(
                "/users",
                newUserRequest,
                GoBarberUser.class
        );

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("save returns status code 400 bad request when any mandatory user argument is empty")
    void save_ReturnsStatusCode400_WhenAnyMandatoryUserArgumentIsEmpty() {
        userRepository.save(USER);
        NewUserRequest newUserRequest = new NewUserRequest();
        ResponseEntity<GoBarberUser> responseEntity = testRestTemplate.postForEntity(
                "/users",
                newUserRequest,
                GoBarberUser.class
        );

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("save returns status code 400 bad request when email is not in the correct format")
    void save_ReturnsStatusCode400_WhenEmailIsNotFormatted() {
        userRepository.save(USER);
        NewUserRequest newUserRequest = NewUserRequestCreator.createNewUserRequest();
        newUserRequest.setEmail("emailNotFormatted");
        ResponseEntity<GoBarberUser> responseEntity = testRestTemplate.postForEntity(
                "/users",
                newUserRequest,
                GoBarberUser.class
        );

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("save returns status code 400 bad request when password is less than 6 characters")
    void save_ReturnsStatusCode400_WhenPasswordIsLessThan6Characters() {
        userRepository.save(USER);
        NewUserRequest newUserRequest = NewUserRequestCreator.createNewUserRequest();
        newUserRequest.setPassword("123");
        ResponseEntity<GoBarberUser> responseEntity = testRestTemplate.postForEntity(
                "/users",
                newUserRequest,
                GoBarberUser.class
        );

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("replace updates user when successful")
    void replace_UpdatesUser_WhenSuccessful() {
        userRepository.save(UserCreator.createUserToBeSavedWithPasswordEncoded());
        userRepository.save(USER);
        ReplaceUserRequest replaceUserRequest = ReplaceUserRequestCreator.createReplaceUserRequest();
        replaceUserRequest.setName("Updated user");
        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/users",
                HttpMethod.PUT,
                new HttpEntity<>(replaceUserRequest),
                Void.class
        );
        log.info(responseEntity.getBody());
        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("replace returns status code 400 BadRequest when user is not found")
    void replace_ReturnsStatusCode400_WhenUserIsNotFound() {
        GoBarberUser userNotExists = UserCreator.createValidUser();
        Long id = userRepository.save(USER).getId();
        userNotExists.setId(id + 1L);
        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/users",
                HttpMethod.PUT,
                new HttpEntity<>(userNotExists),
                Void.class
        );
        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("delete deletes user when successful")
    void delete_DeletesUser_WhenSuccessful() {
        GoBarberUser savedUser = userRepository.save(UserCreator.createUserToBeSaved());
        userRepository.save(USER);
        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/users/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                savedUser.getId()
        );

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete returns status code 400 BadRequest when user is not found")
    void delete_ReturnsStatusCode400_WhenUserIsNotFound() {
        Long id = userRepository.save(USER).getId();
        id += 1L;
        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/users/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                id
        );

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("resetPassword updates user password when successful")
    void resetPassword_UpdatesUserPassword_WhenSuccessful() {
        userRepository.save(UserCreator.createUserToBeSavedWithPasswordEncoded());
        userRepository.save(USER);
        ResetPasswordRequest resetPasswordRequest = ResetPasswordRequestCreator.createUserResetPasswordRequest();
        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/users/reset-password",
                HttpMethod.PUT,
                new HttpEntity<>(resetPasswordRequest),
                Void.class
        );

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("resetPassword returns status code 400 BadRequest when user is not found")
    void resetPassword_ReturnsStatusCode400_WhenUserIsNotFound() {
        ResetPasswordRequest userNotExists = ResetPasswordRequestCreator.createUserResetPasswordRequest();
        userNotExists.setUsername("userNotExists");
        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/users",
                HttpMethod.PUT,
                new HttpEntity<>(userNotExists),
                Void.class
        );

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @TestConfiguration
    @Lazy
    static class config {
        @Bean
        public TestRestTemplate testRestTemplate(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("admin", "admin");
            return new TestRestTemplate(restTemplateBuilder);
        }
    }

}
