# popup
PopupWindow

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
 
 ![示例图片](https://img-blog.csdnimg.cn/20190421180921471.jpeg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2oxODg3NDk2NDAyOHNzcw==,size_16,color_FFFFFF,t_70)
