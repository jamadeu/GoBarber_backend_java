package br.com.jamadeu.gobarber.modules.user.requests;

import br.com.jamadeu.gobarber.modules.user.domain.GoBarberProvider;
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
public class NewProviderRequest {
    @NotEmpty(message = "The provider name can not be empty")
    @Schema(description = "This is the provider's name", example = "Name", required = true)
    private String name;

    @NotEmpty(message = "The provider username can not be empty")
    @Schema(description = "This is the provider's username, this must be unique.",
            example = "name",
            required = true)
    private String username;

    @NotEmpty(message = "The provider email can not be empty")
    @Email(message = "The provider email must be in a valid email format")
    @Schema(description = "This is the provider's email address, this must be unique.",
            example = "email@example.com",
            format = "local-part@domain",
            required = true)
    private String email;

    @NotEmpty(message = "The provider password can not be empty")
    @Size(min = 6, message = "The provider password must be at least 6 characters")
    @Schema(description = "This is a provider password unencrypted", required = true)
    private String password;


    @Schema(description = "This is the provider's avatar",
            defaultValue = "null",
            nullable = true
    )
    private String avatar;

    public GoBarberProvider toUser() {
        return GoBarberProvider.builder()
                .name(name)
                .username(username)
                .email(email)
                .password(password)
                .avatar(avatar)
                .build();
    }
}
