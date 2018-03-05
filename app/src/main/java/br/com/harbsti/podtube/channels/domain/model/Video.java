package br.com.harbsti.podtube.channels.domain.model;

import com.google.auto.value.AutoValue;

import java.util.Date;

/**
 * Created by marcosharbs on 05/12/16.
 */

@AutoValue
public abstract class Video {

    public abstract String id();
    public abstract String image();
    public abstract String title();
    public abstract Date pusblishDate();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(String id);
        public abstract Builder image(String image);
        public abstract Builder title(String title);
        public abstract Builder pusblishDate(Date pusblishDate);
        public abstract Video build();
    }

    public static Builder builder() {
        return new AutoValue_Video.Builder();
    }

}
