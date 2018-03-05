package br.com.harbsti.podtube.channels.data.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.math.BigInteger;

/**
 * Created by marcosharbs on 05/12/16.
 */

@AutoValue
public abstract class Channel {

    public abstract String id();
    public abstract String image();
    public abstract String title();
    public abstract String description();
    @Nullable public abstract String subscriptionId();
    @Nullable public abstract String bannerImage();
    @Nullable public abstract BigInteger videosCount();
    @Nullable public abstract BigInteger subscribersCount();
    @Nullable public abstract String uploadsPlaylistName();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(String id);
        public abstract Builder image(String image);
        public abstract Builder title(String title);
        public abstract Builder description(String description);
        public abstract Builder subscriptionId(@Nullable String subscriptionId);
        public abstract Builder bannerImage(@Nullable String bannerImage);
        public abstract Builder videosCount(@Nullable BigInteger videosCount);
        public abstract Builder subscribersCount(@Nullable BigInteger subscribersCount);
        public abstract Builder uploadsPlaylistName(@Nullable String uploadsPlaylistName);
        public abstract Channel build();
    }

    public static Builder builder() {
        return new AutoValue_Channel.Builder();
    }

}
