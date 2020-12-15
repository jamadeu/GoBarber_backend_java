package br.com.jamadeu.gobarber.modules.appointment.domain;

import br.com.jamadeu.gobarber.modules.user.domain.GoBarberProvider;
import br.com.jamadeu.gobarber.modules.user.domain.GoBarberUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractAppointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "This is the appointment's id")
    private Long id;

    @NotNull(message = "User can not be empty")
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "This is the appointment's user")
    private GoBarberUser user;

    @NotNull(message = "Provider can not be empty")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "provider_id", nullable = false)
    @Schema(description = "This is the appointment's provider")
    private GoBarberProvider provider;

    @NotNull(message = "Appointment date can not be null")
    @Column(nullable = false)
    @Schema(description = "This is the appointment's date")
    private LocalDateTime date;
}
