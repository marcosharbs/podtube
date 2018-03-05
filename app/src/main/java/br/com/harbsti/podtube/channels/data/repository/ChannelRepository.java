package br.com.harbsti.podtube.channels.data.repository;

import br.com.harbsti.podtube.channels.data.model.Channel;
import br.com.harbsti.podtube.channels.data.model.ChannelSearchResult;
import br.com.harbsti.podtube.channels.data.model.VideoSearchResult;
import rx.Observable;

/**
 * Created by marcosharbs on 05/12/16.
 */

public interface ChannelRepository {

    Observable<ChannelSearchResult> getChannels(String term, String offset);

    Observable<Channel> getChannelDetail(String channelId);

    Observable<VideoSearchResult> getVideos(String playlistId, String offset);

}
