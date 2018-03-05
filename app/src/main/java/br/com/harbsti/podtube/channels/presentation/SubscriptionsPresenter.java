package br.com.harbsti.podtube.channels.presentation;

import br.com.harbsti.podtube.channels.presentation.model.ChannelSearchResult;
import rx.Scheduler;
import rx.Subscriber;

/**
 * Created by marcosharbs on 06/12/16.
 */

public class SubscriptionsPresenter implements SubscriptionsMVP.Presenter {

    private SubscriptionsMVP.View view;
    private SubscriptionsMVP.Model model;
    private Scheduler subscriberSchduler;
    private Scheduler observerScheduler;

    public SubscriptionsPresenter(SubscriptionsMVP.Model model,
                                  Scheduler subscriberSchduler,
                                  Scheduler observerScheduler) {

        this.model = model;
        this.subscriberSchduler = subscriberSchduler;
        this.observerScheduler = observerScheduler;
    }

    @Override
    public void setView(SubscriptionsMVP.View view) {
        this.view = view;
    }

    @Override
    public void getSubscriptions(String offset) {
        model.getSubscriptions(offset)
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
                        view.onSubscriptionsLoaded(channelSearchResult);
                    }
                });
    }

}
