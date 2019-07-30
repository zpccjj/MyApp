package com.hsic.qp.sz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hsic.qp.sz.R;

import java.util.List;

import bean.ItemQpInfo;

public class QpInfoAdapter extends BaseAdapter {
    List<ItemQpInfo> mList;
    private Context mContext;

    public QpInfoAdapter(Context context, List<ItemQpInfo> list){
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
        convertView = inflater.inflate(R.layout.item_qpinfo, null);

        ((TextView) convertView.findViewById(R.id.sb_item_1)).setText(mList.get(position).getGoodsName());
        ((TextView) convertView.findViewById(R.id.sb_item_2)).setText(mList.get(position).getSendNum());
        ((TextView) convertView.findViewById(R.id.sb_item_3)).setText(mList.get(position).getReceiveNum());

        return convertView;
    }}
