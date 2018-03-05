package br.com.harbsti.podtube.channels.domain.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.util.List;

/**
 * Created by marcosharbs on 05/12/16.
 */

@AutoValue
public abstract class ChannelSearchResult {

    @Nullable public abstract String offset();
    public abstract List<Channel> channels();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder offset(@Nullable String offset);
        public abstract Builder channels(List<Channel> channels);
        public abstract ChannelSearchResult build();
    }

    public static Builder builder() {
        return new AutoValue_ChannelSearchResult.Builder();
    }

}
