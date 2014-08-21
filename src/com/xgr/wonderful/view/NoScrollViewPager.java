package com.xgr.wonderful.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author kingofglory 
 * 		   email: kingofglory@yeah.net 
 *         blog: http:www.google.com
 * @date 2014-2-27 
 * TODO 禁止滚动的viewpager, isPagingEnabled = true为允许滑动，false为不允许
 */

public class NoScrollViewPager extends ViewPager {

	private boolean isPagingEnabled;

	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.isPagingEnabled = false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (this.isPagingEnabled) {
			return super.onTouchEvent(event);
		}

		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (this.isPagingEnabled) {
			return super.onInterceptTouchEvent(event);
		}

		return false;
	}

	public void setPagingEnabled(boolean b) {
		this.isPagingEnabled = b;
	}
}
