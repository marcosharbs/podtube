package br.com.harbsti.podtube.login.domain.usecase.impl;

import br.com.harbsti.podtube.login.data.repository.EmailRepository;
import br.com.harbsti.podtube.login.domain.usecase.EmailUseCase;
import rx.Observable;

/**
 * Created by marcosharbs on 02/08/17.
 */

public class EmailUseCaseImpl implements EmailUseCase {

    private EmailRepository emailRepository;

    public EmailUseCaseImpl(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @Override
    public Observable<String> saveEmail(String email) {
        return emailRepository.saveEmail(email);
    }

    @Override
    public Observable<String> getEmail() {
        return emailRepository.getEmail();
    }

}
