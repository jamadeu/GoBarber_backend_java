package br.com.jamadeu.gobarber.requests;

import br.com.jamadeu.gobarber.domain.GoBarberUser;
import br.com.jamadeu.gobarber.exception.BadRequestException;
import br.com.jamadeu.gobarber.repository.UserRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    @NotEmpty(message = "The user name can not be empty")
    @Schema(description = "This is the user's name", example = "Name", required = true)
    private String name;

    @NotEmpty(message = "The user name can not be empty")
    @Schema(description = "This is the user's username, this must be unique.",
            example = "name",
            required = true)
    private String username;

    @NotEmpty(message = "The user email can not be empty")
    @Email(message = "The user email must be in a valid email format")
    @Schema(description = "This is the user's email address, this must be unique.",
            example = "email@example.com",
            format = "local-part@domain",
            required = true)
    private String email;

    @NotEmpty(message = "The user password can not be empty")
    @Size(min = 6, message = "The user password must be at least 6 characters")
    @Schema(description = "This is a user password unencrypted", required = true)
    private String password;

    @Schema(description = "This records if the user is a provider",
            defaultValue = "false"
    )
    private boolean isProvider;

    @Schema(description = "This is the user's avatar",
            defaultValue = "null",
            nullable = true
    )
    private String avatar;

    private String authorities = "ROLE_USER";

    public GoBarberUser toUser(@NotNull UserRepository userRepository) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new BadRequestException("This email is already in use: " + email);
        }
        if (userRepository.findByUsername(username).isPresent()) {
            throw new BadRequestException("This username is already in use: " + username);
        }
        if (isProvider()) {
            setAuthorities("ROLE_PROVIDER");
        }
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String passwordEncoded = passwordEncoder.encode(password);
        return GoBarberUser.builder()
                .name(name)
                .username(username)
                .email(email)
                .password(passwordEncoded)
                .avatar(avatar)
                .isProvider(isProvider)
                .authorities(authorities)
                .build();
    }
}
