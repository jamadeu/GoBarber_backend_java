package br.com.jamadeu.gobarber.controller;

import br.com.jamadeu.gobarber.domain.User;
import br.com.jamadeu.gobarber.service.UserService;
import br.com.jamadeu.gobarber.util.UserCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setup(){
        PageImpl<User> userPage = new PageImpl<>(List.of(UserCreator.createValidUser()));
        PageImpl<User> providerPage = new PageImpl<>(List.of(UserCreator.createValidProvider()));
    }



}