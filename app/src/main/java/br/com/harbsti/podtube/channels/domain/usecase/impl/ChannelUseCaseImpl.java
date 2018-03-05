package br.com.harbsti.podtube.channels.domain.usecase.impl;

import br.com.harbsti.podtube.channels.data.repository.ChannelRepository;
import br.com.harbsti.podtube.channels.domain.mapper.ChannelDomainMapper;
import br.com.harbsti.podtube.channels.domain.mapper.ChannelSearchResultDomainMapper;
import br.com.harbsti.podtube.channels.domain.mapper.VideoSearchResultDomainMapper;
import br.com.harbsti.podtube.channels.domain.model.Channel;
import br.com.harbsti.podtube.channels.domain.model.ChannelSearchResult;
import br.com.harbsti.podtube.channels.domain.model.VideoSearchResult;
import br.com.harbsti.podtube.channels.domain.usecase.ChannelUseCase;
import rx.Observable;

/**
 * Created by marcosharbs on 05/12/16.
 */

public class ChannelUseCaseImpl implements ChannelUseCase {

    private ChannelRepository channelRepository;
    private ChannelDomainMapper channelDomainMapper;
    private ChannelSearchResultDomainMapper channelSearchResultDomainMapper;
    private VideoSearchResultDomainMapper videoSearchResultDomainMapper;

    public ChannelUseCaseImpl(ChannelRepository channelRepository,
                              ChannelDomainMapper channelDomainMapper,
                              ChannelSearchResultDomainMapper channelSearchResultDomainMapper,
                              VideoSearchResultDomainMapper videoSearchResultDomainMapper) {

        this.channelRepository = channelRepository;
        this.channelDomainMapper = channelDomainMapper;
        this.channelSearchResultDomainMapper = channelSearchResultDomainMapper;
        this.videoSearchResultDomainMapper = videoSearchResultDomainMapper;
    }

    @Override
    public Observable<ChannelSearchResult> getChannels(String term, String offset) {
        return channelRepository.getChannels(term, offset)
                .compose(channelSearchResultDomainMapper.getTransformer());
    }

    @Override
    public Observable<Channel> getChannelDetail(String channelId) {
        return channelRepository.getChannelDetail(channelId)
                .compose(channelDomainMapper.getTransformer());
    }

    @Override
    public Observable<VideoSearchResult> getChannelVideos(String playlistId, String offset) {
        return channelRepository.getVideos(playlistId, offset)
                .compose(videoSearchResultDomainMapper.getTransformer());
    }

}
