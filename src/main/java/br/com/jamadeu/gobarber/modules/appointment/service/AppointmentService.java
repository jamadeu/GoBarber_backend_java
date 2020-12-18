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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

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
        GoBarberUser user = checkIfUserExists(id);
        return appointmentRepository.findByUser(user, PageRequest.of(0, 5));
    }

    public Page<Appointment> findByProviderId(Long id) {
        GoBarberProvider provider = checkIfProviderExists(id);
        return appointmentRepository.findByProvider(provider, PageRequest.of(0, 5));
    }

    @Transactional
    public Appointment create(NewAppointmentRequest newAppointmentRequest) {
        checkIfUserExists(newAppointmentRequest.getUser().getId());
        checkIfProviderExists(newAppointmentRequest.getProvider().getId());
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
        if (!appointmentToReplace.getUser().getUsername().equals(appointmentSaved.getUser().getUsername())) {
            checkIfUserExists(replaceAppointmentRequest.getUser().getId());
        }
        if (!appointmentToReplace.getProvider().getUsername().equals(appointmentSaved.getProvider().getUsername())) {
            checkIfProviderExists(replaceAppointmentRequest.getProvider().getId());
        }
        appointmentRepository.save(appointmentToReplace);
    }

    public Page<Appointment> listAllProvidersAppointmentsByMonth(Long id, int month) {
        GoBarberProvider provider = checkIfProviderExists(id);
        List<Appointment> appointments = appointmentRepository.findByProvider(provider);
        return new PageImpl<>(appointments.stream().filter(
                appointment ->
                        appointment.getDate().getMonth().equals(Month.of(month))
        ).collect(Collectors.toList()));
    }

    private GoBarberUser checkIfUserExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));
    }

    private GoBarberProvider checkIfProviderExists(Long providerId) {
        return providerRepository.findById(providerId)
                .orElseThrow(() -> new BadRequestException("Provider not found"));
    }
}
