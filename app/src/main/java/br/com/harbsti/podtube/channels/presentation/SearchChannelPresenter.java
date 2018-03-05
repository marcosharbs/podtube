package br.com.harbsti.podtube.channels.presentation;

import br.com.harbsti.podtube.channels.presentation.model.ChannelSearchResult;
import rx.Scheduler;
import rx.Subscriber;

/**
 * Created by marcosharbs on 05/12/16.
 */

public class SearchChannelPresenter implements SearchChannelMVP.Presenter {

    private SearchChannelMVP.View view;
    private SearchChannelMVP.Model model;
    private Scheduler subscriberSchduler;
    private Scheduler observerScheduler;

    public SearchChannelPresenter(SearchChannelMVP.Model model,
                                  Scheduler subscriberSchduler,
                                  Scheduler observerScheduler) {

        this.model = model;
        this.subscriberSchduler = subscriberSchduler;
        this.observerScheduler = observerScheduler;
    }

    @Override
    public void setView(SearchChannelMVP.View view) {
        this.view = view;
    }

    @Override
    public void searchChannels(String term, String offset) {
        model.getChannels(term, offset)
                .subscribeOn(subscriberSchduler)
                .observeOn(observerScheduler)
                .subscribe(new Subscriber<ChannelSearchResult>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        view.onError(e);
                    }

                    @Override
                    public void onNext(ChannelSearchResult channelSearchResult) {
                        view.onChannelsLoaded(channelSearchResult);
                    }
                });
    }

}
