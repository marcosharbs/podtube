package br.com.harbsti.podtube.downloads.data.repository;

import rx.Observable;

/**
 * Created by marcosharbs on 06/12/16.
 */

public interface Mp3Repository {

    Observable<byte[]> getMp3(String video);

}
