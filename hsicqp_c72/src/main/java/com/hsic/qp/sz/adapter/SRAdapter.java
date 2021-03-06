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

import bean.QPInfo;

public class SRAdapter extends BaseAdapter {
	private List<QPInfo> mList;
	private Context mContext;
	private int mKey;//1收 2发

	public SRAdapter(Context context, List<QPInfo> list, int key){
		mContext = context;
		mList = list;
		mKey = key;
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

		TextView tv1 = (TextView) convertView.findViewById(R.id.task_qp_1);
		TextView tv3 = (TextView) convertView.findViewById(R.id.task_qp_3);
		TextView tv2 = (TextView) convertView.findViewById(R.id.task_qp_2);
		((TextView) convertView.findViewById(R.id.task_qp_4)).setVisibility(View.GONE);

		if(mList.get(position).getCQDW()!=null && mList.get(position).getLabelNo()!=null)
			tv1.setText("标签号：" + mList.get(position).getCQDW() + mList.get(position).getLabelNo() + " "
					+ (mList.get(position).getIsJG()==1 ? "集格瓶" : "散瓶"));
		else tv1.setText("标签号：" );
		tv3.setText(mList.get(position).getIsByHand()==1 ? "手动输入" : "自动扫描");


//		if(mKey==1){
//			tv2.setText(mList.get(position).getMsg()!=null ? mList.get(position).getMsg() : "" ) ;
//			//		+ (mList.get(position).getOpType()==9 ? " ,非本站气瓶" : " ,本站气瓶"));
//		}else{
//			tv2.setText( mList.get(position).getMsg()!=null ? mList.get(position).getMsg() : "" );
//		}

		tv2.setText( (mList.get(position).getMsg()!=null ? mList.get(position).getMsg() : "") );
		if(mList.get(position).getColor()==0){
			tv2.setTextColor(Color.rgb(0, 0, 255));
		}else if(mList.get(position).getColor()==2){
			tv2.setTextColor(Color.rgb(255, 0, 0));
		}

		return convertView;

	}

}
