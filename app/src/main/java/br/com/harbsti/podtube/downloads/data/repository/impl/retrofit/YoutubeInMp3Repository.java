package br.com.harbsti.podtube.downloads.data.repository.impl.retrofit;

import android.net.Uri;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.Callable;

import br.com.harbsti.podtube.R;
import br.com.harbsti.podtube.utils.ServiceHelper;
import br.com.harbsti.podtube.downloads.data.repository.Mp3Repository;
import br.com.harbsti.podtube.utils.ResIdException;
import br.com.harbsti.podtube.downloads.data.repository.impl.retrofit.service.Mp3Service;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

/**
 * Created by marcosharbs on 06/12/16.
 */

public class YoutubeInMp3Repository implements Mp3Repository {

    private Mp3Service mp3Service;

    public YoutubeInMp3Repository(Mp3Service mp3Service) {
        this.mp3Service = mp3Service;
    }

    @Override
    public Observable<byte[]> getMp3(String video) {
        return mp3Service.getYoutubeInMp3Source("https://www.youtube.com/watch?v="+video)
                .flatMap(new Func1<Response<ResponseBody>, Observable<Response<ResponseBody>>>() {
                    @Override
                    public Observable<Response<ResponseBody>> call(Response<ResponseBody> response) {
                        try{
                            Document doc = Jsoup.parse(response.body().string());
                            Elements elements = doc.select("#download");
                            Uri uri = Uri.parse(elements.first().attr("href"));
                            String iParam = uri.getQueryParameter("i");
                            return mp3Service.getYoutubeInMp3Download(iParam);
                        }catch (Exception e){
                            throw Exceptions.propagate(new ResIdException(R.string.download_error));
                        }
                    }
                })
                .flatMap(new Func1<Response<ResponseBody>, Observable<Response<ResponseBody>>>() {
                    @Override
                    public Observable<Response<ResponseBody>> call(final Response<ResponseBody> responseBodyResponse) {
                        if(responseBodyResponse.body().contentType().toString().equals("text/html")){
                            try{
                                Document doc = Jsoup.parse(responseBodyResponse.body().string());
                                Elements elements = doc.select("meta");
                                String content = elements.first().attr("content");
                                String url = content.substring(content.indexOf("url="), content.length()-1).replaceAll("url='//", "");
                                Uri urlUri = Uri.parse(url);
                                Mp3Service mp3Service = ServiceHelper.createRetrfitService(Mp3Service.class, "http://"+urlUri.getPathSegments().get(0));
                                return mp3Service.getYoutubeinMp3DownloadFile(urlUri.getQueryParameter("r"), urlUri.getQueryParameter("id"), urlUri.getQueryParameter("t"));
                            }catch (Exception e){
                                throw Exceptions.propagate(new ResIdException(R.string.download_error));
                            }
                        }else if(responseBodyResponse.body().contentType().toString().equals("audio/mpeg")){
                            return Observable.just(responseBodyResponse);
                        }
                        return null;
                    }
                })
                .flatMap(new Func1<Response<ResponseBody>, Observable<byte[]>>() {
                    @Override
                    public Observable<byte[]> call(final Response<ResponseBody> responseBodyResponse) {
                        return Observable.fromCallable(new Callable<byte[]>() {
                            @Override
                            public byte[] call() throws Exception {
                                if(responseBodyResponse.body().contentType().toString().equals("text/html")){
                                    throw Exceptions.propagate(new ResIdException(R.string.download_error));
                                }else {
                                    byte[] buffer = new byte[1024];
                                    int bytesRead;
                                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                                    while ((bytesRead = responseBodyResponse.body().byteStream().read(buffer)) != -1) {
                                        output.write(buffer, 0, bytesRead);
                                    }
                                    return output.toByteArray();
                                }
                            }
                        });
                    }
                });
    }

}
