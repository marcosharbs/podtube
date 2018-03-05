package br.com.harbsti.podtube.login.presentation;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import rx.Observable;

/**
 * Created by marcosharbs on 02/08/17.
 */

public interface LoginMVP {

    interface Model {
        Observable<String> saveEmail(String email);
    }

    interface View {
        void onInvalidEmail();
        void onLoginSuccess();
        String getEmail();
        void setEmail(String email);
        void startGoogleSignInIntent();
        void askContactsPermission();
        void askYoutubePermission();
        void onGoogleError(String error);
        void onError(Throwable throwable);
    }

    interface Presenter {
        void setView(LoginMVP.View view);
        void onGoogleLogin();
        void onGoogleSignInResult(GoogleSignInResult result);
        void onContactsPermissionResult(String permissions[], int[] grantResults);
        void onPermissionContactsGranted();
        void onYoutubeGranted();
    }

}
