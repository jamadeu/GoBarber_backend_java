package br.com.jamadeu.gobarber.appointment.service;

import br.com.jamadeu.gobarber.appointment.domain.Appointment;
import br.com.jamadeu.gobarber.appointment.repository.AppointmentRepository;
import br.com.jamadeu.gobarber.shared.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    public Appointment findByIdOrThrowBadRequestException(long id) {
        return appointmentRepository.findById(id).orElseThrow(
                () -> new BadRequestException("Appointment not found")
        );
    }
}
