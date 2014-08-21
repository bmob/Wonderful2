package com.xgr.wonderful.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.xgr.wonderful.R;
import com.xgr.wonderful.MyApplication;
import com.xgr.wonderful.entity.QiangYu;
import com.xgr.wonderful.entity.User;
import com.xgr.wonderful.ui.base.BaseHomeFragment;
import com.xgr.wonderful.utils.ActivityUtil;
import com.xgr.wonderful.utils.CacheUtils;
import com.xgr.wonderful.utils.Constant;
import com.xgr.wonderful.utils.LogUtils;
@Deprecated
public class PersonalEditFragment extends BaseHomeFragment implements OnClickListener{

	RelativeLayout iconLayout;
	ImageView userIcon;
	
	RelativeLayout signatureLayout;
	TextView signatureContent;
	EditText signatureEdit;
	
	RadioGroup sexChoice;
	
	Button commitEdit;
	
	AlertDialog albumDialog;
	
	String dateTime;
	
	String sex;
	String newSex;
	
	User currentUser;
	
	public static PersonalEditFragment newInstance(){
		PersonalEditFragment fragment = new PersonalEditFragment();
		return fragment;
	}
	
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_personal_edit;
	}

	@Override
	protected void findViews(View view) {
		// TODO Auto-generated method stub
		iconLayout = (RelativeLayout)view.findViewById(R.id.personal_icon);
		userIcon = (ImageView)view.findViewById(R.id.personal_icon_content);
		
		sexChoice = (RadioGroup)view.findViewById(R.id.personal_sex_content);
		
		signatureLayout = (RelativeLayout)view.findViewById(R.id.personal_signature);
		signatureContent = (TextView)view.findViewById(R.id.personal_signature_content);
		signatureEdit = (EditText)view.findViewById(R.id.personal_signature_content_edit);
	
		commitEdit = (Button)view.findViewById(R.id.personal_commit);
	}

	@Override
	protected void setupViews(Bundle bundle) {
		// TODO Auto-generated method stub
		currentUser = BmobUser.getCurrentUser(mContext, User.class);
		sexChoice.check(currentUser.getSex().equals(Constant.SEX_MALE)?R.id.personal_sex_male:R.id.personal_sex_female);
		signatureContent.setText(currentUser.getSignature());
		BmobFile avatarFile = currentUser.getAvatar();
		if(null != avatarFile){
			ImageLoader.getInstance()
			.displayImage(avatarFile.getFileUrl(), userIcon, 
					MyApplication.getInstance().getOptions(R.drawable.ic_launcher),
					new SimpleImageLoadingListener(){

						@Override
						public void onLoadingComplete(String imageUri, View view,
								Bitmap loadedImage) {
							// TODO Auto-generated method stub
							super.onLoadingComplete(imageUri, view, loadedImage);
						}
				
			});
		}

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		iconLayout.setOnClickListener(this);
		userIcon.setOnClickListener(this);
		
		signatureLayout.setOnClickListener(this);
		signatureContent.setOnClickListener(this);
		
		sexChoice.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId == R.id.personal_sex_male){
					newSex = Constant.SEX_MALE;
				}else{
					newSex = Constant.SEX_FEMALE;
				}
			}
		});
		
		commitEdit.setOnClickListener(this);
	}

	@Override
	protected void fetchData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.personal_signature:
		case R.id.personal_signature_content:
			signatureContent.setVisibility(View.GONE);
			signatureEdit.setVisibility(View.VISIBLE);
			signatureEdit.requestFocus();
			signatureEdit.requestFocusFromTouch();
			break;
		case R.id.personal_commit:
			setAvata(iconUrl);
			break;
		case R.id.personal_icon:
		case R.id.personal_icon_content:
			showAlbumDialog();
			break;
		default:
			break;
		}
	}
	
	private void setAvata(String avataPath){
		if(avataPath!=null){
			final BmobFile file = new BmobFile(new File(iconUrl));
			file.upload(mContext, new UploadFileListener() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					LogUtils.i(TAG, "上传文件成功。"+file.getFileUrl());
					currentUser.setAvatar(file);
					if(!TextUtils.isEmpty(signatureEdit.getText().toString().trim())){
						currentUser.setSignature(signatureEdit.getText().toString().trim());
					}
					currentUser.setSex(newSex);
					currentUser.update(mContext, new UpdateListener() {
						
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							ActivityUtil.show(getActivity(), "更新信息成功。");
							currentUser = BmobUser.getCurrentUser(getActivity(),User.class);
							LogUtils.i(TAG,"new url:"+BmobUser.getCurrentUser(getActivity(),User.class).getAvatar().getFileUrl());
							getActivity().setResult(Activity.RESULT_OK);
							getActivity().finish();
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							ActivityUtil.show(getActivity(), "更新信息失败。请检查网络~");
							LogUtils.i(TAG,"更新失败2-->"+arg0);
						}
					});
				}

				@Override
				public void onProgress(Integer arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					LogUtils.i(TAG, "上传文件失败。"+arg1);
				}
			});
		}else{
			currentUser.setSex(newSex);
			if(!TextUtils.isEmpty(signatureEdit.getText().toString().trim())){
				currentUser.setSignature(signatureEdit.getText().toString().trim());
			}
			currentUser.update(mContext, new UpdateListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						ActivityUtil.show(getActivity(),"更新信息成功。");
						LogUtils.i(TAG,"更新信息成功。");
						getActivity().setResult(Activity.RESULT_OK);
						getActivity().finish();
					}
	
				
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						ActivityUtil.show(getActivity(), "更新信息失败。请检查网络~");
						LogUtils.i(TAG,"更新失败1-->"+arg1);
					}
				});
		}
	}

	
	String iconUrl;
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == getActivity().RESULT_OK){
			switch (requestCode) {
			case 1:
				String files =CacheUtils.getCacheDirectory(mContext, true, "icon") + dateTime;
				File file = new File(files);
				if(file.exists()&&file.length() > 0){
					Uri uri = Uri.fromFile(file);
					startPhotoZoom(uri);
				}else{
					
				}
				break;
			case 2:
				if (data == null) {
					return;
				}
				startPhotoZoom(data.getData());
				break;
			case 3:
				if (data != null) {
					Bundle extras = data.getExtras();
					if (extras != null) {
						Bitmap bitmap = extras.getParcelable("data");
						// 锟斤拷锟斤拷图片
						iconUrl = saveToSdCard(bitmap);
						userIcon.setImageBitmap(bitmap);
					}
				}
				break;
			default:
				break;
			}
		}
	}
	
	public void showAlbumDialog(){
		albumDialog = new AlertDialog.Builder(mContext).create();
		albumDialog.setCanceledOnTouchOutside(false);
		View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_usericon, null);
		albumDialog.show();
		albumDialog.setContentView(v);
		albumDialog.getWindow().setGravity(Gravity.CENTER);
		
		
		TextView albumPic = (TextView)v.findViewById(R.id.album_pic);
		TextView cameraPic = (TextView)v.findViewById(R.id.camera_pic);
		albumPic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				albumDialog.dismiss();
				Date date1 = new Date(System.currentTimeMillis());
				dateTime = date1.getTime() + "";
				getAvataFromAlbum();
			}
		});
		cameraPic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				albumDialog.dismiss();
				Date date = new Date(System.currentTimeMillis());
				dateTime = date.getTime() + "";
				getAvataFromCamera();
			}
		});
	}
	
	private void getAvataFromCamera(){
		File f = new File(CacheUtils.getCacheDirectory(mContext, true, "icon") + dateTime);
		if (f.exists()) {
			f.delete();
		}
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Uri uri = Uri.fromFile(f);
		Log.e("uri", uri + "");
		
		Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		camera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(camera, 1);
	}
	
	private void getAvataFromAlbum(){
		Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
		intent2.setType("image/*");
		startActivityForResult(intent2, 2);
	}
	
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 锟斤拷锟斤拷锟斤拷锟絚rop=true锟斤拷锟斤拷锟斤拷锟节匡拷锟斤拷锟斤拷Intent锟斤拷锟斤拷锟斤拷锟斤拷示锟斤拷VIEW锟缴裁硷拷
		// aspectX aspectY 锟角匡拷叩谋锟斤拷锟�
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 锟角裁硷拷图片锟斤拷锟�
		intent.putExtra("outputX", 120);
		intent.putExtra("outputY", 120);
		intent.putExtra("crop", "true");
		intent.putExtra("scale", true);// 去锟斤拷锟节憋拷
		intent.putExtra("scaleUpIfNeeded", true);// 去锟斤拷锟节憋拷
		// intent.putExtra("noFaceDetection", true);//锟斤拷锟斤拷识锟斤拷
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);

	}
	
	public String saveToSdCard(Bitmap bitmap){
		String files =CacheUtils.getCacheDirectory(mContext, true, "icon") + dateTime+"_12";
		File file=new File(files);
        try {
            FileOutputStream out=new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)){
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        LogUtils.i(TAG, file.getAbsolutePath());
        return file.getAbsolutePath();
	}
	
	private Bitmap compressImageFromFile(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;//只读边,不读内容
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 800f;//
		float ww = 480f;//
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置采样率
		
		newOpts.inPreferredConfig = Config.ARGB_8888;//该模式是默认的,可不设
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收
		
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
//		return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
									//其实是无效的,大家尽管尝试
		return bitmap;
	}
}
