package com.hsic.qp.sz.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hsic.qp.sz.R;

import java.util.List;

import bean.QPGoods;

public class SelectAdapter extends BaseAdapter {
    private List<QPGoods> mList;
    private Context mContext;

    public SelectAdapter(Context context, List<QPGoods> list) {
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
                .getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_select, null);

        ((TextView) convertView.findViewById(R.id.select_item_1)).setText(mList.get(position).getGoodsName());
//        if(mList.get(position).getIsJG()==1){
//        	((TextView) convertView.findViewById(R.id.select_item_2)).setText("集格 x"+String.valueOf(mList.get(position).getNum()));
//        }else{
//        	((TextView) convertView.findViewById(R.id.select_item_2)).setText("散瓶");
//        }

        return convertView;
    }


}
