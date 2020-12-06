package br.com.jamadeu.gobarber.user.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

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
}
