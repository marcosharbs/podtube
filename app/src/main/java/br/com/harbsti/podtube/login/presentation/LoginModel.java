package br.com.harbsti.podtube.login.presentation;


import br.com.harbsti.podtube.login.domain.usecase.EmailUseCase;
import rx.Observable;

/**
 * Created by marcosharbs on 02/08/17.
 */

public class LoginModel implements LoginMVP.Model {

    private EmailUseCase emailUseCase;

    public LoginModel(EmailUseCase emailUseCase) {
        this.emailUseCase = emailUseCase;
    }

    @Override
    public Observable<String> saveEmail(String email) {
        return emailUseCase.saveEmail(email);
    }

}
