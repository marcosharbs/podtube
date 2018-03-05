package br.com.harbsti.podtube.di.modules;

import android.content.Context;

import br.com.harbsti.podtube.channels.data.repository.ChannelRepository;
import br.com.harbsti.podtube.channels.data.repository.SubscriptionRepository;
import br.com.harbsti.podtube.channels.data.repository.impl.youtubedataapi.ChannelYoutubeDataApiRepository;
import br.com.harbsti.podtube.channels.data.repository.impl.youtubedataapi.SubscriptionYoutubeDataApiRepository;
import br.com.harbsti.podtube.channels.domain.mapper.ChannelDataMapper;
import br.com.harbsti.podtube.channels.domain.mapper.ChannelDomainMapper;
import br.com.harbsti.podtube.channels.domain.mapper.ChannelSearchResultDomainMapper;
import br.com.harbsti.podtube.channels.domain.mapper.VideoDomainMapper;
import br.com.harbsti.podtube.channels.domain.mapper.VideoSearchResultDomainMapper;
import br.com.harbsti.podtube.channels.domain.usecase.ChannelUseCase;
import br.com.harbsti.podtube.channels.domain.usecase.SubscriptionUseCase;
import br.com.harbsti.podtube.channels.domain.usecase.impl.ChannelUseCaseImpl;
import br.com.harbsti.podtube.channels.domain.usecase.impl.SubscriptionUseCaseImpl;
import br.com.harbsti.podtube.channels.presentation.ChannelDetailMVP;
import br.com.harbsti.podtube.channels.presentation.ChannelDetailModel;
import br.com.harbsti.podtube.channels.presentation.ChannelDetailPresenter;
import br.com.harbsti.podtube.channels.presentation.ChannelDetailView;
import br.com.harbsti.podtube.utils.AuthHelper;
import br.com.harbsti.podtube.channels.presentation.mapper.ChannelViewMapper;
import br.com.harbsti.podtube.channels.presentation.mapper.VideoSearchResultViewMapper;
import br.com.harbsti.podtube.channels.presentation.mapper.VideoViewMapper;
import dagger.Module;
import dagger.Provides;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by marcosharbs on 03/08/17.
 */

@Module
public class ChannelDetailModule {

    private ChannelDetailView view;

    public ChannelDetailModule(ChannelDetailView view) {
        this.view = view;
    }

    @Provides
    public Context provideContext() {
        return view;
    }

    @Provides
    public ChannelDetailMVP.Presenter providesPresenter(ChannelDetailMVP.Model model) {
        return new ChannelDetailPresenter(model, Schedulers.newThread(), AndroidSchedulers.mainThread());
    }

    @Provides
    public ChannelDetailMVP.Model providesModel(ChannelUseCase channelUseCase,
                                                SubscriptionUseCase subscriptionUseCase,
                                                ChannelViewMapper channelViewMapper,
                                                VideoSearchResultViewMapper videoSearchResultViewMapper) {

        return new ChannelDetailModel(channelUseCase,
                subscriptionUseCase,
                channelViewMapper,
                videoSearchResultViewMapper);
    }

    @Provides
    public ChannelUseCase providesChannelUseCase(ChannelRepository channelRepository,
                                                 ChannelDomainMapper channelDomainMapper,
                                                 ChannelSearchResultDomainMapper channelSearchResultDomainMapper,
                                                 VideoSearchResultDomainMapper videoSearchResultDomainMapper) {

        return new ChannelUseCaseImpl(channelRepository,
                channelDomainMapper,
                channelSearchResultDomainMapper,
                videoSearchResultDomainMapper);
    }

    @Provides
    public SubscriptionUseCase providesSubscriptionUseCase(SubscriptionRepository subscriptionRepository,
                                                           ChannelSearchResultDomainMapper channelSearchResultDomainMapper) {
        return new SubscriptionUseCaseImpl(subscriptionRepository,
                channelSearchResultDomainMapper);
    }

    @Provides
    public ChannelRepository providesChannelRepository(Context context) {
        return new ChannelYoutubeDataApiRepository(AuthHelper.getYoutubeClient(context));
    }

    @Provides
    public SubscriptionRepository providesSubscriptionRepository(Context context) {
        return new SubscriptionYoutubeDataApiRepository(AuthHelper.getYoutubeClient(context));
    }

    @Provides
    public ChannelViewMapper providesChannelViewMapper() {
        return new ChannelViewMapper();
    }

    @Provides
    public VideoSearchResultViewMapper providesVideoSearchResultViewMapper(VideoViewMapper videoViewMapper) {
        return new VideoSearchResultViewMapper(videoViewMapper);
    }

    @Provides
    public br.com.harbsti.podtube.channels.presentation.mapper.ChannelDomainMapper providesChannelDomainMapper() {
        return new br.com.harbsti.podtube.channels.presentation.mapper.ChannelDomainMapper();
    }

    @Provides
    public ChannelDataMapper providesChannelDataMapper() {
        return new ChannelDataMapper();
    }

    @Provides
    public ChannelDomainMapper providesChannelDomainMapperToData() {
        return new ChannelDomainMapper();
    }

    @Provides
    public ChannelSearchResultDomainMapper providesChannelSearchResultDomainMapper(ChannelDomainMapper channelDomainMapper) {
        return new ChannelSearchResultDomainMapper(channelDomainMapper);
    }

    @Provides
    public VideoSearchResultDomainMapper providesVideoSearchResultDomainMapper(VideoDomainMapper videoDomainMapper) {
        return new VideoSearchResultDomainMapper(videoDomainMapper);
    }

    @Provides
    public VideoViewMapper providesVideoViewMapper() {
        return new VideoViewMapper();
    }

    @Provides
    public VideoDomainMapper providesVideoDomainMapper() {
        return new VideoDomainMapper();
    }

}
