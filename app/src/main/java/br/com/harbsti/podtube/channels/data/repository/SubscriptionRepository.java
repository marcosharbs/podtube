package br.com.harbsti.podtube.channels.data.repository;

import br.com.harbsti.podtube.channels.data.model.ChannelSearchResult;
import rx.Observable;

/**
 * Created by marcosharbs on 06/12/16.
 */

public interface SubscriptionRepository {

    Observable<ChannelSearchResult> getSubscriptions(String offset);

    Observable<String> getUserSubscription(String channelId);

    Observable<String> addSubscription(String channelId);

    Observable<Void> deleteSubscription(String subscriptionId);

}
