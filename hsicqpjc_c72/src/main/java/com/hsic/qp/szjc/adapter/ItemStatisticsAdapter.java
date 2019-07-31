package com.hsic.qp.szjc.adapter;

import java.util.List;

import com.hsic.qp.szjc.R;

import bean.StatisticsItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ItemStatisticsAdapter extends BaseAdapter {
	private List<StatisticsItem> mList;
	private Context mContext;
	
	public ItemStatisticsAdapter(Context context, List<StatisticsItem> list){
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
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_statistics, null);
        
        ((TextView) convertView.findViewById(R.id.statistics_item_1)).setText(mList.get(position).getCheckDate());
        ((TextView) convertView.findViewById(R.id.statistics_item_2)).setText(mList.get(position).getNum());
        ((TextView) convertView.findViewById(R.id.statistics_item_3)).setText(mList.get(position).getUploaded());
        ((TextView) convertView.findViewById(R.id.statistics_item_4)).setText(mList.get(position).getNotUploaded());
        
		return convertView;
	}
}
