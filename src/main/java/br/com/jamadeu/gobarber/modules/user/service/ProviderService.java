package br.com.jamadeu.gobarber.modules.user.service;

import br.com.jamadeu.gobarber.modules.user.domain.GoBarberProvider;
import br.com.jamadeu.gobarber.modules.user.mapper.UserMapper;
import br.com.jamadeu.gobarber.modules.user.repository.ProviderRepository;
import br.com.jamadeu.gobarber.modules.user.requests.NewProviderRequest;
import br.com.jamadeu.gobarber.modules.user.requests.ReplaceProviderRequest;
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
public class ProviderService implements UserDetailsService {
    private final ProviderRepository providerRepository;
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public Page<GoBarberProvider> listAll(Pageable pageable) {
        return providerRepository.findAll(pageable);
    }

    public GoBarberProvider findByIdOrThrowBadRequestException(long id) {
        return checkIfProviderExists(id);
    }

    @Transactional
    public GoBarberProvider save(NewProviderRequest newProviderRequest) {
        GoBarberProvider newProvider = UserMapper.INSTANCE.toProvider(newProviderRequest);
        checkIfEmailAlreadyInUse(newProvider.getEmail());
        checkIfUsernameAlreadyInUse(newProvider.getUsername());
        newProvider.setAuthorities("ROLE_PROVIDER");
        String passwordEncoded = passwordEncoder.encode(newProvider.getPassword());
        newProvider.setPassword(passwordEncoded);
        return providerRepository.save(newProvider);
    }

    @Transactional
    public void replace(ReplaceProviderRequest replaceProviderRequest) {
        GoBarberProvider providerToReplace = UserMapper.INSTANCE.toProvider(replaceProviderRequest);
        GoBarberProvider savedProvider = checkIfProviderExists(providerToReplace.getId());
        if (!providerToReplace.getEmail().equals(savedProvider.getEmail())) {
            checkIfEmailAlreadyInUse(providerToReplace.getEmail());
        }
        if (!providerToReplace.getUsername().equals(savedProvider.getUsername())) {
            checkIfUsernameAlreadyInUse(providerToReplace.getUsername());
        }
        providerToReplace.setPassword(savedProvider.getPassword());
        providerRepository.save(providerToReplace);
    }

    @Transactional
    public void delete(Long id) {
        providerRepository.delete(findByIdOrThrowBadRequestException(id));
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return checkIfProviderExists(username);
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        GoBarberProvider provider = checkIfProviderExists(resetPasswordRequest.getUsername());
        if (!passwordEncoder.matches(resetPasswordRequest.getOldPassword(), provider.getPassword())) {
            throw new BadRequestException("Old password is wrong");
        }
        provider.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        providerRepository.save(provider);
    }

    private GoBarberProvider checkIfProviderExists(Long userId) {
        return providerRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("Provider not found"));
    }

    private GoBarberProvider checkIfProviderExists(String username) {
        return providerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Provider not found"));
    }

    private void checkIfEmailAlreadyInUse(String email) {
        if (providerRepository.findByEmail(email).isPresent()) {
            throw new BadRequestException("This email is already in use: " + email);
        }
    }

    private void checkIfUsernameAlreadyInUse(String username) {
        if (providerRepository.findByUsername(username).isPresent()) {
            throw new BadRequestException("This username is already in use: " + username);
        }
    }
}
