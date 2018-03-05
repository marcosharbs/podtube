package br.com.harbsti.podtube.channels.presentation;

import java.math.BigInteger;

import br.com.harbsti.podtube.channels.presentation.model.Channel;
import br.com.harbsti.podtube.channels.presentation.model.VideoSearchResult;
import rx.Observable;

/**
 * Created by marcosharbs on 03/08/17.
 */

public interface ChannelDetailMVP {

    interface Model {
        Observable<Channel> getChannelDetail(String channelId);
        Observable<VideoSearchResult> getChannelVideos(String playlistId, String offset);
        Observable<String> getUserSubscription(String channelId);
        Observable<String> addSubscription(String channelId);
        Observable<Void> deleteSubscription(String subscriptionId);
    }

    interface View {
        void setBannerImage(String image);
        void setChannelImage(String image);
        void setChannelTitle(String title);
        void setChannelDescription(String description);
        void setSubscriptionId(String subscriptionId);
        void setChannelDetail(Channel channelDetail);
        void setVideosCount(BigInteger videosCount);
        void setChannelSubscribers(BigInteger subscribersCount);
        void appendChannelVideos(VideoSearchResult videoSearchResult);
        String getSubscriptionId();
        String getChannelId();
        String getUploadsPlaylistName();
        String getOffset();
        void hideChannelDescription();
        void showUnsubscribe();
        void hideSubscribe();
        void showSubscribe();
        void hideUnsubscribe();
        void onError(Throwable throwable);
    }

    interface Presenter {
        void setView(ChannelDetailMVP.View view);
        void getChannelDetail();
        void getChannelVideos();
        void getUserSubscription();
        void addSubscription();
        void deleteSubscription();
    }

}
