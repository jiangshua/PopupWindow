package com.popup.simplepopupwindow.popup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;

import com.popup.simplepopupwindow.R;
import com.popup.simplepopupwindow.popup.base.BasePopupWindow;
import com.popup.simplepopupwindow.popup.base.PopupPriority;
import com.popup.simplepopupwindow.popup.base.XGravity;
import com.popup.simplepopupwindow.popup.base.YGravity;
import com.popup.simplepopupwindow.utils.SharedPreferencesUtil;

public class PopupOnceShowOneActivity extends AppCompatActivity {

  private View mText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.once_show_one);
    mText = findViewById(R.id.mytext);
    mText.getViewTreeObserver()
        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override
          public void onGlobalLayout() {
            mText.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            showManyPop(mText);
          }
        });
  }

  private void showManyPop(View targetView) {
    if (!SharedPreferencesUtil.checkData(this, "one")) {
      new BasePopupWindow.Builder(this)
          .setText("第一个气泡")
          .setAnchorView(targetView)
          .setYGravity(YGravity.ABOVE)
          .setPopupPriority(PopupPriority.SECOND_PRIORITY)
          .setOnShowListener(() -> SharedPreferencesUtil.save(PopupOnceShowOneActivity.this, "one"))
          .build()
          .show();
    }

    if (!SharedPreferencesUtil.checkData(this, "two")) {
      new BasePopupWindow.Builder(this)
          .setText("第二个气泡")
          .setAnchorView(targetView)
          .setYGravity(YGravity.BELOW)
          .setPopupPriority(PopupPriority.SECOND_PRIORITY)
          .setOnShowListener(() -> SharedPreferencesUtil.save(PopupOnceShowOneActivity.this, "two"))
          .build().show();
    }

    if (!SharedPreferencesUtil.checkData(this, "three")) {
      new BasePopupWindow.Builder(this)
          .setText("第三个气泡")
          .setAnchorView(targetView)
          .setXGravity(XGravity.LEFT)
          .setPopupPriority(PopupPriority.SECOND_PRIORITY)
          .setOnShowListener(
              () -> SharedPreferencesUtil.save(PopupOnceShowOneActivity.this, "three"))
          .build().show();
    }
  }
}
