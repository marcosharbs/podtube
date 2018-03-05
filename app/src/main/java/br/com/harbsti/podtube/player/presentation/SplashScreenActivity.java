package br.com.harbsti.podtube.player.presentation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.common.collect.Lists;

import br.com.harbsti.podtube.Henson;
import br.com.harbsti.podtube.utils.AuthHelper;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.OkHttpClient;

/**
 * Created by marcosharbs on 23/11/16.
 */

public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OkHttpClient client = new OkHttpClient.Builder().build();
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
                .newBuilder(this, client)
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this, config);

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);

        final String email = PreferenceManager.getDefaultSharedPreferences(this).getString("USER_EMAIL", null);

        if(email != null){
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... voids) {
                    try{
                        GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(SplashScreenActivity.this, Lists.newArrayList(YouTubeScopes.YOUTUBE));
                        credential.setSelectedAccountName(email);
                        YouTube youtube = new YouTube.Builder(AuthHelper.HTTP_TRANSPORT,
                                AuthHelper.JSON_FACTORY, credential)
                                .setApplicationName("PodTube").build();
                        youtube.channels().list("snippet").setMine(true).setMaxResults(1l).execute();
                        startActivity(Henson.with(SplashScreenActivity.this).gotoPlayerView().build());
                        finish();
                    }catch (Exception e) {
                        Intent intent = Henson.with(SplashScreenActivity.this).gotoLoginView().build();
                        startActivity(intent);
                        finish();
                    }
                    return null;
                }
            }.execute();
        }else{
            Intent intent = Henson.with(this).gotoLoginView().build();
            startActivity(intent);
            finish();
        }
    }
}
