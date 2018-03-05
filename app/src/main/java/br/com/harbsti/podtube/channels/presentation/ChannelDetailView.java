package br.com.harbsti.podtube.channels.presentation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import java.math.BigInteger;

import javax.inject.Inject;

import br.com.harbsti.podtube.di.App;
import br.com.harbsti.podtube.BuildConfig;
import br.com.harbsti.podtube.R;
import br.com.harbsti.podtube.di.components.DaggerChannelDetailComponent;
import br.com.harbsti.podtube.di.modules.ChannelDetailModule;
import br.com.harbsti.podtube.utils.ExceptionHelper;
import br.com.harbsti.podtube.channels.presentation.model.Channel;
import br.com.harbsti.podtube.channels.presentation.model.Video;
import br.com.harbsti.podtube.channels.presentation.model.VideoSearchResult;
import br.com.harbsti.podtube.downloads.domain.service.impl.DownloadServiceImpl;
import br.com.harbsti.podtube.downloads.presentation.VideosAdapter;
import br.com.harbsti.podtube.utils.FormatHelper;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by marcosharbs on 06/12/16.
 */

public class ChannelDetailView extends AppCompatActivity implements ChannelDetailMVP.View, VideosAdapter.OnVideosAdapter, MenuItem.OnMenuItemClickListener {

    @InjectView(R.id.image_channel_banner) SimpleDraweeView imageChannelBanner;
    @InjectView(R.id.image_channel) SimpleDraweeView imageChannel;
    @InjectView(R.id.label_channel_title) TextView labelChannelTitle;
    @InjectView(R.id.label_channel_description) TextView labelChannelDescription;
    @InjectView(R.id.label_channel_subscribers) TextView labelChannelSubscribers;
    @InjectView(R.id.videos_recyclerview) RecyclerView videosRecyclerview;
    @InjectExtra Channel channel;
    @Inject ChannelDetailMVP.Presenter presenter;
    private VideosAdapter videosAdapter;
    private MenuItem menuItemChannelSubscribe;
    private MenuItem menuItemChannelUnsubscribe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.channel_detail);

        ButterKnife.inject(this);
        Dart.inject(this);

        DaggerChannelDetailComponent.builder()
                .applicationComponent(App.get(this).getComponent())
                .channelDetailModule(new ChannelDetailModule(this))
                .build()
                .inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        videosRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        videosAdapter = new VideosAdapter(this, this);
        videosRecyclerview.setAdapter(videosAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);
        presenter.getChannelDetail();
    }

    @Override
    public void setBannerImage(String image) {
        imageChannelBanner.setImageURI(ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(image))
                .build()
                .getSourceUri());
    }

    @Override
    public void setChannelImage(String image) {
        imageChannel.setImageURI(ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(image))
                .build()
                .getSourceUri());
    }

    @Override
    public void setChannelTitle(String title) {
        getSupportActionBar().setTitle(title);
        labelChannelTitle.setText(title);
    }

    @Override
    public void setChannelDescription(String description) {
        labelChannelDescription.setText(description);
    }

    @Override
    public void setSubscriptionId(String subscriptionId) {
        channel = Channel.builder()
                .id(channel.id())
                .title(channel.title())
                .description(channel.description())
                .image(channel.image())
                .bannerImage(channel.bannerImage())
                .videosCount(channel.videosCount())
                .subscribersCount(channel.subscribersCount())
                .uploadsPlaylistName(channel.uploadsPlaylistName())
                .subscriptionId(subscriptionId)
                .build();
    }

    @Override
    public void setChannelDetail(Channel channelDetail) {
        channel = Channel.builder()
                .id(channel.id())
                .title(channel.title())
                .description(channel.description())
                .image(channel.image())
                .bannerImage(channelDetail.bannerImage())
                .videosCount(channelDetail.videosCount())
                .subscribersCount(channelDetail.subscribersCount())
                .uploadsPlaylistName(channelDetail.uploadsPlaylistName())
                .subscriptionId(channelDetail.subscriptionId())
                .build();
    }

    @Override
    public void setVideosCount(BigInteger videosCount) {
        videosAdapter.setVideosCount(videosCount);
    }

    @Override
    public String getSubscriptionId() {
        return channel.subscriptionId();
    }

    @Override
    public String getChannelId() {
        return channel.id();
    }

    @Override
    public String getUploadsPlaylistName() {
        return channel.uploadsPlaylistName();
    }

    @Override
    public String getOffset() {
        return videosAdapter.getOffset();
    }

    @Override
    public void hideChannelDescription() {
        labelChannelDescription.setVisibility(View.GONE);
    }

    @Override
    public void setChannelSubscribers(BigInteger subscribersCount) {
        labelChannelSubscribers.setText(FormatHelper.getInstance(this).formatLongInt(subscribersCount)
                + " "
                + getString(R.string.label_subscriptions));
    }

    @Override
    public void appendChannelVideos(VideoSearchResult videoSearchResult) {
        videosAdapter.appendVideoResult(videoSearchResult);
    }

    @Override
    public void showUnsubscribe() {
        menuItemChannelUnsubscribe.setVisible(true);
        menuItemChannelUnsubscribe.getIcon().setAlpha(255);
    }

    @Override
    public void hideSubscribe() {
        menuItemChannelSubscribe.setVisible(false);
    }

    @Override
    public void showSubscribe() {
        menuItemChannelSubscribe.setVisible(true);
        menuItemChannelSubscribe.getIcon().setAlpha(100);
    }

    @Override
    public void hideUnsubscribe() {
        menuItemChannelUnsubscribe.setVisible(false);
    }

    @Override
    public void onError(Throwable throwable) {
        ExceptionHelper.handleThrowable(this, throwable);
    }

    @Override
    public void onLoadMore(String offset) {
        presenter.getChannelVideos();
    }

    @Override
    public void onDownload(Video video) {
        Toast.makeText(this, R.string.label_download_msg, Toast.LENGTH_SHORT).show();
        Bundle extras = new Bundle();
        extras.putParcelable("VIDEO", video);
        Intent downloadService = new Intent(this, DownloadServiceImpl.class);
        downloadService.putExtras(extras);
        startService(downloadService);
    }

    @Override
    public void onViewMovie(Video video) {
        Intent intent = YouTubeStandalonePlayer.createVideoIntent(this,
                BuildConfig.YOUTUBE_KEY,
                video.id(),
                0,
                true,
                true);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.channel_detail_menu, menu);
        menuItemChannelSubscribe = menu.findItem(R.id.channel_subscribe);
        menuItemChannelUnsubscribe = menu.findItem(R.id.channel_unsubscribe);
        menuItemChannelSubscribe.setOnMenuItemClickListener(this);
        menuItemChannelUnsubscribe.setOnMenuItemClickListener(this);

        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.channel_subscribe:
                presenter.addSubscription();
                break;
            case R.id.channel_unsubscribe:
                presenter.deleteSubscription();
                break;
        }
        return false;
    }

}