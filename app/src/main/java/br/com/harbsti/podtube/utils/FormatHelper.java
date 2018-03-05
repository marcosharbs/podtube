package br.com.harbsti.podtube.utils;

import android.content.Context;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.harbsti.podtube.R;

/**
 * Created by marcosharbs on 16/10/17.
 */

public class FormatHelper {

    private static FormatHelper instance;

    private static SimpleDateFormat dateMediumFormat;

    private DecimalFormat intLongFormatter;

    private FormatHelper(){}

    public static FormatHelper getInstance(Context context) {
        if(instance == null){
            instance = new FormatHelper();
            instance.dateMediumFormat = new SimpleDateFormat(context.getString(R.string.date_medium_format));
            instance.intLongFormatter = new DecimalFormat(context.getString(R.string.int_long_format));
        }

        return instance;
    }

    public String formatMediumDate(Date date) {
        return dateMediumFormat.format(date);
    }

    public String  formatLongInt(BigInteger value) {
        return intLongFormatter.format(value);
    }

}
