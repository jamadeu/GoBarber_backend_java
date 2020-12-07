package br.com.jamadeu.gobarber.appointment.service;

import br.com.jamadeu.gobarber.appointment.domain.Appointment;
import br.com.jamadeu.gobarber.appointment.repository.AppointmentRepository;
import br.com.jamadeu.gobarber.appointment.requests.NewAppointmentRequest;
import br.com.jamadeu.gobarber.shared.exception.BadRequestException;
import br.com.jamadeu.gobarber.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public Appointment findByIdOrThrowBadRequestException(long id) {
        return appointmentRepository.findById(id).orElseThrow(
                () -> new BadRequestException("Appointment not found")
        );
    }

    public Page<Appointment> findByUserId(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new BadRequestException("User not found");
        }
        return appointmentRepository.findByUserId(userId, PageRequest.of(0, 5));
    }

    public Page<Appointment> findByProviderId(Long providerId) {
        if (userRepository.findById(providerId).isEmpty()) {
            throw new BadRequestException("Provider not found");
        }
        return appointmentRepository.findByUserId(providerId, PageRequest.of(0, 5));
    }

    public Appointment create(NewAppointmentRequest newAppointmentRequest) {
        if (userRepository.findById(newAppointmentRequest.getUserId()).isEmpty()) {
            throw new BadRequestException("User not found");
        }
        if (userRepository.findById(newAppointmentRequest.getProviderId()).isEmpty()) {
            throw new BadRequestException("Provider not found");
        }
        return appointmentRepository.save(newAppointmentRequest.toAppointment());
    }
}
