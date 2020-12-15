package br.com.jamadeu.gobarber.modules.appointment.mapper;

import br.com.jamadeu.gobarber.modules.appointment.domain.Appointment;
import br.com.jamadeu.gobarber.modules.appointment.requests.NewAppointmentRequest;
import br.com.jamadeu.gobarber.modules.appointment.requests.ReplaceAppointmentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class AppointmentMapper {
    public static final AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);

    @Mapping(target = "id", ignore = true)
    public abstract Appointment toAppointment(NewAppointmentRequest newAppointmentRequest);

    public abstract Appointment toAppointment(ReplaceAppointmentRequest replaceAppointmentRequest);
}
