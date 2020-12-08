package br.com.jamadeu.gobarber.user.domain;

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
public class GoBarberProvider extends AbstractUser {
    @Builder
    public GoBarberProvider(
            Long id,
            @NotEmpty(message = "The provider name can not be empty")
                    String name,
            @NotEmpty(message = "The provider username can not be empty")
                    String username,
            @NotEmpty(message = "The provider email can not be empty")
            @Email(message = "The provider email must be in a valid email format")
                    String email,
            @NotEmpty(message = "The provider password can not be empty")
            @Size(min = 6, message = "The provider password must be at least 6 characters")
                    String password,
            String avatar,
            String authorities) {
        super(id, name, username, email, password, avatar, authorities);
    }
}
