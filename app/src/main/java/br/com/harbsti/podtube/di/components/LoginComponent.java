package br.com.harbsti.podtube.di.components;

import br.com.harbsti.podtube.di.ActivityScope;
import br.com.harbsti.podtube.di.modules.LoginModule;
import br.com.harbsti.podtube.login.presentation.LoginView;
import dagger.Component;

/**
 * Created by marcosharbs on 02/08/17.
 */

@ActivityScope
@Component(
        dependencies = ApplicationComponent.class,
        modules = LoginModule.class
)
public interface LoginComponent {

    void inject(LoginView view);

}
