package br.com.harbsti.podtube.utils;

/**
 * Created by marcosharbs on 11/11/16.
 */

public class ResIdException extends Exception {

    int resId;

    public ResIdException(int resId) {
        this.resId = resId;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
