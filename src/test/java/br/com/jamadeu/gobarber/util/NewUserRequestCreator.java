package br.com.jamadeu.gobarber.util;

import br.com.jamadeu.gobarber.domain.GoBarberUser;
import br.com.jamadeu.gobarber.requests.NewUserRequest;

public class NewUserRequestCreator {
    public static NewUserRequest createNewUserRequest() {
        GoBarberUser user = UserCreator.createUserToBeSaved();
        return NewUserRequest.builder()
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getAuthorities().toString())
                .build();
    }

    public static NewUserRequest createNewProviderRequest() {
        GoBarberUser provider = UserCreator.createProviderToBeSaved();
        return NewUserRequest.builder()
                .name(provider.getName())
                .username(provider.getUsername())
                .email(provider.getEmail())
                .password(provider.getPassword())
                .isProvider(provider.isProvider())
                .authorities(provider.getAuthorities().toString())
                .build();
    }
}
