package br.com.jamadeu.gobarber.requests;

import br.com.jamadeu.gobarber.domain.User;
import br.com.jamadeu.gobarber.exception.BadRequestException;
import br.com.jamadeu.gobarber.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String name;

    @NotEmpty(message = "The user email can not be empty")
    @Email(message = "The user email must be in a valid email format")
    private String email;

    @NotEmpty(message = "The user password can not be empty")
    @Size(min = 6, message = "The user password must be at least 6 characters")
    private String password;

    private boolean isProvider;

    private String avatar;

    public User toUser(@NotNull UserRepository userRepository) {
        if (userRepository.findByEmail(this.email).isPresent()) {
            throw new BadRequestException("Email already in use");
        }
        return User.builder()
                .name(this.name)
                .email(this.email)
                .password(this.password)
                .avatar(this.avatar)
                .isProvider(this.isProvider)
                .build();
    }
}
