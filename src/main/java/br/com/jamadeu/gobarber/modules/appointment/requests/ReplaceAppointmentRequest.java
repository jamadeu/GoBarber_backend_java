package br.com.jamadeu.gobarber.modules.appointment.requests;


import br.com.jamadeu.gobarber.modules.user.domain.GoBarberProvider;
import br.com.jamadeu.gobarber.modules.user.domain.GoBarberUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplaceAppointmentRequest {
    @NotNull(message = "Appointment can not be null")
    private Long id;

    @NotNull(message = "User can not be null")
    private GoBarberUser user;

    @NotNull(message = "Provider can not be null")
    private GoBarberProvider provider;

    @NotNull(message = "Appointment date can not be null")
    @Future(message = "Appointment date can not be past")
    private LocalDateTime date;
}
