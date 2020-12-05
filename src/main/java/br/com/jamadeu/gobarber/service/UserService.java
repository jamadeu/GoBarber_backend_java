package br.com.jamadeu.gobarber.service;

import br.com.jamadeu.gobarber.domain.GoBarberUser;
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
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public Page<GoBarberUser> listAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public GoBarberUser findByIdOrThrowBadRequestException(long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new BadRequestException("User not found")
        );
    }

    public Page<GoBarberUser> listAllProviders(Pageable pageable) {
        return userRepository.findByIsProviderTrue(pageable);
    }

    @Transactional
    public GoBarberUser save(NewUserRequest newUserRequest) {
        GoBarberUser newUser = newUserRequest.toUser();
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new BadRequestException("This email is already in use: " + newUser.getEmail());
        }
        if (userRepository.findByUsername(newUser.getUsername()).isPresent()) {
            throw new BadRequestException("This username is already in use: " + newUser.getUsername());
        }
        if (newUser.isProvider()) {
            newUser.setAuthorities("ROLE_PROVIDER");
        }
        String passwordEncoded = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(passwordEncoded);
        return userRepository.save(newUser);
    }

    @Transactional
    public void replace(ReplaceUserRequest replaceUserRequest) {
        GoBarberUser userToReplace = replaceUserRequest.toUser();
        GoBarberUser savedUser = userRepository.findById(userToReplace.getId())
                .orElseThrow(() -> new BadRequestException("User not found"));
        if (!userToReplace.getEmail().equals(savedUser.getEmail()) &&
                userRepository.findByEmail(userToReplace.getEmail()).isPresent()) {
            throw new BadRequestException("This email is already in use: " + userToReplace.getEmail());
        }
        if (!userToReplace.getUsername().equals(savedUser.getUsername()) &&
                userRepository.findByUsername(userToReplace.getUsername()).isPresent()) {
            throw new BadRequestException("This username is  already in use: " + userToReplace.getUsername());
        }
        userToReplace.setPassword(savedUser.getPassword());
        userRepository.save(userToReplace);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.delete(findByIdOrThrowBadRequestException(id));
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
