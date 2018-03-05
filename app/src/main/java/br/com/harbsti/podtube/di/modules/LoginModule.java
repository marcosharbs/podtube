package br.com.harbsti.podtube.di.modules;

import android.content.Context;

import br.com.harbsti.podtube.login.data.repository.EmailRepository;
import br.com.harbsti.podtube.login.data.repository.impl.preferences.EmailPreferencesRepository;
import br.com.harbsti.podtube.login.domain.usecase.EmailUseCase;
import br.com.harbsti.podtube.login.domain.usecase.impl.EmailUseCaseImpl;
import br.com.harbsti.podtube.login.presentation.LoginMVP;
import br.com.harbsti.podtube.login.presentation.LoginModel;
import br.com.harbsti.podtube.login.presentation.LoginPresenter;
import br.com.harbsti.podtube.login.presentation.LoginView;
import dagger.Module;
import dagger.Provides;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by marcosharbs on 02/08/17.
 */

@Module
public class LoginModule {

    private LoginView view;

    public LoginModule(LoginView view) {
        this.view = view;
    }

    @Provides
    public Context provideContext() {
        return view;
    }

    @Provides
    public LoginMVP.Model provideLoginModel(EmailUseCase emailUseCase) {
        return new LoginModel(emailUseCase);
    }

    @Provides
    public EmailUseCase provideEmailUseCase(EmailRepository emailRepository) {
        return new EmailUseCaseImpl(emailRepository);
    }

    @Provides
    public EmailRepository provideEmailRepository(Context context) {
        return new EmailPreferencesRepository(context);
    }

    @Provides
    public LoginMVP.Presenter provideLoginPresenter(LoginMVP.Model model) {
        return new LoginPresenter(model, Schedulers.newThread(), AndroidSchedulers.mainThread());
    }

}
