package br.com.harbsti.podtube.channels.presentation.mapper;

import br.com.harbsti.podtube.utils.Mapper;
import br.com.harbsti.podtube.channels.presentation.model.Video;

/**
 * Created by marcosharbs on 06/12/16.
 */

public class VideoViewMapper extends Mapper<br.com.harbsti.podtube.channels.domain.model.Video, Video> {

    @Override
    public Video transform(br.com.harbsti.podtube.channels.domain.model.Video video) {
        return Video.builder()
                .id(video.id())
                .image(video.image())
                .title(video.title())
                .pusblishDate(video.pusblishDate())
                .build();
    }

}
