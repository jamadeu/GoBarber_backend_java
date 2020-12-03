package br.com.jamadeu.gobarber.util;

import br.com.jamadeu.gobarber.domain.User;

public class UserCreator {

    public static User createUserToBeSaved() {
        return User.builder()
                .name("User")
                .username("username")
                .email("user@gobarber.com")
                .password("123123")
                .authorities("ROLE_USER")
                .build();
    }

    public static User createUserToBeSavedWithPasswordEncoded() {
        return User.builder()
                .name("User")
                .username("username")
                .email("user@gobarber.com")
                .password("{bcrypt}$2a$10$pncFwVKOhZk60qmP8Y.Sjuuj7pBzsVT5OxZdC.OKVHjja6jC/murG")
                .authorities("ROLE_USER")
                .build();
    }

    public static User createProviderToBeSaved() {
        return User.builder()
                .name("Provider")
                .username("providerUsername")
                .email("provider@gobarber.com")
                .password("123123")
                .authorities("ROLE_PROVIDER")
                .isProvider(true)
                .build();
    }

    public static User createValidUser() {
        return User.builder()
                .id(1L)
                .name("User")
                .username("username")
                .email("provider@gobarber.com")
                .password("{bcrypt}$2a$10$pncFwVKOhZk60qmP8Y.Sjuuj7pBzsVT5OxZdC.OKVHjja6jC/murG")
                .authorities("ROLE_USER")
                .build();
    }

    public static User createValidProvider() {
        return User.builder()
                .id(2L)
                .name("Provider")
                .username("providerUsername")
                .email("provider@gobarber.com")
                .password("{bcrypt}$2a$10$pncFwVKOhZk60qmP8Y.Sjuuj7pBzsVT5OxZdC.OKVHjja6jC/murG")
                .isProvider(true)
                .authorities("ROLE_PROVIDER")
                .build();
    }
}
