package br.com.harbsti.podtube.channels.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import br.com.harbsti.podtube.Henson;
import br.com.harbsti.podtube.R;
import br.com.harbsti.podtube.di.components.DaggerSubscriptionsComponent;
import br.com.harbsti.podtube.di.modules.SubscriptionsModule;
import br.com.harbsti.podtube.di.App;
import br.com.harbsti.podtube.utils.ExceptionHelper;
import br.com.harbsti.podtube.channels.presentation.model.Channel;
import br.com.harbsti.podtube.channels.presentation.model.ChannelSearchResult;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by marcosharbs on 29/11/16.
 */

public class SubscriptionsView extends Fragment implements SubscriptionsMVP.View, ChannelsAdapter.OnChannelsAdapter {

    @InjectView(R.id.channels_recyclerview) RecyclerView channelsRecyclerView;
    @InjectView(R.id.channels_swiperefresh) SwipeRefreshLayout channelSwipeRefresh;
    @Inject SubscriptionsMVP.Presenter presenter;
    private ChannelsAdapter channelsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.subscriptions, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);

        DaggerSubscriptionsComponent.builder()
                .applicationComponent(App.get(getActivity()).getComponent())
                .subscriptionsModule(new SubscriptionsModule(this))
                .build()
                .inject(this);

        channelsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        channelsAdapter = null;
        channelSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                channelsAdapter = null;
                presenter.getSubscriptions(null);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.setView(this);
        presenter.getSubscriptions(null);
    }

    @Override
    public void onSubscriptionsLoaded(ChannelSearchResult channelSearchResult) {
        if (channelsAdapter == null) {
            channelsAdapter = new ChannelsAdapter(getActivity(), channelSearchResult, this);
            channelsRecyclerView.setAdapter(channelsAdapter);
        } else {
            channelsAdapter.appendChannelResult(channelSearchResult);
        }
        channelSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void onError(Throwable throwable) {
        channelSwipeRefresh.setRefreshing(false);
        ExceptionHelper.handleThrowable(getActivity(), throwable);
    }

    @Override
    public void onLoadMore(String offset) {
        presenter.getSubscriptions(offset);
    }

    @Override
    public void onItemSelect(Channel channel) {
        Intent intent = Henson.with(getActivity())
                .gotoChannelDetailView()
                .channel(channel)
                .build();
        startActivity(intent);
    }

}
