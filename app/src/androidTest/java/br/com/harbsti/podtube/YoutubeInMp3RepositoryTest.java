package br.com.harbsti.podtube;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.harbsti.podtube.downloads.data.repository.impl.retrofit.YoutubeInMp3Repository;
import br.com.harbsti.podtube.downloads.data.repository.impl.retrofit.service.Mp3Service;
import br.com.harbsti.podtube.utils.ServiceHelper;
import rx.Subscriber;
import rx.schedulers.Schedulers;

@RunWith(AndroidJUnit4.class)
public class YoutubeInMp3RepositoryTest {

    @Test
    public void testMp3Service() {
        Mp3Service service = ServiceHelper.createRetrfitService(
                Mp3Service.class,
                "http://www.youtubeinmp3.com"
        );
        YoutubeInMp3Repository repository = new YoutubeInMp3Repository(service);
        repository.getMp3("G-DFo1mppFI")
                .subscribeOn(Schedulers.immediate())
                .observeOn(Schedulers.immediate())
                .subscribe(new Subscriber<byte[]>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Assert.assertTrue(2 == 1);
                    }

                    @Override
                    public void onNext(byte[] bytes) {
                        Assert.assertTrue(bytes != null && bytes.length > 0);
                    }
                });
    }

}
