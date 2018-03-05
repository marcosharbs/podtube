package br.com.harbsti.podtube.downloads.presentation;

import java.util.List;

import br.com.harbsti.podtube.downloads.domain.usecase.DownloadVideoUseCase;
import br.com.harbsti.podtube.downloads.presentation.model.Download;
import br.com.harbsti.podtube.downloads.presentation.mapper.DownloadViewMapper;
import rx.Observable;

/**
 * Created by marcosharbs on 02/08/17.
 */

public class DownloadModel implements DownloadMVP.Model {

    private DownloadVideoUseCase useCase;
    private DownloadViewMapper mapper;

    public DownloadModel(DownloadVideoUseCase useCase, DownloadViewMapper mapper) {
        this.useCase = useCase;
        this.mapper = mapper;
    }

    @Override
    public Observable<List<Download>> getDownloads() {
        return useCase.getDownloads().compose(mapper.getTransformerList());
    }

    @Override
    public Observable<Void> deleteDownload(String id) {
        return useCase.deleteDownload(id);
    }

}
