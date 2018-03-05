package br.com.harbsti.podtube.channels.presentation;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.f2prateek.dart.HensonNavigable;

import javax.inject.Inject;

import br.com.harbsti.podtube.Henson;
import br.com.harbsti.podtube.R;
import br.com.harbsti.podtube.di.components.DaggerSearchChannelComponent;
import br.com.harbsti.podtube.di.modules.SearchChannelModule;
import br.com.harbsti.podtube.di.App;
import br.com.harbsti.podtube.utils.ExceptionHelper;
import br.com.harbsti.podtube.channels.presentation.model.Channel;
import br.com.harbsti.podtube.channels.presentation.model.ChannelSearchResult;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by marcosharbs on 05/12/16.
 */

@HensonNavigable
public class SearchChannelView extends AppCompatActivity implements SearchChannelMVP.View, ChannelsAdapter.OnChannelsAdapter, SearchView.OnQueryTextListener {

    @InjectView(R.id.channels_recyclerview) RecyclerView channelsRecyclerView;
    @Inject SearchChannelMVP.Presenter presenter;
    private SearchView searchView;
    private ChannelsAdapter channelsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_channel);
        ButterKnife.inject(this);

        DaggerSearchChannelComponent.builder()
                .applicationComponent(App.get(this).getComponent())
                .searchChannelModule(new SearchChannelModule(this))
                .build()
                .inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        channelsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);
    }

    @Override
    public void onChannelsLoaded(ChannelSearchResult channelSearchResult) {
        if(channelsAdapter == null){
            channelsAdapter = new ChannelsAdapter(this, channelSearchResult, this);
            channelsRecyclerView.setAdapter(channelsAdapter);
        }else{
            channelsAdapter.appendChannelResult(channelSearchResult);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        ExceptionHelper.handleThrowable(this, throwable);
    }

    @Override
    public void onLoadMore(String offset) {
        presenter.searchChannels(searchView.getQuery().toString(), offset);
    }

    @Override
    public void onItemSelect(Channel channel) {
        Intent intent = Henson.with(this)
                .gotoChannelDetailView()
                .channel(channel)
                .build();
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_channel_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.channel_search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getString(R.string.search_channel_hint));
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        channelsAdapter = null;
        presenter.searchChannels(s, null);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
}
