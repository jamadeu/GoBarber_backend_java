package br.com.jamadeu.gobarber.modules.user.service;

import br.com.jamadeu.gobarber.modules.user.domain.GoBarberUser;
import br.com.jamadeu.gobarber.modules.user.mapper.UserMapper;
import br.com.jamadeu.gobarber.modules.user.repository.UserRepository;
import br.com.jamadeu.gobarber.modules.user.requests.NewUserRequest;
import br.com.jamadeu.gobarber.modules.user.requests.ReplaceUserRequest;
import br.com.jamadeu.gobarber.modules.user.requests.ResetPasswordRequest;
import br.com.jamadeu.gobarber.shared.exception.BadRequestException;
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
        return checkIfUserExists(id);
    }

    @Transactional
    public GoBarberUser save(NewUserRequest newUserRequest) {
        GoBarberUser newUser = UserMapper.INSTANCE.toUser(newUserRequest);
        checkIfEmailAlreadyInUse(newUser.getEmail());
        checkIfUsernameAlreadyInUse(newUser.getUsername());
        newUser.setAuthorities("ROLE_USER");
        String passwordEncoded = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(passwordEncoded);
        return userRepository.save(newUser);
    }

    @Transactional
    public void replace(ReplaceUserRequest replaceUserRequest) {
        GoBarberUser userToReplace = UserMapper.INSTANCE.toUser(replaceUserRequest);
        GoBarberUser savedUser = checkIfUserExists(userToReplace.getId());
        if (!userToReplace.getEmail().equals(savedUser.getEmail())) {
            checkIfEmailAlreadyInUse(userToReplace.getEmail());
        }
        if (!userToReplace.getUsername().equals(savedUser.getUsername())) {
            checkIfUsernameAlreadyInUse(userToReplace.getUsername());
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
        return checkIfUserExists(username);
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        GoBarberUser user = checkIfUserExists(resetPasswordRequest.getUsername());
        if (!passwordEncoder.matches(resetPasswordRequest.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Old password is wrong");
        }
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        userRepository.save(user);
    }

    private GoBarberUser checkIfUserExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));
    }

    private GoBarberUser checkIfUserExists(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private void checkIfEmailAlreadyInUse(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new BadRequestException("This email is already in use: " + email);
        }
    }

    private void checkIfUsernameAlreadyInUse(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new BadRequestException("This username is already in use: " + username);
        }
    }
}
