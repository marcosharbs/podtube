package br.com.harbsti.podtube.channels.presentation.mapper;

import br.com.harbsti.podtube.utils.Mapper;
import br.com.harbsti.podtube.channels.presentation.model.Video;

/**
 * Created by marcosharbs on 06/12/16.
 */

public class VideoDomainMapper extends Mapper<Video, br.com.harbsti.podtube.channels.domain.model.Video> {
    @Override
    public br.com.harbsti.podtube.channels.domain.model.Video transform(Video video) {
        return br.com.harbsti.podtube.channels.domain.model.Video.builder()
                .id(video.id())
                .image(video.image())
                .title(video.title())
                .pusblishDate(video.pusblishDate())
                .build();
    }
}
