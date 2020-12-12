package br.com.jamadeu.gobarber.modules.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;

@MappedSuperclass
@ToString(exclude = {"authorities"})
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "The user name can not be empty")
    @Column(nullable = false)
    private String name;

    @NotEmpty(message = "The user username can not be empty")
    @Column(nullable = false, unique = true)
    private String username;

    @NotEmpty(message = "The user email can not be empty")
    @Email(message = "The user email must be in a valid email format")
    @Column(nullable = false, unique = true)
    private String email;

    @NotEmpty(message = "The user password can not be empty")
    @Size(min = 6, message = "The user password must be at least 6 characters")
    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Schema(description = "This is the user's avatar",
            defaultValue = "null",
            nullable = true
    )
    private String avatar;

    @Schema(description = "This is the user's roles",
            defaultValue = "ROLE_USER",
            nullable = true
    )
    private String authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(authorities));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
