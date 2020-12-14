package br.com.jamadeu.gobarber.util.user;

import br.com.jamadeu.gobarber.modules.user.domain.GoBarberProvider;
import br.com.jamadeu.gobarber.modules.user.requests.NewProviderRequest;

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
