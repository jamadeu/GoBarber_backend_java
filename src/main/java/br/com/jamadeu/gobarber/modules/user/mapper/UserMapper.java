package br.com.jamadeu.gobarber.modules.user.mapper;

import br.com.jamadeu.gobarber.modules.user.domain.GoBarberProvider;
import br.com.jamadeu.gobarber.modules.user.domain.GoBarberUser;
import br.com.jamadeu.gobarber.modules.user.requests.NewProviderRequest;
import br.com.jamadeu.gobarber.modules.user.requests.NewUserRequest;
import br.com.jamadeu.gobarber.modules.user.requests.ReplaceProviderRequest;
import br.com.jamadeu.gobarber.modules.user.requests.ReplaceUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    public static final UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    public abstract GoBarberUser toUser(NewUserRequest newUserRequest);

    @Mapping(target = "authorities", ignore = true)
    public abstract GoBarberUser toUser(ReplaceUserRequest replaceUserRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    public abstract GoBarberProvider toProvider(NewProviderRequest newProviderRequest);

    @Mapping(target = "authorities", ignore = true)
    public abstract GoBarberProvider toProvider(ReplaceProviderRequest replaceProviderRequest);
}
