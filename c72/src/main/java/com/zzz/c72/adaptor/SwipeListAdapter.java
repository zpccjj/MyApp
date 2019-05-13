package com.zzz.c72.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzz.c72.R;

import java.util.List;
import java.util.Set;

import app.bean.RfidData;
import app.ui.MyOnSlipStatusListener;
import app.ui.SwipeListLayout;

public class SwipeListAdapter extends BaseAdapter {
    Context mContext;
    List<RfidData> mList;
    Set<SwipeListLayout> mSets;
    boolean mChangeStatus;//是否点击执行侧滑
    public SwipeListAdapter(Context context, List<RfidData> list, Set<SwipeListLayout> sets, boolean changeStatus) {
        mContext = context;
        mList = list;
        mSets = sets;
        mChangeStatus = changeStatus;
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(final int arg0, View view, ViewGroup arg2) {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.item_slip_list, null);
        }
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        TextView tv_num = (TextView) view.findViewById(R.id.tv_num);
        tv_name.setText(mList.get(arg0).getEPC());
        tv_num.setText(String.valueOf(mList.get(arg0).getNUM()));
        LinearLayout slip_item = (LinearLayout) view.findViewById(R.id.slip_item);
        final SwipeListLayout sll_main = (SwipeListLayout) view
                .findViewById(R.id.sll_main);
        TextView tv_top = (TextView) view.findViewById(R.id.tv_top);
        TextView tv_delete = (TextView) view.findViewById(R.id.tv_delete);
        sll_main.setOnSwipeStatusListener(new MyOnSlipStatusListener(sll_main, mSets));

//        tv_top.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sll_main.setStatus(SwipeListLayout.Status.Close, true);
//                RfidData rfid = mList.get(arg0);
//                mList.remove(arg0);
//                mList.add(0, rfid);
//                notifyDataSetChanged();
//                mSets.clear();
//            }
//        });
        tv_top.setVisibility(View.GONE);
        tv_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                sll_main.setStatus(SwipeListLayout.Status.Close, true);
                mList.remove(arg0);
                notifyDataSetChanged();
                mSets.clear();
            }
        });

        if(mChangeStatus) {
            slip_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sll_main.changeStatus(true);
                    notifyDataSetChanged();
                }
            });
        }

        return view;
    }
}
