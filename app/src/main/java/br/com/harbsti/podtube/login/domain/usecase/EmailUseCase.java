package br.com.harbsti.podtube.login.domain.usecase;

import rx.Observable;

/**
 * Created by marcosharbs on 02/08/17.
 */

public interface EmailUseCase {

    Observable<String> saveEmail(String email);

    Observable<String> getEmail();

}
