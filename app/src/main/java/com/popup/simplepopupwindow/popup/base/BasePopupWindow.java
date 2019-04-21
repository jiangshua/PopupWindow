package com.popup.simplepopupwindow.popup.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.support.v4.widget.PopupWindowCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.popup.simplepopupwindow.R;
import com.popup.simplepopupwindow.utils.ViewUtil;

/**
 * <p>
 * Base class for PopupWindow.
 * this lifecycle is : show , dismiss
 *
 * support :
 * 1、可自定义xml
 * see {@link BasePopupWindow.Builder#setPopupView(int layoutId)}
 * see {@link BasePopupWindow.Builder#setPopupView(View contentView)}
 * 如果未设置xml布局，走默认布局
 * see {@link PopupWindowHelper#popupDefaultLayout(BasePopupWindow kwaiPopupWindow)}
 * 但注意要设置color和orientation
 *
 * 2、可设置优先级
 * (1): 对于二级气泡页面每个生命周期只会显示一个
 * 设置see {@link BasePopupWindow.Builder#setPopupPriority(int priority)}
 * 实现see {@link SimplePopupWindowQueue#removeQueueHeaderPopupWindow()}
 * (2): 对气泡做优先级排序
 * 设置see {@link BasePopupWindow.Builder#setOrderAtTime(long orderTimeMillis)}
 * 实现see {@link BasePopupWindow#compareTo(BasePopupWindow popupWindow)}
 *
 * 3、可控制气泡消失是否消费点击事件
 * see {@link BasePopupWindow.Builder#setFocusable(boolean focusable)}
 * focusable为false时，气泡消失不消费点击事件
 *
 * 4、可一键设置气泡各种样式，如：Gravity、Color、OffsetX、OffsetY
 * see {@link BasePopupWindow.Builder}
 * 其中Gravity中ALIGN_LEFT表示popupWindow与AnchorView左对齐，同理其它
 * </p>
 */

public class BasePopupWindow implements PopupLifeCycle, Comparable<BasePopupWindow> {

  private static final String TAG = "KwaiPopupWindow";
  private Context mContext;
  private int mWidth;
  private int mHeight;
  private View mAnchorView;
  private int mOffsetX;
  private int mOffsetY;

  /** PopupWindow对象 **/
  private PopupWindow mPopupWindow;

  /** popupWindow ContentView **/
  private View mPopupContentLayout;

  /** popupWindow ContentView layoutId **/
  private int mLayoutId;

  /** outside of popupWindow 点击事件是否有响应 **/
  private boolean mOutsideTouchable;

  /** the popupWindow content text **/
  private String mText;

  /** the popupWindow animation **/
  private int mAnimationStyle;

  /** the popupWindow show listener **/
  private BasePopupWindow.OnShowListener mOnShowListener;

  /** 是否可以点击PopupWindow之外的地方dismiss **/
  private boolean mFocusAndOutsideEnable;

  /** the popup show time **/
  private long mShowDuration;

  /**
   * popupWindow focusable,the default value is true
   * 当popup window为true时，其dismiss会消费一次点击事件
   **/
  private boolean mFocusable;

  /** 控制popupWindow执行优先级，默认值为当前时间 **/
  private Long mOrderTimeMillis;

  @PopupColor
  private int mColorType;
  @PopupPriority
  private int mPopupPriority;

  /** the gravity of popupWindow */
  @YGravity
  private int mYGravity;
  @XGravity
  private int mXGravity;

  private BasePopupWindow(Builder builder) {
    this.mContext = builder.mContext;
    this.mWidth = builder.mWidth;
    this.mHeight = builder.mHeight;
    this.mAnchorView = builder.mAnchorView;
    this.mOffsetX = builder.mOffsetX;
    this.mOffsetY = builder.mOffsetY;
    this.mPopupContentLayout = builder.mPopupContentLayout;
    this.mLayoutId = builder.mLayoutId;
    this.mOutsideTouchable = builder.mOutsideTouchable;
    this.mText = builder.mText;
    this.mFocusable = builder.mFocusable;
    this.mAnimationStyle = builder.mAnimationStyle;
    this.mOnShowListener = builder.mOnShowListener;
    this.mFocusAndOutsideEnable = builder.mFocusAndOutsideEnable;
    this.mShowDuration = builder.mShowDuration;
    this.mYGravity = builder.mYGravity;
    this.mXGravity = builder.mXGravity;
    this.mColorType = builder.mColorType;
    this.mPopupPriority = builder.mPopupPriority;
    this.mOrderTimeMillis = builder.mOrderTimeMillis;
  }


  @Override
  public void show() {
    if (mPopupWindow == null) {
      mPopupWindow = new PopupWindow();
    }
    initPopupWindow();
    registerOnGlobalLayoutListener();
    showAtAnchorView();
  }

  @Override
  public void dismiss() {
    if (mPopupWindow != null && mPopupWindow.isShowing()) {
      mPopupWindow.dismiss();
    }
  }

  protected void initPopupWindow() {
    if (mAnchorView == null)
      throw new IllegalArgumentException("anchorView must not be null");
    initPopupContentView();
    initContentViewMeasure();
    initFocusAndBack();
    initTextView();
    if (mAnimationStyle != 0) {
      mPopupWindow.setAnimationStyle(mAnimationStyle);
    }
    if (mShowDuration > 0) {
      new Handler().postDelayed(() -> dismiss(), mShowDuration);
    }
  }

  /**
   * create popup content view
   */
  private void initPopupContentView() {
    if (mPopupContentLayout == null) {
      int layoutId =
          mLayoutId == 0 ? PopupWindowHelper.popupDefaultLayout(this) : mLayoutId;
      if (layoutId != 0 && mContext != null) {
        mPopupWindow.setContentView(LayoutInflater.from(mContext).inflate(layoutId, null));
      } else {
        throw new IllegalArgumentException(
            "The content view is null,the layoutId=" + mLayoutId +
                ",context=" + mContext +
                ",popupContentLayout=" + mPopupContentLayout);
      }
    } else {
      mPopupWindow.setContentView(mPopupContentLayout);
    }
  }

  /**
   * init popup text view
   */
  private void initTextView() {
    TextView textView = findViewById(R.id.popup_text);
    if (textView != null && mText != null) {
      textView.setText(mText);
    }
  }

  /**
   * init popup window content view width and height
   */
  private void initContentViewMeasure() {
    if (mWidth > 0
        || mWidth == ViewGroup.LayoutParams.WRAP_CONTENT
        || mWidth == ViewGroup.LayoutParams.MATCH_PARENT) {
      mPopupWindow.setWidth(mWidth);
    } else {
      mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    if (mHeight > 0
        || mHeight == ViewGroup.LayoutParams.WRAP_CONTENT
        || mHeight == ViewGroup.LayoutParams.MATCH_PARENT) {
      mPopupWindow.setHeight(mHeight);
    } else {
      mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
    }
  }

  /**
   * popupWindow 焦点和返回按钮初始化，以及解决些兼容性问题
   */
  private void initFocusAndBack() {
    if (!mFocusAndOutsideEnable) {
      mPopupWindow.setFocusable(true);
      mPopupWindow.setOutsideTouchable(false);
      mPopupWindow.setBackgroundDrawable(null);
      // 注意下面这三个是contentView 不是PopupWindow，响应返回按钮事件
      mPopupWindow.getContentView().setFocusable(true);
      mPopupWindow.getContentView().setFocusableInTouchMode(true);
      mPopupWindow.getContentView().setOnKeyListener((v, keyCode, event) -> {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
          mPopupWindow.dismiss();
          return true;
        }
        return false;
      });
      // 在Android 6.0以上 ，只能通过拦截事件来解决
      mPopupWindow.setTouchInterceptor((v, event) -> {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        if ((event.getAction() == MotionEvent.ACTION_DOWN)
            && ((x < 0) || (x >= mWidth) || (y < 0) || (y >= mHeight))) {
          // outside
          Log.d(TAG, "onTouch outside:mWidth=" + mWidth + ",mHeight=" + mHeight);
          return true;
        } else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
          // outside
          Log.d(TAG, "onTouch outside event:mWidth=" + mWidth + ",mHeight=" + mHeight);
          return true;
        }
        return false;
      });
    } else {
      mPopupWindow.setFocusable(mFocusable);
      mPopupWindow.setOutsideTouchable(mOutsideTouchable);
      mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
  }

  /**
   * 更新 PopupWindow 到精准的位置
   */
  private void updatePopupLocation(int width, int height, @NonNull View anchor,
      @YGravity final int yGravity, @XGravity int xGravity, int x, int y) {
    if (mPopupWindow == null) {
      return;
    }
    x = PopupWindowHelper.calculateX(anchor, xGravity, width, x);
    y = PopupWindowHelper.calculateY(anchor, yGravity, height, y);
    mPopupWindow.update(anchor, x, y, width, height);
  }

  /**
   * the PopupWindow to show
   * the content view in a popup window anchored
   */
  protected void showAtAnchorView() {
    showAtAnchorView(mAnchorView, mYGravity, mXGravity, mOffsetX, mOffsetY);
  }

  protected void showAtAnchorView(@NonNull View anchor, @YGravity final int vertGravity,
      @XGravity int horizGravity, int x, int y) {
    if (mOnShowListener != null) {
      mOnShowListener.onShow();
    }
    mAnchorView = anchor;
    mOffsetX = x;
    mOffsetY = y;
    mYGravity = vertGravity;
    mXGravity = horizGravity;
    // 处理背景变暗
    x = PopupWindowHelper.calculateX(anchor, horizGravity, mWidth, mOffsetX);
    y = PopupWindowHelper.calculateY(anchor, vertGravity, mHeight, mOffsetY);
    PopupWindowCompat.showAsDropDown(mPopupWindow, anchor, x, y, Gravity.NO_GRAVITY);
  }

  /**
   * 是否正在显示
   *
   */
  public boolean isShowing() {
    return mPopupWindow != null && mPopupWindow.isShowing();
  }

  /**
   * 获取view
   *
   */
  public <T extends View> T findViewById(@IdRes int viewId) {
    View view = null;
    if (getContentView() != null) {
      view = getContentView().findViewById(viewId);
    }
    return (T) view;
  }

  /**
   * 注册GlobalLayoutListener 获取精准的宽高
   */
  private void registerOnGlobalLayoutListener() {
    getContentView().getViewTreeObserver()
        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
          @Override
          public void onGlobalLayout() {
            getContentView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
            mWidth = getContentView().getWidth();
            mHeight = getContentView().getHeight();
            if (isShowing()) {
              updatePopupLocation(mWidth, mHeight, mAnchorView, mYGravity, mXGravity,
                  mOffsetX, mOffsetY);
              updateArrowLocation();
            }
          }
        });
  }

  /**
   * 更新popupWindow三角符号位置
   */
  public void updateArrowLocation() {
    View arrow = findViewById(R.id.popup_arrow);
    View contentView = getContentView();
    if (arrow != null && contentView != null) {
      switch (mXGravity) {
        case XGravity.ALIGN_RIGHT:
          arrow.post(() -> arrow.setTranslationX((contentView.getMeasuredWidth() / 2
              - mAnchorView.getMeasuredWidth() / 2 - mOffsetX)));
          return;
        case XGravity.ALIGN_LEFT:
          arrow.post(() -> arrow.setTranslationX(-(contentView.getMeasuredWidth() / 2
              - mAnchorView.getMeasuredWidth() / 2 + mOffsetX)));
          return;
      }
    }
    arrow.setTranslationX(-mOffsetX);
    arrow.setTranslationY(- mOffsetY);
    //目前对左右两侧的气泡 三角箭头暂时不做居中处理
    if (mXGravity != XGravity.LEFT && mXGravity != XGravity.RIGHT) {
      arrowCloseToLeftRight(arrow, contentView);
    }
  }

  private void arrowCloseToLeftRight(View arrow, View contentView) {
    if (arrow != null && contentView != null) {

      int[] anchorScreenLocation = new int[2];
      mAnchorView.getLocationOnScreen(anchorScreenLocation);
      int anchorOffsetX = anchorScreenLocation[0];

      int[] arrowScreenLocation = new int[2];
      arrow.getLocationOnScreen(arrowScreenLocation);
      int arrowOffsetX = arrowScreenLocation[0];

      int halfContentWidth = contentView.getMeasuredWidth() / 2;
      int anchorLocationX = anchorOffsetX + mAnchorView.getMeasuredWidth() / 2;
      int screenWidth = ViewUtil.getDisplayWidth((Activity) arrow.getContext());

      if (halfContentWidth > anchorLocationX) { // 左侧挤压
        arrow.setTranslationX(-(halfContentWidth - anchorLocationX));
      } else if (screenWidth - anchorLocationX < halfContentWidth) { // 右侧挤压
        arrow.setTranslationX(anchorLocationX - (arrowOffsetX + arrow.getMeasuredWidth() / 2));
      }
    }
  }

  @Override
  public int compareTo(BasePopupWindow popupWindow) {
    return this.mOrderTimeMillis.compareTo(popupWindow.getUptimeMillis());
  }

  public interface OnShowListener {
    void onShow();
  }

  public View getAnchorView() {
    return mAnchorView;
  }

  public int getPopupColorType() {
    return mColorType;
  }

  public PopupWindow getPopupWindow() {
    return mPopupWindow;
  }

  public long getUptimeMillis() {
    return mOrderTimeMillis;
  }

  /**
   * 获取PopupWindow中加载的view
   *
   */
  @Nullable
  public View getContentView() {
    if (mPopupWindow != null) {
      return mPopupWindow.getContentView();
    }
    return null;
  }

  public String getText() {
    return mText;
  }

  public int getPopupPriority() {
    return mPopupPriority;
  }

  /**
   * 获取YGravity
   */
  public int getYGravity() {
    return mYGravity;
  }

  /**
   * 获取XGravity
   */
  public int getXGravity() {
    return mXGravity;
  }

  public static class Builder {
    private Context mContext;
    private int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
    private View mAnchorView;
    private int mOffsetX;
    private int mOffsetY;
    private View mPopupContentLayout;
    private int mLayoutId;
    private boolean mOutsideTouchable = true;
    private String mText;
    private int mAnimationStyle;
    private BasePopupWindow.OnShowListener mOnShowListener;
    private boolean mFocusAndOutsideEnable = true;
    private long mShowDuration;
    private boolean mFocusable = true;
    private long mOrderTimeMillis = SystemClock.uptimeMillis();
    @YGravity
    private int mYGravity = YGravity.CENTER;
    @XGravity
    private int mXGravity = XGravity.CENTER;
    @PopupColor
    private int mColorType = PopupColor.ORANGE;
    @PopupPriority
    private int mPopupPriority = PopupPriority.FIRST_PRIORITY;

    public Builder(Context context) {
      this.mContext = context;
    }

    public Builder setShowDuration(long duration) {
      this.mShowDuration = duration;
      return this;
    }

    public Builder setPopupView(View contentView) {
      this.mPopupContentLayout = contentView;
      this.mLayoutId = 0;
      return this;
    }

    public Builder setPopupView(@LayoutRes int layoutId) {
      this.mPopupContentLayout = null;
      this.mLayoutId = layoutId;
      return this;
    }

    public Builder setText(String text) {
      this.mText = text;
      return this;
    }

    public Builder setOrderAtTime(long orderTimeMillis) {
      this.mOrderTimeMillis = orderTimeMillis;
      return this;
    }

    public Builder setPopupPriority(@PopupPriority int priority) {
      this.mPopupPriority = priority;
      return this;
    }

    public Builder setAnchorView(View view) {
      this.mAnchorView = view;
      return this;
    }

    public Builder setColorType(@PopupColor int colorType) {
      this.mColorType = colorType;
      return this;
    }

    public Builder setOffsetX(int offsetX) {
      this.mOffsetX = offsetX;
      return this;
    }

    public Builder setXGravity(@XGravity int xGravity) {
      this.mXGravity = xGravity;
      return this;
    }

    public Builder setOffsetY(int offsetY) {
      this.mOffsetY = offsetY;
      return this;
    }

    public Builder setYGravity(@YGravity int yGravity) {
      this.mYGravity = yGravity;
      return this;
    }

    public Builder setAnimationStyle(@StyleRes int animationStyle) {
      this.mAnimationStyle = animationStyle;
      return this;
    }

    public Builder setFocusable(boolean focusable) {
      this.mFocusable = focusable;
      return this;
    }

    public Builder setOnShowListener(BasePopupWindow.OnShowListener listener) {
      this.mOnShowListener = listener;
      return this;
    }

    public SimplePopupWindowProxy build() {
      return new SimplePopupWindowProxy(new BasePopupWindow(this));
    }
  }
}
