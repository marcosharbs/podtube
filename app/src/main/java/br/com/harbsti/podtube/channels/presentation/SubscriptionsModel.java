package br.com.harbsti.podtube.channels.presentation;

import br.com.harbsti.podtube.channels.domain.usecase.SubscriptionUseCase;
import br.com.harbsti.podtube.channels.presentation.mapper.ChannelSearchResultViewMapper;
import br.com.harbsti.podtube.channels.presentation.model.ChannelSearchResult;
import rx.Observable;

/**
 * Created by marcosharbs on 29/08/17.
 */

public class SubscriptionsModel implements SubscriptionsMVP.Model {

    private SubscriptionUseCase subscriptionUseCase;
    private ChannelSearchResultViewMapper channelSearchResultViewMapper;

    public SubscriptionsModel(SubscriptionUseCase subscriptionUseCase,
                              ChannelSearchResultViewMapper channelSearchResultViewMapper) {
        this.subscriptionUseCase = subscriptionUseCase;
        this.channelSearchResultViewMapper = channelSearchResultViewMapper;
    }

    @Override
    public Observable<ChannelSearchResult> getSubscriptions(String offset) {
        return subscriptionUseCase.getSubscriptions(offset)
                .compose(channelSearchResultViewMapper.getTransformer());
    }

}
