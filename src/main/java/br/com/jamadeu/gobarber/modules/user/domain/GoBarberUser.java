package br.com.jamadeu.gobarber.modules.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(value = {"authorities"})
public class GoBarberUser extends AbstractUser {

    @Builder
    public GoBarberUser(
            Long id,
            @NotEmpty(message = "The user name can not be empty")
                    String name,
            @NotEmpty(message = "The user username can not be empty")
                    String username,
            @NotEmpty(message = "The user email can not be empty")
            @Email(message = "The user email must be in a valid email format")
                    String email,
            @NotEmpty(message = "The user password can not be empty")
            @Size(min = 6, message = "The user password must be at least 6 characters")
                    String password,
            String avatar,
            String authorities) {
        super(id, name, username, email, password, avatar, authorities);
    }
}
