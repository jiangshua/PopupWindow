package com.popup.simplepopupwindow.popup.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;

import android.content.ContextWrapper;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;


public class SimplePopupWindowQueue {

  private static SimplePopupWindowQueue singleton = null;

  private SimplePopupWindowQueue() {}

  public static SimplePopupWindowQueue getInstance() {
    if (singleton == null) {
      singleton = new SimplePopupWindowQueue();
    }
    return singleton;
  }

  public static WeakHashMap<FragmentManager, List<BasePopupWindow>> sDialogFragments =
      new WeakHashMap<>();
  private List<BasePopupWindow> mSiblingDialogs;

  public void addPopupWindowToQueue(BasePopupWindow basePopupWindow) {
    View anchorView = basePopupWindow.getAnchorView();
    if (anchorView == null || basePopupWindow == null) {
      throw new IllegalArgumentException(
          "The anchorView view should not be null,the anchorView = " + anchorView);
    } else {
      FragmentActivity activity = null;
      if (basePopupWindow.getAnchorView().getContext() instanceof FragmentActivity) {
        activity = (FragmentActivity) basePopupWindow.getAnchorView().getContext();
      } else if (basePopupWindow.getAnchorView().getContext() instanceof ContextWrapper) {
        activity =
            (FragmentActivity) ((ContextWrapper) basePopupWindow.getAnchorView().getContext())
                .getBaseContext();
      }
      if (activity != null) {
        final FragmentManager manager = activity.getSupportFragmentManager();
        mSiblingDialogs = sDialogFragments.get(manager);
        if (mSiblingDialogs == null) {
          mSiblingDialogs = new ArrayList<>();
          sDialogFragments.put(manager, mSiblingDialogs);
        }
        if (!mSiblingDialogs.contains(basePopupWindow)) {
          mSiblingDialogs.add(basePopupWindow);
          //对添加进来的KwaiPopupWindow进行排序
          Collections.sort(mSiblingDialogs);
        }
      }
    }
  }

  public void removeQueueHeaderPopupWindow() {
    if (mSiblingDialogs != null && !mSiblingDialogs.isEmpty()) {
      BasePopupWindow currentPopupWindow = getQueueHeaderPopupWindow();
      int currentPopupPriority = currentPopupWindow.getPopupPriority();
      mSiblingDialogs.remove(currentPopupWindow);
      if (currentPopupPriority == PopupPriority.SECOND_PRIORITY
          && !mSiblingDialogs.isEmpty()) {
        Iterator<BasePopupWindow> iterator = mSiblingDialogs.iterator();
        while (iterator.hasNext()) {
          BasePopupWindow basePopupWindow = iterator.next();
          if (basePopupWindow.getPopupPriority() == PopupPriority.SECOND_PRIORITY) {
            iterator.remove();
          }
        }
      }
    }
  }

  public List<BasePopupWindow> getQueuePopupWindow() {
    return mSiblingDialogs;
  }

  @Nullable
  public BasePopupWindow getQueueHeaderPopupWindow() {
    if (mSiblingDialogs != null && mSiblingDialogs.size() > 0) {
      return mSiblingDialogs.get(0);
    }
    return null;
  }
}
