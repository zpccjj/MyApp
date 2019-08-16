package com.hsic.qp.sz.adapter;

import java.util.List;

import com.hsic.qp.sz.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import bean.GasBaseInfo;

public class ItemReadAdapter extends BaseAdapter {
	List<GasBaseInfo> mList;
	private Context mContext;

	public ItemReadAdapter(Context context, List<GasBaseInfo> list){
		this.mList = list;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(mList!=null && mList.size()>0) return mList.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(mList!=null && mList.size()>0)
			return mList.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		if(mList!=null && mList.size()>0)
			return position;
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.item_read, null);

		((TextView) convertView.findViewById(R.id.item_read_1)).setText("瓶号:"+ (mList.get(position).getGPNO()!=null ? mList.get(position).getGPNO() : ""));
		((TextView) convertView.findViewById(R.id.item_read_2)).setText("制造日期:"+ (mList.get(position).getMakeDate()!=null ? mList.get(position).getMakeDate() : ""));

		return convertView;
	}
}
