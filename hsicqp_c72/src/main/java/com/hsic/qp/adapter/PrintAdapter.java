package com.hsic.qp.adapter;

import java.util.List;

import com.hsic.qp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import bean.InfoItem;

public class PrintAdapter extends BaseAdapter {
	List<InfoItem> mList;
	private Context mContext;

	public PrintAdapter(Context context, List<InfoItem> list){
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
		if(convertView==null){
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_print, null);
		}

		TextView txt1 = (TextView) convertView.findViewById(R.id.print_item_1);
		TextView txt2 = (TextView) convertView.findViewById(R.id.print_item_2);
		TextView txt3 = (TextView) convertView.findViewById(R.id.print_item_3);

		//key -1：--------，0：换行，1：单列左对齐， 2：双列Name左对齐 Value右对齐，3：三列 Name Value Value2
		//android:layout_height="0.5dp" android:layout_marginTop="3dp" android:background="#000000"
		if(mList.get(position).getKey()==null) return null;
		else{
			if(mList.get(position).getKey().equals("-1")){
				txt1.setText("----------");
				txt2.setVisibility(View.GONE);
				txt3.setVisibility(View.GONE);
			}else if(mList.get(position).getKey().equals("0")){
				txt1.setText(" ");
				txt2.setVisibility(View.GONE);
				txt3.setVisibility(View.GONE);
			}else if(mList.get(position).getKey().equals("1")){
				txt1.setText(mList.get(position).getName()!=null ? mList.get(position).getName() : "");
				txt2.setVisibility(View.GONE);
				txt3.setVisibility(View.GONE);
			}else if(mList.get(position).getKey().equals("2")){
				txt1.setText(mList.get(position).getName()!=null ? mList.get(position).getName() : "");
				txt2.setText(mList.get(position).getValue()!=null ? mList.get(position).getValue() : "");
				txt3.setVisibility(View.GONE);
			}else if(mList.get(position).getKey().equals("3")){
				txt1.setText(mList.get(position).getName()!=null ? mList.get(position).getName() : "");
				txt2.setText(mList.get(position).getValue()!=null ? mList.get(position).getValue() : "");
				txt3.setText(mList.get(position).getValue2()!=null ? mList.get(position).getValue2() : "");
			}else return null;

			return convertView;
		}

	}
}
