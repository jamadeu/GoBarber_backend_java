package br.com.jamadeu.gobarber.util;

import br.com.jamadeu.gobarber.domain.User;

public class UserCreator {

    public static User createUserToBeSaved() {
        return User.builder()
                .name("User")
                .email("user@gobarber.com")
                .password("123123")
                .build();
    }

    public static User createProviderToBeSaved() {
        return User.builder()
                .name("Provider")
                .email("provider@gobarber.com")
                .password("123123")
                .isProvider(true)
                .build();
    }

    public static User createValidUser() {
        return User.builder()
                .id(1L)
                .name("User")
                .email("provider@gobarber.com")
                .password("123123")
                .isProvider(true)
                .build();
    }

    public static User createValidProvider() {
        return User.builder()
                .id(2L)
                .name("Provider")
                .email("provider@gobarber.com")
                .password("123123")
                .isProvider(true)
                .build();
    }

    public static User createValidUpdatedUser() {
        return User.builder()
                .id(1L)
                .name("Updated User")
                .email("provider@gobarber.com")
                .password("123123")
                .isProvider(true)
                .build();
    }

    public static User createValidUpdatedProvider() {
        return User.builder()
                .id(2L)
                .name("Updated Provider")
                .email("provider@gobarber.com")
                .password("123123")
                .isProvider(true)
                .build();
    }


}
