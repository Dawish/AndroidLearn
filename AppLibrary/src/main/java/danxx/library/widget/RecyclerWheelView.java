package danxx.library.widget;
import danxx.library.R;
import danxx.library.tools.UIUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by dawish on 2017/8/9.
 */

public class RecyclerWheelView extends RecyclerView {
    public interface OnSelectListener {
        void onSelect(int position);
    }

    private ArrayList<String> mData;
    private int mOffset = 2;//默认每页显示五条
    private float mItemHeight;
    private int mWidth;
    private LinearLayoutManager mManager;
    private LinearSnapHelper mSnapHelper;

    private OnSelectListener mOnSelectListener;

    public RecyclerWheelView(Context context) {
        super(context);
        initData();
    }

    public RecyclerWheelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    public RecyclerWheelView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initData();
    }

    private void initData() {
        mData = new ArrayList<>();

        mItemHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getResources().getDisplayMetrics());
        mManager = new LinearLayoutManager(getContext());
        mManager.setOrientation(LinearLayoutManager.VERTICAL);

        if (null == paint) {
            paint = new Paint();
            paint.setColor(Color.parseColor("#83cde6"));
            paint.setStrokeWidth(UIUtils.dp2px(getContext(), 1f));
        }
        post(new Runnable() {
            @Override
            public void run() {//RecyclerView还未初始化完成不能直接调用其方法否则会报空指针，类构造方法中只能进行初始化成员变量
                getLayoutParams().height = (int) (mItemHeight * (mOffset * 2 + 1));
                setLayoutManager(mManager);

                setAdapter(new MySpaceAdapter());

                getAdapter().notifyDataSetChanged();

                mWidth = getWidth();
            }
        });

        mSnapHelper = new LinearSnapHelper();
        mSnapHelper.attachToRecyclerView(this);
    }

    public ArrayList<String> getData() {
        if (mData.size() < 1) {
            return null;
        }
        ArrayList<String> data = new ArrayList<>();
        for (int i = 1; i < mData.size() - 1; i++) {
            data.add(mData.get(i));
        }
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.mData.clear();
        this.mData.addAll(data);
        mData.add(0, null);
        mData.add(null);
        if (getAdapter() != null)
            getAdapter().notifyDataSetChanged();
    }

    private List<TextView> textViews;

    /**
     * RecyclerView实现WheelView
     */
    class MySpaceAdapter extends RecyclerView.Adapter {


        MySpaceAdapter() {
            textViews = new ArrayList<>();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 1) {
                return new SpaceViewHolder(new View(getContext()));
            } else {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.item_recycler_wheel, parent, false);
                textViews.add((TextView) view);
                return new NormalViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof NormalViewHolder) {
                TextView textView = ((NormalViewHolder) holder).mText;
                textView.setText(mData.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        @Override
        public int getItemViewType(int position) {
            return (position == 0 || position == mData.size() - 1) ? 1 : 0;
        }
    }

    class NormalViewHolder extends RecyclerView.ViewHolder {
        TextView mText;

        NormalViewHolder(View itemView) {
            super(itemView);
            mText = (TextView) itemView;
            mText.getLayoutParams().height = (int) mItemHeight;
        }
    }

    class SpaceViewHolder extends RecyclerView.ViewHolder {
        SpaceViewHolder(View itemView) {
            super(itemView);
            //根据每一条高度及偏移量设置高度
            float height = mOffset * mItemHeight;
            itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) height));
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        /**
         * 手动滑动或者惯性滑动时触发该方法，不过由于本身没有滑动所以l,t,oldl,oldt都是0
         * 此处只是在滑动时设置相应状态
         */
        View viewContainer = mSnapHelper.findSnapView(mManager);//获取中间位置的view
        TextView textView = ((NormalViewHolder) getChildViewHolder(viewContainer)).mText;

        int centerPosition = mData.indexOf(textView.getText().toString());
        if (mCenterPosition == centerPosition) {
            return;
        }
        mCenterPosition = centerPosition;

        for (TextView view : textViews) {//设置选中颜色
            view.setTextColor(Color.parseColor("#dddddd"));
            if (view == textView) {
                view.setTextColor(Color.parseColor("#0288ce"));
            }
        }

        if (mOnSelectListener != null) {
            //第一条是占位条
            mOnSelectListener.onSelect(mCenterPosition - 1);
        }
    }

    private int mCenterPosition;//获取选中位置时要减一（有占位条）
    private Paint paint;

    /**
     * 获取选中区域的边界
     */
    private int[] selectedAreaBorder;

    private int[] obtainSelectedAreaBorder() {
        if (null == selectedAreaBorder) {
            selectedAreaBorder = new int[2];
            selectedAreaBorder[0] = (int) (mItemHeight * mOffset);
            selectedAreaBorder[1] = (int) (mItemHeight * (mOffset + 1));
        }
        return selectedAreaBorder;
    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        c.drawLine(mWidth * 1.0F / 6, obtainSelectedAreaBorder()[0], mWidth * 5 / 6, obtainSelectedAreaBorder()[0], paint);
        c.drawLine(mWidth * 1.0F / 6, obtainSelectedAreaBorder()[1], mWidth * 5 / 6, obtainSelectedAreaBorder()[1], paint);
    }

    public void setmOnSelectListener(OnSelectListener mOnSelectListener) {
        this.mOnSelectListener = mOnSelectListener;
    }
}
