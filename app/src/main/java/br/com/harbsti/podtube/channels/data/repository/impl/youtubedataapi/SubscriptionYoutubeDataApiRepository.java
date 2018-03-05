package br.com.harbsti.podtube.channels.data.repository.impl.youtubedataapi;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.Subscription;
import com.google.api.services.youtube.model.SubscriptionListResponse;
import com.google.api.services.youtube.model.SubscriptionSnippet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import br.com.harbsti.podtube.channels.data.model.Channel;
import br.com.harbsti.podtube.channels.data.model.ChannelSearchResult;
import br.com.harbsti.podtube.channels.data.repository.SubscriptionRepository;
import rx.Observable;

/**
 * Created by marcosharbs on 06/12/16.
 */

public class SubscriptionYoutubeDataApiRepository implements SubscriptionRepository {

    private static long PAGE_SZIE = 15l;

    private YouTube youTube;

    public SubscriptionYoutubeDataApiRepository(YouTube youTube) {
        this.youTube = youTube;
    }

    @Override
    public Observable<ChannelSearchResult> getSubscriptions(final String offset) {
        return Observable.fromCallable(new Callable<ChannelSearchResult>() {
            @Override
            public ChannelSearchResult call() throws Exception {
                YouTube.Subscriptions.List subscriptions = youTube.subscriptions().list("id,snippet")
                        .setMine(true)
                        .setMaxResults(PAGE_SZIE);

                if(offset != null && !offset.isEmpty()){
                    subscriptions.setPageToken(offset);
                }

                SubscriptionListResponse subscriptionListResponse = subscriptions.execute();

                List<Channel> channels = new ArrayList<>();

                for(Subscription subscription : subscriptionListResponse.getItems()){
                    channels.add(Channel.builder()
                            .id(subscription.getSnippet().getResourceId().getChannelId())
                            .title(subscription.getSnippet().getTitle())
                            .description(subscription.getSnippet().getDescription())
                            .image(subscription.getSnippet().getThumbnails().getDefault().getUrl())
                            .bannerImage(null)
                            .videosCount(null)
                            .subscribersCount(null)
                            .uploadsPlaylistName(null)
                            .subscriptionId(null)
                            .build());
                }

                return ChannelSearchResult.builder()
                        .channels(channels)
                        .offset(subscriptionListResponse.getNextPageToken())
                        .build();
            }
        });
    }

    @Override
    public Observable<String> getUserSubscription(final String channelId) {
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                YouTube.Subscriptions.List subscriptions = youTube.subscriptions().list("id")
                        .setMine(true)
                        .setForChannelId(channelId)
                        .setMaxResults(1l);

                List<Subscription> subscriptionList = subscriptions.execute().getItems();

                if(subscriptionList != null && subscriptionList.size() > 0){
                    return subscriptionList.get(0).getId();
                }
                return null;
            }
        });
    }

    @Override
    public Observable<String> addSubscription(final String channelId) {
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                ResourceId resourceId = new ResourceId();
                resourceId.setChannelId(channelId);
                SubscriptionSnippet snippet = new SubscriptionSnippet();
                snippet.setResourceId(resourceId);
                Subscription subscription = new Subscription();
                subscription.setSnippet(snippet);
                YouTube.Subscriptions.Insert subscriptionInsert = youTube.subscriptions().insert("snippet", subscription);
                return subscriptionInsert.execute().getId();
            }
        });
    }

    @Override
    public Observable<Void> deleteSubscription(final String subscriptionId) {
        return Observable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                YouTube.Subscriptions.Delete subscriptionDelete = youTube.subscriptions().delete(subscriptionId);
                subscriptionDelete.execute();
                return null;
            }
        });
    }

}
