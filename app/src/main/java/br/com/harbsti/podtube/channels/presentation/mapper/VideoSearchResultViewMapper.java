package br.com.harbsti.podtube.channels.presentation.mapper;

import br.com.harbsti.podtube.utils.Mapper;
import br.com.harbsti.podtube.channels.presentation.model.VideoSearchResult;

/**
 * Created by marcosharbs on 05/12/16.
 */

public class VideoSearchResultViewMapper extends Mapper<br.com.harbsti.podtube.channels.domain.model.VideoSearchResult, VideoSearchResult> {

    private VideoViewMapper videoViewMapper;

    public VideoSearchResultViewMapper(VideoViewMapper videoViewMapper) {
        this.videoViewMapper = videoViewMapper;
    }

    public VideoSearchResult transform(br.com.harbsti.podtube.channels.domain.model.VideoSearchResult videoSearchResult){
        return VideoSearchResult.builder()
                .offset(videoSearchResult.offset())
                .videos(videoViewMapper.transform(videoSearchResult.videos()))
                .build();
    }

}
