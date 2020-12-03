package br.com.jamadeu.gobarber.requests;

import br.com.jamadeu.gobarber.domain.User;
import br.com.jamadeu.gobarber.exception.BadRequestException;
import br.com.jamadeu.gobarber.repository.UserRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Log4j2
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {

    @NotEmpty(message = "The user name can not be empty")
    @Schema(description = "This is the user's username, this must be unique.",
            example = "name",
            required = true)
    private String username;

    @NotEmpty(message = "The user password can not be empty")
    @Size(min = 6, message = "The user password must be at least 6 characters")
    @Schema(description = "This is a user's old password unencrypted", required = true)
    private String oldPassword;

    @NotEmpty(message = "The user password can not be empty")
    @Size(min = 6, message = "The user password must be at least 6 characters")
    @Schema(description = "This is a user's new password unencrypted", required = true)
    private String newPassword;

    public User toUser(UserRepository userRepository) {
        User user = userRepository.findByUsername(this.username)
                .orElseThrow(() -> new BadRequestException("User not found"));
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        if (!passwordEncoder.matches(this.oldPassword, user.getPassword())) {
            throw new BadRequestException("Old password is wrong");
        }
        user.setPassword(passwordEncoder.encode(this.newPassword));
        return user;
    }
}
