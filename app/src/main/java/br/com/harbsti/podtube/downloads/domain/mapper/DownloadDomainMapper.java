package br.com.harbsti.podtube.downloads.domain.mapper;

import br.com.harbsti.podtube.utils.Mapper;
import br.com.harbsti.podtube.downloads.domain.model.Download;

/**
 * Created by marcosharbs on 06/12/16.
 */

public class DownloadDomainMapper extends Mapper<br.com.harbsti.podtube.downloads.data.model.Download, Download> {

    @Override
    public Download transform(br.com.harbsti.podtube.downloads.data.model.Download download) {
        return Download.builder()
                .id(download.id())
                .image(download.image())
                .mp3File(download.mp3File())
                .pusblishDate(download.pusblishDate())
                .title(download.title())
                .build();
    }

}
