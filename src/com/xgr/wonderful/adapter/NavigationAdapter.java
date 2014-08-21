package com.xgr.wonderful.adapter;
import com.xgr.wonderful.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NavigationAdapter extends BaseAdapter{
	
	private String[] titles ;
	private int[] icons = {R.drawable.ic_navi_settings,R.drawable.ic_navi_feedback,R.drawable.ic_navi_intro,R.drawable.ic_navi_about};
	private Context mContext;

	public NavigationAdapter(String[] titles, Context mContext) {
		super();
		this.titles = titles;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return titles.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return titles[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder ;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.navi_item, null);
			viewHolder.title = (TextView)convertView.findViewById(R.id.navi_menu_item);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.title.setText(titles[position]);
		viewHolder.title.setCompoundDrawablesWithIntrinsicBounds(icons[position], 0, 0, 0);
		return convertView;
	}

	class ViewHolder{
		TextView title;
	}

}
