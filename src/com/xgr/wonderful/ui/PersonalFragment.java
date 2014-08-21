package com.xgr.wonderful.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.xgr.wonderful.R;
import com.xgr.wonderful.MyApplication;
import com.xgr.wonderful.adapter.AIContentAdapter;
import com.xgr.wonderful.adapter.PersonCenterContentAdapter;
import com.xgr.wonderful.entity.QiangYu;
import com.xgr.wonderful.entity.User;
import com.xgr.wonderful.ui.base.BaseHomeFragment;
import com.xgr.wonderful.utils.ActivityUtil;
import com.xgr.wonderful.utils.Constant;
import com.xgr.wonderful.utils.LogUtils;

public class PersonalFragment extends BaseHomeFragment implements OnClickListener{

	private ImageView personalIcon;
	private TextView personalName;
	private TextView personalSign;
	
	private ImageView goSettings;
	
	private TextView personalTitle;
	private PullToRefreshListView mPullToRefreshListView;
	private ListView mListView;
	
	private ArrayList<QiangYu> mQiangYus;
	private PersonCenterContentAdapter mAdapter;
	
	private User mUser ;
	
	private int pageNum ;
	
	public static final int EDIT_USER = 1;
	
	public enum RefreshType{
		REFRESH,LOAD_MORE
	}
	private RefreshType mRefreshType = RefreshType.LOAD_MORE;
	
	
	public static PersonalFragment newInstance(){
		PersonalFragment fragment = new PersonalFragment();
		return fragment;
	}
	
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_personal;
	}

	@Override
	protected void findViews(View view) {
		// TODO Auto-generated method stub
		personalIcon = (ImageView)view.findViewById(R.id.personal_icon);
		personalName = (TextView)view.findViewById(R.id.personl_name);
		personalSign = (TextView)view.findViewById(R.id.personl_signature);
		
		goSettings = (ImageView)view.findViewById(R.id.go_settings);
		
		personalTitle = (TextView)view.findViewById(R.id.personl_title);
		
		mPullToRefreshListView = (PullToRefreshListView)view.findViewById(R.id.pull_refresh_list_personal);
	}

	@Override
	protected void setupViews(Bundle bundle) {
		// TODO Auto-generated method stub
		mUser = MyApplication.getInstance().getCurrentQiangYu().getAuthor();
		
		updatePersonalInfo(mUser);
		
		initMyPublish();
		
	}

	private void initMyPublish() {
		
		if(isCurrentUser(mUser)){
			personalTitle.setText("我发表过的");
			goSettings.setVisibility(View.VISIBLE);
			User user = BmobUser.getCurrentUser(mContext, User.class);
			updatePersonalInfo(user);
		}else{
			goSettings.setVisibility(View.GONE);
			if(mUser !=null && mUser.getSex().equals(Constant.SEX_FEMALE)){
				personalTitle.setText("她发表过的");
			}else if(mUser !=null && mUser.getSex().equals(Constant.SEX_MALE)){
				personalTitle.setText("他发表过的");
			}
		}
		
		mPullToRefreshListView.setMode(Mode.BOTH);
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				mRefreshType = RefreshType.REFRESH;
				pageNum = 0;
				fetchData();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				mRefreshType = RefreshType.LOAD_MORE;
				fetchData();
			}
		});
		mPullToRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				
			}
		});
		mListView = mPullToRefreshListView.getRefreshableView();
		mQiangYus = new ArrayList<QiangYu>();
		mAdapter = new PersonCenterContentAdapter(mContext, mQiangYus);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
//				MyApplication.getInstance().setCurrentQiangYu(mQiangYus.get(position-1));
				Intent intent = new Intent();
				intent.setClass(getActivity(), CommentActivity.class);
				intent.putExtra("data", mQiangYus.get(position-1));
				startActivity(intent);
			}
		});
	}

	private void updatePersonalInfo(User user) {
		personalName.setText(user.getUsername());
		personalSign.setText(user.getSignature());
		if(user.getAvatar() != null){
			ImageLoader.getInstance()
			.displayImage(user.getAvatar().getFileUrl(), personalIcon, 
					MyApplication.getInstance().getOptions(R.drawable.content_image_default),
					new SimpleImageLoadingListener(){
	
						@Override
						public void onLoadingComplete(String imageUri, View view,
								Bitmap loadedImage) {
							// TODO Auto-generated method stub
							super.onLoadingComplete(imageUri, view, loadedImage);
							LogUtils.i(TAG,"load personal icon completed.");
						}
				
			});
		}
	}

	/**
	 * 判断点击条目的用户是否是当前登录用户
	 * @return
	 */
	private boolean isCurrentUser(User user){
		if(null != user){
			User cUser = BmobUser.getCurrentUser(mContext, User.class);
			if(cUser != null && cUser.getObjectId().equals(user.getObjectId())){
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		personalIcon.setOnClickListener(this);
		personalSign.setOnClickListener(this);
		personalTitle.setOnClickListener(this);
		goSettings.setOnClickListener(this);
	}

	@Override
	protected void fetchData() {
		// TODO Auto-generated method stub
		getPublishion();
	}

	private void getPublishion() {
		mIProgressControllor.showActionBarProgress();
		BmobQuery<QiangYu> query = new BmobQuery<QiangYu>();
		query.setLimit(Constant.NUMBERS_PER_PAGE);
		query.setSkip(Constant.NUMBERS_PER_PAGE*(pageNum++));
		query.order("-createdAt");
		query.include("author");
		query.addWhereEqualTo("author", mUser);
		query.findObjects(mContext, new FindListener<QiangYu>() {
			
			@Override
			public void onSuccess(List<QiangYu> data) {
				// TODO Auto-generated method stub
				mIProgressControllor.hideActionBarProgress();
				if(data.size()!=0&&data.get(data.size()-1)!=null){
					if(mRefreshType == RefreshType.REFRESH){
						mQiangYus.clear();
					}
					
					if(data.size()<Constant.NUMBERS_PER_PAGE){
						ActivityUtil.show(getActivity(), "已加载完所有数据~");
					}
					
					mQiangYus.addAll(data);
					mAdapter.notifyDataSetChanged();
					mPullToRefreshListView.onRefreshComplete();
				}else{
					ActivityUtil.show(getActivity(), "暂无更多数据~");
					pageNum--;
					mPullToRefreshListView.onRefreshComplete();
				}
			}

			@Override
			public void onError(int arg0, String msg) {
				// TODO Auto-generated method stub
				mIProgressControllor.hideActionBarProgress();
				LogUtils.i(TAG,"find failed."+msg);
				pageNum--;
				mPullToRefreshListView.onRefreshComplete();
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.personal_icon:
		case R.id.personl_signature:
		case R.id.go_settings:
			if(isCurrentUser(mUser)){
				Intent intent = new Intent();
				intent.setClass(mContext, SettingsActivity.class);
				startActivityForResult(intent, EDIT_USER);
				LogUtils.i(TAG,"current user edit...");
			}
			break;
		case R.id.personl_title:
			
			break;
		default:
			break;
		}
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try{
			mIProgressControllor = (IProgressControllor)activity;
		}catch(ClassCastException e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==Activity.RESULT_OK){
			switch (requestCode) {
			case EDIT_USER:
				getCurrentUserInfo();
				pageNum = 0;
				mRefreshType = RefreshType.REFRESH;
				getPublishion();
				break;

			default:
				break;
			}
		}
	}
	
	/**
	 * 查询当前用户具体信息
	 */
	private void getCurrentUserInfo(){
		mIProgressControllor.showActionBarProgress();
		User user = BmobUser.getCurrentUser(mContext, User.class);
		LogUtils.i(TAG,"sign:"+user.getSignature()+"sex:"+user.getSex());
		updatePersonalInfo(user);
		ActivityUtil.show(mContext, "更新信息成功。");
		mIProgressControllor.hideActionBarProgress();
	}
	
	
	public interface IProgressControllor{
		void showActionBarProgress();
		void hideActionBarProgress();
	}
	private IProgressControllor mIProgressControllor;

}
