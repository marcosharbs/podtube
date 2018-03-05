package br.com.harbsti.podtube.channels.presentation;


import org.apache.commons.lang3.StringUtils;

import br.com.harbsti.podtube.channels.presentation.model.Channel;
import br.com.harbsti.podtube.channels.presentation.model.VideoSearchResult;
import rx.Scheduler;
import rx.Subscriber;

/**
 * Created by marcosharbs on 06/12/16.
 */

public class ChannelDetailPresenter implements ChannelDetailMVP.Presenter {

    private ChannelDetailMVP.View view;
    private ChannelDetailMVP.Model model;
    private Scheduler subscriberSchduler;
    private Scheduler observerScheduler;

    public ChannelDetailPresenter(ChannelDetailMVP.Model model,
                                  Scheduler subscriberSchduler,
                                  Scheduler observerScheduler) {

        this.model = model;
        this.subscriberSchduler = subscriberSchduler;
        this.observerScheduler = observerScheduler;
    }

    @Override
    public void setView(ChannelDetailMVP.View view) {
        this.view = view;
    }

    @Override
    public void getChannelDetail() {
        model.getChannelDetail(view.getChannelId())
                .subscribeOn(subscriberSchduler)
                .observeOn(observerScheduler)
                .subscribe(new Subscriber<Channel>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        view.onError(e);
                    }

                    @Override
                    public void onNext(Channel channel) {
                        onChannelLoaded(channel);
                    }
                });
    }

    private void onChannelLoaded(Channel channel) {
        view.setChannelDetail(channel);
        view.setVideosCount(channel.videosCount());
        view.setBannerImage(channel.bannerImage());
        view.setChannelImage(channel.image());
        view.setChannelTitle(channel.title());
        view.setChannelSubscribers(channel.subscribersCount());

        if(StringUtils.isEmpty(channel.description())){
            view.hideChannelDescription();
        }else{
            view.setChannelDescription(channel.description());
        }

        getUserSubscription();
        getChannelVideos();
    }

    @Override
    public void getChannelVideos() {
        model.getChannelVideos(view.getUploadsPlaylistName(), view.getOffset())
                .subscribeOn(subscriberSchduler)
                .observeOn(observerScheduler)
                .subscribe(new Subscriber<VideoSearchResult>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        view.onError(e);
                    }

                    @Override
                    public void onNext(VideoSearchResult videoSearchResult) {
                        view.appendChannelVideos(videoSearchResult);
                    }
                });
    }

    @Override
    public void getUserSubscription() {
        model.getUserSubscription(view.getChannelId())
                .subscribeOn(subscriberSchduler)
                .observeOn(observerScheduler)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        view.onError(e);
                    }

                    @Override
                    public void onNext(String s) {
                        handleUserSubscription(s);
                    }
                });
    }

    @Override
    public void addSubscription() {
        model.addSubscription(view.getChannelId())
                .subscribeOn(subscriberSchduler)
                .observeOn(observerScheduler)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        view.onError(e);
                    }

                    @Override
                    public void onNext(String s) {
                        handleUserSubscription(s);
                    }
                });
    }

    @Override
    public void deleteSubscription() {
        model.deleteSubscription(view.getSubscriptionId())
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
                        handleUserSubscription(null);
                    }
                });
    }

    private void handleUserSubscription(String subscriptionId) {
        view.setSubscriptionId(subscriptionId);

        if(subscriptionId != null && !subscriptionId.isEmpty()) {
            view.showUnsubscribe();
            view.hideSubscribe();
        } else {
            view.showSubscribe();
            view.hideUnsubscribe();
        }
    }

}