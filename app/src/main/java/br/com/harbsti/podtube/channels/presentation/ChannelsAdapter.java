package br.com.harbsti.podtube.channels.presentation;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.wang.avi.AVLoadingIndicatorView;

import br.com.harbsti.podtube.R;
import br.com.harbsti.podtube.channels.presentation.model.Channel;
import br.com.harbsti.podtube.channels.presentation.model.ChannelSearchResult;
import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by marcosharbs on 29/11/16.
 */

public class ChannelsAdapter extends RecyclerView.Adapter<ChannelsAdapter.ViewHolder> {

    private static final int VIEW_CHANNEL = 0;
    private static final int VIEW_LOAD = 1;

    private ChannelSearchResult channelResult;
    private Context context;
    private OnChannelsAdapter onChannelsAdapter;

    public ChannelsAdapter(Context context, ChannelSearchResult channelResult, OnChannelsAdapter onChannelsAdapter) {
        this.context = context;
        this.channelResult = channelResult;
        this.onChannelsAdapter = onChannelsAdapter;
    }

    public void appendChannelResult(ChannelSearchResult channelResult) {
        this.channelResult = ChannelSearchResult.builder()
                .offset(channelResult.offset())
                .channels(this.channelResult.channels())
                .build();
        this.channelResult.channels().addAll(channelResult.channels());
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_CHANNEL){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel, parent, false);
            return new ViewHolderChannel(view);
        }else if(viewType == VIEW_LOAD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_load, parent, false);
            return new ViewHolderLoad(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if(channelResult.offset() != null && position == channelResult.channels().size()){
            return VIEW_LOAD;
        }
        return VIEW_CHANNEL;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(holder instanceof ViewHolderChannel) {
            final Channel channel = channelResult.channels().get(position);

            ViewHolderChannel holderChannel = (ViewHolderChannel) holder;
            holderChannel.imageChannel.setImageURI(ImageRequestBuilder.newBuilderWithSource(Uri.parse(channel.image())).build().getSourceUri());
            holderChannel.textChannelName.setText(channel.title());
            holderChannel.textChannelDescription.setText(channel.description());

            holderChannel.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onChannelsAdapter != null){
                        onChannelsAdapter.onItemSelect(channel);
                    }
                }
            });
        }else if(holder instanceof ViewHolderLoad) {
            final ViewHolderLoad holderLoad = (ViewHolderLoad) holder;
            holderLoad.labelLoadMore.setText(context.getString(R.string.label_loading_more));
            holderLoad.loadingIndicator.setVisibility(View.VISIBLE);
            onChannelsAdapter.onLoadMore(channelResult.offset());
        }
    }

    @Override
    public int getItemCount() {
        return channelResult.channels().size() + (channelResult.offset() != null ? 1 : 0);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ViewHolderLoad extends ViewHolder {
        @InjectView(R.id.label_load_more) TextView labelLoadMore;
        @InjectView(R.id.loading_indicator) AVLoadingIndicatorView loadingIndicator;

        public ViewHolderLoad(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    class ViewHolderChannel extends ViewHolder {
        @InjectView(R.id.image_channel) SimpleDraweeView imageChannel;
        @InjectView(R.id.text_channel_name) TextView textChannelName;
        @InjectView(R.id.text_channel_description) TextView textChannelDescription;

        public ViewHolderChannel(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public interface OnChannelsAdapter {
        void onLoadMore(String offset);
        void onItemSelect(Channel channel);
    }

}
