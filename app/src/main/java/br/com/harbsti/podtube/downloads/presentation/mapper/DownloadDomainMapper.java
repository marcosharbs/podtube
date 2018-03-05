package br.com.harbsti.podtube.downloads.presentation.mapper;

import br.com.harbsti.podtube.utils.Mapper;
import br.com.harbsti.podtube.downloads.presentation.model.Download;

/**
 * Created by marcosharbs on 06/12/16.
 */

public class DownloadDomainMapper extends Mapper<Download, br.com.harbsti.podtube.downloads.domain.model.Download> {

    @Override
    public br.com.harbsti.podtube.downloads.domain.model.Download transform(Download download) {
        return br.com.harbsti.podtube.downloads.domain.model.Download.builder()
                .id(download.id())
                .image(download.image())
                .mp3File(download.mp3File())
                .pusblishDate(download.pusblishDate())
                .title(download.title())
                .build();
    }

}
