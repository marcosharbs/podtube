package br.com.harbsti.podtube.utils;

import android.content.Context;
import android.widget.Toast;

import br.com.harbsti.podtube.utils.ResIdException;

/**
 * Created by marcosharbs on 11/11/16.
 */

public class ExceptionHelper {

    public static void handleThrowable(Context context, Throwable throwable) {
        if(throwable instanceof ResIdException) {
            Toast.makeText(context, ((ResIdException)throwable).getResId(), Toast.LENGTH_SHORT).show();
        }else if(throwable.getCause() instanceof ResIdException) {
            Toast.makeText(context, ((ResIdException)throwable.getCause()).getResId(), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
