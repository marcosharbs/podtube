package br.com.harbsti.podtube.downloads.domain.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.util.Date;

/**
 * Created by marcosharbs on 06/12/16.
 */

@AutoValue
public abstract class Download {

    public abstract String id();
    public abstract String image();
    public abstract String title();
    public abstract Date pusblishDate();
    @Nullable public abstract String mp3File();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(String id);
        public abstract Builder image(String image);
        public abstract Builder title(String title);
        public abstract Builder pusblishDate(Date pusblishDate);
        public abstract Builder mp3File(@Nullable String mp3File);
        public abstract Download build();
    }

    public static Builder builder() {
        return new AutoValue_Download.Builder();
    }

}
