package com.hsic.qp.sz.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hsic.qp.sz.R;

import java.util.List;

import bean.Rfid;
import data.ConfigData;

public class RfidAdapter extends BaseAdapter {
	List<Rfid> mList;
	private Context mContext;

	public RfidAdapter(Context context, List<Rfid> list){
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
		convertView = inflater.inflate(R.layout.item_rfid, null);

		TextView LabelNo = (TextView) convertView.findViewById(R.id.rfid_item_1);
		TextView MediumName = (TextView) convertView.findViewById(R.id.rfid_item_2);
		TextView NextCheckDate = (TextView) convertView.findViewById(R.id.rfid_item_3);
		TextView Overdue = (TextView) convertView.findViewById(R.id.rfid_item_4);

		LabelNo.setText("标签号:" + (mList.get(position).getQPDJCode()!=null ? mList.get(position).getQPDJCode() : "")
				+ (mList.get(position).getIsJG()==1? "   集格瓶" : "   散瓶") );

		String txt = "充装介质:"+ (mList.get(position).getMediumName()!=null ? mList.get(position).getMediumName() : "");
		if(mList.get(position).getGoodsName()!=null && mList.get(position).getGoodsName().length()>0){
			txt += ",  最新充装:" + mList.get(position).getGoodsName();
		}
		MediumName.setText(txt);
		if(mList.get(position).getColor()==0){
			MediumName.setTextColor(Color.rgb(0, 0, 255));
		}else if(mList.get(position).getColor()==2){
			MediumName.setTextColor(Color.rgb(255, 0, 0));
		}

		NextCheckDate.setText("下次检验日期:" + (mList.get(position).getNextCheckDate()!=null ? mList.get(position).getNextCheckDate() : ""));

		if(mList.get(position).getNextCheckDate()!=null && mList.get(position).getNextCheckDate().length()==4){
			int ret = ConfigData.IsOverdue(mList.get(position).getNextCheckDate());
			if(ret==ConfigData.OVERDUE){
				Overdue.setText(mContext.getResources().getString(R.string.txt_home_11));
				Overdue.setTextColor(android.graphics.Color.RED);
			}if(ret==ConfigData.FORTHCOMING){
				Overdue.setText(mContext.getResources().getString(R.string.txt_home_12));
				Overdue.setTextColor(android.graphics.Color.BLUE);
			}
		}

		return convertView;

	}

}
