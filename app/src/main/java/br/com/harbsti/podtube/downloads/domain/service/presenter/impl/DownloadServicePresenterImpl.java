package br.com.harbsti.podtube.downloads.domain.service.presenter.impl;

import br.com.harbsti.podtube.downloads.presentation.mapper.DownloadDomainMapper;
import br.com.harbsti.podtube.downloads.domain.usecase.DownloadVideoUseCase;
import br.com.harbsti.podtube.downloads.presentation.mapper.DownloadViewMapper;
import br.com.harbsti.podtube.downloads.presentation.model.Download;
import br.com.harbsti.podtube.channels.presentation.model.Video;
import br.com.harbsti.podtube.downloads.domain.service.DownloadService;
import br.com.harbsti.podtube.downloads.domain.service.presenter.DownloadServicePresenter;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by marcosharbs on 06/12/16.
 */

public class DownloadServicePresenterImpl implements DownloadServicePresenter {

    private DownloadService downloadService;
    private DownloadVideoUseCase downloadVideoUseCase;
    private DownloadViewMapper downloadViewMapper;
    private DownloadDomainMapper downloadDomainMapper;
    private Scheduler subscriberSchduler;
    private Scheduler observerScheduler;

    public DownloadServicePresenterImpl(DownloadService downloadService,
                                        DownloadVideoUseCase downloadVideoUseCase,
                                        DownloadViewMapper downloadViewMapper,
                                        DownloadDomainMapper downloadDomainMapper,
                                        Scheduler subscriberSchduler,
                                        Scheduler observerScheduler) {

        this.downloadService = downloadService;
        this.downloadVideoUseCase = downloadVideoUseCase;
        this.downloadViewMapper = downloadViewMapper;
        this.subscriberSchduler = subscriberSchduler;
        this.observerScheduler = observerScheduler;
        this.downloadDomainMapper = downloadDomainMapper;
    }

    @Override
    public void persistDownload(Video video) {
        Observable.just(video)
                .flatMap(new Func1<Video, Observable<Download>>() {
                    @Override
                    public Observable<Download> call(Video video) {
                        return Observable.just(Download.builder()
                                .id(video.id())
                                .image(video.image())
                                .mp3File(null)
                                .pusblishDate(video.pusblishDate())
                                .title(video.title())
                                .build());
                    }
                })
                .compose(downloadDomainMapper.getTransformer())
                .flatMap(new Func1<br.com.harbsti.podtube.downloads.domain.model.Download, Observable<br.com.harbsti.podtube.downloads.domain.model.Download>>() {
                    @Override
                    public Observable<br.com.harbsti.podtube.downloads.domain.model.Download> call(br.com.harbsti.podtube.downloads.domain.model.Download download) {
                        return downloadVideoUseCase.persist(download);
                    }
                })
                .compose(downloadViewMapper.getTransformer())
                .subscribeOn(subscriberSchduler)
                .observeOn(observerScheduler)
                .subscribe(new Subscriber<Download>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        downloadService.onError(e);
                    }

                    @Override
                    public void onNext(Download download) {
                        downloadService.onDownloadPersisted(download);
                    }
                });

    }

    @Override
    public void downloadVideo(Download download, final String dirToSave) {
        Observable.just(download)
                .compose(downloadDomainMapper.getTransformer())
                .flatMap(new Func1<br.com.harbsti.podtube.downloads.domain.model.Download, Observable<br.com.harbsti.podtube.downloads.domain.model.Download>>() {
                    @Override
                    public Observable<br.com.harbsti.podtube.downloads.domain.model.Download> call(br.com.harbsti.podtube.downloads.domain.model.Download download) {
                        return downloadVideoUseCase.downloadMp3(download, dirToSave);
                    }
                })
                .compose(downloadViewMapper.getTransformer())
                .subscribeOn(subscriberSchduler)
                .observeOn(observerScheduler)
                .subscribe(new Subscriber<Download>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        downloadService.onError(e);
                    }

                    @Override
                    public void onNext(Download download) {
                        downloadService.onDownloadCompleted(download);
                    }
                });
    }

    @Override
    public void deleteDownload(String id) {
        downloadVideoUseCase.deleteDownload(id)
                .subscribeOn(subscriberSchduler)
                .observeOn(observerScheduler)
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        //downloadService.onError(e);
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        downloadService.onDownloadDeleted();
                    }
                });

    }
}
