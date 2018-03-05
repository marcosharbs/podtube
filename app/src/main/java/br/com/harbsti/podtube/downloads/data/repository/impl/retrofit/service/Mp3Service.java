package br.com.harbsti.podtube.downloads.data.repository.impl.retrofit.service;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by marcosharbs on 08/11/16.
 */

public interface Mp3Service {

    @GET("/en/download/")
    Observable<Response<ResponseBody>> getYoutubeInMp3Source(@Query("video") String video);

    @GET("/download/get/")
    Observable<Response<ResponseBody>> getYoutubeInMp3Download(@Query("i") String i);

    @GET("/download/get/")
    Observable<Response<ResponseBody>> getYoutubeinMp3DownloadFile(@Query("r") String r, @Query("id") String id, @Query("t") String t);

}
