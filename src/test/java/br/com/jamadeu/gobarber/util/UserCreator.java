package br.com.jamadeu.gobarber.util;

import br.com.jamadeu.gobarber.user.domain.GoBarberUser;

public class UserCreator {

    public static GoBarberUser createUserToBeSaved() {
        return GoBarberUser.builder()
                .name("User")
                .username("username")
                .email("user@gobarber.com")
                .password("123123")
                .isProvider(false)
                .authorities("ROLE_USER")
                .build();
    }

    public static GoBarberUser createUserToBeSavedWithPasswordEncoded() {
        return GoBarberUser.builder()
                .name("User")
                .username("username")
                .email("user@gobarber.com")
                .password("{bcrypt}$2a$10$pncFwVKOhZk60qmP8Y.Sjuuj7pBzsVT5OxZdC.OKVHjja6jC/murG")
                .isProvider(false)
                .authorities("ROLE_USER")
                .build();
    }

    public static GoBarberUser createProviderToBeSaved() {
        return GoBarberUser.builder()
                .name("Provider")
                .username("providerUsername")
                .email("provider@gobarber.com")
                .password("123123")
                .isProvider(true)
                .authorities("ROLE_USER")
                .build();
    }

    public static GoBarberUser createValidUser() {
        return GoBarberUser.builder()
                .id(1L)
                .name("User")
                .username("username")
                .email("provider@gobarber.com")
                .password("{bcrypt}$2a$10$pncFwVKOhZk60qmP8Y.Sjuuj7pBzsVT5OxZdC.OKVHjja6jC/murG")
                .authorities("ROLE_USER")
                .build();
    }

    public static GoBarberUser createValidProvider() {
        return GoBarberUser.builder()
                .id(1L)
                .name("Provider")
                .username("providerUsername")
                .email("provider@gobarber.com")
                .password("{bcrypt}$2a$10$pncFwVKOhZk60qmP8Y.Sjuuj7pBzsVT5OxZdC.OKVHjja6jC/murG")
                .isProvider(true)
                .authorities("ROLE_PROVIDER")
                .build();
    }
}
