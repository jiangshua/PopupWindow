package com.popup.simplepopupwindow.popup.base;

import android.view.View;

import com.popup.simplepopupwindow.R;

public class PopupWindowHelper {

  /**
   * 根据垂直gravity计算y偏移
   */
  public static int calculateY(View anchor, int vertGravity, int measuredH, int y) {
    switch (vertGravity) {
      case YGravity.ABOVE:
        // anchor view之上
        y -= measuredH + anchor.getHeight();
        break;
      case YGravity.ALIGN_BOTTOM:
        // anchor view底部对齐
        y -= measuredH;
        break;
      case YGravity.CENTER:
        // anchor view垂直居中
        y -= anchor.getHeight() / 2 + measuredH / 2;
        break;
      case YGravity.ALIGN_TOP:
        // anchor view顶部对齐
        y -= anchor.getHeight();
        break;
      case YGravity.BELOW:
      default:
        // anchor view之下
        break;
    }
    return y;
  }

  /**
   * 根据水平gravity计算x偏移
   *
   * @return
   */
  public static int calculateX(View anchor, int horizGravity, int measuredW, int x) {
    switch (horizGravity) {
      case XGravity.LEFT:
        // anchor view左侧
        x -= measuredW;
        break;
      case XGravity.ALIGN_RIGHT:
        // 与anchor view右边对齐
        x -= measuredW - anchor.getWidth();
        break;
      case XGravity.CENTER:
        // anchor view水平居中
        x += anchor.getWidth() / 2 - measuredW / 2;
        break;
      case XGravity.ALIGN_LEFT:
        // 与anchor view左边对齐
        // Default position.
        break;
      case XGravity.RIGHT:
        // anchor view右侧
        x += anchor.getWidth();
        break;
    }

    return x;
  }

  public static int popupDefaultLayout(BasePopupWindow basePopupWindow) {
    int xGravity = basePopupWindow.getXGravity();
    int yGravity = basePopupWindow.getYGravity();
    int colorType = basePopupWindow.getPopupColorType();
    switch (yGravity) {
      case YGravity.ABOVE:
        switch (colorType) {
          case PopupColor.WHITE:
            return R.layout.popup_layout_above_white;
          case PopupColor.BLACK:
            return R.layout.popup_layout_above_black;
          case PopupColor.ORANGE:
            return R.layout.popup_layout_above_orange;
        }
      case YGravity.BELOW:
        switch (colorType) {
          case PopupColor.WHITE:
            return R.layout.popup_layout_below_white;
          case PopupColor.BLACK:
            return R.layout.popup_layout_below_black;
          case PopupColor.ORANGE:
            return R.layout.popup_layout_below_orange;
        }
    }

    switch (xGravity) {
      case XGravity.LEFT:
        return R.layout.popup_layout_left;
      case XGravity.RIGHT:
        return R.layout.popup_layout_right;
    }

    return 0;
  }
}