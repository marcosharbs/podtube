package br.com.harbsti.podtube.login.presentation;

import android.content.pm.PackageManager;
import android.support.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import rx.Scheduler;
import rx.Subscriber;

/**
 * Created by marcosharbs on 02/08/17.
 */

public class LoginPresenter implements LoginMVP.Presenter {

    @Nullable private LoginMVP.View view;
    private LoginMVP.Model model;
    private Scheduler subscriberSchduler;
    private Scheduler observerScheduler;

    public LoginPresenter(LoginMVP.Model model,
                          Scheduler subscriberSchduler,
                          Scheduler observerScheduler) {

        this.model = model;
        this.subscriberSchduler = subscriberSchduler;
        this.observerScheduler = observerScheduler;
    }

    @Override
    public void setView(LoginMVP.View view) {
        this.view = view;
    }

    @Override
    public void onGoogleLogin() {
        if(view != null){
            view.startGoogleSignInIntent();
        }
    }

    @Override
    public void onGoogleSignInResult(GoogleSignInResult result) {
        if(view != null){
            if(result.isSuccess()){
                view.setEmail(result.getSignInAccount().getEmail());
                view.askContactsPermission();
            }else{
                view.onGoogleError(result.getStatus().getStatusMessage());
            }
        }
    }

    @Override
    public void onContactsPermissionResult(String permissions[], int[] grantResults) {
        if(view != null){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onPermissionContactsGranted();
            }
        }
    }

    @Override
    public void onPermissionContactsGranted() {
        if(view != null){
            view.askYoutubePermission();
        }
    }

    @Override
    public void onYoutubeGranted() {
        if(view != null){
            if(view.getEmail() == null || view.getEmail().length() == 0){
                view.onInvalidEmail();
            }else{
                model.saveEmail(view.getEmail())
                        .subscribeOn(subscriberSchduler)
                        .observeOn(observerScheduler)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        view.onError(e);
                    }

                    @Override
                    public void onNext(String s) {
                        view.onLoginSuccess();
                    }
                });
            }
        }
    }

}
