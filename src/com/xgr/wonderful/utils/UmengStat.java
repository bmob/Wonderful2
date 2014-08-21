package com.xgr.wonderful.utils;

public interface UmengStat {
	
	/**
	 * //在继承FragmentActivity的类中，为false;
	 * 在只有Activity的类中，为true。
	 * 统计分为统计时长MobclickAgent.onResume()+MobclickAgent.onPause()
	 * 和统计页面MobclickAgent.onPageStart()+MobclickAgent.onPageEnd()。
	 * 1.Activity中只要调用MobclickAgent.onResume()+MobclickAgent.onPause()，就已经完成两中操作。
	 * 2.FragmentACtivity中，在activity类中调用MobclickAgent.onResume()+MobclickAgent.onPause()统计时长，
	 * 在Fragment中调用MobclickAgent.onPageStart()+MobclickAgent.onPageEnd()统计页面。
	 * 同时要在入口activity中调用MobclickAgent.openActivityDurationTrack(false)禁止掉1中的自动统计。
	 */
	boolean IS_OPEN_ACTIVITY_AUTO_STAT = false;//在继承FragmentActivity的类中，为false;
	
	String MAIN_MENU_OPEN = "main_menu_open";
	String MAIN_MENU_CLOSE = "main_menu_close";
	
	String MAIN_MENU_SETTINGS_CLICK= "main_menu_settings_click";
	String MAIN_MENU_FEEDBACK = "main_menu_feedback_click";
	String MAIN_MENU_INTRO_CLICK = "main_menu_intro_click";
	String MAIN_MENU_ABOUT_CLICK = "main_menu_about_click";
	
	String HOME_ACTION_FAV_CLICK = "home_action_fav_click";
	String HOME_ACTION_PUBLISH_CLICK = "home_action_publish_click";
}
