package br.com.jamadeu.gobarber.modules.appointment.domain;

import br.com.jamadeu.gobarber.modules.user.domain.GoBarberProvider;
import br.com.jamadeu.gobarber.modules.user.domain.GoBarberUser;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = false)
public class Appointment extends AbstractAppointment {

    @Builder
    public Appointment(
            Long id,
            @NotEmpty(message = "User id can not be empty")
                    GoBarberUser user,
            @NotEmpty(message = "Provider id can not be empty")
                    GoBarberProvider provider,
            @NotEmpty(message = "Appointment date can not be empty")
            @Future(message = "Appointment date can not be past")
                    LocalDateTime date) {
        super(id, user, provider, date);
    }
}
