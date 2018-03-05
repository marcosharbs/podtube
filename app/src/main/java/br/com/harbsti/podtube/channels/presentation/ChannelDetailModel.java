package br.com.harbsti.podtube.channels.presentation;

import br.com.harbsti.podtube.channels.domain.usecase.ChannelUseCase;
import br.com.harbsti.podtube.channels.domain.usecase.SubscriptionUseCase;
import br.com.harbsti.podtube.channels.presentation.mapper.ChannelViewMapper;
import br.com.harbsti.podtube.channels.presentation.mapper.VideoSearchResultViewMapper;
import br.com.harbsti.podtube.channels.presentation.model.Channel;
import br.com.harbsti.podtube.channels.presentation.model.VideoSearchResult;
import rx.Observable;

/**
 * Created by marcosharbs on 03/08/17.
 */

public class ChannelDetailModel implements ChannelDetailMVP.Model {

    private ChannelUseCase channelUseCase;
    private SubscriptionUseCase subscriptionUseCase;
    private ChannelViewMapper channelViewMapper;
    private VideoSearchResultViewMapper videoSearchResultViewMapper;

    public ChannelDetailModel(ChannelUseCase channelUseCase,
                              SubscriptionUseCase subscriptionUseCase,
                              ChannelViewMapper channelViewMapper,
                              VideoSearchResultViewMapper videoSearchResultViewMapper) {

        this.channelUseCase = channelUseCase;
        this.subscriptionUseCase = subscriptionUseCase;
        this.channelViewMapper = channelViewMapper;
        this.videoSearchResultViewMapper = videoSearchResultViewMapper;
    }

    @Override
    public Observable<Channel> getChannelDetail(String channelId) {
        return channelUseCase.getChannelDetail(channelId)
                .compose(channelViewMapper.getTransformer());
    }

    @Override
    public Observable<VideoSearchResult> getChannelVideos(String playlistId, String offset) {
        return channelUseCase.getChannelVideos(playlistId, offset)
                .compose(videoSearchResultViewMapper.getTransformer());
    }

    @Override
    public Observable<String> getUserSubscription(String channelId) {
        return subscriptionUseCase.getUserSubscription(channelId);
    }

    @Override
    public Observable<String> addSubscription(String channelId) {
        return subscriptionUseCase.addSubscription(channelId);
    }

    @Override
    public Observable<Void> deleteSubscription(String subscriptionId) {
        return subscriptionUseCase.deleteSubscription(subscriptionId);
    }

}
