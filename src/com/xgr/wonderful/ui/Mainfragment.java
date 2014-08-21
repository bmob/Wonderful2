package com.xgr.wonderful.ui;

import com.xgr.wonderful.R;
import com.xgr.wonderful.adapter.QiangContentAdapter;
import com.xgr.wonderful.ui.base.BaseFragment;
import com.xgr.wonderful.utils.LogUtils;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * @author kingofglory
 *         email: kingofglory@yeah.net
 *         blog:  http:www.google.com
 * @date 2014-2-23
 * TODO
 */

public class Mainfragment extends BaseFragment implements OnPageChangeListener{

	private View contentView;
	private ViewPager mViewPager;
	private QiangContentAdapter mAdapter;
	
	
	public static BaseFragment newInstance(){
		BaseFragment  fragment = new Mainfragment();
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		contentView = inflater.inflate(R.layout.fragment_main, null);
		mViewPager = (ViewPager)contentView.findViewById(R.id.viewpager);
		mAdapter = new QiangContentAdapter(getActivity().getSupportFragmentManager());
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setOffscreenPageLimit(4);
		return contentView;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), arg0+"", Toast.LENGTH_SHORT).show();
		LogUtils.i(TAG, arg0+"--->");
	}

	
	public void setCurrentPage(int targetIndex){
		mViewPager.setCurrentItem(targetIndex, false);
	}
	
}
