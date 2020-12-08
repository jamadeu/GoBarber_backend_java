package br.com.jamadeu.gobarber.modules.appointment.requests;


import br.com.jamadeu.gobarber.modules.appointment.domain.Appointment;
import br.com.jamadeu.gobarber.modules.user.domain.GoBarberUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewAppointmentRequest {
    @NotNull(message = "User id can not be null")
    @ManyToOne(targetEntity = GoBarberUser.class,
            cascade = CascadeType.ALL,
            optional = false
    )
    private Long userId;

    @NotNull(message = "Provider id can not be null")
    @ManyToOne(targetEntity = GoBarberUser.class,
            cascade = CascadeType.ALL,
            optional = false
    )
    private Long providerId;

    @NotNull(message = "Appointment date can not be null")
    @Future(message = "Appointment date can not be past")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date date;

    public Appointment toAppointment() {
        return Appointment.builder()
                .userId(userId)
                .providerId(providerId)
                .date(date)
                .build();
    }
}
