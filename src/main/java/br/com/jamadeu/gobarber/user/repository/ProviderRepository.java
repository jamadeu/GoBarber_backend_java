package br.com.jamadeu.gobarber.user.repository;

import br.com.jamadeu.gobarber.user.domain.GoBarberProvider;
import br.com.jamadeu.gobarber.user.domain.GoBarberUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<GoBarberUser, Long> {
    Optional<GoBarberProvider> findByEmail(String email);

    Optional<GoBarberProvider> findByUsername(String username);
}
