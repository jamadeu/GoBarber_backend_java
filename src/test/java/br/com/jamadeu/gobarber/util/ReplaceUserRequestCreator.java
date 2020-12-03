package br.com.jamadeu.gobarber.util;

import br.com.jamadeu.gobarber.domain.User;
import br.com.jamadeu.gobarber.requests.ReplaceUserRequest;

public class ReplaceUserRequestCreator {
    public static ReplaceUserRequest createReplaceUserRequest() {
        User user = UserCreator.createValidUser();
        return ReplaceUserRequest.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
