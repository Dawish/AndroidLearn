package danxx.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import danxx.library.R;

/**
 * 
 * 下拉刷新控件
 * -------------------------------------------------------
 * 
 * @Description: NLPullRefreshView
 * @Author: danxingxi
 * @CreateDate: 2016-8-3
 * @version 1.0.0
 * 
 */
public class DXPullRefreshView extends LinearLayout {
	private static final String TAG = "DXPullRefreshView";

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	enum Status {
		NORMAL,     // 正常无操作
		DRAGGING,   // 拖动中
		REFRESHING, // 刷新中
	}

	private Status status = Status.NORMAL;

	private final static String REFRESH_RELEASE_TEXT = "释放后执行刷新";
	private final static String REFRESH_DOWN_TEXT = "下拉可准备执行刷新";
	/****/
	private final static float MIN_MOVE_DISTANCE = 8.0f;// 最小移动距离，用于判断是否在下拉，设置为0则touch事件的判断会过于频繁。具体值可以根据自己来设定

	private Scroller scroller;
	private View refreshView;
	private ImageView refreshIndicatorView;
	private int refreshTargetTop = -68;
	private ProgressBar bar;
	private TextView downTextView;
	private TextView timeTextView;

	private RefreshListener refreshListener;// 刷新监听器

	// 需要用到的文字引用
	private String downCanRefreshText;
	private String releaseCanRefreshText;

	private String refreshTime ;
	private int lastY;
	private Context mContext;

	public DXPullRefreshView(Context context) {
		super(context);
		mContext = context;
	}

	public DXPullRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	/**
	 * 初始化
	 * 
	 * @MethodDescription init
	 * @exception
	 * @since 1.0.0
	 */
	private void init() {
		// TODO Auto-generated method stub
		refreshTargetTop = getPixelByDip(mContext, refreshTargetTop);
		// 滑动对象，
		scroller = new Scroller(mContext);
		// 刷新视图顶端的的view
		refreshView = LayoutInflater.from(mContext).inflate(
				R.layout.refresh_top_item, null);
		// 指示器view
		refreshIndicatorView = (ImageView) refreshView
				.findViewById(R.id.indicator);
		// 刷新bar
		bar = (ProgressBar) refreshView.findViewById(R.id.progress);
		// 下拉显示text
		downTextView = (TextView) refreshView.findViewById(R.id.refresh_hint);
		// 下来显示时间
		timeTextView = (TextView) refreshView.findViewById(R.id.refresh_time);
		LayoutParams lp = new LayoutParams(
				LayoutParams.MATCH_PARENT, -refreshTargetTop);
		lp.topMargin = refreshTargetTop;
		lp.gravity = Gravity.CENTER;
		addView(refreshView, lp);
		// //文字资源可以归档在资源集中，此处为了方便。
		downCanRefreshText = REFRESH_DOWN_TEXT;
		releaseCanRefreshText = REFRESH_RELEASE_TEXT;
		refreshTime = "2016-12-24 12:12:12";//可以从保存文件中取得上次的更新时间
		if (refreshTime != null) {
			setRefreshTime(refreshTime);
		}
	}

	/**
	 * 设置刷新后的内容
	 * 
	 * @MethodDescription setRefreshText
	 * @param time
	 * @exception
	 * @since 1.0.0
	 */
	private void setRefreshText(String time) {
		Log.i(TAG, "------>setRefreshText");
		timeTextView.setText(time);
	}

	/**
	 * 事件分发拦截
	 * onInterceptTouchEvent()用于处理事件并改变事件的传递方向。
	 * 返回值为false时事件会传递给子控件的onInterceptTouchEvent()；
	 * 返回值为true时事件会传递给当前控件的onTouchEvent()，而不在传递给子控件，这就是所谓的Intercept(截断)。
	 * @param e
	 * @return
     */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		// layout截取touch事件
		int action = e.getAction();
		int y = (int) e.getRawY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			lastY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			// y移动坐标
			int m = y - lastY;
			// 记录下此刻y坐标
			this.lastY = y;
			/**如果下拉距离足够并且 当前 情况 可以下拉就返回true，这样事件会传递给当前控件的onTouchEvent()**/
			if (m > MIN_MOVE_DISTANCE && canScroll()) {
				Log.i(TAG, "canScroll");
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		}
		return false;
	}

	/**
	 * 事件分发
	 * onTouchEvent() 用于处理事件，返回值决定当前控件是否消费（consume）了这个事件
	 * @param event
	 * @return
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (status == Status.REFRESHING)
		/**如果正在刷新就不响应**/
			return false;
		/**以屏幕左上角为坐标原点计算的Y轴坐标**/
		int y = (int) event.getRawY();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				Log.i(TAG, "MotionEvent.ACTION_DOWN");
				// 手指按下时记录下y坐标
				lastY = y;
				break;
			case MotionEvent.ACTION_MOVE:
				Log.i(TAG, "MotionEvent.ACTION_MOVE");
				// 因为是向下滑动所以是减，要是向上滑动就是加了！
				// 负负得正
				// 手指向下滑动时 y坐标 = 屏幕左上角为坐标原点计算的Y轴坐标 - 手指滑动的Y轴坐标
				Log.d("danxx" ,"y-->"+y);
				Log.d("danxx" ,"lastY-->"+lastY);
				int m = y - lastY;
				Log.d("danxx" ,"m-->"+m);
				doMovement(m);
				// 记录下此刻y坐标
				this.lastY = y;
				break;
			case MotionEvent.ACTION_UP:
				Log.i(TAG, "MotionEvent.ACTION_UP");
				fling();
				break;
		}
		return true;
	}

	/**
	 * up事件处理
	 */
	private void fling() {
		// TODO Auto-generated method stub
		LayoutParams lp = (LayoutParams) refreshView
				.getLayoutParams();

		if (lp.topMargin > 0) {// 拉到了触发可刷新事件
			status = Status.REFRESHING;
			Log.i(TAG, "fling ----->REFRESHING");
			refresh();
		} else {
			Log.i(TAG, "fling ----->NORMAL");
			status = Status.NORMAL;
			returnInitState();
		}
	}

	private void returnInitState() {
		// TODO Auto-generated method stub
		LayoutParams lp = (LayoutParams) this.refreshView
				.getLayoutParams();
		int i = lp.topMargin;
		Log.i(TAG, "returnInitState top = "+i);
		scroller.startScroll(0, i, 0, refreshTargetTop);
		invalidate();
	}

	/**
	 * 执行刷新
	 * 
	 * @MethodDescription refresh
	 * @exception
	 * @since 1.0.0
	 */
	private void refresh() {
		// TODO Auto-generated method stub
		Log.i(TAG, " ---> refresh()");
		LayoutParams lp = (LayoutParams) this.refreshView
				.getLayoutParams();
		int i = lp.topMargin;
		refreshIndicatorView.setVisibility(View.GONE);
		bar.setVisibility(View.VISIBLE);
		downTextView.setVisibility(View.GONE);
		scroller.startScroll(0, i, 0, 0 - i);
		invalidate();
		if (refreshListener != null) {
			refreshListener.onRefresh(this);
		}
	}

	/**
	 * 
	 */
	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {//scroll 动作还未结束
			Log.i(TAG, "----->computeScroll()");
			int i = this.scroller.getCurrY();
			LayoutParams lp = (LayoutParams) this.refreshView
					.getLayoutParams();
			int k = Math.max(i, refreshTargetTop);
			lp.topMargin = k;
			this.refreshView.setLayoutParams(lp);
			postInvalidate();
		}
	}

	/**
	 * 下拉move事件处理
	 * 边下拉边修改刷新视图的topMargin值，达到刷新视图在滚动的感觉
	 * @param moveY 向下手指滑动的距离
	 */
	private void doMovement(int moveY) {
		/**把当前状态改为拉动中**/
		status = Status.DRAGGING;
		/**获取刷新视图的LayoutParams，获取topMargin并修改**/
		LayoutParams lp = (LayoutParams) refreshView
				.getLayoutParams();
		float f1 = lp.topMargin;
		float f2 = moveY * 0.3F;// 以0.3比例拖动
		int newTopMargin = (int) (f1 + f2);
		// 修改上边距
		lp.topMargin = newTopMargin;
		// 修改后刷新
		refreshView.setLayoutParams(lp);
		/**刷新视图重绘**/
		refreshView.invalidate();
		invalidate();

		timeTextView.setVisibility(View.VISIBLE);
		downTextView.setVisibility(View.VISIBLE);
		refreshIndicatorView.setVisibility(View.VISIBLE);
		bar.setVisibility(View.GONE);
		if (lp.topMargin > 0) {  //本来是-50  如果下拉距离有50的话就
			downTextView.setText(releaseCanRefreshText);
			refreshIndicatorView.setImageResource(R.drawable.refresh_arrow_up);
		} else {
			downTextView.setText(downCanRefreshText);
			refreshIndicatorView.setImageResource(R.drawable.refresh_arrow_down);
		}

	}

	/**
	 * 设置刷新时间
	 * @MethodDescription setRefreshTime 
	 * @param refreshTime 
	 * @exception 
	 * @since  1.0.0
	 */
	public void setRefreshTime(String refreshTime){
		timeTextView.setText("更新于:"+refreshTime);
	}

	/**
	 * 设置监听
	 * @MethodDescription setRefreshListener 
	 * @param listener 
	 * @exception 
	 * @since  1.0.0
	 */
	public void setRefreshListener(RefreshListener listener) {
		this.refreshListener = listener;
	}

	/**
	 * 刷新时间
	 * 
	 */
	private void refreshTimeBySystem() {
		String dateStr = dateFormat.format(new Date());//可以将时间保存起来
		this.setRefreshText("更新于:"+dateStr);
		
	}

	/**
	 * 结束刷新事件
	 */
	public void finishRefresh() {
		Log.i(TAG, "------->finishRefresh()");
		LayoutParams lp = (LayoutParams) this.refreshView
				.getLayoutParams();
		int i = lp.topMargin;
		refreshIndicatorView.setVisibility(View.VISIBLE);//下拉箭头显示
		timeTextView.setVisibility(View.VISIBLE);//时间控件
		downTextView.setVisibility(VISIBLE);//下拉提示语控件
		refreshTimeBySystem();//修改时间；
		bar.setVisibility(GONE);
		scroller.startScroll(0, i, 0, refreshTargetTop);
		invalidate();
		status = Status.NORMAL;
	}

	/**
	 * 此方法兼容两种子布局的判断，listview，scrollview 主要作用是判断两个子View是否滚动到了最上面，若是，则表示此次touch
	 * move事件截取然后让layout来处理，来移动下拉视图，反之则不然
	 * 
	 * @MethodDescription canScroll
	 * @return
	 * @exception
	 * @since 1.0.0
	 */
	private boolean canScroll() {
		// TODO Auto-generated method stub
		View childView;
		if (getChildCount() > 1) {
			childView = this.getChildAt(1);
			if (childView instanceof ListView) {
				int top = ((ListView) childView).getChildAt(0).getTop();
				int pad = ((ListView) childView).getListPaddingTop();
				if ((Math.abs(top - pad)) < 3
						&& ((ListView) childView).getFirstVisiblePosition() == 0) {
					return true;
				} else {
					return false;
				}
			} else if (childView instanceof ScrollView) {
				/**ScrollView滚动到顶部时才可以下拉刷新**/
				if (((ScrollView) childView).getScrollY() == 0) {
					return true;
				} else {
					return false;
				}
			}

		}
		return false;
	}

	public static int getPixelByDip(Context c, int pix) {
		float f1 = c.getResources().getDisplayMetrics().density;
		float f2 = pix;
		return (int) (f1 * f2 + 0.5F);
	}

	/**
	 * 刷新监听接口
	 * 
	 * @author Nono
	 * 
	 */
	public interface RefreshListener {
		public void onRefresh(DXPullRefreshView view);
	}
	
	
}
