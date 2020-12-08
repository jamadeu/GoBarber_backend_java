package br.com.jamadeu.gobarber.user.requests;

import br.com.jamadeu.gobarber.user.domain.GoBarberProvider;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplaceProviderRequest {

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

    @Schema(description = "This is the user's avatar",
            defaultValue = "null",
            nullable = true
    )
    private String avatar;

    public GoBarberProvider toUser() {
        return GoBarberProvider.builder()
                .id(id)
                .name(name)
                .username(username)
                .email(email)
                .password(password)
                .avatar(avatar)
                .build();
    }
}
