package br.com.jamadeu.gobarber.service;

import br.com.jamadeu.gobarber.domain.User;
import br.com.jamadeu.gobarber.exception.BadRequestException;
import br.com.jamadeu.gobarber.repository.UserRepository;
import br.com.jamadeu.gobarber.requests.NewUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Page<User> listAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User findByIdOrThrowBadRequestException(long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new BadRequestException("User not found")
        );
    }

    @Transactional
    public User save(NewUserRequest newUserRequest) {
        return userRepository.save(newUserRequest.toUser(userRepository));
    }
}
