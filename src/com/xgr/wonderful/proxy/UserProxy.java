package com.xgr.wonderful.proxy;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.ResetPasswordListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.xgr.wonderful.entity.User;
import com.xgr.wonderful.utils.Constant;
import com.xgr.wonderful.utils.LogUtils;

import android.content.Context;

public class UserProxy {

	public static final String TAG = "UserProxy";
	
	private Context mContext;
	
	public UserProxy(Context context){
		this.mContext = context;
	}
	
	public void signUp(String userName,String password,String email){
		User user = new User();
		user.setUsername(userName);
		user.setPassword(password);
		user.setEmail(email);
		user.setSex(Constant.SEX_FEMALE);
		user.setSignature("这个家伙很懒，什么也不说。。。");
		user.signUp(mContext, new SaveListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				if(signUpLister != null){
					signUpLister.onSignUpSuccess();
				}else{
					LogUtils.i(TAG,"signup listener is null,you must set one!");
				}
			}

			@Override
			public void onFailure(int arg0, String msg) {
				// TODO Auto-generated method stub
				if(signUpLister != null){
					signUpLister.onSignUpFailure(msg);
				}else{
					LogUtils.i(TAG,"signup listener is null,you must set one!");
				}
			}
		});
	}
	
	public interface ISignUpListener{
		void onSignUpSuccess();
		void onSignUpFailure(String msg);
	}
	private ISignUpListener signUpLister;
	public void setOnSignUpListener(ISignUpListener signUpLister){
		this.signUpLister = signUpLister;
	}
	
	
	public User getCurrentUser(){
		User user = BmobUser.getCurrentUser(mContext, User.class);
		if(user != null){
			LogUtils.i(TAG,"本地用户信息" + user.getObjectId() + "-"
					+ user.getUsername() + "-"
					+ user.getSessionToken() + "-"
					+ user.getCreatedAt() + "-"
					+ user.getUpdatedAt() + "-"
					+ user.getSignature() + "-"
					+ user.getSex());
			return user;
		}else{
			LogUtils.i(TAG,"本地用户为null,请登录。");
		}
		return null;
	}
	
	public void login(String userName,String password){
		final BmobUser user = new BmobUser();
		user.setUsername(userName);
		user.setPassword(password);
		user.login(mContext, new SaveListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				if(loginListener != null){
					loginListener.onLoginSuccess();
				}else{
					LogUtils.i(TAG, "login listener is null,you must set one!");
				}
			}

			@Override
			public void onFailure(int arg0, String msg) {
				// TODO Auto-generated method stub
				if(loginListener != null){
					loginListener.onLoginFailure(msg);
				}else{
					LogUtils.i(TAG, "login listener is null,you must set one!");
				}
			}
		});
	}
	
	public interface ILoginListener{
		void onLoginSuccess();
		void onLoginFailure(String msg);
	}
	private ILoginListener loginListener;
	public void setOnLoginListener(ILoginListener loginListener){
		this.loginListener  = loginListener;
	}
	
	public void logout(){
		BmobUser.logOut(mContext);
		LogUtils.i(TAG, "logout result:"+(null == getCurrentUser()));
	}
	
	public void update(String... args){
		User user = getCurrentUser();
		user.setUsername(args[0]);
		user.setEmail(args[1]);
		user.setPassword(args[2]);
		user.setSex(args[3]);
		user.setSignature(args[4]);
		//...
		user.update(mContext, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				if(updateListener != null){
					updateListener.onUpdateSuccess();
				}else{
					LogUtils.i(TAG,"update listener is null,you must set one!");
				}
			}

			@Override
			public void onFailure(int arg0, String msg) {
				// TODO Auto-generated method stub
				if(updateListener != null){
					updateListener.onUpdateFailure(msg);
				}else{
					LogUtils.i(TAG,"update listener is null,you must set one!");
				}
			}
		});
	}
	
	public interface IUpdateListener{
		void onUpdateSuccess();
		void onUpdateFailure(String msg);
	}
	private IUpdateListener updateListener;
	public void setOnUpdateListener(IUpdateListener updateListener){
		this.updateListener = updateListener;
	}
	
	public void resetPassword(String email){
		BmobUser.resetPassword(mContext, email, new ResetPasswordListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				if(resetPasswordListener != null){
					resetPasswordListener.onResetSuccess();
				}else{
					LogUtils.i(TAG,"reset listener is null,you must set one!");
				}
			}

			@Override
			public void onFailure(int arg0, String msg) {
				// TODO Auto-generated method stub
				if(resetPasswordListener != null){
					resetPasswordListener.onResetFailure(msg);
				}else{
					LogUtils.i(TAG,"reset listener is null,you must set one!");
				}
			}
		});
	}
	public interface IResetPasswordListener{
		void onResetSuccess();
		void onResetFailure(String msg);
	}
	private IResetPasswordListener resetPasswordListener;
	public void setOnResetPasswordListener(IResetPasswordListener resetPasswordListener){
		this.resetPasswordListener = resetPasswordListener;
	}

}
