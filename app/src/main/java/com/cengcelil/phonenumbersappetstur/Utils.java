package com.cengcelil.phonenumbersappetstur;

import android.view.View;
import android.widget.ProgressBar;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Utils {
    public static final int NO_CHARACTER_CODE = 55;
    public static final int NEED_CHARACTER_CODE = 56;
    public static final int UNKNOWN_CODE= 57;
    public static final int OK_CHARACTER_CODE= 58;

    public static final String DATE_FORMAT = "dd/ MM/ yy";
    public static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, new Locale("tr"));

    public static void showProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
    }

    public static void hideProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);

    }
}
