package br.com.jamadeu.gobarber.modules.appointment.controller;

import br.com.jamadeu.gobarber.modules.appointment.domain.Appointment;
import br.com.jamadeu.gobarber.modules.appointment.requests.NewAppointmentRequest;
import br.com.jamadeu.gobarber.modules.appointment.requests.ReplaceAppointmentRequest;
import br.com.jamadeu.gobarber.modules.appointment.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Find appointment by id",
            tags = {"appointments"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "When appointment does not found")
    })
    public ResponseEntity<Appointment> findById(@PathVariable Long id) {
        return new ResponseEntity<>(appointmentService.findByIdOrThrowBadRequestException(id), HttpStatus.OK);
    }

    @GetMapping("/find-by-user/{userId}")
    @Operation(summary = "List all appointments by user id paginated",
            tags = {"appointments"}
    )
    public ResponseEntity<Page<Appointment>> findByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(appointmentService.findByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/find-by-provider/{providerId}")
    @Operation(summary = "List all appointments by provider id paginated",
            tags = {"appointments"}
    )
    public ResponseEntity<Page<Appointment>> findByProviderId(@PathVariable Long providerId) {
        return new ResponseEntity<>(appointmentService.findByProviderId(providerId), HttpStatus.OK);
    }

    @GetMapping("/find-by-provider/{providerId}/by-month/{month}")
    @Operation(summary = "List all provider's appointments by month paginated",
            tags = {"appointments"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "When provider does not found")
    })
    public ResponseEntity<Page<Appointment>> findAllProviderAppointmentsByMonth(@PathVariable Long providerId, @PathVariable int month) {
        return new ResponseEntity<>(appointmentService.listAllProvidersAppointmentsByMonth(providerId, month), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create a new appointment",
            description = "all args are mandatory, date can not be past",
            tags = {"users"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "When there is an error with some mandatory field")
    })
    public ResponseEntity<Appointment> create(@RequestBody @Valid NewAppointmentRequest newAppointmentRequest) {
        return new ResponseEntity<>(appointmentService.create(newAppointmentRequest), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an existing appointment")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "When appointment not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        appointmentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    @Operation(summary = "Replace an existing appointment")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "When user or provider not found")
    })
    public ResponseEntity<Void> replace(@RequestBody @Valid ReplaceAppointmentRequest replaceAppointmentRequest) {
        appointmentService.replace(replaceAppointmentRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
