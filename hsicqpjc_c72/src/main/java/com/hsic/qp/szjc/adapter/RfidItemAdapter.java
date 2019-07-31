package com.hsic.qp.szjc.adapter;

import java.util.List;

import com.hsic.qp.szjc.R;

import bean.RfidItem;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RfidItemAdapter extends BaseAdapter {
	private List<RfidItem> mList;
	private Context mContext;

	public RfidItemAdapter(Context context, List<RfidItem> list){
		this.mContext = context;
		this.mList = list;
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
		convertView = inflater.inflate(R.layout.item_rfid, null);

		((TextView) convertView.findViewById(R.id.item_rfid_1)).setText("标签号:"+mList.get(position).getQPDJCODE());
		((TextView) convertView.findViewById(R.id.item_rfid_3)).setText("气瓶号:"+mList.get(position).getGNO());
		TextView tv4 = (TextView) convertView.findViewById(R.id.item_rfid_4);
		TextView tv2 = (TextView) convertView.findViewById(R.id.item_rfid_2);
		tv2.setText(mList.get(position).getMSG());
		if(mList.get(position).getColor()==1){
			tv2.setTextColor(Color.rgb(255, 0, 0));
			tv4.setText("制造日期:"+mList.get(position).getNEXTCHECKDATE());
		}else if(mList.get(position).getColor()==2){
			tv2.setTextColor(Color.rgb(0, 0, 255));
			tv4.setText("下检日期:"+mList.get(position).getNEXTCHECKDATE());
		}else if(mList.get(position).getColor()==9){
			tv4.setText("报废");
		}else{
			tv4.setText("下检日期:"+mList.get(position).getNEXTCHECKDATE());
		}

		return convertView;
	}


}
