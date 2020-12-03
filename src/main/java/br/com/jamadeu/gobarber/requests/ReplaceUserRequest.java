package br.com.jamadeu.gobarber.requests;

import br.com.jamadeu.gobarber.domain.User;
import br.com.jamadeu.gobarber.exception.BadRequestException;
import br.com.jamadeu.gobarber.repository.UserRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplaceUserRequest {

    @NotNull(message = "The user id can not be empty")
    @Positive(message = "The anime id cannot be zero or negative")
    @Schema(description = "This is the user's id", required = true)
    private Long id;

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
    @Schema(description = "This is the user's email, this must be unique",
            example = "email@example.com",
            format = "local-part@domain",
            required = true)
    private String email;

    @NotEmpty(message = "The user password can not be empty")
    @Size(min = 6, message = "The user password must be at least 6 characters")
    @Schema(description = "This is the user's password", required = true)
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

    public User toUser(@NotNull UserRepository userRepository) {
        User savedUser = userRepository.findById(this.id)
                .orElseThrow(() -> new BadRequestException("User not found"));
        if (!this.email.equals(savedUser.getEmail()) && userRepository.findByEmail(this.email).isPresent()) {
            throw new BadRequestException("This email is already in use: " + this.email);
        }
        if (!this.username.equals(savedUser.getUsername()) && userRepository.findByUsername(this.username).isPresent()) {
            throw new BadRequestException("This username is  already in use: " + this.username);
        }
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String passwordEncoded = passwordEncoder.encode(this.password);
        return User.builder()
                .id(savedUser.getId())
                .name(this.name)
                .email(this.email)
                .password(passwordEncoded)
                .isProvider(this.isProvider)
                .avatar(this.avatar)
                .build();
    }
}
