package br.com.harbsti.podtube.channels.domain.mapper;

import br.com.harbsti.podtube.channels.domain.model.Channel;
import br.com.harbsti.podtube.utils.Mapper;

/**
 * Created by marcosharbs on 05/12/16.
 */

public class ChannelDataMapper extends Mapper<Channel, br.com.harbsti.podtube.channels.data.model.Channel> {

    public br.com.harbsti.podtube.channels.data.model.Channel transform(Channel channel) {
        return br.com.harbsti.podtube.channels.data.model.Channel.builder()
                .id(channel.id())
                .title(channel.title())
                .description(channel.description())
                .image(channel.image())
                .bannerImage(channel.bannerImage())
                .videosCount(channel.videosCount())
                .subscribersCount(channel.subscribersCount())
                .uploadsPlaylistName(channel.uploadsPlaylistName())
                .subscriptionId(channel.subscriptionId())
                .build();
    }

}
