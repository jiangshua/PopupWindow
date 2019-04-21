package com.popup.simplepopupwindow.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {

  public static void save(Context context, String key) {
    SharedPreferences preferences = context.getSharedPreferences("shared", MODE_PRIVATE);
    SharedPreferences.Editor editor = preferences.edit();
    editor.putBoolean(key, true);
    editor.commit();
  }

  public static boolean checkData(Context context, String key) {
    SharedPreferences preference = context.getSharedPreferences("shared", MODE_PRIVATE);
    return preference.getBoolean(key, false);
  }
}
