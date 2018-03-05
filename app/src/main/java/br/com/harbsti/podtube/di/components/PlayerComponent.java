package br.com.harbsti.podtube.di.components;

import br.com.harbsti.podtube.di.ActivityScope;
import br.com.harbsti.podtube.di.modules.PlayerModule;
import br.com.harbsti.podtube.player.presentation.PlayerView;
import dagger.Component;

/**
 * Created by marcosharbs on 29/08/17.
 */

@ActivityScope
@Component(
        dependencies = ApplicationComponent.class,
        modules = PlayerModule.class
)
public interface PlayerComponent {

    void inject(PlayerView view);

}
