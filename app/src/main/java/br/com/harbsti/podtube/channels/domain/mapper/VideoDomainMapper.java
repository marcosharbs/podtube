package br.com.harbsti.podtube.channels.domain.mapper;

import br.com.harbsti.podtube.channels.domain.model.Video;
import br.com.harbsti.podtube.utils.Mapper;

/**
 * Created by marcosharbs on 05/12/16.
 */

public class VideoDomainMapper extends Mapper<br.com.harbsti.podtube.channels.data.model.Video, Video> {

    public Video transform(br.com.harbsti.podtube.channels.data.model.Video video) {
        return Video.builder()
                .id(video.id())
                .image(video.image())
                .title(video.title())
                .pusblishDate(video.pusblishDate())
                .build();
    }

}
