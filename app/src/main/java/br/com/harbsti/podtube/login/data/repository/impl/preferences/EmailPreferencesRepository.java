package br.com.harbsti.podtube.login.data.repository.impl.preferences;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.concurrent.Callable;

import br.com.harbsti.podtube.login.data.repository.EmailRepository;
import rx.Observable;

/**
 * Created by marcosharbs on 02/08/17.
 */

public class EmailPreferencesRepository implements EmailRepository {

    private Context context;

    public EmailPreferencesRepository(Context context) {
        this.context = context;
    }

    @Override
    public Observable<String> saveEmail(final String email) {
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                PreferenceManager.getDefaultSharedPreferences(context).edit().putString("USER_EMAIL", email).apply();
                return email;
            }
        });
    }

    @Override
    public Observable<String> getEmail() {
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return PreferenceManager.getDefaultSharedPreferences(context).getString("USER_EMAIL", null);
            }
        });
    }

}
