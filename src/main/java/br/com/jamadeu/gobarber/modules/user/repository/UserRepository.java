package br.com.jamadeu.gobarber.modules.user.repository;

import br.com.jamadeu.gobarber.modules.user.domain.GoBarberUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<GoBarberUser, Long> {
    Optional<GoBarberUser> findByEmail(String email);

    Optional<GoBarberUser> findByUsername(String username);
}
