package br.com.jamadeu.gobarber.appointment.repository;

import br.com.jamadeu.gobarber.appointment.domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

}
