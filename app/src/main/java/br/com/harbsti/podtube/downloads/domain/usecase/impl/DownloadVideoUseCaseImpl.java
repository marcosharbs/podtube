package br.com.harbsti.podtube.downloads.domain.usecase.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import br.com.harbsti.podtube.downloads.data.repository.DownloadRepository;
import br.com.harbsti.podtube.downloads.data.repository.Mp3Repository;
import br.com.harbsti.podtube.downloads.domain.mapper.DownloadDataMapper;
import br.com.harbsti.podtube.downloads.domain.mapper.DownloadDomainMapper;
import br.com.harbsti.podtube.downloads.domain.model.Download;
import br.com.harbsti.podtube.downloads.domain.usecase.DownloadVideoUseCase;
import rx.Observable;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

/**
 * Created by marcosharbs on 06/12/16.
 */

public class DownloadVideoUseCaseImpl implements DownloadVideoUseCase {

    private Mp3Repository mp3Repository;
    private DownloadRepository downloadRepository;
    private DownloadDataMapper downloadDataMapper;
    private DownloadDomainMapper downloadDomainMapper;

    public DownloadVideoUseCaseImpl(Mp3Repository mp3Repository,
                                    DownloadRepository downloadRepository,
                                    DownloadDataMapper downloadDataMapper,
                                    DownloadDomainMapper downloadDomainMapper) {

        this.mp3Repository = mp3Repository;
        this.downloadRepository = downloadRepository;
        this.downloadDataMapper = downloadDataMapper;
        this.downloadDomainMapper = downloadDomainMapper;
    }

    @Override
    public Observable<Download> downloadMp3(final Download download, final String dirToSave) {
        return  mp3Repository.getMp3(download.id())
                .flatMap(new Func1<byte[], Observable<br.com.harbsti.podtube.downloads.data.model.Download>>() {
                    @Override
                    public Observable<br.com.harbsti.podtube.downloads.data.model.Download> call(byte[] bytes) {
                        try {
                            File path = new File(dirToSave + "/" + download.id() + ".mp3");

                            FileOutputStream fos = new FileOutputStream(path);
                            fos.write(bytes);
                            fos.close();

                            return Observable.just(br.com.harbsti.podtube.downloads.data.model.Download.builder()
                                    .id(download.id())
                                    .image(download.image())
                                    .mp3File(path.getAbsolutePath())
                                    .pusblishDate(download.pusblishDate())
                                    .title(download.title())
                                    .build());
                        }catch (Exception e) {
                            throw Exceptions.propagate(e);
                        }
                    }
                }).flatMap(new Func1<br.com.harbsti.podtube.downloads.data.model.Download, Observable<br.com.harbsti.podtube.downloads.data.model.Download>>() {
                    @Override
                    public Observable<br.com.harbsti.podtube.downloads.data.model.Download> call(br.com.harbsti.podtube.downloads.data.model.Download download) {
                        return downloadRepository.persist(download);
                    }
                })
                .compose(downloadDomainMapper.getTransformer());
    }

    @Override
    public Observable<List<Download>> getDownloads() {
        return downloadRepository.getDownloads().compose(downloadDomainMapper.getTransformerList());
    }

    @Override
    public Observable<Download> getDownload(String id) {
        return downloadRepository.getDownload(id).compose(downloadDomainMapper.getTransformer());
    }

    @Override
    public Observable<Void> deleteDownload(String id) {
        return downloadRepository.deleteDownload(id);
    }

    @Override
    public Observable<Download> persist(Download download) {
        return Observable.just(download)
                .compose(downloadDataMapper.getTransformer())
                .flatMap(new Func1<br.com.harbsti.podtube.downloads.data.model.Download, Observable<br.com.harbsti.podtube.downloads.data.model.Download>>() {
                    @Override
                    public Observable<br.com.harbsti.podtube.downloads.data.model.Download> call(br.com.harbsti.podtube.downloads.data.model.Download download) {
                        return downloadRepository.persist(download);
                    }
                })
                .compose(downloadDomainMapper.getTransformer());
    }
}
