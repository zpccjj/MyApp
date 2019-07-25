package com.hsic.qp.sz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hsic.qp.sz.R;

import java.util.List;

import bean.QPGoods;

public class ReceiveAdapter extends BaseAdapter {
	List<QPGoods> mList;
	private Context mContext;

	public ReceiveAdapter(Context context, List<QPGoods> list){
		mContext = context;
		mList = list;
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
		convertView = inflater.inflate(R.layout.item_receive, null);

		TextView name = (TextView) convertView.findViewById(R.id.r_qp_1);
		TextView num = (TextView) convertView.findViewById(R.id.r_qp_2);

		name.setText(mList.get(position).getGoodsName()!=null ? mList.get(position).getGoodsName() : "");
		if(mList.get(position).getIsJG()==1)
			num.setText(String.valueOf(mList.get(position).getGoodsNum()) + "x" + String.valueOf(mList.get(position).getNum()) + " ƿ");
		else num.setText(String.valueOf(mList.get(position).getGoodsNum()) + " ƿ");

		return convertView;
	}
}
