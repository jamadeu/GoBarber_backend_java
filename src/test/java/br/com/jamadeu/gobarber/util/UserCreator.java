package br.com.jamadeu.gobarber.util;

import br.com.jamadeu.gobarber.domain.User;
import lombok.extern.log4j.Log4j2;

@Log4j2
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
                .password("123123")
                .authorities("ROLE_USER")
                .build();
    }

    public static User createValidProvider() {
        return User.builder()
                .id(2L)
                .name("Provider")
                .username("providerUsername")
                .email("provider@gobarber.com")
                .password("123123")
                .isProvider(true)
                .authorities("ROLE_PROVIDER")
                .build();
    }

//    public static User createValidUpdatedUser() {
//        return User.builder()
//                .id(1L)
//                .name("Updated User")
//                .username("username")
//                .email("provider@gobarber.com")
//                .password("123123")
//                .isProvider(true)
//                .build();
//    }
//
//    public static User createValidUpdatedProvider() {
//        return User.builder()
//                .id(2L)
//                .name("Updated Provider")
//                .email("provider@gobarber.com")
//                .password("123123")
//                .isProvider(true)
//                .build();
//    }


}
