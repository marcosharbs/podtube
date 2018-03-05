package br.com.harbsti.podtube.channels.presentation;

import br.com.harbsti.podtube.channels.presentation.model.ChannelSearchResult;
import rx.Observable;

/**
 * Created by marcosharbs on 29/08/17.
 */

public interface SearchChannelMVP {

    interface Model {
        Observable<ChannelSearchResult> getChannels(String term, String offset);
    }

    interface View {
        void onChannelsLoaded(ChannelSearchResult channelSearchResult);
        void onError(Throwable throwable);
    }

    interface Presenter {
        void setView(View view);
        void searchChannels(String term, String offset);
    }

}
