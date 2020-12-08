package br.com.jamadeu.gobarber.util;

import br.com.jamadeu.gobarber.user.domain.GoBarberProvider;
import br.com.jamadeu.gobarber.user.requests.NewProviderRequest;

public class NewProviderRequestCreator {

    public static NewProviderRequest createNewProviderRequest() {
        GoBarberProvider provider = UserCreator.createProviderToBeSaved();
        return NewProviderRequest.builder()
                .name(provider.getName())
                .username(provider.getUsername())
                .email(provider.getEmail())
                .password(provider.getPassword())
                .build();
    }
}
