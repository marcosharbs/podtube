package br.com.harbsti.podtube.downloads.data.repository.impl.realm;

import java.util.List;
import java.util.concurrent.Callable;

import br.com.harbsti.podtube.downloads.data.mapper.DownloadDataRealmMapper;
import br.com.harbsti.podtube.downloads.data.mapper.DownloadRealmDataMapper;
import br.com.harbsti.podtube.downloads.data.model.Download;
import br.com.harbsti.podtube.downloads.data.model.realm.DownloadRealm;
import br.com.harbsti.podtube.downloads.data.repository.DownloadRepository;
import io.realm.Realm;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by marcosharbs on 06/12/16.
 */

public class DownloadRealmRepository implements DownloadRepository {

    private DownloadRealmDataMapper downloadRealmDataMapper;
    private DownloadDataRealmMapper downloadDataRealmMapper;

    public DownloadRealmRepository(DownloadRealmDataMapper downloadRealmDataMapper,
                                   DownloadDataRealmMapper downloadDataRealmMapper) {

        this.downloadRealmDataMapper = downloadRealmDataMapper;
        this.downloadDataRealmMapper = downloadDataRealmMapper;
    }

    @Override
    public Observable<List<Download>> getDownloads() {
        return Observable.fromCallable(new Callable<List<DownloadRealm>>() {
            @Override
            public List<DownloadRealm> call() throws Exception {
                Realm realm = Realm.getDefaultInstance();
                return realm.where(DownloadRealm.class).findAll();
            }
        }).compose(downloadRealmDataMapper.getTransformerList());
    }

    @Override
    public Observable<Download> persist(Download download) {
        return Observable.just(download)
                .compose(downloadDataRealmMapper.getTransformer())
                .flatMap(new Func1<DownloadRealm, Observable<DownloadRealm>>() {
                    @Override
                    public Observable<DownloadRealm> call(DownloadRealm downloadRealm) {
                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        downloadRealm = realm.copyToRealmOrUpdate(downloadRealm);
                        realm.commitTransaction();
                        return Observable.just(downloadRealm);
                    }
                })
                .compose(downloadRealmDataMapper.getTransformer());
    }

    @Override
    public Observable<Download> getDownload(final String id) {
        return Observable.fromCallable(new Callable<DownloadRealm>() {
            @Override
            public DownloadRealm call() throws Exception {
                Realm realm = Realm.getDefaultInstance();
                return realm.where(DownloadRealm.class).equalTo("id", id).findFirst();
            }
        }).compose(downloadRealmDataMapper.getTransformer());
    }

    @Override
    public Observable<Void> deleteDownload(final String id) {
        return Observable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                DownloadRealm downloadRealm = realm.where(DownloadRealm.class).equalTo("id", id).findFirst();
                downloadRealm.deleteFromRealm();
                realm.commitTransaction();
                return null;
            }
        });
    }

}
