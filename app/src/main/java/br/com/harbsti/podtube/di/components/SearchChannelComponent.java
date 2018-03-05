package br.com.harbsti.podtube.di.components;

import br.com.harbsti.podtube.di.modules.SearchChannelModule;
import br.com.harbsti.podtube.channels.presentation.SearchChannelView;
import br.com.harbsti.podtube.di.ActivityScope;
import dagger.Component;

/**
 * Created by marcosharbs on 29/08/17.
 */

@ActivityScope
@Component(
        dependencies = ApplicationComponent.class,
        modules = SearchChannelModule.class
)
public interface SearchChannelComponent {

    void inject(SearchChannelView view);

}
