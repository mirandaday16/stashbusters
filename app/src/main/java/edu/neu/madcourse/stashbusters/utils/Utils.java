package edu.neu.madcourse.stashbusters.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Utils
 */
public class Utils {
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
