package br.com.jamadeu.gobarber.modules.user.integration;

import br.com.jamadeu.gobarber.modules.user.domain.GoBarberProvider;
import br.com.jamadeu.gobarber.modules.user.repository.ProviderRepository;
import br.com.jamadeu.gobarber.modules.user.requests.NewProviderRequest;
import br.com.jamadeu.gobarber.modules.user.requests.ReplaceProviderRequest;
import br.com.jamadeu.gobarber.modules.user.requests.ResetPasswordRequest;
import br.com.jamadeu.gobarber.util.user.NewProviderRequestCreator;
import br.com.jamadeu.gobarber.util.user.ReplaceProviderRequestCreator;
import br.com.jamadeu.gobarber.util.user.ResetPasswordRequestCreator;
import br.com.jamadeu.gobarber.util.user.UserCreator;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProviderControllerIT {
    public static final GoBarberProvider USER = GoBarberProvider.builder()
            .name("admin")
            .password("{bcrypt}$2a$10$w80PLxkKSJMlJR2//Y7zcekwzFK1k2tIuo/hf.7toZ5y2rEu0302i")
            .username("admin")
            .email("admin@gobarber.com")
            .authorities("ROLE_PROVIDER")
            .build();

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private ProviderRepository providerRepository;

    @Test
    @DisplayName("findById returns provider when successful")
    void findById_ReturnsProvider_WhenSuccessful() {
        GoBarberProvider savedProvider = providerRepository.save(UserCreator.createProviderToBeSaved());
        providerRepository.save(USER);
        Long expectedId = savedProvider.getId();
        GoBarberProvider provider = testRestTemplate.getForObject(
                "/providers/{id}",
                GoBarberProvider.class,
                expectedId
        );

        Assertions.assertThat(provider).isNotNull();
        Assertions.assertThat(provider.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findById returns status code 400 BadRequest when provider is not found")
    void findById_ReturnsStatusCode400BadRequest_WhenProviderIsNotFound() {
        Long id = providerRepository.save(USER).getId();
        id += 1L;
        ResponseEntity<GoBarberProvider> responseEntity = testRestTemplate.exchange(
                "/providers/{id}",
                HttpMethod.GET,
                null,
                GoBarberProvider.class,
                id
        );

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("save returns provider when successful")
    void save_ReturnsProvider_WhenSuccessful() {
        providerRepository.save(USER);
        NewProviderRequest newProviderRequest = NewProviderRequestCreator.createNewProviderRequest();
        ResponseEntity<GoBarberProvider> responseEntity = testRestTemplate.postForEntity(
                "/providers",
                newProviderRequest,
                GoBarberProvider.class
        );

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("save returns status code 400 bad request when any mandatory provider argument is empty")
    void save_ReturnsStatusCode400_WhenAnyMandatoryProviderArgumentIsEmpty() {
        providerRepository.save(USER);
        NewProviderRequest newProviderRequest = new NewProviderRequest();
        ResponseEntity<GoBarberProvider> responseEntity = testRestTemplate.postForEntity(
                "/providers",
                newProviderRequest,
                GoBarberProvider.class
        );

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("save returns status code 400 bad request when email is not in the correct format")
    void save_ReturnsStatusCode400_WhenEmailIsNotFormatted() {
        providerRepository.save(USER);
        NewProviderRequest newProviderRequest = NewProviderRequestCreator.createNewProviderRequest();
        newProviderRequest.setEmail("emailNotFormatted");
        ResponseEntity<GoBarberProvider> responseEntity = testRestTemplate.postForEntity(
                "/providers",
                newProviderRequest,
                GoBarberProvider.class
        );

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("save returns status code 400 bad request when password is less than 6 characters")
    void save_ReturnsStatusCode400_WhenPasswordIsLessThan6Characters() {
        providerRepository.save(USER);
        NewProviderRequest newProviderRequest = NewProviderRequestCreator.createNewProviderRequest();
        newProviderRequest.setPassword("123");
        ResponseEntity<GoBarberProvider> responseEntity = testRestTemplate.postForEntity(
                "/providers",
                newProviderRequest,
                GoBarberProvider.class
        );

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("replace updates provider when successful")
    void replace_UpdatesProvider_WhenSuccessful() {
        providerRepository.save(UserCreator.createProviderToBeSavedWithPasswordEncoded());
        providerRepository.save(USER);
        ReplaceProviderRequest replaceProviderRequest = ReplaceProviderRequestCreator.createReplaceProviderRequest();
        replaceProviderRequest.setName("Updated user");
        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/providers",
                HttpMethod.PUT,
                new HttpEntity<>(replaceProviderRequest),
                Void.class
        );
        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("replace returns status code 400 BadRequest when provider is not found")
    void replace_ReturnsStatusCode400_WhenProviderIsNotFound() {
        GoBarberProvider providerNotExists = UserCreator.createValidProvider();
        Long id = providerRepository.save(USER).getId();
        providerNotExists.setId(id + 1L);
        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/providers",
                HttpMethod.PUT,
                new HttpEntity<>(providerNotExists),
                Void.class
        );
        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("delete deletes provider when successful")
    void delete_DeletesProvider_WhenSuccessful() {
        GoBarberProvider savedProvider = providerRepository.save(UserCreator.createProviderToBeSaved());
        providerRepository.save(USER);
        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/providers/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                savedProvider.getId()
        );

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete returns status code 400 BadRequest when provider is not found")
    void delete_ReturnsStatusCode400_WhenProviderIsNotFound() {
        Long id = providerRepository.save(USER).getId();
        id += 1L;
        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/providers/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                id
        );

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("resetPassword returns status code 400 BadRequest when provider is not found")
    void resetPassword_ReturnsStatusCode400_WhenProviderIsNotFound() {
        ResetPasswordRequest providerNotExists = ResetPasswordRequestCreator.createProviderResetPasswordRequest();
        providerNotExists.setUsername("providerNotExists");
        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/providers",
                HttpMethod.PUT,
                new HttpEntity<>(providerNotExists),
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
