package br.com.harbsti.podtube.channels.presentation;

import br.com.harbsti.podtube.channels.presentation.model.ChannelSearchResult;
import rx.Observable;

/**
 * Created by marcosharbs on 29/08/17.
 */

public interface SubscriptionsMVP {

    interface Model {
        Observable<ChannelSearchResult> getSubscriptions(String offset);
    }

    interface View {
        void onSubscriptionsLoaded(ChannelSearchResult channelSearchResult);
        void onError(Throwable throwable);
    }

    interface Presenter {
        void setView(View view);
        void getSubscriptions(String offset);
    }

}
