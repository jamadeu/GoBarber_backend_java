package br.com.jamadeu.gobarber.modules.appointment.service;

import br.com.jamadeu.gobarber.modules.appointment.domain.Appointment;
import br.com.jamadeu.gobarber.modules.appointment.mapper.AppointmentMapper;
import br.com.jamadeu.gobarber.modules.appointment.repository.AppointmentRepository;
import br.com.jamadeu.gobarber.modules.appointment.requests.NewAppointmentRequest;
import br.com.jamadeu.gobarber.modules.appointment.requests.ReplaceAppointmentRequest;
import br.com.jamadeu.gobarber.modules.user.domain.GoBarberProvider;
import br.com.jamadeu.gobarber.modules.user.domain.GoBarberUser;
import br.com.jamadeu.gobarber.modules.user.repository.ProviderRepository;
import br.com.jamadeu.gobarber.modules.user.repository.UserRepository;
import br.com.jamadeu.gobarber.shared.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final ProviderRepository providerRepository;

    public Appointment findByIdOrThrowBadRequestException(long id) {
        return appointmentRepository.findById(id).orElseThrow(
                () -> new BadRequestException("Appointment not found")
        );
    }

    public Page<Appointment> findByUserId(Long id) {
        GoBarberUser user = userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("User not found"));
        return appointmentRepository.findByUser(user, PageRequest.of(0, 5));
    }

    public Page<Appointment> findByProviderId(Long id) {
        GoBarberProvider provider = providerRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Provider not found"));
        return appointmentRepository.findByProvider(provider, PageRequest.of(0, 5));
    }

    @Transactional
    public Appointment create(NewAppointmentRequest newAppointmentRequest) {
        if (userRepository.findById(newAppointmentRequest.getUser().getId()).isEmpty()) {
            throw new BadRequestException("User not found");
        }
        if (providerRepository.findById(newAppointmentRequest.getProvider().getId()).isEmpty()) {
            throw new BadRequestException("Provider not found");
        }
        return appointmentRepository.save(AppointmentMapper.INSTANCE.toAppointment(newAppointmentRequest));
    }

    @Transactional
    public void delete(Long id) {
        appointmentRepository.delete(findByIdOrThrowBadRequestException(id));
    }

    @Transactional
    public void replace(ReplaceAppointmentRequest replaceAppointmentRequest) {
        Appointment appointmentToReplace = AppointmentMapper.INSTANCE.toAppointment(replaceAppointmentRequest);
        Appointment appointmentSaved = findByIdOrThrowBadRequestException(appointmentToReplace.getId());
        if (!appointmentToReplace.getUser().getUsername().equals(appointmentSaved.getUser().getUsername()) &&
                userRepository.findById(appointmentToReplace.getUser().getId()).isEmpty()) {
            throw new BadRequestException("User not found");
        }
        if (!appointmentToReplace.getProvider().getUsername().equals(appointmentSaved.getProvider().getUsername()) &&
                providerRepository.findById(appointmentToReplace.getProvider().getId()).isEmpty()) {
            throw new BadRequestException("Provider not found");
        }
        appointmentRepository.save(appointmentToReplace);
    }
}
