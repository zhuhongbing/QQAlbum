package com.vtime.qqalbum.view.itemDecoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;

import com.vtime.qqalbum.bean.AlbumBean;
import com.vtime.qqalbum.util.SmallUtil;

import java.util.List;


/**
 *
 */

public class EaseItemDecoration extends RecyclerView.ItemDecoration {
    private final Drawable mDivider;
    private List<? extends AlbumBean> mDatas;
    private Paint mPaint;
    private Rect mBounds;

    private LayoutInflater mInflater;

    private int mTitleHeight;
    private static int COLOR_TITLE_BG = Color.parseColor("#ffffff");
    private static int COLOR_TITLE_FONT = Color.parseColor("#333333");
    private static int COLOR_DATE_COLOR = Color.parseColor("#666666");
    private static int mTitleFontSize;

    private int width;

    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    public EaseItemDecoration(Context context, List<? extends AlbumBean> datas) {
        super();
        mDatas = datas;
        mPaint = new Paint();
        mBounds = new Rect();
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        mTitleHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, context.getResources().getDisplayMetrics());
        mTitleFontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, context.getResources().getDisplayMetrics());
        mPaint.setTextSize(mTitleFontSize);
        mPaint.setAntiAlias(true);
        mInflater = LayoutInflater.from(context);
        width = SmallUtil.getScreenWidth(context) / 3;
    }

    public void setmDatas(List<? extends AlbumBean> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int position = params.getViewLayoutPosition();
            //我记得Rv的item position在重置时可能为-1.保险点判断一下吧
            if (position > -1) {
                if (position == 0) {
                    drawTitleArea(c, left, right, child, params, position);
                } else {
                    if (null != mDatas.get(position).getSubId() && !mDatas.get(position).getSubId().equals(mDatas.get(position - 1).getSubId())) {
                        //不为空 且跟前一个tag不一样了，说明是新的分类，也要title
                        drawTitleArea(c, left, right, child, params, position);
                    } else {
                    }
                }
            }
        }
    }

    /**
     * 绘制Title区域背景和文字的方法
     */
    private void drawTitleArea(Canvas c, int left, int right, View child, RecyclerView.LayoutParams params, int position) {//最先调用，绘制在最下层

        mPaint.setColor(COLOR_TITLE_BG);
        c.drawRect(left, child.getTop() - params.topMargin - mTitleHeight, right, child.getTop() - params.topMargin, mPaint);
        mPaint.setColor(COLOR_TITLE_FONT);
        mPaint.setTextSize(SmallUtil.sp2px(17));
        String date = mDatas.get(position).getTitle();
        mPaint.getTextBounds(date, 0, date.length(), mBounds);
        c.drawText(date, (width * 3) / 2 - mBounds.width() / 2, child.getTop() - params.topMargin - mTitleHeight / 2, mPaint);
    }

    @Override
    public void onDrawOver(Canvas c, final RecyclerView parent, RecyclerView.State state) {//最后调用 绘制在最上层
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        if (position > -1 && position < mDatas.size()) {
            if (position + 1 < mDatas.size()) {
                if (null != mDatas.get(position).getSubId() && !mDatas.get(position).getSubId().equals(mDatas.get(position + 1).getSubId())) {
                    int d = 2 - (mDatas.get(position).value + position) % 3;
                    if (isNewLine(position, mDatas)) {
                        outRect.set(0, mTitleHeight, width * d + mDivider.getIntrinsicHeight(), mDivider.getIntrinsicHeight());
                    } else {
                        outRect.set(0, 0, width * d + mDivider.getIntrinsicHeight(), mDivider.getIntrinsicHeight());
                    }
                } else {
                    if (isNewLine(position, mDatas)) {
                        outRect.set(0, mTitleHeight, mDivider.getIntrinsicHeight(), mDivider.getIntrinsicHeight());
                    } else {
                        outRect.set(0, 0, mDivider.getIntrinsicHeight(), mDivider.getIntrinsicHeight());
                    }
                }
            } else {
                if (isNewLine(position, mDatas)) {
                    outRect.set(0, mTitleHeight, mDivider.getIntrinsicHeight(), mDivider.getIntrinsicHeight());
                } else {
                    outRect.set(0, 0, mDivider.getIntrinsicHeight(), mDivider.getIntrinsicHeight());
                }
            }

        }
    }

    private boolean isNewLine(int position, List<? extends AlbumBean> mDatas) {
        boolean isNew = false;
        if (position == 0 || position == 1 || position == 2)
            return true;
        int size = position > 2 ? position - 3 : 0;
        for (int i = size; i < position; i++) {
            if (null != mDatas.get(i).getSubId() && !mDatas.get(i).getSubId().equals(mDatas.get(i + 1).getSubId())) {
                isNew = true;
                break;
            }
        }
        return isNew;
    }
}
