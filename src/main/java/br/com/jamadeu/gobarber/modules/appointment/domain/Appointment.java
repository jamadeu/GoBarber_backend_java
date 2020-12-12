package br.com.jamadeu.gobarber.modules.appointment.domain;

import br.com.jamadeu.gobarber.modules.user.domain.GoBarberProvider;
import br.com.jamadeu.gobarber.modules.user.domain.GoBarberUser;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = false)
public class Appointment extends AbstractAppointment {

    @Builder
    public Appointment(
            Long id,
            @NotNull(message = "User id can not be empty")
                    GoBarberUser user,
            @NotNull(message = "Provider id can not be empty")
                    GoBarberProvider provider,
            @NotNull(message = "Appointment date can not be empty")
                    LocalDateTime date) {
        super(id, user, provider, date);
    }
}
