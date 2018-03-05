package br.com.harbsti.podtube.di.modules;

import android.content.Context;

import br.com.harbsti.podtube.player.presentation.PlayerMVP;
import br.com.harbsti.podtube.player.presentation.PlayerPresenter;
import br.com.harbsti.podtube.player.presentation.PlayerView;
import dagger.Module;
import dagger.Provides;

/**
 * Created by marcosharbs on 29/08/17.
 */

@Module
public class PlayerModule {

    private PlayerView view;

    public PlayerModule(PlayerView view) {
        this.view = view;
    }

    @Provides
    public Context provideContext() {
        return view;
    }

    @Provides
    public PlayerMVP.Presenter providesPresenter() {
        return new PlayerPresenter();
    }

}
