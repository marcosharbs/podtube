package br.com.harbsti.podtube.downloads.data.model.realm;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by marcosharbs on 06/12/16.
 */

public class DownloadRealm extends RealmObject {

    @PrimaryKey
    private String id;
    private String image;
    private String title;
    private Date pusblishDate;
    private String mp3File;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPusblishDate() {
        return pusblishDate;
    }

    public void setPusblishDate(Date pusblishDate) {
        this.pusblishDate = pusblishDate;
    }

    public String getMp3File() {
        return mp3File;
    }

    public void setMp3File(String mp3File) {
        this.mp3File = mp3File;
    }

}
