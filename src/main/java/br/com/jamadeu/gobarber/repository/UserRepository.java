package br.com.jamadeu.gobarber.repository;

import br.com.jamadeu.gobarber.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
