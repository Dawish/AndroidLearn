package danxx.library.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class CustomImgContainer extends ViewGroup
{

    private static final String TAG = "CustomImgContainer";
    private int currentItemIndex = -1;
    public CustomImgContainer(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public CustomImgContainer(Context context)
    {
        super(context);
    }

    public CustomImgContainer(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    /**
     * 计算所有ChildView的宽度和高度 然后根据ChildView的计算结果，设置自己的宽和高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        Log.e(TAG, (heightMode == MeasureSpec.UNSPECIFIED) + "," + sizeHeight
                + "," + getLayoutParams().height);

        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        /**
         * 记录如果是wrap_content是设置的宽和高
         */
        int width = 0;
        int height = 0;

        // 用于计算左边两个childView的高度
        int lHeight = 0;
        // 用于计算右边两个childView的高度，最终高度取二者之间大值
        int rHeight = 0;

        // 用于计算上边两个childView的宽度
        int tWidth = 0;
        // 用于计算下面两个childiew的宽度，最终宽度取二者之间大值
        int bWidth = 0;
        // 总共的view个数
        int cCount = getChildCount();

        /**用于在遍历的时候记录每一个View的实际宽度*/
        int cWidth = 0;
        /**用于在遍历的时候记录每一个View的实际高度*/
        int cHeight = 0;
        /**用于在遍历的时候记录每一个View的Margin值*/
        MarginLayoutParams cParams = null;

        /**
         * 根据childView计算的出的宽和高，以及设置的margin计算容器的宽和高，主要用于容器是warp_content时
         * 遍历计算子View的上下左右的Margin值来最终确定自己的宽高
         */
        for (int i = 0; i < cCount; i++)
        {
            View childView = getChildAt(i);
            /***/
            cWidth = childView.getMeasuredWidth();
            /***/
            cHeight = childView.getMeasuredHeight();
            /***/
            cParams = (MarginLayoutParams) childView.getLayoutParams();

            // 上面两个childView 总共的宽度
            if (i == 0 || i == 1)
            {
                tWidth += cWidth + cParams.leftMargin + cParams.rightMargin;
            }
            // 下面两个childView 总共的宽度
            if (i == 2 || i == 3)
            {
                bWidth += cWidth + cParams.leftMargin + cParams.rightMargin;
            }
            // 左面两个childView 总共的高度
            if (i == 0 || i == 2)
            {
                lHeight += cHeight + cParams.topMargin + cParams.bottomMargin;
            }
            // 右面两个childView 总共的高度
            if (i == 1 || i == 3)
            {
                rHeight += cHeight + cParams.topMargin + cParams.bottomMargin;
            }

        }

        width = Math.max(tWidth, bWidth);
        height = Math.max(lHeight, rHeight);

        /**
         * 根绝子View的宽高来确定自己的宽高
         * 如果是wrap_content设置为我们计算的值
         * 否则：直接设置为父容器计算的值
         */
        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? sizeWidth
                : width, (heightMode == MeasureSpec.EXACTLY) ? sizeHeight
                : height);
    }

    // abstract method in viewgroup
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        int cCount = getChildCount();
        int cWidth = 0;
        int cHeight = 0;
        MarginLayoutParams cParams = null;
        /**
         * 遍历所有childView根据其宽和高，以及margin进行布局
         */
        for (int i = 0; i < cCount; i++)
        {
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
            cParams = (MarginLayoutParams) childView.getLayoutParams();
            //四个方向的margin值
            int ml = 0, mt = 0, mr = 0, mb = 0;

            switch (i)
            {
                case 0:
                    //让此View在左上角
                    ml = cParams.leftMargin;
                    mt = cParams.topMargin;
                    break;
                case 1:
                    //让此View在右上角
                    ml = getWidth() - cWidth - cParams.leftMargin - cParams.rightMargin;
                    mt = cParams.topMargin;

                    break;
                case 2:
                    //让此View在左下角
                    ml = cParams.leftMargin;
                    mt = getHeight() - cHeight - cParams.bottomMargin;
                    break;
                case 3:
                    //让此View在右下角
                    ml = getWidth() - cWidth - cParams.leftMargin
                            - cParams.rightMargin;
                    mt = getHeight() - cHeight - cParams.bottomMargin;
                    break;

            }
            mr = ml + cWidth;
            mb = cHeight + mt;
            //根据子View的margin值来确定自己的位置
            childView.layout(ml, mt, mr, mb);
        }

    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams()
    {
        Log.e(TAG, "generateDefaultLayoutParams");
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(
            ViewGroup.LayoutParams p)
    {
        Log.e(TAG, "generateLayoutParams p");
        return new MarginLayoutParams(p);
    }

    /**
     * 获取子控件dispatchDraw的次序
     */
    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (currentItemIndex < 0) {
            return i;
        }
        if (i < (childCount - 1)) {
            if (currentItemIndex == i)
                i = childCount - 1;
        } else {
            if (currentItemIndex < childCount)
                i = currentItemIndex;
        }
        return i;
    }

    /*
	 * if (heightMode == MeasureSpec.UNSPECIFIED)
		{
			int tmpHeight = 0 ;
			LayoutParams lp = getLayoutParams();
			if (lp.height == LayoutParams.MATCH_PARENT)
			{
				Rect outRect = new Rect();
				getWindowVisibleDisplayFrame(outRect);
				tmpHeight = outRect.height();
			}else
			{
				tmpHeight = getLayoutParams().height ;
			}
			height = Math.max(height, tmpHeight);

		}
	 */
}
