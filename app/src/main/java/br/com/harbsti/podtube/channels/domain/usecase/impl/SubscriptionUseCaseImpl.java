package br.com.harbsti.podtube.channels.domain.usecase.impl;

import br.com.harbsti.podtube.channels.data.repository.SubscriptionRepository;
import br.com.harbsti.podtube.channels.domain.mapper.ChannelSearchResultDomainMapper;
import br.com.harbsti.podtube.channels.domain.model.ChannelSearchResult;
import br.com.harbsti.podtube.channels.domain.usecase.SubscriptionUseCase;
import rx.Observable;

/**
 * Created by marcosharbs on 06/12/16.
 */

public class SubscriptionUseCaseImpl implements SubscriptionUseCase {

    private SubscriptionRepository subscriptionRepository;
    private ChannelSearchResultDomainMapper channelSearchResultDomainMapper;

    public SubscriptionUseCaseImpl(SubscriptionRepository subscriptionRepository,
                                   ChannelSearchResultDomainMapper channelSearchResultDomainMapper) {

        this.subscriptionRepository = subscriptionRepository;
        this.channelSearchResultDomainMapper = channelSearchResultDomainMapper;
    }

    @Override
    public Observable<ChannelSearchResult> getSubscriptions(String offset) {
        return subscriptionRepository.getSubscriptions(offset).compose(channelSearchResultDomainMapper.getTransformer());
    }

    @Override
    public Observable<String> getUserSubscription(String channelId) {
        return subscriptionRepository.getUserSubscription(channelId);
    }

    @Override
    public Observable<String> addSubscription(String channelId) {
        return subscriptionRepository.addSubscription(channelId);
    }

    @Override
    public Observable<Void> deleteSubscription(String subscriptionId) {
        return subscriptionRepository.deleteSubscription(subscriptionId);
    }

}
