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
        return providerRepository.findById(id).orElseThrow(
                () -> new BadRequestException("Provider not found")
        );
    }

    @Transactional
    public GoBarberProvider save(NewProviderRequest newProviderRequest) {
        GoBarberProvider newProvider = UserMapper.INSTANCE.toProvider(newProviderRequest);
        if (providerRepository.findByEmail(newProvider.getEmail()).isPresent()) {
            throw new BadRequestException("This email is already in use: " + newProvider.getEmail());
        }
        if (providerRepository.findByUsername(newProvider.getUsername()).isPresent()) {
            throw new BadRequestException("This username is already in use: " + newProvider.getUsername());
        }
        newProvider.setAuthorities("ROLE_PROVIDER");
        String passwordEncoded = passwordEncoder.encode(newProvider.getPassword());
        newProvider.setPassword(passwordEncoded);
        return providerRepository.save(newProvider);
    }

    @Transactional
    public void replace(ReplaceProviderRequest replaceProviderRequest) {
        GoBarberProvider providerToReplace = UserMapper.INSTANCE.toProvider(replaceProviderRequest);
        GoBarberProvider savedProvider = providerRepository.findById(providerToReplace.getId())
                .orElseThrow(() -> new BadRequestException("Provider not found"));
        if (!providerToReplace.getEmail().equals(savedProvider.getEmail()) &&
                providerRepository.findByEmail(providerToReplace.getEmail()).isPresent()) {
            throw new BadRequestException("This email is already in use: " + providerToReplace.getEmail());
        }
        if (!providerToReplace.getUsername().equals(savedProvider.getUsername()) &&
                providerRepository.findByUsername(providerToReplace.getUsername()).isPresent()) {
            throw new BadRequestException("This username is already in use: " + providerToReplace.getUsername());
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
        return providerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        GoBarberProvider provider = providerRepository.findByUsername(resetPasswordRequest.getUsername())
                .orElseThrow(() -> new BadRequestException("Provider not found"));
        if (!passwordEncoder.matches(resetPasswordRequest.getOldPassword(), provider.getPassword())) {
            throw new BadRequestException("Old password is wrong");
        }
        provider.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        providerRepository.save(provider);
    }
}
