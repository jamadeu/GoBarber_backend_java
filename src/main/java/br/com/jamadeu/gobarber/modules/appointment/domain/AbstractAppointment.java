package br.com.jamadeu.gobarber.modules.appointment.domain;

import br.com.jamadeu.gobarber.modules.user.domain.GoBarberProvider;
import br.com.jamadeu.gobarber.modules.user.domain.GoBarberUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractAppointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "User id can not be empty")
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_id", nullable = false)
    private GoBarberUser user;

    @NotEmpty(message = "Provider id can not be empty")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "provider_id", nullable = false)
    private GoBarberProvider provider;

    @NotEmpty(message = "Appointment date can not be empty")
    @Future(message = "Appointment date can not be past")
    @Column(nullable = false)
    private LocalDateTime date;
}
