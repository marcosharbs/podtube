package br.com.harbsti.podtube.downloads.domain.service.impl;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.NotificationCompat;

import br.com.harbsti.podtube.R;
import br.com.harbsti.podtube.utils.ServiceHelper;
import br.com.harbsti.podtube.downloads.data.mapper.DownloadDataRealmMapper;
import br.com.harbsti.podtube.downloads.data.mapper.DownloadRealmDataMapper;
import br.com.harbsti.podtube.downloads.data.repository.impl.realm.DownloadRealmRepository;
import br.com.harbsti.podtube.downloads.data.repository.impl.retrofit.YoutubeInMp3Repository;
import br.com.harbsti.podtube.downloads.data.repository.impl.retrofit.service.Mp3Service;
import br.com.harbsti.podtube.downloads.domain.mapper.DownloadDataMapper;
import br.com.harbsti.podtube.downloads.domain.mapper.DownloadDomainMapper;
import br.com.harbsti.podtube.downloads.domain.usecase.impl.DownloadVideoUseCaseImpl;
import br.com.harbsti.podtube.downloads.domain.broadcast.DownloadCompletedReceiver;
import br.com.harbsti.podtube.downloads.domain.broadcast.DownloadDeletedReceiver;
import br.com.harbsti.podtube.downloads.domain.broadcast.DownloadStartedReceiver;
import br.com.harbsti.podtube.downloads.presentation.mapper.DownloadViewMapper;
import br.com.harbsti.podtube.downloads.presentation.model.Download;
import br.com.harbsti.podtube.channels.presentation.model.Video;
import br.com.harbsti.podtube.downloads.domain.service.DownloadService;
import br.com.harbsti.podtube.downloads.domain.service.presenter.DownloadServicePresenter;
import br.com.harbsti.podtube.downloads.domain.service.presenter.impl.DownloadServicePresenterImpl;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by marcosharbs on 06/12/16.
 */

public class DownloadServiceImpl extends IntentService implements DownloadService {

    private DownloadServicePresenter downloadServicePresenter;
    private Video video;

    public DownloadServiceImpl() {
        super(DownloadServiceImpl.class.getName());
        initPresenter();
    }

    private void initPresenter() {
        downloadServicePresenter = new DownloadServicePresenterImpl(this,
                new DownloadVideoUseCaseImpl(new YoutubeInMp3Repository(ServiceHelper.createRetrfitService(Mp3Service.class,
                        "http://www.youtubeinmp3.com")),
                        new DownloadRealmRepository(
                                new DownloadRealmDataMapper(),
                                new DownloadDataRealmMapper()),
                        new DownloadDataMapper(),
                        new DownloadDomainMapper()),
                new DownloadViewMapper(),
                new br.com.harbsti.podtube.downloads.presentation.mapper.DownloadDomainMapper(),
                Schedulers.newThread(),
                AndroidSchedulers.mainThread());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        video = intent.getExtras().getParcelable("VIDEO");
        downloadServicePresenter.persistDownload(video);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onError(Throwable throwable) {
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                .setBigContentTitle(getString(R.string.label_error_title))
                .bigText(getString(R.string.label_error_msg, video.title()));
        Notification notification = new NotificationCompat.Builder(this)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setSmallIcon(R.drawable.icon_notification)
                .setStyle(bigTextStyle)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(video.id().hashCode(), notification);
        downloadServicePresenter.deleteDownload(video.id());
    }

    @Override
    public void onDownloadPersisted(Download download) {
        sendBroadcast(new Intent(DownloadStartedReceiver.FILTER));
        downloadServicePresenter.downloadVideo(download, getCacheDir().getAbsolutePath());
    }

    @Override
    public void onDownloadCompleted(Download download) {
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                .setBigContentTitle(getString(R.string.label_download_title))
                .bigText(getString(R.string.label_download_notification_msg, video.title()));
        Notification notification = new NotificationCompat.Builder(this)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setSmallIcon(R.drawable.icon_notification)
                .setStyle(bigTextStyle)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(video.id().hashCode(), notification);
        sendBroadcast(new Intent(DownloadCompletedReceiver.FILTER));
    }

    @Override
    public void onDownloadDeleted() {
        sendBroadcast(new Intent(DownloadDeletedReceiver.FILTER));
    }
}
