package br.com.jamadeu.gobarber.appointment.domain;

import br.com.jamadeu.gobarber.user.domain.GoBarberUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "User id can not be empty")
    @ManyToOne(targetEntity = GoBarberUser.class,
            cascade = CascadeType.ALL,
            optional = false
    )
    private Long userId;

    @NotEmpty(message = "Provider id can not be empty")
    @ManyToOne(targetEntity = GoBarberUser.class,
            cascade = CascadeType.ALL,
            optional = false
    )
    private Long providerId;

    @NotEmpty(message = "Appointment date can not be empty")
    @Future(message = "Appointment date can not be past")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date date;


}
