package br.com.harbsti.podtube.channels.domain.usecase;

import br.com.harbsti.podtube.channels.domain.model.ChannelSearchResult;
import rx.Observable;

/**
 * Created by marcosharbs on 06/12/16.
 */

public interface SubscriptionUseCase {

    Observable<ChannelSearchResult> getSubscriptions(String offset);

    Observable<String> getUserSubscription(String channelId);

    Observable<String> addSubscription(String channelId);

    Observable<Void> deleteSubscription(String subscriptionId);

}
