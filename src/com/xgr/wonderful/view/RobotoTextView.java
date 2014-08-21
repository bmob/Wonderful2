package com.xgr.wonderful.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author kingofglory
 *         email: kingofglory@yeah.net
 *         blog:  http:www.google.com
 * @date 2014-2-27
 * TODO
 */

public class RobotoTextView extends TextView{

	public RobotoTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub\
		style(context);
	}

	public RobotoTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		style(context);
	}
	
	public RobotoTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		style(context);
	}

	private void style(Context context){
		Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-regular.ttf");
		setTypeface(typeface);
	}
}
