package br.com.jamadeu.gobarber.requests;

import br.com.jamadeu.gobarber.domain.GoBarberUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
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

    @Schema(description = "This is the user's roles",
            defaultValue = "ROLE_USER",
            nullable = true
    )
    private String authorities = "ROLE_USER";

    public GoBarberUser toUser() {
        return GoBarberUser.builder()
                .name(name)
                .username(username)
                .email(email)
                .password(password)
                .avatar(avatar)
                .isProvider(isProvider)
                .authorities(authorities)
                .build();
    }
}
