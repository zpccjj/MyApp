package com.hsic.qp.sz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hsic.qp.sz.R;

import java.util.List;

import bean.Sale;
import bean.SaleDetail;

public class TaskAdapter extends BaseAdapter {
	private List<Sale> mList;
	private Context mContext;

	public TaskAdapter(Context context, List<Sale> list){
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
		convertView = inflater.inflate(R.layout.item_task, null);

		TextView id = (TextView) convertView.findViewById(R.id.task_item_0);
		TextView name = (TextView) convertView.findViewById(R.id.task_item_1);
		TextView phone = (TextView) convertView.findViewById(R.id.task_item_2);
		TextView address = (TextView) convertView.findViewById(R.id.task_item_3);
		TextView txt = (TextView) convertView.findViewById(R.id.task_item_4);

		id.setText(mList.get(position).getSaleID()!=null ? mList.get(position).getSaleID() : "");
		name.setText(mList.get(position).getCustomerName()!=null ? mList.get(position).getCustomerName() : "" );
		phone.setText(mList.get(position).getCustomerTelephone()!=null ? mList.get(position).getCustomerTelephone() : "");
		String add = mList.get(position).getAddress()!=null ? mList.get(position).getAddress() : "";
		if(mList.get(position).getPS()!=null && mList.get(position).getPS().length()>0) add+= "("+mList.get(position).getPS()+")";
		address.setText(add);
		txt.setText(getNum(mList.get(position).getSaleDetail()));

		return convertView;
	}

	private String getNum(List<SaleDetail> list){
		String txt = "";

		if(list!=null && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				if(list.get(i)!=null && list.get(i).getGoodsName()!=null && list.get(i).getGoodsName().length()>0 && list.get(i).getPlanSendNum()>0){
					if(txt.length()==0) txt += list.get(i).getGoodsName() ;
					else txt += ", " + list.get(i).getGoodsName() ;

					if(list.get(i).getGoodsType()==6){
						txt += ":"+list.get(i).getGoodsPrice().toString()+"Ԫ";
					}else if(list.get(i).getGoodsType()==1){
						if(list.get(i).getIsJG()==1){
							txt += ":"+String.valueOf(list.get(i).getPlanSendNum())+ "x" + String.valueOf(list.get(i).getNum()) + "ƿ";
						}else
							txt += ":"+String.valueOf(list.get(i).getPlanSendNum()) + "ƿ";
					}
				}
			}
		}

		return txt;
	}
}
