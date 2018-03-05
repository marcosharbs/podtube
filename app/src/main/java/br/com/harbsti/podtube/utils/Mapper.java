package br.com.harbsti.podtube.utils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by marcosharbs on 06/12/16.
 */

public abstract class Mapper<Source, Destiny> {

    public abstract Destiny transform(Source source);

    public List<Destiny> transform(List<Source> sourceList){
        List<Destiny> destinyList = new ArrayList<>();
        for(Source source : sourceList){
            destinyList.add(transform(source));
        }
        return destinyList;
    }

    public Observable.Transformer<Source, Destiny> getTransformer() {
        return new Observable.Transformer<Source, Destiny>() {
            @Override
            public Observable<Destiny> call(Observable<Source> sourceObservable) {
                return sourceObservable.map(new Func1<Source, Destiny>() {
                    @Override
                    public Destiny call(Source source) {
                        return transform(source);
                    }
                });
            }
        };
    }

    public Observable.Transformer<List<Source>, List<Destiny>> getTransformerList() {
        return new Observable.Transformer<List<Source>, List<Destiny>>() {
            @Override
            public Observable<List<Destiny>> call(Observable<List<Source>> listObservable) {
                return listObservable.map(new Func1<List<Source>, List<Destiny>>() {
                    @Override
                    public List<Destiny> call(List<Source> sourceList) {
                        return transform(sourceList);
                    }
                });
            }
        };
    }

}
