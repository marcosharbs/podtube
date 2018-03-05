package br.com.harbsti.podtube.di.components;

import br.com.harbsti.podtube.di.modules.SubscriptionsModule;
import br.com.harbsti.podtube.channels.presentation.SubscriptionsView;
import br.com.harbsti.podtube.di.ActivityScope;
import dagger.Component;

/**
 * Created by marcosharbs on 29/08/17.
 */

@ActivityScope
@Component(
        dependencies = ApplicationComponent.class,
        modules = SubscriptionsModule.class
)
public interface SubscriptionsComponent {

    void inject(SubscriptionsView view);

}
