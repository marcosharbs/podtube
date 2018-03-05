package br.com.harbsti.podtube.channels.domain.mapper;

import br.com.harbsti.podtube.channels.domain.model.ChannelSearchResult;
import br.com.harbsti.podtube.utils.Mapper;

/**
 * Created by marcosharbs on 05/12/16.
 */

public class ChannelSearchResultDomainMapper extends Mapper<br.com.harbsti.podtube.channels.data.model.ChannelSearchResult, ChannelSearchResult> {

    private ChannelDomainMapper channelMapper;

    public ChannelSearchResultDomainMapper(ChannelDomainMapper channelMapper) {
        this.channelMapper = channelMapper;
    }

    public ChannelSearchResult transform(br.com.harbsti.podtube.channels.data.model.ChannelSearchResult channelSearchResult){
        return ChannelSearchResult.builder()
                .offset(channelSearchResult.offset())
                .channels(channelMapper.transform(channelSearchResult.channels()))
                .build();
    }

}
