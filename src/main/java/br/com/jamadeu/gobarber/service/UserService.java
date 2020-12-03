package br.com.jamadeu.gobarber.service;

import br.com.jamadeu.gobarber.domain.User;
import br.com.jamadeu.gobarber.exception.BadRequestException;
import br.com.jamadeu.gobarber.repository.UserRepository;
import br.com.jamadeu.gobarber.requests.NewUserRequest;
import br.com.jamadeu.gobarber.requests.ReplaceUserRequest;
import br.com.jamadeu.gobarber.requests.ResetPasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public Page<User> listAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User findByIdOrThrowBadRequestException(long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new BadRequestException("User not found")
        );
    }

    public Page<User> listAllProviders(Pageable pageable) {
        return userRepository.findByIsProviderTrue(pageable);
    }

    @Transactional
    public User save(NewUserRequest newUserRequest) {
        return userRepository.save(newUserRequest.toUser(userRepository));
    }

    @Transactional
    public void replace(ReplaceUserRequest replaceUserRequest) {
        userRepository.save(replaceUserRequest.toUser(userRepository));
    }

    @Transactional
    public void delete(Long id) {
        userRepository.delete(this.findByIdOrThrowBadRequestException(id));
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        userRepository.save(resetPasswordRequest.toUser(userRepository));
    }
}
