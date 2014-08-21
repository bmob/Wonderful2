package com.xgr.wonderful.ui.base;

import cn.bmob.v3.Bmob;

import com.xgr.wonderful.utils.Constant;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
/**
 * @author kingofglory
 *         email: kingofglory@yeah.net
 *         blog:  http:www.google.com
 * @date 2014-2-21
 * TODO 
 */

public abstract class BasePageActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		Bmob.initialize(this, Constant.BMOB_APP_ID);
		setLayoutView();
		init(bundle);
	}
//////
	private void init(Bundle bundle) {
		// TODO Auto-generated method stub
		findViews();
		setupViews(bundle);
		setListener();
		fetchData();
	}


	protected abstract void setLayoutView();
	
	protected abstract void findViews();

	protected abstract void setupViews(Bundle bundle);

	protected abstract void setListener();

	protected abstract void fetchData();

	
}
