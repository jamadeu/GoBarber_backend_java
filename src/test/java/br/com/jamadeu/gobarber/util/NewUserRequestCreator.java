package br.com.jamadeu.gobarber.util;

import br.com.jamadeu.gobarber.modules.user.domain.GoBarberUser;
import br.com.jamadeu.gobarber.modules.user.requests.NewUserRequest;

public class NewUserRequestCreator {
    public static NewUserRequest createNewUserRequest() {
        GoBarberUser user = UserCreator.createUserToBeSaved();
        return NewUserRequest.builder()
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
