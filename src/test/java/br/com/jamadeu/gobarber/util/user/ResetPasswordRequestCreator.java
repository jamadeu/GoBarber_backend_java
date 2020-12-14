package br.com.jamadeu.gobarber.util.user;

import br.com.jamadeu.gobarber.modules.user.domain.GoBarberProvider;
import br.com.jamadeu.gobarber.modules.user.domain.GoBarberUser;
import br.com.jamadeu.gobarber.modules.user.requests.ResetPasswordRequest;

public class ResetPasswordRequestCreator {
    public static ResetPasswordRequest createUserResetPasswordRequest() {
        GoBarberUser user = UserCreator.createValidUser();
        return ResetPasswordRequest.builder()
                .username(user.getUsername())
                .oldPassword("123123")
                .newPassword("newPassword")
                .build();
    }

    public static ResetPasswordRequest createProviderResetPasswordRequest() {
        GoBarberProvider provider = UserCreator.createValidProvider();
        return ResetPasswordRequest.builder()
                .username(provider.getUsername())
                .oldPassword("123123")
                .newPassword("newPassword")
                .build();
    }
}
