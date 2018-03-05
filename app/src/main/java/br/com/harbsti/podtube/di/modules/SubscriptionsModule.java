package br.com.harbsti.podtube.di.modules;

import android.content.Context;

import br.com.harbsti.podtube.channels.data.repository.SubscriptionRepository;
import br.com.harbsti.podtube.channels.data.repository.impl.youtubedataapi.SubscriptionYoutubeDataApiRepository;
import br.com.harbsti.podtube.channels.domain.mapper.ChannelDomainMapper;
import br.com.harbsti.podtube.channels.domain.mapper.ChannelSearchResultDomainMapper;
import br.com.harbsti.podtube.channels.domain.usecase.SubscriptionUseCase;
import br.com.harbsti.podtube.channels.domain.usecase.impl.SubscriptionUseCaseImpl;
import br.com.harbsti.podtube.channels.presentation.SubscriptionsMVP;
import br.com.harbsti.podtube.channels.presentation.SubscriptionsModel;
import br.com.harbsti.podtube.channels.presentation.SubscriptionsPresenter;
import br.com.harbsti.podtube.channels.presentation.SubscriptionsView;
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
public class SubscriptionsModule {

    private SubscriptionsView view;

    public SubscriptionsModule(SubscriptionsView view) {
        this.view = view;
    }

    @Provides
    public Context provideContext() {
        return view.getActivity();
    }

    @Provides
    public SubscriptionsMVP.Presenter provideSubscriptionsPresenter(SubscriptionsMVP.Model model) {
        return new SubscriptionsPresenter(model, Schedulers.newThread(), AndroidSchedulers.mainThread());
    }

    @Provides
    public SubscriptionsMVP.Model provideSubscriptionsModel(SubscriptionUseCase subscriptionUseCase,
                                                            ChannelSearchResultViewMapper channelSearchResultViewMapper) {
        return new SubscriptionsModel(subscriptionUseCase, channelSearchResultViewMapper);
    }

    @Provides
    public ChannelSearchResultViewMapper provideChannelSearchResultViewMapper(ChannelViewMapper channelViewMapper) {
        return new ChannelSearchResultViewMapper(channelViewMapper);
    }

    @Provides
    public ChannelViewMapper provideChannelViewMapper() {
        return new ChannelViewMapper();
    }

    @Provides
    public SubscriptionUseCase provideSubscriptionUseCase(SubscriptionRepository subscriptionRepository,
                                                          ChannelSearchResultDomainMapper channelSearchResultDomainMapper) {
        return new SubscriptionUseCaseImpl(subscriptionRepository, channelSearchResultDomainMapper);
    }

    @Provides
    public ChannelSearchResultDomainMapper provideChannelSearchResultDomainMapper(ChannelDomainMapper channelMapper) {
        return new ChannelSearchResultDomainMapper(channelMapper);
    }

    @Provides
    public ChannelDomainMapper provideChannelDomainMapper() {
        return new ChannelDomainMapper();
    }

    @Provides
    public SubscriptionRepository provideSubscriptionRepository(Context context) {
        return new SubscriptionYoutubeDataApiRepository(AuthHelper.getYoutubeClient(context));
    }

}
