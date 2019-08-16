package com.hsic.qp.sz.adapter;

import java.util.List;

import com.hsic.qp.sz.R;

import data.ConfigData;

import bean.Rfid;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RfidCheckAdapter extends BaseAdapter {
    List<Rfid> mList;
    private Context mContext;

    public RfidCheckAdapter(Context context, List<Rfid> list){
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
        convertView = inflater.inflate(R.layout.item_rfid_check, null);

        TextView LabelNo = (TextView) convertView.findViewById(R.id.rfid_check_item_1);
        TextView MediumName = (TextView) convertView.findViewById(R.id.rfid_check_item_2);
        TextView NextCheckDate = (TextView) convertView.findViewById(R.id.rfid_check_item_3);
        TextView Overdue = (TextView) convertView.findViewById(R.id.rfid_check_item_4);
        TextView Dm = (TextView) convertView.findViewById(R.id.rfid_check_item_5);
        TextView JG = (TextView) convertView.findViewById(R.id.rfid_check_item_6);

        LabelNo.setText("标签号:" + (mList.get(position).getQPDJCode()!=null ? mList.get(position).getQPDJCode() : ""));
        MediumName.setText("充装介质:"+ (mList.get(position).getMediumName()!=null ? mList.get(position).getMediumName() : ""));
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

        if(mList.get(position).getFaultDm()!=null && mList.get(position).getFaultDm().length()!=0){
            Dm.setText("检验结果: "+ getResult(mList.get(position).getFaultDm()));
            if(!mList.get(position).getFaultDm().equals("0")){
                Dm.setTextColor(android.graphics.Color.RED);
            }
        }else{
            Dm.setText("检验结果:");
        }
        if(mList.get(position).getIsJG()==1) JG.setText("集格瓶");
        else JG.setText("散瓶");

        return convertView;

    }

    private String getResult(String code){
        String ret = "";

        try {
            int id = Integer.valueOf(code);
            String[] dm = mContext.getResources().getStringArray(R.array.checkdm);

            ret = dm[id];
        } catch (Exception e) {
            // TODO: handle exception
        }

        return ret;
    }

}
