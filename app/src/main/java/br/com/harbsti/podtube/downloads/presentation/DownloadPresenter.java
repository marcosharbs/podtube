package br.com.harbsti.podtube.downloads.presentation;

import java.util.List;

import br.com.harbsti.podtube.downloads.presentation.model.Download;
import rx.Scheduler;
import rx.Subscriber;

/**
 * Created by marcosharbs on 07/12/16.
 */

public class DownloadPresenter implements DownloadMVP.Presenter {

    private DownloadMVP.View view;
    private DownloadMVP.Model model;
    private Scheduler subscriberSchduler;
    private Scheduler observerScheduler;

    public DownloadPresenter(DownloadMVP.Model model,
                             Scheduler subscriberSchduler,
                             Scheduler observerScheduler) {

        this.model = model;
        this.subscriberSchduler = subscriberSchduler;
        this.observerScheduler = observerScheduler;
    }

    @Override
    public void setView(DownloadMVP.View view) {
        this.view = view;
    }

    @Override
    public void getDownloads() {
        model.getDownloads()
                .subscribeOn(subscriberSchduler)
                .observeOn(observerScheduler)
                .subscribe(new Subscriber<List<Download>>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        view.onError(e);
                    }

                    @Override
                    public void onNext(List<Download> downloads) {
                        view.onDownloadsLoaded(downloads);
                    }
                });
    }

    @Override
    public void deleteDownload(Download download) {
        model.deleteDownload(download.id())
                .subscribeOn(subscriberSchduler)
                .observeOn(observerScheduler)
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        view.onError(e);
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        view.onDownloadRemoved();
                    }
                });
    }

}
