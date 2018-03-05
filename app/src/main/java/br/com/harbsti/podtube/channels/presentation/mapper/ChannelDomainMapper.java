package br.com.harbsti.podtube.channels.presentation.mapper;

import br.com.harbsti.podtube.utils.Mapper;
import br.com.harbsti.podtube.channels.presentation.model.Channel;

/**
 * Created by marcosharbs on 03/08/17.
 */

public class ChannelDomainMapper extends Mapper<Channel, br.com.harbsti.podtube.channels.domain.model.Channel> {

public br.com.harbsti.podtube.channels.domain.model.Channel transform(Channel channel) {
        return br.com.harbsti.podtube.channels.domain.model.Channel.builder()
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
