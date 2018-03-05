package br.com.harbsti.podtube.di.components;

import br.com.harbsti.podtube.di.ActivityScope;
import br.com.harbsti.podtube.di.modules.DownloadModule;
import br.com.harbsti.podtube.downloads.presentation.DownloadsView;
import dagger.Component;

/**
 * Created by marcosharbs on 02/08/17.
 */

@ActivityScope
@Component(
        dependencies = ApplicationComponent.class,
        modules = DownloadModule.class
)
public interface DownloadComponent {

    void inject(DownloadsView view);

}
