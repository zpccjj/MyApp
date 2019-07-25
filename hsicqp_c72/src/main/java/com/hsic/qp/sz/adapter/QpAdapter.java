package com.hsic.qp.sz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hsic.qp.sz.R;

import java.util.List;

import bean.InfoItem;

public class QpAdapter extends BaseAdapter {
	List<InfoItem> mList;
	private Context mContext;

	public QpAdapter(Context context, List<InfoItem> list){
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
		convertView = inflater.inflate(R.layout.item_qp, null);

		TextView key = (TextView) convertView.findViewById(R.id.task_qp_1);
		TextView value = (TextView) convertView.findViewById(R.id.task_qp_2);
		TextView name = (TextView) convertView.findViewById(R.id.task_qp_3);
		TextView value2 = (TextView) convertView.findViewById(R.id.task_qp_4);

		key.setText(mList.get(position).getKey()!=null ? mList.get(position).getKey() : "");
		value.setText(mList.get(position).getValue()!=null ? mList.get(position).getValue() : "");
		name.setText(mList.get(position).getName()!=null ? mList.get(position).getName() : "");
		value2.setText(mList.get(position).getValue2()!=null ? mList.get(position).getValue2() : "");

		if(mList.get(position).getName()!=null && mList.get(position).getName().equals("-")){
			name.setVisibility(View.GONE);
		}
		if(mList.get(position).getValue()!=null && mList.get(position).getValue().equals("-")){
			value.setVisibility(View.GONE);
			value2.setVisibility(View.GONE);
		}

		return convertView;

	}

}
