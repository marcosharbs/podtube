package br.com.harbsti.podtube.di.modules;

import android.content.Context;

import br.com.harbsti.podtube.channels.data.repository.ChannelRepository;
import br.com.harbsti.podtube.channels.data.repository.impl.youtubedataapi.ChannelYoutubeDataApiRepository;
import br.com.harbsti.podtube.channels.domain.mapper.ChannelDataMapper;
import br.com.harbsti.podtube.channels.domain.mapper.ChannelDomainMapper;
import br.com.harbsti.podtube.channels.domain.mapper.ChannelSearchResultDomainMapper;
import br.com.harbsti.podtube.channels.domain.mapper.VideoDomainMapper;
import br.com.harbsti.podtube.channels.domain.mapper.VideoSearchResultDomainMapper;
import br.com.harbsti.podtube.channels.domain.usecase.ChannelUseCase;
import br.com.harbsti.podtube.channels.domain.usecase.impl.ChannelUseCaseImpl;
import br.com.harbsti.podtube.channels.presentation.SearchChannelMVP;
import br.com.harbsti.podtube.channels.presentation.SearchChannelModel;
import br.com.harbsti.podtube.channels.presentation.SearchChannelPresenter;
import br.com.harbsti.podtube.channels.presentation.SearchChannelView;
import br.com.harbsti.podtube.channels.presentation.mapper.ChannelSearchResultViewMapper;
import br.com.harbsti.podtube.channels.presentation.mapper.ChannelViewMapper;
import br.com.harbsti.podtube.utils.AuthHelper;
import dagger.Module;
import dagger.Provides;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by marcosharbs on 29/08/17.
 */

@Module
public class SearchChannelModule {

    private SearchChannelView view;

    public SearchChannelModule(SearchChannelView view) {
        this.view = view;
    }

    @Provides
    public Context provideContext() {
        return view;
    }

    @Provides
    public SearchChannelMVP.Presenter providesPresenter(SearchChannelMVP.Model model) {
        return new SearchChannelPresenter(model, Schedulers.newThread(), AndroidSchedulers.mainThread());
    }

    @Provides
    public SearchChannelMVP.Model providesModel(ChannelUseCase channelUseCase,
                                                ChannelSearchResultViewMapper channelSearchResultViewMapper) {
        return new SearchChannelModel(channelUseCase, channelSearchResultViewMapper);
    }

    @Provides
    public ChannelSearchResultViewMapper providesChannelSearchResultViewMapper(ChannelViewMapper channelMapper) {
        return new ChannelSearchResultViewMapper(channelMapper);
    }

    @Provides
    public ChannelViewMapper providesChannelViewMapper() {
        return new ChannelViewMapper();
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
    public ChannelDataMapper providesChannelDataMapper() {
        return new ChannelDataMapper();
    }

    @Provides
    public ChannelDomainMapper providesChannelDomainMapper() {
        return new ChannelDomainMapper();
    }

    @Provides
    public ChannelSearchResultDomainMapper providesChannelSearchResultDomainMapper(ChannelDomainMapper channelMapper) {
        return new ChannelSearchResultDomainMapper(channelMapper);
    }

    @Provides
    public VideoSearchResultDomainMapper providesVideoSearchResultDomainMapper(VideoDomainMapper videoDomainMapper) {
        return new VideoSearchResultDomainMapper(videoDomainMapper);
    }

    @Provides
    public VideoDomainMapper providesVideoDomainMapper() {
        return new VideoDomainMapper();
    }

    @Provides
    public ChannelRepository providesChannelRepository(Context context) {
        return new ChannelYoutubeDataApiRepository(AuthHelper.getYoutubeClient(context));
    }

}
