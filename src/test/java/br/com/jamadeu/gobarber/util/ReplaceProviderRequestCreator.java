package br.com.jamadeu.gobarber.util;

import br.com.jamadeu.gobarber.user.domain.GoBarberProvider;
import br.com.jamadeu.gobarber.user.requests.ReplaceProviderRequest;

public class ReplaceProviderRequestCreator {
    public static ReplaceProviderRequest createReplaceProviderRequest() {
        GoBarberProvider provider = UserCreator.createValidProvider();
        return ReplaceProviderRequest.builder()
                .id(provider.getId())
                .name(provider.getName())
                .username(provider.getUsername())
                .email(provider.getEmail())
                .password(provider.getPassword())
                .build();
    }
}
