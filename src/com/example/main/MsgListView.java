package com.example.main;

import java.util.Date;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 
 * 自定义MsgListView，继承了ListView，
 * 但填充了listview的头部，即下拉刷新样式，并实现其功能
 * @author yanbo
 *
 */

public class MsgListView extends ListView implements OnScrollListener {

	//方便使用log
	 private final static String TAG = "abc";
	 private static boolean log = false;
	 
	 private final static int RELEASE_To_REFRESH = 0;
	 private final static int PULL_To_REFRESH = 1;
	 private final static int REFRESHING = 2;
	 private final static int DONE = 3;

		/**实际的padding的距离与界面上偏移距离的比例**/
		private final static int RATIO = 2;

	 private LayoutInflater inflater;

	 public LinearLayout headView; // 头部

	 private TextView tipsTextview;//下拉刷新
	 private TextView lastUpdatedTextView;//最新更新
	 private ImageView arrowImageView;//箭头
	 private ProgressBar progressBar;//刷新进度条

	 private RotateAnimation animation;//旋转特效 刷新中箭头翻转 向下变向上
	 private RotateAnimation reverseAnimation;

	 // 用于保证startY的值在一个完整的touch事件中只被记录一次
	 private boolean isRecored;

	 private int headContentWidth;//头部宽度
	 public int headContentHeight;//头部高度

	 private int startY;//高度起始位置，用来记录与头部距离
	 private int firstItemIndex;//列表中首行索引，用来记录其与头部距离

	 private int state;//下拉刷新中、松开刷新中、正在刷新中、完成刷新

	 private boolean isBack;

//	 public OnRefreshListener refreshListener;//刷新监听

	 
	 /*****************更多*****************/
	 private static final int START_PULL_DEVIATION = 50; // 移动误差
//	 private static final int WHAT_DID_MORE = 5; // Handler what 已经获取完更多
//	 private static final int WHAT_DID_REFRESH = 3; // Handler what 已经刷新完
	 /**底部更多的按键**/
	 private LinearLayout mFooterView;
	 /**底部更多的按键**/
	 public TextView mFooterTextView;
	 /**底部更多的按键**/
	 private ProgressBar mFooterLoadingView;
	 /**刷新和更多的事件接口**/
	 private OnPullDownListener mOnPullDownListener;

	 private float mMotionDownLastY; // 按下时候的Y轴坐标
	 private boolean mIsFetchMoreing; // 是否获取更多中
	 private boolean mIsPullUpDone; // 是否回推完成
	 @SuppressWarnings("unused")
	 private boolean mEnableAutoFetchMore; // 是否允许自动获取更多
	 /*****************更多*****************/

	 /** 刷新和获取更多事件接口*/
	 public interface OnPullDownListener {
		 /**刷新事件接口  这里要注意的是获取更多完 要关闭 刷新的进度条RefreshComplete()**/
		 public void onRefresh();
		 /**刷新事件接口  这里要注意的是获取更多完 要关闭 更多的进度条 notifyDidMore()**/
		 public void onMore();
	 }
	 
	 /**
		 * 设置监听器
		 * 
		 * @param listener
		 */
	 public void setOnPullDownListener(OnPullDownListener listener) {
		  this.mOnPullDownListener = listener;
		 }
	 
	 public void enableAutoFetchMore(boolean enable, int index) {
			if (enable) {
//				this.setBottomPosition(index);
				mFooterLoadingView.setVisibility(View.VISIBLE);
			} else {
				mFooterTextView.setText("点击获取更多");
				mFooterLoadingView.setVisibility(View.GONE);
			}
			mEnableAutoFetchMore = enable;
		}
	 
	 public MsgListView(Context context, AttributeSet attrs) {
	  super(context, attrs);
	  init(context);
	 }
	 
	 /**隐藏底部 禁用上拉更多**/
		public void setHideFooter() {
			mFooterView.setVisibility(View.GONE);
			mFooterTextView.setVisibility(View.GONE);
			mFooterLoadingView.setVisibility(View.GONE);
			enableAutoFetchMore(false, 1);
		}
		
		
		/**显示底部 使用上拉更多**/
		public void setShowFooter() {
			mFooterView.setVisibility(View.VISIBLE);
			mFooterTextView.setVisibility(View.VISIBLE);
			mFooterLoadingView.setVisibility(View.VISIBLE);
			enableAutoFetchMore(true, 1);
		}
		
//		private Handler mUIHandler = new Handler() {
//
//			@Override
//			public void handleMessage(Message msg) {
//				switch (msg.what) {
//				case WHAT_DID_REFRESH: {
////					mListView.onRefreshComplete();
//					return;
//				}
//
//				case WHAT_DID_MORE: {
//					mIsFetchMoreing = false;
//					mFooterTextView.setText("更多邮件");
//					mFooterLoadingView.setVisibility(View.GONE);
//				}
//				}
//			}
//
//		};

		synchronized private void init(Context context) {
		 
	  inflater = LayoutInflater.from(context);

	  headView = (LinearLayout) inflater.inflate(R.layout.head, null);//listview拼接headview
	  headView.setFocusable(false);


		mOnPullDownListener = new OnPullDownListener() {
			@Override
			synchronized public void onRefresh() {
			}

			@Override
			synchronized public void onMore() {
			}
		};
		
		 /*** 自定义脚部文件*/
			mFooterView = (LinearLayout)inflater.inflate(R.layout.pulldown_footer, null);
			mFooterTextView = (TextView) mFooterView.findViewById(R.id.pulldown_footer_text);
			mFooterLoadingView = (ProgressBar) mFooterView.findViewById(R.id.pulldown_footer_loading);
			mFooterView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!mIsFetchMoreing) {

						mIsFetchMoreing = true;
						mFooterTextView.setText("加载更多中...");
						mFooterLoadingView.setVisibility(View.VISIBLE);
						mOnPullDownListener.onMore();
					}
				}
			});
			
			addFooterView(mFooterView);
			 /*** 自定义脚部文件*/
	  
	  headView.setOnClickListener(new OnClickListener() {
		  
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			headView.setPadding(0, -1 * headContentHeight, 0, 0);
		}
	});

	  /***************定义头部****************/
	  arrowImageView = (ImageView) headView
	    .findViewById(R.id.head_arrowImageView);//headview中各view
	  arrowImageView.setMinimumWidth(75);
	  arrowImageView.setMinimumHeight(75);
	  progressBar = (ProgressBar) headView
	    .findViewById(R.id.head_progressBar);//headview中各view
	  tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);//headview中各view
	  lastUpdatedTextView = (TextView) headView
	    .findViewById(R.id.head_lastUpdatedTextView);//headview中各view

	  measureView(headView);
	  headContentHeight = headView.getMeasuredHeight();//头部高度
	  headContentWidth = headView.getMeasuredWidth();//头部宽度

	  headView.setPadding(0, -1 * headContentHeight, 0, 0);//setPadding(int left, int top, int right, int bottom) 
	  headView.invalidate();//Invalidate the whole view

	  if(log)Log.v("size", "width:" + headContentWidth + " height:"
	    + headContentHeight);

	  addHeaderView(headView);//添加进headview
	  setOnScrollListener(this);//滚动监听

	  animation = new RotateAnimation(0, -180,
			  RotateAnimation.RELATIVE_TO_SELF, 0.5f,
			  RotateAnimation.RELATIVE_TO_SELF, 0.5f);
	  animation.setInterpolator(new LinearInterpolator());
	  animation.setDuration(250*2);
	  animation.setFillAfter(true);//特效animation设置

	  reverseAnimation = new RotateAnimation(-180, 0,
			  RotateAnimation.RELATIVE_TO_SELF, 0.5f,
			  RotateAnimation.RELATIVE_TO_SELF, 0.5f);
	  reverseAnimation.setInterpolator(new LinearInterpolator());
	  reverseAnimation.setDuration(250*2);
	  reverseAnimation.setFillAfter(true);//特效reverseAnimation设置
	  /***************定义头部****************/
	 }

//		 @Override
	public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2,//滚动事件
	   int arg3) {
	  firstItemIndex = firstVisiableItem;//得到首item索引
	 }

//	 @Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		 switch (arg1) {           
		 // 当不滚动时           
		 case OnScrollListener.SCROLL_STATE_IDLE:                
			 // 判断滚动到底部                
			 if (arg0.getLastVisiblePosition() == (arg0.getCount() - 1)&& mMotionDownLastY-startY < 0 && isFillScreenItem()) {    
				 //更多
					mIsFetchMoreing = true;
					mFooterTextView.setText("加载更多中...");
					mFooterLoadingView.setVisibility(View.VISIBLE);
					mOnPullDownListener.onMore();      
//				 isLastisNext++;              
				 }              
			 break;
		 }
	 }

//	 @Override
//	public boolean onInterceptTouchEvent(MotionEvent event) {
//		// TODO Auto-generated method stub
//		return true;
//	}
	 /***********************头部动画******************************/
//	 @Override
	 synchronized public boolean onTouchEvent(MotionEvent event) {//触摸事件
		 

	  switch (event.getAction()) {
	  case MotionEvent.ACTION_DOWN://手按下  对应下拉刷新状态
	   if (firstItemIndex == 0 && !isRecored) {//如果首item索引为0，且尚未记录startY,则在下拉时记录之，并执行isRecored = true;
	    startY = (int) event.getY();
	    isRecored = true;

	    if(log)Log.v(TAG, "在down时候记录当前位置‘");
	   }

		 mIsPullUpDone = false;
	   break;

	  case MotionEvent.ACTION_UP://手松开  对应松开刷新状态

	   if (state != REFRESHING) {//手松开有4个状态：下拉刷新、松开刷新、正在刷新、完成刷新。如果当前不是正在刷新
	    if (state == DONE) {//如果当前是完成刷新，什么都不做
	    }
	    if (state == PULL_To_REFRESH) {//如果当前是下拉刷新，状态设为完成刷新（意即下拉刷新中就松开了，实际未完成刷新），执行changeHeaderViewByState()
	     state = DONE;
	     changeHeaderViewByState();

	     if(log)Log.v(TAG, "由下拉刷新状态，到done状态");
	    }
	    if (state == RELEASE_To_REFRESH) {//如果当前是松开刷新，状态设为正在刷新（意即松开刷新中松开手，才是真正地刷新），执行changeHeaderViewByState()
	     state = REFRESHING;
	     changeHeaderViewByState();
	     onRefresh();//真正刷新，所以执行onrefresh，执行后状态设为完成刷新

	     if(log)Log.v(TAG, "由松开刷新状态，到done状态");
	    }
	   }

	   isRecored = false;//手松开，则无论怎样，可以重新记录startY,因为只要手松开就认为一次刷新已完成
	   isBack = false;

		 mMotionDownLastY = event.getRawY();
//		if (mMotionDownLastY-event.getRawY()>500) {
//			return true;
//		}
	   break;

	  case MotionEvent.ACTION_MOVE://手拖动，拖动过程中不断地实时记录当前位置
	   int tempY = (int) event.getY();
	   if (!isRecored && firstItemIndex == 0) {//如果首item索引为0，且尚未记录startY,则在拖动时记录之，并执行isRecored = true;
	    if(log)Log.v(TAG, "在move时候记录下位置");
	    isRecored = true;
	    startY = tempY;
	   }
	   if (state != REFRESHING && isRecored) {//如果状态不是正在刷新，且已记录startY：tempY为拖动过程中一直在变的高度，startY为拖动起始高度
	    // 可以松手去刷新了
	    if (state == RELEASE_To_REFRESH) {//如果状态是松开刷新
	     // 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
	     if (((tempY - startY) / RATIO  < headContentHeight)//如果实时高度大于起始高度，且两者之差小于头部高度，则状态设为下拉刷新
	       && (tempY - startY) > 0) {
	      state = PULL_To_REFRESH;
	      changeHeaderViewByState();

	      if(log)Log.v(TAG, "由松开刷新状态转变到下拉刷新状态");
	     }
	     // 一下子推到顶了
	     else if (tempY - startY <= 0) {//如果实时高度小于等于起始高度了，则说明到顶了，状态设为完成刷新
	      state = DONE;
	      changeHeaderViewByState();

	      if(log)Log.v(TAG, "由松开刷新状态转变到done状态");
	     }
	     // 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
	     else {//如果当前拖动过程中既没有到下拉刷新的地步，也没有到完成刷新（到顶）的地步，则保持松开刷新状态
	      // 不用进行特别的操作，只用更新paddingTop的值就行了
	     }
	    }
	    // 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
	    if (state == PULL_To_REFRESH) {//如果状态是下拉刷新
	     // 下拉到可以进入RELEASE_TO_REFRESH的状态
	     if ((tempY - startY) / RATIO >= headContentHeight) {//如果实时高度与起始高度之差大于等于头部高度，则状态设为松开刷新
	      state = RELEASE_To_REFRESH;
	      isBack = true;
	      changeHeaderViewByState();

	      if(log)Log.v(TAG, "由done或者下拉刷新状态转变到松开刷新");
	     }
	     // 上推到顶了
	     else if (tempY - startY <= 0) {//如果实时高度小于等于起始高度了，则说明到顶了，状态设为完成刷新
	      state = DONE;
	      changeHeaderViewByState();

	      if(log)Log.v(TAG, "由DOne或者下拉刷新状态转变到done状态");
	     }
	    }

	    // done状态下
	    if (state == DONE) {//如果状态是完成刷新
	     if (tempY - startY > 0) {//如果实时高度大于起始高度了，则状态设为下拉刷新
	      state = PULL_To_REFRESH;
	      changeHeaderViewByState();
	     }
	    }

	    // 更新headView的size
	    if (state == PULL_To_REFRESH) {//如果状态是下拉刷新，更新headview的size           ?
	     headView.setPadding(0, -1 * headContentHeight
	       + (tempY - startY) / RATIO, 0, 0);
	     headView.invalidate();
	    }

	    // 更新headView的paddingTop
	    if (state == RELEASE_To_REFRESH) {//如果状态是松开刷新，更新 headview的paddingtop      ?
	     headView.setPadding(0, (tempY - startY) / RATIO - headContentHeight,
	       0, 0);
	     headView.invalidate();
	    }
	   }
	   if (mIsPullUpDone)
			return true;

		// 如果开始按下到滑动距离不超过误差值，则不滑动
		final int absMotionY = (int) Math.abs(event.getRawY() - mMotionDownLastY);
		if (absMotionY < START_PULL_DEVIATION)
			return true;

//		return false;
	   break;
	  }
	  return super.onTouchEvent(event);
	 }
	 
	 // 当状态改变时候，调用该方法，以更新界面
	 private void changeHeaderViewByState() {
	  switch (state) {
	  case RELEASE_To_REFRESH:
	   arrowImageView.setVisibility(View.VISIBLE);
	   progressBar.setVisibility(View.GONE);
	   tipsTextview.setVisibility(View.VISIBLE);
	   lastUpdatedTextView.setVisibility(View.VISIBLE);

	   arrowImageView.clearAnimation();
	   arrowImageView.startAnimation(animation);

	   tipsTextview.setText("松开刷新");

	   if(log)Log.v(TAG, "当前状态，松开刷新");
	   break;
	  case PULL_To_REFRESH:
	   progressBar.setVisibility(View.GONE);
	   tipsTextview.setVisibility(View.VISIBLE);
	   lastUpdatedTextView.setVisibility(View.VISIBLE);
	   arrowImageView.clearAnimation();
	   arrowImageView.setVisibility(View.VISIBLE);
	   // 是由RELEASE_To_REFRESH状态转变来的
	   if (isBack) {
	    isBack = false;
	    arrowImageView.clearAnimation();
	    arrowImageView.startAnimation(reverseAnimation);

	    tipsTextview.setText("下拉刷新");
	   } else {
	    tipsTextview.setText("下拉刷新");
	   }
	   if(log)Log.v(TAG, "当前状态，下拉刷新");
	   break;

	  case REFRESHING:

	   headView.setPadding(0, 0, 0, 0);
	   headView.invalidate();

	   progressBar.setVisibility(View.VISIBLE);
	   arrowImageView.clearAnimation();
	   arrowImageView.setVisibility(View.GONE);
	   tipsTextview.setText("正在刷新...");
	   lastUpdatedTextView.setVisibility(View.VISIBLE);

	   if(log)Log.v(TAG, "当前状态,正在刷新...");
	   break;
	  case DONE:
	   headView.setPadding(0, -1 * headContentHeight, 0, 0);
	   headView.invalidate();

	   progressBar.setVisibility(View.GONE);
	   arrowImageView.clearAnimation();
	   arrowImageView
	     .setImageResource(R.drawable.ic_pulltorefresh_arrow);
	   tipsTextview.setText("下拉刷新");
	   lastUpdatedTextView.setVisibility(View.VISIBLE);

	   if(log)Log.v(TAG, "当前状态，done");
	   break;
	  }
	 }
	 /***********************头部动画******************************/

//	 public void setonRefreshListener(OnRefreshListener onRefreshListener) {
//	  this.onRefreshListener = onRefreshListener;
//	 }

//	 public boolean onMotionDown(MotionEvent ev) {
//			mIsPullUpDone = false;
//			mMotionDownLastY = ev.getRawY();
//
//			return false;
//		}

//		public boolean onMotionMove(MotionEvent ev, int delta) {
//			// 当头部文件回推消失的时候，不允许滚动
//			if (mIsPullUpDone)
//				return true;
//
//			// 如果开始按下到滑动距离不超过误差值，则不滑动
//			final int absMotionY = (int) Math.abs(ev.getRawY() - mMotionDownLastY);
//			if (absMotionY > START_PULL_DEVIATION)
//				return true;
//
//			return false;
//		}

//		public boolean onMotionUp(MotionEvent ev) {
////			if (ScrollOverListView.canRefleash) {
////				ScrollOverListView.canRefleash = false;
////				mOnPullDownListener.onRefresh();
////			}
//			return false;
//		}
		
		
		
//	 public interface OnRefreshListener {
//	  public void onRefresh();
//	 }

	 @SuppressWarnings("deprecation")
	public void onRefreshComplete() {
	  state = DONE;
	  lastUpdatedTextView.setText("最近更新:" + new Date().toLocaleString());//刷新完成时，头部提醒的刷新日期
	  changeHeaderViewByState();
	 }
	 
	 public void onMoreComplete() {
			mIsFetchMoreing = false;
			mFooterTextView.setText("点击获取更多");
			mFooterLoadingView.setVisibility(View.GONE);
		 }

	 private void onRefresh() {
	  if (mOnPullDownListener != null) {
		  mOnPullDownListener.onRefresh();
	  }
	 }
	 
	 @SuppressWarnings("unused")
	private void onMore() {
//		  if (mOnPullDownListener != null) {
//			  mOnPullDownListener.onMore();
//		  }

			if (!mIsFetchMoreing) {
				mIsFetchMoreing = true;
				mFooterTextView.setText("加载更多中...");
				mFooterLoadingView.setVisibility(View.VISIBLE);
				mOnPullDownListener.onMore();
			}
		 }
	 
	 public boolean onListViewBottomAndPullUp(int delta) {
//			if (!mEnableAutoFetchMore || mIsFetchMoreing)
//				return false;
			// 数量充满屏幕才触发
//			if (isFillScreenItem()) {
//				mIsFetchMoreing = true;
//				mFooterTextView.setText("加载更多中...");
//				mFooterLoadingView.setVisibility(View.VISIBLE);
//				mOnPullDownListener.onMore();
//				return true;
//			}
			return false;
		}
	 
	 private boolean isFillScreenItem() {
			final int firstVisiblePosition = this.getFirstVisiblePosition();
			final int lastVisiblePostion = this.getLastVisiblePosition()
					- this.getFooterViewsCount();
			final int visibleItemCount = lastVisiblePostion - firstVisiblePosition
					+ 1;
			final int totalItemCount = this.getCount()
					- this.getFooterViewsCount();

			if (visibleItemCount < totalItemCount)
				return true;
			return false;
		}

	 //此方法直接照搬自网络上的一个下拉刷新的demo，此处是“估计”headView的width以及height
	 @SuppressWarnings("deprecation")
	private void measureView(View child) {
	  ViewGroup.LayoutParams p = child.getLayoutParams();
	  if (p == null) {
	   p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
	     ViewGroup.LayoutParams.WRAP_CONTENT);
	  }
	  int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
	  int lpHeight = p.height;
	  int childHeightSpec;
	  if (lpHeight > 0) {
	   childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
	     MeasureSpec.EXACTLY);
	  } else {
	   childHeightSpec = MeasureSpec.makeMeasureSpec(0,
	     MeasureSpec.UNSPECIFIED);
	  }
	  child.measure(childWidthSpec, childHeightSpec);
	 }
	}