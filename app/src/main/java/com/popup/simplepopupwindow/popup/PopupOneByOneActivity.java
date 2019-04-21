package com.popup.simplepopupwindow.popup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.popup.simplepopupwindow.R;
import com.popup.simplepopupwindow.popup.base.BasePopupWindow;
import com.popup.simplepopupwindow.popup.base.PopupColor;
import com.popup.simplepopupwindow.popup.base.XGravity;
import com.popup.simplepopupwindow.popup.base.YGravity;

public class PopupOneByOneActivity extends AppCompatActivity {

  private View mText1, mText2, mText3,mText11, mText22, mText33;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.one_by_one);
    mText1 = findViewById(R.id.mytext1);
    mText2 = findViewById(R.id.mytext2);
    mText3 = findViewById(R.id.mytext3);
    mText11 = findViewById(R.id.mytext11);
    mText22 = findViewById(R.id.mytext22);
    mText33 = findViewById(R.id.mytext33);
    mText1.post(() -> new BasePopupWindow.Builder(this)
        .setText("第一个气泡")
        .setYGravity(YGravity.BELOW)
        .setColorType(PopupColor.WHITE)
        .setOrderAtTime(6)
        .setFocusable(false)
        .setAnchorView(mText1)
        .setOnShowListener(() -> { }).build().show());

    mText2.post(() -> new BasePopupWindow.Builder(this)
        .setText("第二个气泡第二个气泡")
        .setYGravity(YGravity.BELOW)
        .setColorType(PopupColor.WHITE)
        .setAnimationStyle(R.style.BottomPopAnim)
        .setShowDuration(3000L)
        .setOrderAtTime(5)
        .setFocusable(false)
        .setAnchorView(mText2)
        .setOnShowListener(() -> { }).build().show());

    mText3.post(() -> new BasePopupWindow.Builder(this)
        .setText("第三个气泡第三个气泡")
        .setYGravity(YGravity.BELOW)
        .setXGravity(XGravity.ALIGN_RIGHT)
        .setColorType(PopupColor.WHITE)
        .setFocusable(false)
        .setOrderAtTime(4)
        .setOnShowListener(() -> { })
        .setAnchorView(mText3)
        .build().show());


    mText11.post(() -> new BasePopupWindow.Builder(this)
        .setText("第四个气泡")
        .setYGravity(YGravity.BELOW)
        .setColorType(PopupColor.WHITE)
        .setFocusable(false)
        .setOrderAtTime(3)
        .setAnchorView(mText11)
        .setOnShowListener(() -> {

        })
        .setOnShowListener(() -> { }).build().show());

    mText22.post(() -> new BasePopupWindow.Builder(this)
        .setText("第五个气泡第五个气泡")
        .setYGravity(YGravity.BELOW)
        .setColorType(PopupColor.WHITE)
        .setOrderAtTime(2)
        .setAnimationStyle(R.style.BottomPopAnim)
        .setFocusable(false)
        .setAnchorView(mText22)
        .setOnShowListener(() -> { }).build().show());

    mText33.post(() -> new BasePopupWindow.Builder(this)
        .setText("第六个气泡第六个气泡第")
        .setXGravity(XGravity.LEFT)
        .setYGravity(YGravity.ALIGN_TOP)
        .setColorType(PopupColor.WHITE)
        .setOrderAtTime(1)
        .setFocusable(false)
        .setOnShowListener(() -> { })
        .setAnchorView(mText33)
        .build().show());
  }
}
