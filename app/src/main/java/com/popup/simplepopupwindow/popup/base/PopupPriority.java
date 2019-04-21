package com.popup.simplepopupwindow.popup.base;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import android.support.annotation.IntDef;

@IntDef({
    PopupPriority.FIRST_PRIORITY,
    PopupPriority.SECOND_PRIORITY,
})
@Retention(RetentionPolicy.SOURCE)
public @interface PopupPriority {
  int FIRST_PRIORITY = 0;
  int SECOND_PRIORITY = 1;
}
