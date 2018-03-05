package br.com.harbsti.podtube.di.components;

import br.com.harbsti.podtube.di.modules.ChannelDetailModule;
import br.com.harbsti.podtube.di.ActivityScope;
import br.com.harbsti.podtube.channels.presentation.ChannelDetailView;
import dagger.Component;

/**
 * Created by marcosharbs on 03/08/17.
 */

@ActivityScope
@Component(
        dependencies = ApplicationComponent.class,
        modules = ChannelDetailModule.class
)
public interface ChannelDetailComponent {

    void inject(ChannelDetailView view);

}
