package br.com.jamadeu.gobarber.repository;

import br.com.jamadeu.gobarber.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
