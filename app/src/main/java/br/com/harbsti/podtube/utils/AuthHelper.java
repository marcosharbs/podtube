package br.com.harbsti.podtube.utils;

/**
 * Created by marcosharbs on 25/11/16.
 */

import android.content.Context;
import android.preference.PreferenceManager;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.common.collect.Lists;

/**
 * Shared class used by every sample. Contains methods for authorizing a user and caching credentials.
 */
public class AuthHelper {

    /**
     * Define a global instance of the HTTP transport.
     */
    public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /**
     * Define a global instance of the JSON factory.
     */
    public static final JsonFactory JSON_FACTORY = new JacksonFactory();

    private static YouTube youTube;

    public static YouTube getYoutubeClient(Context context) {
        if(youTube == null){
            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(context,
                    Lists.newArrayList(YouTubeScopes.YOUTUBE));
            credential.setSelectedAccountName(PreferenceManager.getDefaultSharedPreferences(context).getString("USER_EMAIL", null));
            youTube = new com.google.api.services.youtube.YouTube.Builder(AuthHelper.HTTP_TRANSPORT,
                    AuthHelper.JSON_FACTORY, credential)
                            .setApplicationName("PodTube").build();
        }

        return youTube;
    }
}