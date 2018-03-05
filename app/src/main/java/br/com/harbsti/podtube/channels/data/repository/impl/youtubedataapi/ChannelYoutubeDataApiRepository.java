package br.com.harbsti.podtube.channels.data.repository.impl.youtubedataapi;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import br.com.harbsti.podtube.channels.data.model.Channel;
import br.com.harbsti.podtube.channels.data.model.ChannelSearchResult;
import br.com.harbsti.podtube.channels.data.model.Video;
import br.com.harbsti.podtube.channels.data.model.VideoSearchResult;
import br.com.harbsti.podtube.channels.data.repository.ChannelRepository;
import rx.Observable;

/**
 * Created by marcosharbs on 05/12/16.
 */

public class ChannelYoutubeDataApiRepository implements ChannelRepository {

    private static long PAGE_SZIE = 15l;

    private YouTube youTube;

    public ChannelYoutubeDataApiRepository(YouTube youTube) {
        this.youTube = youTube;
    }

    @Override
    public Observable<ChannelSearchResult> getChannels(final String term, final String offset) {
        return Observable.fromCallable(new Callable<ChannelSearchResult>() {
            @Override
            public ChannelSearchResult call() throws Exception {
                YouTube.Search.List search = youTube.search().list("id,snippet")
                        .setQ(term)
                        .setType("channel")
                        .setMaxResults(PAGE_SZIE);

                if(offset != null && !offset.isEmpty()){
                    search.setPageToken(offset);
                }

                SearchListResponse searchListResponse = search.execute();

                List<Channel> channels = new ArrayList<>();

                for(SearchResult searchResult : searchListResponse.getItems()){
                    channels.add(Channel.builder()
                            .id(searchResult.getSnippet().getChannelId())
                            .title(searchResult.getSnippet().getTitle())
                            .description(searchResult.getSnippet().getDescription())
                            .image(searchResult.getSnippet().getThumbnails().getDefault().getUrl())
                            .bannerImage(null)
                            .videosCount(null)
                            .subscribersCount(null)
                            .uploadsPlaylistName(null)
                            .subscriptionId(null)
                            .build());
                }

                return ChannelSearchResult.builder()
                        .channels(channels)
                        .offset(searchListResponse.getNextPageToken())
                        .build();
            }
        });
    }

    @Override
    public Observable<Channel> getChannelDetail(final String channelId) {
        return Observable.fromCallable(new Callable<Channel>() {
            @Override
            public Channel call() throws Exception {
                YouTube.Channels.List channelRequest = youTube.channels()
                        .list("id,snippet,brandingSettings,statistics,contentDetails")
                        .setId(channelId);

                com.google.api.services.youtube.model.Channel youtubeChannel = channelRequest.execute().getItems().get(0);


                return Channel.builder()
                        .id(youtubeChannel.getId())
                        .title(youtubeChannel.getSnippet().getTitle())
                        .description(youtubeChannel.getSnippet().getDescription())
                        .image(youtubeChannel.getSnippet().getThumbnails().getDefault().getUrl())
                        .bannerImage(youtubeChannel.getBrandingSettings().getImage().getBannerImageUrl())
                        .videosCount(youtubeChannel.getStatistics().getVideoCount())
                        .subscribersCount(youtubeChannel.getStatistics().getSubscriberCount())
                        .uploadsPlaylistName(youtubeChannel.getContentDetails().getRelatedPlaylists().getUploads())
                        .subscriptionId(null)
                        .build();
            }
        });
    }

    @Override
    public Observable<VideoSearchResult> getVideos(final String playlistId, final String offset) {
        return Observable.fromCallable(new Callable<VideoSearchResult>() {
            @Override
            public VideoSearchResult call() throws Exception {
                YouTube.PlaylistItems.List playlist = youTube.playlistItems()
                        .list("id,snippet,contentDetails")
                        .setPlaylistId(playlistId)
                        .setMaxResults(PAGE_SZIE);

                if(offset != null && !offset.isEmpty()){
                    playlist.setPageToken(offset);
                }

                PlaylistItemListResponse playlistItemListResponse = playlist.execute();

                List<Video> videos = new ArrayList<>();

                for(PlaylistItem playlistItem : playlistItemListResponse.getItems()){
                    videos.add(Video.builder()
                            .id(playlistItem.getContentDetails().getVideoId())
                            .image(playlistItem.getSnippet().getThumbnails().getDefault().getUrl())
                            .title(playlistItem.getSnippet().getTitle())
                            .pusblishDate(new Date(playlistItem.getSnippet().getPublishedAt().getValue()))
                            .build());
                }

                return VideoSearchResult.builder()
                        .offset(playlistItemListResponse.getNextPageToken())
                        .videos(videos)
                        .build();
            }
        });
    }

}