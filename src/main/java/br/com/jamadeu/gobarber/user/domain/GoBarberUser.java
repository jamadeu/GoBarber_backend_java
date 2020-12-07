package br.com.jamadeu.gobarber.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;

@Data
@NoArgsConstructor
@SuperBuilder
@Entity
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(value = {"authorities"})
public class GoBarberUser extends AbstractUser {

}
