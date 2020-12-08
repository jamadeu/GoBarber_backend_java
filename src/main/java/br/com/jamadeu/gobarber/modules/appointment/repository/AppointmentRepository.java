package br.com.jamadeu.gobarber.modules.appointment.repository;

import br.com.jamadeu.gobarber.modules.appointment.domain.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Page<Appointment> findByUserId(Long id, Pageable pageable);

    Page<Appointment> findByProviderId(Long id, Pageable pageable);

    Optional<Appointment> findByProviderIdAndDate(Long providerId, Date date);
}
