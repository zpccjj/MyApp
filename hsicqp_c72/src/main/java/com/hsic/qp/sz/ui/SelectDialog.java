package com.hsic.qp.sz.ui;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.hsic.qp.sz.R;
import java.util.ArrayList;
import java.util.List;
import bean.QPGoods;

public class SelectDialog extends Dialog{
    private Button button_close;//
    private EditText search;
    private ListView lv;

    List<QPGoods> mList = new ArrayList<QPGoods>();
    SelectAdapter selectAdapter;

    public SelectDialog(Context context, final EditText editText, final List<QPGoods> selectGoods, final QPGoods nowGood) {
        super(context);
        // TODO Auto-generated constructor stub

        this.setContentView(R.layout.diag_select);
        this.setCancelable(false);
        this.setTitle("选择商品");
        this.mList.addAll(selectGoods);
        button_close = (Button) findViewById(R.id.select_close);
        search = (EditText) findViewById(R.id.select_search);
        lv = (ListView) findViewById(R.id.select_lv);

        selectAdapter = new SelectAdapter(context, mList);
        lv.setAdapter(selectAdapter);

        button_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                SelectDialog.this.dismiss();
            }
        });

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                mList.clear();
                s.toString().trim();
                for (int i = 0; i < selectGoods.size(); i++) {
                    if(selectGoods.get(i).getGoodsName().contains(s.toString().trim())){
                        mList.add(selectGoods.get(i));
                    }
                }
                selectAdapter.notifyDataSetChanged();
            }
        });

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                nowGood.setCZJZ(mList.get(position).getCZJZ());
                nowGood.setGoodsCode(mList.get(position).getGoodsCode());
                nowGood.setGoodsName(mList.get(position).getGoodsName());
                nowGood.setGoodsNum(mList.get(position).getGoodsNum());
                nowGood.setIsDelete(mList.get(position).getIsDelete());
                nowGood.setIsJG(mList.get(position).getIsJG());
                nowGood.setMediumCode(mList.get(position).getMediumCode());
                nowGood.setNum(mList.get(position).getNum());

                editText.setText(mList.get(position).getGoodsName());

                SelectDialog.this.dismiss();
            }
        });

    }

}
