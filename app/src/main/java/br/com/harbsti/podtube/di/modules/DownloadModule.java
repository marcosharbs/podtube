package br.com.harbsti.podtube.di.modules;

import android.content.Context;

import br.com.harbsti.podtube.utils.ServiceHelper;
import br.com.harbsti.podtube.downloads.data.repository.Mp3Repository;
import br.com.harbsti.podtube.downloads.data.repository.impl.retrofit.YoutubeInMp3Repository;
import br.com.harbsti.podtube.downloads.data.repository.impl.retrofit.service.Mp3Service;
import br.com.harbsti.podtube.downloads.data.mapper.DownloadDataRealmMapper;
import br.com.harbsti.podtube.downloads.data.mapper.DownloadRealmDataMapper;
import br.com.harbsti.podtube.downloads.data.repository.DownloadRepository;
import br.com.harbsti.podtube.downloads.data.repository.impl.realm.DownloadRealmRepository;
import br.com.harbsti.podtube.downloads.domain.mapper.DownloadDataMapper;
import br.com.harbsti.podtube.downloads.domain.mapper.DownloadDomainMapper;
import br.com.harbsti.podtube.downloads.domain.usecase.DownloadVideoUseCase;
import br.com.harbsti.podtube.downloads.domain.usecase.impl.DownloadVideoUseCaseImpl;
import br.com.harbsti.podtube.downloads.presentation.DownloadMVP;
import br.com.harbsti.podtube.downloads.presentation.DownloadModel;
import br.com.harbsti.podtube.downloads.presentation.DownloadPresenter;
import br.com.harbsti.podtube.downloads.presentation.DownloadsView;
import br.com.harbsti.podtube.downloads.presentation.mapper.DownloadViewMapper;
import dagger.Module;
import dagger.Provides;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by marcosharbs on 02/08/17.
 */

@Module
public class DownloadModule {

    private DownloadsView view;

    public DownloadModule(DownloadsView view) {
        this.view = view;
    }

    @Provides
    public Context provideContext() {
        return view.getActivity();
    }

    @Provides
    public DownloadMVP.Presenter providePresenter(DownloadMVP.Model model) {
        return new DownloadPresenter(model, Schedulers.newThread(), AndroidSchedulers.mainThread());
    }

    @Provides
    public DownloadMVP.Model provideModel(DownloadVideoUseCase useCase,
                                          DownloadViewMapper mapper){

        return new DownloadModel(useCase, mapper);
    }

    @Provides
    public DownloadVideoUseCase providesDownloadVideoUseCase(Mp3Repository mp3Repository,
                                                             DownloadRepository downloadRepository,
                                                             DownloadDataMapper downloadDataMapper,
                                                             DownloadDomainMapper downloadDomainMapper) {
        return new DownloadVideoUseCaseImpl(mp3Repository,
                downloadRepository,
                downloadDataMapper,
                downloadDomainMapper);
    }

    @Provides
    public DownloadRepository provideDownloadRepository(DownloadRealmDataMapper downloadRealmDataMapper,
                                                        DownloadDataRealmMapper downloadDataRealmMapper) {
        return new DownloadRealmRepository(downloadRealmDataMapper, downloadDataRealmMapper);
    }

    @Provides
    public Mp3Repository providesMp3Repository(Mp3Service mp3Service) {
        return new YoutubeInMp3Repository(mp3Service);
    }

    @Provides
    public Mp3Service providesMp3Service() {
        return ServiceHelper.createRetrfitService(Mp3Service.class, "http://www.youtubeinmp3.com");
    }

    @Provides
    public DownloadViewMapper providesDownloadViewMapper() {
        return new DownloadViewMapper();
    }

    @Provides
    public DownloadDataMapper providesDownloadDataMapper() {
        return new DownloadDataMapper();
    }

    @Provides
    public DownloadDomainMapper provideDownloadDomainMapper() {
        return new DownloadDomainMapper();
    }

    @Provides
    public DownloadRealmDataMapper provideDownloadRealmDataMapper() {
        return new DownloadRealmDataMapper();
    }

    @Provides
    public DownloadDataRealmMapper provideDownloadDataRealmMapper(){
        return new DownloadDataRealmMapper();
    }

}
