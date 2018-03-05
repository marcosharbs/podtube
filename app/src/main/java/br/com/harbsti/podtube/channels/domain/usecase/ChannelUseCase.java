package br.com.harbsti.podtube.channels.domain.usecase;

import br.com.harbsti.podtube.channels.domain.model.Channel;
import br.com.harbsti.podtube.channels.domain.model.ChannelSearchResult;
import br.com.harbsti.podtube.channels.domain.model.VideoSearchResult;
import rx.Observable;

/**
 * Created by marcosharbs on 05/12/16.
 */

public interface ChannelUseCase {

    Observable<ChannelSearchResult> getChannels(String term, String offset);

    Observable<Channel> getChannelDetail(String channelId);

    Observable<VideoSearchResult> getChannelVideos(String playlistId, String offset);

}
