package com.popup.simplepopupwindow.popup.base;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import android.support.annotation.IntDef;

@IntDef({
    YGravity.CENTER,
    YGravity.ABOVE,
    YGravity.BELOW,
    YGravity.ALIGN_TOP,
    YGravity.ALIGN_BOTTOM,
})
@Retention(RetentionPolicy.SOURCE)
public @interface YGravity {
  int CENTER = 0;
  int ABOVE = 1;
  int BELOW = 2;
  int ALIGN_TOP = 3;
  int ALIGN_BOTTOM = 4;
}
