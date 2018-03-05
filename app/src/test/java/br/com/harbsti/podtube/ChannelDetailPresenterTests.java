package br.com.harbsti.podtube;

import org.junit.Before;

import br.com.harbsti.podtube.channels.presentation.ChannelDetailMVP;
import br.com.harbsti.podtube.channels.presentation.ChannelDetailPresenter;
import br.com.harbsti.podtube.channels.presentation.model.Channel;
import br.com.harbsti.podtube.channels.presentation.model.Video;
import br.com.harbsti.podtube.channels.presentation.model.VideoSearchResult;
import rx.Observable;
import rx.schedulers.Schedulers;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * Created by marcosharbs on 17/10/17.
 */

public class ChannelDetailPresenterTests {

    ChannelDetailMVP.Model mockChannelDetailModel;
    ChannelDetailMVP.View mockChannelDetailView;
    ChannelDetailPresenter channelDetailPresenter;
    Exception exception = new Exception("error");

    @Before
    public void setup() {
        mockChannelDetailModel = mock(ChannelDetailMVP.Model.class);
        mockChannelDetailView = mock(ChannelDetailMVP.View.class);
        channelDetailPresenter = new ChannelDetailPresenter(
                mockChannelDetailModel,
                Schedulers.immediate(),
                Schedulers.immediate()
        );
        channelDetailPresenter.setView(mockChannelDetailView);
        setupModelData();
    }

    @Test
    public void testLoadChannelWithDescription() {
        when(mockChannelDetailView.getChannelId()).thenReturn("channel_01");
        when(mockChannelDetailView.getUploadsPlaylistName()).thenReturn("channel_01_upload_list");

        channelDetailPresenter.getChannelDetail();

        verify(mockChannelDetailModel, times(1)).getChannelDetail("channel_01");
        verify(mockChannelDetailModel, times(1)).getUserSubscription("channel_01");
        verify(mockChannelDetailModel, times(1)).getChannelVideos("channel_01_upload_list", null);

        verify(mockChannelDetailView, times(1)).setChannelDetail(
                Channel.builder().id("channel_01")
                .bannerImage("channel_01_banner_image")
                .description("Channel 01")
                .image("channel_01_image")
                .subscribersCount(BigInteger.TEN)
                .title("Channel 01")
                .uploadsPlaylistName("playlist_channel_01")
                .subscriptionId("channel_01_subscription_id")
                .videosCount(BigInteger.ONE)
                .build()
        );
        verify(mockChannelDetailView, times(2)).getChannelId();
        verify(mockChannelDetailView, times(1)).getUploadsPlaylistName();
        verify(mockChannelDetailView, times(1)).setVideosCount(BigInteger.ONE);
        verify(mockChannelDetailView, times(1)).setBannerImage("channel_01_banner_image");
        verify(mockChannelDetailView, times(1)).setChannelImage("channel_01_image");
        verify(mockChannelDetailView, times(1)).setChannelTitle("Channel 01");
        verify(mockChannelDetailView, times(1)).setChannelSubscribers(BigInteger.TEN);
        verify(mockChannelDetailView, times(1)).setChannelDescription("Channel 01");
        verify(mockChannelDetailView, never()).hideChannelDescription();
    }

    @Test
    public void testLoadChannelNoDescription() {
        when(mockChannelDetailView.getChannelId()).thenReturn("channel_02");
        when(mockChannelDetailView.getUploadsPlaylistName()).thenReturn("channel_02_upload_list");

        channelDetailPresenter.getChannelDetail();

        verify(mockChannelDetailModel, times(1)).getChannelDetail("channel_02");
        verify(mockChannelDetailModel, times(1)).getUserSubscription("channel_02");
        verify(mockChannelDetailModel, times(1)).getChannelVideos("channel_02_upload_list", null);

        verify(mockChannelDetailView, times(1)).setChannelDetail(
                Channel.builder().id("channel_02")
                        .bannerImage("channel_02_banner_image")
                        .description("")
                        .image("channel_02_image")
                        .subscribersCount(BigInteger.TEN)
                        .title("Channel 02")
                        .uploadsPlaylistName("playlist_channel_02")
                        .subscriptionId("")
                        .videosCount(BigInteger.ONE)
                        .build()
        );

        verify(mockChannelDetailView, times(2)).getChannelId();
        verify(mockChannelDetailView, times(1)).getUploadsPlaylistName();
        verify(mockChannelDetailView, times(1)).setVideosCount(BigInteger.ONE);
        verify(mockChannelDetailView, times(1)).setBannerImage("channel_02_banner_image");
        verify(mockChannelDetailView, times(1)).setChannelImage("channel_02_image");
        verify(mockChannelDetailView, times(1)).setChannelTitle("Channel 02");
        verify(mockChannelDetailView, times(1)).setChannelSubscribers(BigInteger.TEN);
        verify(mockChannelDetailView, never()).setChannelDescription("Channel 02");
        verify(mockChannelDetailView, times(1)).hideChannelDescription();
    }

    @Test
    public void testUserSubscription() {
        when(mockChannelDetailView.getChannelId()).thenReturn("channel_01");

        channelDetailPresenter.getUserSubscription();

        verify(mockChannelDetailModel, times(1)).getUserSubscription("channel_01");

        verify(mockChannelDetailView, times(1)).setSubscriptionId("channel_01_subscription_id");
        verify(mockChannelDetailView, times(1)).showUnsubscribe();
        verify(mockChannelDetailView, times(1)).hideSubscribe();
        verify(mockChannelDetailView, never()).showSubscribe();
        verify(mockChannelDetailView, never()).hideUnsubscribe();
    }

    @Test
    public void testUserNoSubscription() {
        when(mockChannelDetailView.getChannelId()).thenReturn("channel_02");

        channelDetailPresenter.getUserSubscription();

        verify(mockChannelDetailModel, times(1)).getUserSubscription("channel_02");

        verify(mockChannelDetailView, times(1)).getChannelId();
        verify(mockChannelDetailView, times(1)).setSubscriptionId("");
        verify(mockChannelDetailView, never()).showUnsubscribe();
        verify(mockChannelDetailView, never()).hideSubscribe();
        verify(mockChannelDetailView, times(1)).showSubscribe();
        verify(mockChannelDetailView, times(1)).hideUnsubscribe();
    }

    @Test
    public void testAddSubscription() {
        when(mockChannelDetailView.getChannelId()).thenReturn("channel_01");

        channelDetailPresenter.addSubscription();

        verify(mockChannelDetailModel, times(1)).addSubscription("channel_01");

        verify(mockChannelDetailView, times(1)).getChannelId();
        verify(mockChannelDetailView, times(1)).setSubscriptionId("new_subscription_id");
        verify(mockChannelDetailView, times(1)).showUnsubscribe();
        verify(mockChannelDetailView, times(1)).hideSubscribe();
        verify(mockChannelDetailView, never()).showSubscribe();
        verify(mockChannelDetailView, never()).hideUnsubscribe();
    }

    @Test
    public void testDeleteSubscription() {
        when(mockChannelDetailView.getSubscriptionId()).thenReturn("new_subscription_id");

        channelDetailPresenter.deleteSubscription();

        verify(mockChannelDetailModel, times(1)).deleteSubscription("new_subscription_id");

        verify(mockChannelDetailView, times(1)).getSubscriptionId();
        verify(mockChannelDetailView, times(1)).setSubscriptionId(null);
        verify(mockChannelDetailView, never()).showUnsubscribe();
        verify(mockChannelDetailView, never()).hideSubscribe();
        verify(mockChannelDetailView, times(1)).showSubscribe();
        verify(mockChannelDetailView, times(1)).hideUnsubscribe();
    }

    @Test
    public void testLoadVideos() {
        when(mockChannelDetailView.getUploadsPlaylistName()).thenReturn("upload_list_05");
        when(mockChannelDetailView.getOffset()).thenReturn(null);
        channelDetailPresenter.getChannelVideos();
        verify(mockChannelDetailView, times(1)).getUploadsPlaylistName();
        verify(mockChannelDetailView, times(1)).getOffset();
        verify(mockChannelDetailView, times(1)).appendChannelVideos(
                VideoSearchResult.builder()
                .offset("adapter_offset")
                .videos(new ArrayList<Video>())
                .build()
        );
    }

    @Test
    public void testLoadVideosError() {
        when(mockChannelDetailView.getUploadsPlaylistName()).thenReturn("upload_list_00");
        when(mockChannelDetailView.getOffset()).thenReturn(null);

        channelDetailPresenter.getChannelVideos();

        verify(mockChannelDetailModel, times(1)).getChannelVideos("upload_list_00", null);

        verify(mockChannelDetailView, times(1)).getUploadsPlaylistName();
        verify(mockChannelDetailView, times(1)).getOffset();
        verify(mockChannelDetailView, times(1)).onError(exception);
    }

    @Test
    public void testLoadChannelError() {
        when(mockChannelDetailView.getChannelId()).thenReturn("channel_00");

        channelDetailPresenter.getChannelDetail();

        verify(mockChannelDetailModel, times(1)).getChannelDetail("channel_00");

        verify(mockChannelDetailView, times(1)).getChannelId();
        verify(mockChannelDetailView, times(1)).onError(exception);
    }

    @Test
    public void testUserSubscriptionError() {
        when(mockChannelDetailView.getChannelId()).thenReturn("channel_00");
        channelDetailPresenter.getUserSubscription();
        verify(mockChannelDetailView, times(1)).getChannelId();
        verify(mockChannelDetailView, times(1)).onError(exception);
    }

    @Test
    public void testAddSubscriptionError() {
        when(mockChannelDetailView.getChannelId()).thenReturn("channel_00");

        channelDetailPresenter.addSubscription();

        verify(mockChannelDetailModel, times(1)).addSubscription("channel_00");

        verify(mockChannelDetailView, times(1)).getChannelId();
        verify(mockChannelDetailView, times(1)).onError(exception);
    }

    @Test
    public void testDeleteSubscriptionError() {
        when(mockChannelDetailView.getSubscriptionId()).thenReturn("subscription_id_00");

        channelDetailPresenter.deleteSubscription();

        verify(mockChannelDetailModel, times(1)).deleteSubscription("subscription_id_00");

        verify(mockChannelDetailView, times(1)).getSubscriptionId();
        verify(mockChannelDetailView, times(1)).onError(exception);
    }

    private void setupModelData() {
        when(mockChannelDetailModel.getChannelDetail("channel_00")).thenReturn(
                Observable.fromCallable(new Callable<Channel>() {
                    @Override
                    public Channel call() throws Exception {
                        throw exception;
                    }
                })
        );

        when(mockChannelDetailModel.getChannelDetail("channel_01")).thenReturn(
                Observable.just(Channel.builder().id("channel_01")
                        .bannerImage("channel_01_banner_image")
                        .description("Channel 01")
                        .image("channel_01_image")
                        .subscribersCount(BigInteger.TEN)
                        .title("Channel 01")
                        .uploadsPlaylistName("playlist_channel_01")
                        .subscriptionId("channel_01_subscription_id")
                        .videosCount(BigInteger.ONE)
                        .build())
        );

        when(mockChannelDetailModel.getUserSubscription("channel_00")).thenReturn(
                Observable.fromCallable(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        throw exception;
                    }
                })
        );

        when(mockChannelDetailModel.getUserSubscription("channel_01")).thenReturn(
                Observable.just("channel_01_subscription_id")
        );

        when(mockChannelDetailModel.getChannelVideos("channel_01_upload_list", null)).thenReturn(
                Observable.just(VideoSearchResult.builder()
                        .videos(new ArrayList<Video>())
                        .offset("")
                        .build())
        );

        when(mockChannelDetailModel.getChannelVideos("upload_list_00", null)).thenReturn(
                Observable.fromCallable(new Callable<VideoSearchResult>() {
                    @Override
                    public VideoSearchResult call() throws Exception {
                        throw exception;
                    }
                })
        );

        when(mockChannelDetailModel.getChannelDetail("channel_02")).thenReturn(
                Observable.just(Channel.builder().id("channel_02")
                        .bannerImage("channel_02_banner_image")
                        .description("")
                        .image("channel_02_image")
                        .subscribersCount(BigInteger.TEN)
                        .title("Channel 02")
                        .uploadsPlaylistName("playlist_channel_02")
                        .subscriptionId("")
                        .videosCount(BigInteger.ONE)
                        .build())
        );

        when(mockChannelDetailModel.getUserSubscription("channel_02")).thenReturn(
                Observable.just("")
        );

        when(mockChannelDetailModel.getChannelVideos("channel_02_upload_list", null)).thenReturn(
                Observable.just(VideoSearchResult.builder()
                        .videos(new ArrayList<Video>())
                        .offset("")
                        .build())
        );

        when(mockChannelDetailModel.addSubscription("channel_00")).thenReturn(
                Observable.fromCallable(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        throw exception;
                    }
                })
        );

        when(mockChannelDetailModel.addSubscription("channel_01")).thenReturn(
                Observable.just("new_subscription_id")
        );

        when(mockChannelDetailModel.deleteSubscription("subscription_id_00")).thenReturn(
                Observable.fromCallable(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        throw exception;
                    }
                })
        );

        when(mockChannelDetailModel.deleteSubscription("new_subscription_id")).thenReturn(
                Observable.<Void>just(null)
        );

        when(mockChannelDetailModel.getChannelVideos("upload_list_05", null)).thenReturn(
                Observable.just(
                    VideoSearchResult.builder()
                        .offset("adapter_offset")
                        .videos(new ArrayList<Video>())
                        .build()
                )
        );

    }

}