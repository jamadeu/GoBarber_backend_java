package br.com.jamadeu.gobarber.repository;

import br.com.jamadeu.gobarber.domain.User;
import br.com.jamadeu.gobarber.util.UserCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Tests for UserRepository")
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Save persists user when successful")
    void save_PersistUser_WhenSuccessful(){
        User userToBeSaved = UserCreator.createUserToBeSaved();
        User userSaved = this.userRepository.save(userToBeSaved);

        Assertions.assertThat(userSaved).isNotNull();
        Assertions.assertThat(userSaved.getId()).isNotNull();
        Assertions.assertThat(userSaved.getName()).isEqualTo(userToBeSaved.getName());
        Assertions.assertThat(userSaved.getEmail()).isEqualTo(userToBeSaved.getEmail());
        Assertions.assertThat(userSaved.getPassword()).isEqualTo(userToBeSaved.getPassword());
    }
}