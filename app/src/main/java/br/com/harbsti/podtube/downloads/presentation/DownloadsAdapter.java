package br.com.harbsti.podtube.downloads.presentation;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.List;

import br.com.harbsti.podtube.R;
import br.com.harbsti.podtube.downloads.presentation.model.Download;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by marcosharbs on 30/11/16.
 */

public class DownloadsAdapter extends RecyclerView.Adapter<DownloadsAdapter.ViewHolder> {

    private static final int DOWNLOAD_VIDEO = 0;

    private List<Download> downloads;
    private Context context;
    private DownloadsAdapter.OnDownloadsAdapter onDownloadsAdapter;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public DownloadsAdapter(Context context, List<Download> downloads, DownloadsAdapter.OnDownloadsAdapter onDownloadsAdapter) {
        this.context = context;
        this.downloads = downloads;
        this.onDownloadsAdapter = onDownloadsAdapter;
    }

    @Override
    public int getItemViewType(int position) {
        return DOWNLOAD_VIDEO;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_download, parent, false);
        return new DownloadsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Download download = downloads.get(position);

        holder.imageVideo.setImageURI(ImageRequestBuilder.newBuilderWithSource(Uri.parse(download.image())).build().getSourceUri());
        holder.textVideoName.setText(download.title());
        holder.textVideoDate.setText(sdf.format(download.pusblishDate()));

        if(download.mp3File() != null){
            holder.imageDelete.setVisibility(View.VISIBLE);
            holder.downloadindicator.setVisibility(View.GONE);
            holder.downloadindicator.hide();
            holder.imagePlay.setAlpha(1f);
            holder.imagePlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onDownloadsAdapter.onPlay(download);
                }
            });
            holder.imageDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onDownloadsAdapter.onDelete(download);
                }
            });
        }else{
            holder.imageDelete.setVisibility(View.GONE);
            holder.downloadindicator.setVisibility(View.VISIBLE);
            holder.downloadindicator.show();
            holder.imagePlay.setAlpha(0.3f);
            holder.imagePlay.setOnClickListener(null);
            holder.imageDelete.setOnClickListener(null);
        }

        holder.imageMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDownloadsAdapter.onViewMovie(download);
            }
        });
    }

    @Override
    public int getItemCount() {
        return downloads.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.image_video) SimpleDraweeView imageVideo;
        @InjectView(R.id.text_video_name) TextView textVideoName;
        @InjectView(R.id.text_video_date) TextView textVideoDate;
        @InjectView(R.id.download_indicator) AVLoadingIndicatorView downloadindicator;
        @InjectView(R.id.image_play) ImageView imagePlay;
        @InjectView(R.id.image_delete) ImageView imageDelete;
        @InjectView(R.id.image_movie) ImageView imageMovie;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public interface OnDownloadsAdapter {
        void onPlay(Download download);
        void onDelete(Download download);
        void onViewMovie(Download download);
    }

}
