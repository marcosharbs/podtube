package br.com.harbsti.podtube.login.data.repository;

import rx.Observable;

/**
 * Created by marcosharbs on 02/08/17.
 */

public interface EmailRepository {

    Observable<String> saveEmail(String email);

    Observable<String> getEmail();

}
