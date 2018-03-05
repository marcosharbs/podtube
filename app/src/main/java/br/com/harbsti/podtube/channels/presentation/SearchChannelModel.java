package br.com.harbsti.podtube.channels.presentation;

import br.com.harbsti.podtube.channels.domain.usecase.ChannelUseCase;
import br.com.harbsti.podtube.channels.presentation.mapper.ChannelSearchResultViewMapper;
import br.com.harbsti.podtube.channels.presentation.model.ChannelSearchResult;
import rx.Observable;

/**
 * Created by marcosharbs on 29/08/17.
 */

public class SearchChannelModel implements SearchChannelMVP.Model {

    private ChannelUseCase channelUseCase;
    private ChannelSearchResultViewMapper channelSearchResultViewMapper;

    public SearchChannelModel(ChannelUseCase channelUseCase,
                              ChannelSearchResultViewMapper channelSearchResultViewMapper) {

        this.channelUseCase = channelUseCase;
        this.channelSearchResultViewMapper = channelSearchResultViewMapper;
    }

    @Override
    public Observable<ChannelSearchResult> getChannels(String term, String offset) {
        return channelUseCase.getChannels(term, offset)
                .compose(channelSearchResultViewMapper.getTransformer());
    }

}
