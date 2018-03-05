package br.com.harbsti.podtube.channels.data.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.util.List;

/**
 * Created by marcosharbs on 05/12/16.
 */

@AutoValue
public abstract class VideoSearchResult {

    @Nullable public abstract String offset();
    public abstract List<Video> videos();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder offset(@Nullable String offset);
        public abstract Builder videos(List<Video> videos);
        public abstract VideoSearchResult build();
    }

    public static VideoSearchResult.Builder builder() {
        return new AutoValue_VideoSearchResult.Builder();
    }

}
