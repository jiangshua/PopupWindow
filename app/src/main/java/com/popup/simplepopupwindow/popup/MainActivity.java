package com.popup.simplepopupwindow.popup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.popup.simplepopupwindow.R;
import com.popup.simplepopupwindow.popup.base.BasePopupWindow;
import com.popup.simplepopupwindow.popup.base.XGravity;
import com.popup.simplepopupwindow.popup.base.YGravity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private static final String TAG = "MainActivity";

  private View mAboveBtn, mBelowBtn, mRoot;
  private View mRightBtn, mLeftBtn, mTouchIntercept, mNoTouchIntercept, mManyPopup, mManyPopupOnceOne;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_simple_pop);
    initView();
    initEvent();
  }

  private void initView() {
    mAboveBtn = findViewById(R.id.btn_above);
    mBelowBtn = findViewById(R.id.btn_below);
    mRightBtn = findViewById(R.id.btn_right);
    mLeftBtn = findViewById(R.id.btn_left);
    mRoot = findViewById(R.id.root);
    mTouchIntercept = findViewById(R.id.touch_intercept);
    mNoTouchIntercept = findViewById(R.id.no_touch_intercept);
    mManyPopup = findViewById(R.id.many_popup);
    mManyPopupOnceOne = findViewById(R.id.many_popup_once_one);
  }

  private void initEvent() {
    mAboveBtn.setOnClickListener(this);
    mBelowBtn.setOnClickListener(this);
    mRightBtn.setOnClickListener(this);
    mLeftBtn.setOnClickListener(this);
    mRoot.setOnClickListener(this);
    mTouchIntercept.setOnClickListener(this);
    mNoTouchIntercept.setOnClickListener(this);
    mManyPopup.setOnClickListener(this);
    mManyPopupOnceOne.setOnClickListener(this);
  }

  private void showBelowPop(View view) {
    new BasePopupWindow.Builder(this)
        .setText("气泡在下")
        .setAnchorView(view)
        .setYGravity(YGravity.BELOW)
        .setXGravity(XGravity.CENTER)
        .build()
        .show();
  }

  private void showAbovePop(View view) {
    new BasePopupWindow.Builder(this)
        .setText("气泡在上")
        .setAnchorView(view)
        .setYGravity(YGravity.ABOVE)
        .setXGravity(XGravity.CENTER)
        .build()
        .show();
  }

  private void showLeftPop(View view) {
    new BasePopupWindow.Builder(this)
        .setText("气泡在左")
        .setAnchorView(view)
        .setYGravity(YGravity.CENTER)
        .setXGravity(XGravity.LEFT)
        .build()
        .show();
  }

  private void showRightPop(View view) {
    new BasePopupWindow.Builder(this)
        .setText("气泡在右")
        .setAnchorView(view)
        .setYGravity(YGravity.CENTER)
        .setXGravity(XGravity.RIGHT)
        .build().show();
  }

  private void showTouchInterceptPop(View view) {
    new BasePopupWindow.Builder(this)
        .setAnchorView(view)
        .setText("气泡在右")
        .setFocusable(true)
        .setYGravity(YGravity.ABOVE)
        .setXGravity(XGravity.CENTER)
        .build()
        .show();
  }

  private void showNoTouchInterceptPop(View view) {
    new BasePopupWindow.Builder(this)
        .setAnchorView(view)
        .setText("气泡在右")
        .setFocusable(false)
        .setYGravity(YGravity.ABOVE)
        .setXGravity(XGravity.CENTER)
        .build()
        .show();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_above:
        showAbovePop(v);
        break;
      case R.id.btn_below:
        showBelowPop(v);
        break;
      case R.id.btn_left:
        showLeftPop(v);
        break;
      case R.id.btn_right:
        showRightPop(v);
        break;
      case R.id.root:
        Toast.makeText(this, "点击root!", Toast.LENGTH_SHORT).show();
        break;
      case R.id.touch_intercept:
        showTouchInterceptPop(v);
        break;
      case R.id.no_touch_intercept:
        showNoTouchInterceptPop(v);
        break;
      case R.id.many_popup:
        Intent intent = new Intent(this, PopupOneByOneActivity.class);
        startActivity(intent);
        break;
      case R.id.many_popup_once_one:
        Intent intent2 = new Intent(this, PopupOnceShowOneActivity.class);
        startActivity(intent2);
      default:
        break;
    }
  }
}
