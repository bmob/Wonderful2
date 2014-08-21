package com.xgr.wonderful.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseHomeFragment extends BaseFragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View contentView = inflater.inflate(getLayoutId(),container,false);
		findViews(contentView);
		setupViews(savedInstanceState);
		setListener();
		fetchData();
		return contentView;
	}

	protected abstract int getLayoutId();

	protected abstract void findViews(View view);

	protected abstract void setupViews(Bundle bundle);

	protected abstract void setListener();

	protected abstract void fetchData();

}
