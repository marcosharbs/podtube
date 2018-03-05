package br.com.harbsti.podtube.di.components;


import javax.inject.Singleton;

import br.com.harbsti.podtube.di.App;
import br.com.harbsti.podtube.di.modules.ApplicationModule;
import dagger.Component;

/**
 * Created by marcosharbs on 01/08/17.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(App target);

}
