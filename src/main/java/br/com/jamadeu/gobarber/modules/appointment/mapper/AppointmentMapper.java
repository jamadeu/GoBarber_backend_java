package br.com.jamadeu.gobarber.modules.appointment.mapper;

import br.com.jamadeu.gobarber.modules.appointment.domain.Appointment;
import br.com.jamadeu.gobarber.modules.appointment.requests.NewAppointmentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class AppointmentMapper {
    public static final AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);

    public abstract Appointment toAppointment(NewAppointmentRequest newAppointmentRequest);
}
