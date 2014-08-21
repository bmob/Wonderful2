package com.xgr.wonderful.ui;

import net.youmi.android.offers.OffersManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.xgr.wonderful.MyApplication;
import com.xgr.wonderful.R;
import com.xgr.wonderful.utils.ActivityUtil;
import com.xgr.wonderful.utils.Constant;
import com.xgr.wonderful.utils.LogUtils;


public class MainActivity extends SlidingFragmentActivity implements OnClickListener{

    public static final String TAG = "MainActivity";
	private NaviFragment naviFragment;
	private ImageView leftMenu;
	private ImageView rightMenu;
	private SlidingMenu mSlidingMenu;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.center_frame);
		leftMenu = (ImageView)findViewById(R.id.topbar_menu_left);
		rightMenu = (ImageView)findViewById(R.id.topbar_menu_right);
		leftMenu.setOnClickListener(this);
		rightMenu.setOnClickListener(this);
		initFragment();
		//显示提示对话框
//		showDialog();
	}
	
	Dialog dialog ;
	private void showDialog(){
		dialog=new AlertDialog.Builder(this)
		.setTitle("提示")
		.setMessage(R.string.dialog_tips)
		.setPositiveButton("确实", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				
			}
		}).create();
		dialog.show();
	}

	private void initFragment() {
		mSlidingMenu = getSlidingMenu();
		setBehindContentView(R.layout.frame_navi); // 给滑出的slidingmenu的fragment制定layout
		naviFragment = new NaviFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.frame_navi, naviFragment).commit();
		// 设置slidingmenu的属性
		mSlidingMenu.setMode(SlidingMenu.LEFT);// 设置slidingmeni从哪侧出现
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);// 只有在边上才可以打开
		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// 偏移量
		mSlidingMenu.setFadeEnabled(true);
		mSlidingMenu.setFadeDegree(0.5f);
		mSlidingMenu.setMenu(R.layout.frame_navi);
		

		Bundle mBundle = null;
		// 导航打开监听事件
		mSlidingMenu.setOnOpenListener(new OnOpenListener() {
			@Override
			public void onOpen() {
			}
		});
		// 导航关闭监听事件
		mSlidingMenu.setOnClosedListener(new OnClosedListener() {

			@Override
			public void onClosed() {
			}
		});
	}

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId()) {
            case R.id.topbar_menu_left:
                mSlidingMenu.toggle();
                break;
            case R.id.topbar_menu_right:
              //当前用户登录
                BmobUser currentUser = BmobUser.getCurrentUser(MainActivity.this);
                if (currentUser != null) {
                    // 允许用户使用应用,即有了用户的唯一标识符，可以作为发布内容的字段
                    String name = currentUser.getUsername();
                    String email = currentUser.getEmail();
                    LogUtils.i(TAG,"username:"+name+",email:"+email);
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, EditActivity.class);
                    startActivity(intent);
                } else {
                    // 缓存用户对象为空时， 可打开用户注册界面…
                    Toast.makeText(MainActivity.this, "请先登录。",
                            Toast.LENGTH_SHORT).show();
//                  redictToActivity(mContext, RegisterAndLoginActivity.class, null);
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, RegisterAndLoginActivity.class);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	OffersManager.getInstance(MainActivity.this).onAppExit();
    }
    
	private static long firstTime;
	/**
	 * 连续按两次返回键就退出
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (firstTime + 2000 > System.currentTimeMillis()) {
			MyApplication.getInstance().exit();
			super.onBackPressed();
		} else {
			ActivityUtil.show(MainActivity.this, "再按一次退出程序");
		}
		firstTime = System.currentTimeMillis();
	}
}
