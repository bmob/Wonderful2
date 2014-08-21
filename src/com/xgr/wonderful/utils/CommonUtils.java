package com.xgr.wonderful.utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * @author kingofglory
 *         email: kingofglory@yeah.net
 *         blog:  http:www.google.com
 * @date 2014-2-27
 * TODO
 */

public class CommonUtils {
	
	/**
	 * 
	 * @param context
	 * @param assetsPah:"fonts/xxx.ttf"
	 * @return
	 * usage:textView.setTypeface(typeface);
	 */
	public static Typeface getTypeface(Context context,String assetsPah){
		Typeface typeface = Typeface.createFromAsset(context.getAssets(), assetsPah);
		return typeface;
	}
}
