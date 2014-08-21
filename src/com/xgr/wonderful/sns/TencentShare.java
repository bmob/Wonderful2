package com.xgr.wonderful.sns;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import com.tencent.connect.common.Constants;
import com.tencent.open.SocialConstants;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tencent.utils.HttpUtils.HttpStatusException;
import com.tencent.utils.HttpUtils.NetworkUnavailableException;
import com.xgr.wonderful.R;
import com.xgr.wonderful.utils.ActivityUtil;
import com.xgr.wonderful.utils.Constant;
import com.xgr.wonderful.utils.LogUtils;
import com.xgr.wonderful.utils.NetworkUtil;
import com.xgr.wonderful.utils.Sputil;



import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;

/**
 * @author kingofglory email: kingofglory@yeah.net blog: http:www.google.com
 * @date 2014-3-3 TODO 实现QQ好友以及Qzone分享一键完成 usage:1.按opensdk说明文档配置好AndroidManifest.xml 2.在@see{TencentShareConstants}中填写相应分享内容
 *       ,当调用无参数分享方法时，刚采用该接口中默认填写内容，当调用有参数分享时，刚采用传入参数 3.调用示例： TencentShare ts = new TencentShare(Activity,TencentShareEntity);
 *       第二个参数若为空，刚采用第二步中填写默认参数 ts.shareToQQ();//无参数分享到QQ好友 ts.shareToQZone();//分享到QQ空间
 */

public class TencentShare implements TencentShareConstants {

    public static final String TAG="TencentShare";

    public static final String SCOPE="get_simple_userinfo";

    private Activity mContext;

    private Tencent tencent;

    private TencentShareEntity shareEntity;

    private Sputil sputil;

    public TencentShare(Activity context, TencentShareEntity entity) {
        mContext=context;
        initTencent();
        shareEntity=entity;
        if(shareEntity == null) {
            shareEntity=
                new TencentShareEntity(TencentShareConstants.TITLE, TencentShareConstants.IMG_URL,
                    TencentShareConstants.TARGET_URL, TencentShareConstants.SUMMARY, TencentShareConstants.COMMENT);
        }
        sputil=new Sputil(mContext, Constant.PRE_NAME);
    }

    /**
     * 初始化tencent实例
     */
    private void initTencent() {
        if(tencent == null) {
            tencent=Tencent.createInstance(getAppId(), mContext);
        }

    }

    /**
     * 从Adminifest.xml里读取app_id
     * @return
     */
    private String getAppId() {
        String appId="";
        try {
            ApplicationInfo appInfo=
                mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
            appId=appInfo.metaData.getString("TA_APPKEY");
            // LogUtil.i(TAG,appId.substring(3));
        } catch(NameNotFoundException e) {
            e.printStackTrace();
        }
        return appId.substring(3);
    }

    /**
     * 检查网络并开始分享
     */
    public void shareToQQ() {
        shareToQQ(shareEntity);
    }

    /**
     * 检查网络并开始分享,支持动态改变分享参数
     */
    private void shareToQQ(TencentShareEntity entity) {
        if(NetworkUtil.isAvailable(mContext)) {
            doShareToQQ(entity);
        } else {
            ActivityUtil.show(mContext, "网络无连接");
        }
    }

    /**
     * QQ分享实际操作
     * @param title
     * @param imgUrl
     * @param targetUrl
     * @param summary
     */
    private void doShareToQQ(TencentShareEntity entity) {
        System.out.println(entity);
        Bundle params=new Bundle();
        params.putString(SocialConstants.PARAM_TITLE, entity.getTitle());
        params.putString(SocialConstants.PARAM_IMAGE_URL, entity.getImgUrl());
        params.putString(SocialConstants.PARAM_TARGET_URL, entity.getTargetUrl());
        params.putString(SocialConstants.PARAM_SUMMARY, entity.getSummary());
        params.putString(SocialConstants.PARAM_COMMENT, entity.getComment());
        params.putString(SocialConstants.PARAM_APPNAME, mContext.getResources().getString(R.string.app_name));
        initTencent();
        tencent.shareToQQ(mContext, params, new BaseUiListener(0));
    }

    /**
     * 检查网络状态并开始Qzone分享
     */
    public void shareToQZone() {
        shareToQZone(shareEntity);
    }

    /**
     * 检查网络状态并开始Qzone分享，支持动态改变分享参数
     */
    private void shareToQZone(TencentShareEntity entity) {
        if(NetworkUtil.isAvailable(mContext)) {
            doShareToQZone(entity);
        } else {
            ActivityUtil.show(mContext, "网络无连接");
        }
    }

    /**
     * 分享到QQ空间，实际分享操作
     */
    private void doShareToQZone(TencentShareEntity entity) {
        if(ready()) {
            // send story
            sendStoryToQZone(entity);
        } else {
            // go to login
            tencent.login(mContext, SCOPE, new BaseUiListener(2));
        }
    }

    /**
     * 仅仅是绑定QQ
     */
    public void bindQQ() {
        tencent.login(mContext, SCOPE, (IUiListener) new BaseUiListener(3));
    }

    public void unBindQQ() {
        sputil.remove("nick");
        loginOut();
    }

    /**
     * 登出
     */
    private void loginOut() {
        tencent.logout(mContext);
    }

    /**
     * 是否绑定QQ
     * @return
     */
    public boolean isBindQQ() {
        if(!sputil.getValue("nick", "").equals("")) {
            return true;
        }
        return false;
    }

    /**
     * 检验QQ是否在登录状态
     * @return
     */
    private boolean ready() {
        boolean ready=tencent.isSessionValid() && tencent.getOpenId() != null;
        return ready;
    }

    /**
     * 进入QZone分享，实际分享操作
     */
    private void sendStoryToQZone(TencentShareEntity entity) {
        Bundle params=new Bundle();

        params.putString(SocialConstants.PARAM_TITLE, entity.getTitle());
        params.putString(SocialConstants.PARAM_IMAGE, entity.getImgUrl());
        params.putString(SocialConstants.PARAM_SUMMARY, entity.getSummary());
        params.putString(SocialConstants.PARAM_TARGET_URL, entity.getTargetUrl());
        params.putString(SocialConstants.PARAM_COMMENT, entity.getComment());
        params.putString(SocialConstants.PARAM_ACT, "进入应用");
        tencent.story(mContext, params, new BaseUiListener(1));
    }

    private class BaseUiListener implements IUiListener {

        private int flag=-1;

        public BaseUiListener(int flag) {
            this.flag=flag;
        }

        @Override
        public void onError(UiError e) {

            LogUtils.i("QQ", "onError----" + "code:" + e.errorCode + ", msg:" + e.errorMessage + ", detail:" + e.errorDetail);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onComplete(Object arg0) {
            // TODO Auto-generated method stub
            switch(flag) {
                case 0:
                    LogUtils.i(TAG, "share to qq complete!");
                    onShareToQQComplete();
                    break;
                case 1:
                    LogUtils.i(TAG, "share to qzone complete!");
                    onShareToQZoneComplete();
                    break;
                case 2:
                    LogUtils.i(TAG, "login complete and begin to story!");
                    doShareToQZone(shareEntity);
                    onQQLoginComplete();
                    break;
                case 3:
                    onQQLoginComplete();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 登錄完QQ以后想做的操作，比如獲取QQ信息等
     */
    public void onQQLoginComplete() {
        if(ready()) {
            BaseApiListener requestListener=new BaseApiListener("get_simple_userinfo", false);
            Bundle params=new Bundle();
            if(tencent != null && tencent.isSessionValid()) {
                params.putString(Constants.PARAM_ACCESS_TOKEN, tencent.getAccessToken());
                params.putString(Constants.PARAM_CONSUMER_KEY, tencent.getAppId());
                params.putString(Constants.PARAM_OPEN_ID, tencent.getOpenId());
                params.putString("format", "json");
            }
            tencent.requestAsync("user/get_simple_userinfo", params, Constants.HTTP_GET, (IRequestListener) requestListener, null);
        }
    }

    /**
     * 如果QQ分享完成想还有其他操作，请重写该方法实现
     */
    public void onShareToQQComplete() {

    }

    /**
     * 如果QZone分享完成想还有其他操作，请重写该方法实现
     */
    public void onShareToQZoneComplete() {

    }

    private class BaseApiListener implements IRequestListener {

        private String mScope="all";

        private Boolean mNeedReAuth=false;

        public BaseApiListener(String scope, boolean needReAuth) {
            mScope=scope;
            mNeedReAuth=needReAuth;
        }

        @Override
        public void onComplete(JSONObject response) {
            // TODO Auto-generated method stub
            LogUtils.i("onComplete:", response.toString());
            doComplete(response);
        }

        protected void doComplete(JSONObject response) {
            try {
                int ret=response.getInt("ret");
                if(ret == 100030) {
                    if(mNeedReAuth) {
                        Runnable r=new Runnable() {

                            public void run() {
                                tencent.reAuth(mContext, mScope, new BaseUiListener(-1));
                            }
                        };
                        mContext.runOnUiThread(r);
                    }
                } else if(ret == 0) {
                    String nick=response.getString("nickname");
                    sputil.setValue("nick", nick);
                }
            } catch(JSONException e) {
                e.printStackTrace();
                Log.e("toddtest", response.toString());
            }

        }

        @Override
        public void onIOException(final IOException e) {
            LogUtils.i("IRequestListener.onIOException:", e.getMessage());
        }

        @Override
        public void onMalformedURLException(final MalformedURLException e) {
        	LogUtils.i("IRequestListener.onMalformedURLException", e.toString());
        }

        @Override
        public void onJSONException(final JSONException e) {
        	LogUtils.i("IRequestListener.onJSONException:", e.getMessage());
        }

        @Override
        public void onConnectTimeoutException(ConnectTimeoutException arg0) {
        	LogUtils.i("IRequestListener.onConnectTimeoutException:", arg0.getMessage());

        }

        @Override
        public void onSocketTimeoutException(SocketTimeoutException arg0) {
        	LogUtils.i("IRequestListener.SocketTimeoutException:", arg0.getMessage());
        }

        @Override
        public void onUnknowException(Exception arg0) {
        	LogUtils.i("IRequestListener.onUnknowException:", arg0.getMessage());
        }

		@Override
		public void onHttpStatusException(HttpStatusException arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onNetworkUnavailableException(
				NetworkUnavailableException arg0) {
			// TODO Auto-generated method stub
			
		}

       

    }

}
