package com.xgr.wonderful.ui.base;

import cn.bmob.v3.Bmob;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.xgr.wonderful.R;
import com.xgr.wonderful.utils.Constant;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;

public abstract class BaseHomeActivity extends BaseActivity{
	
	protected ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		Bmob.initialize(this, Constant.BMOB_APP_ID);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_base_home);
		
		initActionBar();
		
		initFragment();
	}

	private void initActionBar() {
		actionBar = (ActionBar)findViewById(R.id.actionbar_base);
		actionBar.setTitle(getActionBarTitle());
		actionBar.setDisplayHomeAsUpEnabled(isHomeAsUpEnabled()==true?true:false);
		actionBar.setHomeAction(new Action() {
			
			@Override
			public void performAction(View view) {
				// TODO Auto-generated method stub
				onHomeActionClick();
			}
			
			@Override
			public int getDrawable() {
				// TODO Auto-generated method stub
				return R.drawable.logo;
			}
		});
		addActions();
	}
	
	private void initFragment(){
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(R.id.content_frame_base, getFragment())
				.commit();
	}
	
	protected abstract String getActionBarTitle();
	protected abstract boolean isHomeAsUpEnabled();
	protected abstract void onHomeActionClick();
	protected abstract BaseFragment getFragment();
	protected abstract void addActions();
	
	
}
