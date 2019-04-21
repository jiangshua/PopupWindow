package com.popup.simplepopupwindow.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;

public class ViewUtil {

  public static int getDisplayWidth(@NonNull Activity activity) {
    return getContentView(activity).getWidth();
  }

  public static View getContentView(@NonNull Activity activity) {
    return getContentView(activity.getWindow());
  }

  public static View getContentView(@NonNull Window window) {
    return window.getDecorView().findViewById(android.R.id.content);
  }
}
