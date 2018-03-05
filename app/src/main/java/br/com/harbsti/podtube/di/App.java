package br.com.harbsti.podtube.di;

import android.app.Application;
import android.content.Context;

import br.com.harbsti.podtube.di.components.ApplicationComponent;
import br.com.harbsti.podtube.di.components.DaggerApplicationComponent;
import br.com.harbsti.podtube.di.modules.ApplicationModule;

/**
 * Created by marcosharbs on 01/08/17.
 */

public class App extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

}
