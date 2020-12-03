package br.com.jamadeu.gobarber.repository;

import br.com.jamadeu.gobarber.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Page<User> findByIsProviderTrue(Pageable pageable);

    Optional<User> findByUsername(String username);
}
