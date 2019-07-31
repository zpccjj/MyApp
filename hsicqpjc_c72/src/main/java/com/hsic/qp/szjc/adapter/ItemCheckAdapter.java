package com.hsic.qp.szjc.adapter;

import java.util.List;

import com.hsic.qp.szjc.R;

import bean.ItemCheck;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

public class ItemCheckAdapter extends BaseAdapter {
	private List<ItemCheck> mList;
	private Context mContext;
	private int index = -1;

	public ItemCheckAdapter(Context context, List<ItemCheck> list){
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_check, null);
        if(mList.get(position).getViewType()==1){//EditView
        	((TableRow) convertView.findViewById(R.id.item_check_2)).setVisibility(View.VISIBLE);
        	((TextView) convertView.findViewById(R.id.item_check_3)).setText(mList.get(position).getText());
        	EditText et = (EditText) convertView.findViewById(R.id.item_check_4);
        	et.setText(mList.get(position).getValue()!=null ? mList.get(position).getValue() : "");
        	
//        	et.setOnTouchListener(new View.OnTouchListener() {
//
//				@Override
//				public boolean onTouch(View v, MotionEvent event) {
//					// TODO Auto-generated method stub
//					if (event.getAction() == MotionEvent.ACTION_UP) {
//			            index = position;
//			        }
//					return false;
//				}
//        	});
        	et.setOnFocusChangeListener(new View.OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if(hasFocus) index = position;
				}
        		
        	});
        	
        	et.addTextChangedListener(new TextWatcher(){
				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub
				}
				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// TODO Auto-generated method stub
				}
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					if(s.toString().trim().length()==0) mList.get(position).setValue(null);
					else mList.get(position).setValue(s.toString().trim());
				}});
        	
        	et.clearFocus();
        	if(index!= -1 && index == position) {
        		et.requestFocus();
        	}
        	
        }else{//CheckBox
        	CheckBox cb = (CheckBox) convertView.findViewById(R.id.item_check_1);
        	cb.setVisibility(View.VISIBLE);
        	cb.setText(mList.get(position).getText());
        	cb.setChecked(mList.get(position).isCheck());
        	cb.setOnCheckedChangeListener(new OnCheckedChangeListener(){
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					// TODO Auto-generated method stub
					mList.get(position).setCheck(isChecked);
				}
        	});
        }
		
		return convertView;
	}

}
