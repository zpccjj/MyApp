package com.hsic.qp.sz.adapter;

import java.util.List;

import com.hsic.qp.sz.R;

import bean.SubmitQP;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SubmitQPAdapter extends BaseAdapter {
	List<SubmitQP> mList;
	private Context mContext;
	
	public SubmitQPAdapter(Context context, List<SubmitQP> list){
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
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_qpinfo3, null);
        
        ((TextView) convertView.findViewById(R.id.item_qpinfo3_0)).setText(String.valueOf((position+1)));
        ((TextView) convertView.findViewById(R.id.item_qpinfo3_2)).setText(mList.get(position).getGPNO());
		
		return convertView;
	}
}
