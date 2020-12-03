package br.com.jamadeu.gobarber.util;

import br.com.jamadeu.gobarber.domain.User;
import br.com.jamadeu.gobarber.requests.NewUserRequest;

public class NewUserRequestCreator {
    public static NewUserRequest createNewUserRequest() {
        User user = UserCreator.createUserToBeSaved();
        return NewUserRequest.builder()
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
