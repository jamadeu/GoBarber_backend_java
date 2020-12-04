package br.com.jamadeu.gobarber.repository;

import br.com.jamadeu.gobarber.domain.GoBarberUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<GoBarberUser, Long> {
    Optional<GoBarberUser> findByEmail(String email);

    Page<GoBarberUser> findByIsProviderTrue(Pageable pageable);

    Optional<GoBarberUser> findByUsername(String username);
}
