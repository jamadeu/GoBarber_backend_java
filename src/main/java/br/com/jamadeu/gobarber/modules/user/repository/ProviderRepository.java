package br.com.jamadeu.gobarber.modules.user.repository;

import br.com.jamadeu.gobarber.modules.user.domain.GoBarberProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<GoBarberProvider, Long> {
    Optional<GoBarberProvider> findByEmail(String email);

    Optional<GoBarberProvider> findByUsername(String username);
}
