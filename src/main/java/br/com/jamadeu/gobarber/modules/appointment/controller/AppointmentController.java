package br.com.jamadeu.gobarber.modules.appointment.controller;

import br.com.jamadeu.gobarber.modules.appointment.domain.Appointment;
import br.com.jamadeu.gobarber.modules.appointment.requests.NewAppointmentRequest;
import br.com.jamadeu.gobarber.modules.appointment.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> findById(@PathVariable Long id) {
        return new ResponseEntity<>(appointmentService.findByIdOrThrowBadRequestException(id), HttpStatus.OK);
    }

    @GetMapping("/find-by-user/{userId}")
    public ResponseEntity<Page<Appointment>> findByUserId(@PathVariable Long userId){
        return new ResponseEntity<>(appointmentService.findByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/find-by-provider/{providerId}")
    public ResponseEntity<Page<Appointment>> findByProviderId(@PathVariable Long providerId){
        return new ResponseEntity<>(appointmentService.findByProviderId(providerId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Appointment> create(@RequestBody @Valid NewAppointmentRequest newAppointmentRequest){
        return new ResponseEntity<>(appointmentService.create(newAppointmentRequest), HttpStatus.CREATED);
    }
}
