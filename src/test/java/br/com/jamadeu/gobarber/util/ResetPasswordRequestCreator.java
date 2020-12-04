package br.com.jamadeu.gobarber.util;

import br.com.jamadeu.gobarber.domain.GoBarberUser;
import br.com.jamadeu.gobarber.requests.ResetPasswordRequest;

public class ResetPasswordRequestCreator {
    public static ResetPasswordRequest createResetPasswordRequest() {
        GoBarberUser user = UserCreator.createValidUser();
        return ResetPasswordRequest.builder()
                .username(user.getUsername())
                .oldPassword("123123")
                .newPassword("newPassword")
                .build();
    }
}
