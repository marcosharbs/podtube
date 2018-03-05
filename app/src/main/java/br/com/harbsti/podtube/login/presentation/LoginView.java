package br.com.harbsti.podtube.login.presentation;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.f2prateek.dart.HensonNavigable;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.common.collect.Lists;

import javax.inject.Inject;

import br.com.harbsti.podtube.di.App;
import br.com.harbsti.podtube.Henson;
import br.com.harbsti.podtube.R;
import br.com.harbsti.podtube.di.components.DaggerLoginComponent;
import br.com.harbsti.podtube.di.modules.LoginModule;
import br.com.harbsti.podtube.utils.AuthHelper;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by marcosharbs on 23/11/16.
 */

@HensonNavigable
public class LoginView extends AppCompatActivity implements LoginMVP.View {

    @Inject LoginMVP.Presenter presenter;
    private String email;
    private GoogleSignInOptions gso;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.inject(this);
        DaggerLoginComponent.builder()
                .applicationComponent(App.get(this).getComponent())
                .loginModule(new LoginModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);
    }

    @OnClick(R.id.btn_login_google)
    public void onLoginGoogle() {
        presenter.onGoogleLogin();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 001){
            presenter.onGoogleSignInResult(Auth.GoogleSignInApi.getSignInResultFromIntent(data));
        }else if(requestCode == 002 && resultCode == Activity.RESULT_OK){
            presenter.onYoutubeGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 001: {
                presenter.onContactsPermissionResult(permissions, grantResults);
                return;
            }
        }
    }

    @Override
    public void onInvalidEmail() {
        Toast.makeText(this, "E-mail inválido!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginSuccess() {
        startActivity(Henson.with(this).gotoPlayerView().build());
        finish();
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void startGoogleSignInIntent() {
        if(gso == null){
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail().build();
        }

        if(client == null){
            client = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            Toast.makeText(LoginView.this, connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }

        startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(client), 001);
    }

    @Override
    public void askContactsPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.GET_ACCOUNTS}, 001);
        }else {
            presenter.onPermissionContactsGranted();
        }
    }

    @Override
    public void askYoutubePermission() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try{
                    GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(LoginView.this,
                            Lists.newArrayList(YouTubeScopes.YOUTUBE));
                    credential.setSelectedAccountName(email);
                    YouTube youtube = new YouTube.Builder(AuthHelper.HTTP_TRANSPORT,
                            AuthHelper.JSON_FACTORY, credential)
                            .setApplicationName("PodTube").build();
                    youtube.channels().list("snippet").setMine(true).setMaxResults(1l).execute();
                    presenter.onYoutubeGranted();
                }catch (final UserRecoverableAuthIOException e1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startActivityForResult(e1.getIntent(), 002);
                        }
                    });
                }catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    @Override
    public void onGoogleError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

}
