package com.popup.simplepopupwindow.popup.base;


import java.util.List;

import android.os.Handler;
import android.widget.PopupWindow;

public class SimplePopupWindowProxy implements PopupLifeCycle, PopupWindow.OnDismissListener {

  /** 对同一时间添加进来的气泡进行优先级排序，故show时延迟10ms **/
  private static final int QUEUE_INIT_DELAY_TIME = 10;
  private BasePopupWindow mRealPopupWindow;

  public SimplePopupWindowProxy(BasePopupWindow realPopupWindow) {
    SimplePopupWindowQueue.getInstance().addPopupWindowToQueue(realPopupWindow);
  }

  @Override
  public void show() {
    new Handler().postDelayed(() -> {
      mRealPopupWindow =  SimplePopupWindowQueue.getInstance().getQueueHeaderPopupWindow();
      if (!isExistPopupShowing() && mRealPopupWindow != null) {
        mRealPopupWindow.show();
        mRealPopupWindow.getPopupWindow().setOnDismissListener(SimplePopupWindowProxy.this);
      }
    }, QUEUE_INIT_DELAY_TIME);
  }

  @Override
  public void dismiss() {
    mRealPopupWindow.dismiss();
    SimplePopupWindowQueue.getInstance().removeQueueHeaderPopupWindow();
    mRealPopupWindow =  SimplePopupWindowQueue.getInstance().getQueueHeaderPopupWindow();
    show();
  }

  @Override
  public void onDismiss() {
    dismiss();
  }

  private boolean isExistPopupShowing() {
    List<BasePopupWindow> popupWindows =  SimplePopupWindowQueue.getInstance().getQueuePopupWindow();
    if (popupWindows != null) {
      for (final BasePopupWindow popupWindowItem : popupWindows) {
        if (popupWindowItem.isShowing()) {
          return true;
        }
      }
    }
    return false;
  }
}
