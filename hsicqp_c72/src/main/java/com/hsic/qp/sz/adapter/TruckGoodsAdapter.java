package com.hsic.qp.sz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hsic.qp.sz.R;

import java.util.List;

import bean.TruckGoods;

public class TruckGoodsAdapter extends BaseAdapter {
    private List<TruckGoods> mList;
    private Context mContext;
    private int mIO;

    public TruckGoodsAdapter(Context context, List<TruckGoods> list, int IO) {
        this.mList = list;
        this.mContext = context;
        this.mIO = IO;
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
        convertView = inflater.inflate(R.layout.item_truckgoods, null);

        TextView name = (TextView) convertView.findViewById(R.id.truckgoods_item_1);
        TextView num1 = (TextView) convertView.findViewById(R.id.truckgoods_item_2);
        TextView num2 = (TextView) convertView.findViewById(R.id.truckgoods_item_3);

        name.setText(mList.get(position).getGoodsName());
        String txt = "";
        if(mList.get(position).getIsJG()==1){
            txt = " x" + String.valueOf(mList.get(position).getNum());
        }
        if(mIO==0){
            num2.setVisibility(View.GONE);
            num1.setText("瓶数:"+String.valueOf(mList.get(position).getGoodsNum()) + txt);
        }else{
            num1.setText("满瓶:"+String.valueOf(mList.get(position).getGoodsNum()) + txt);
            num2.setText("空瓶:"+String.valueOf(mList.get(position).getEmptyNum()) + txt);
        }

        return convertView;
    }

}
