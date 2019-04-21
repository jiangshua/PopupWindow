package com.popup.simplepopupwindow.popup.base;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import android.support.annotation.IntDef;

@IntDef({
    PopupColor.ORANGE,
    PopupColor.WHITE,
    PopupColor.BLACK,
    PopupColor.LIGHT_BLACK
})
@Retention(RetentionPolicy.SOURCE)
public @interface PopupColor {
  int ORANGE = 0;
  int WHITE = 1;
  int BLACK = 2;
  int LIGHT_BLACK = 3;
}
