package br.com.jamadeu.gobarber.service;

import br.com.jamadeu.gobarber.domain.User;
import br.com.jamadeu.gobarber.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Page<User> listAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
