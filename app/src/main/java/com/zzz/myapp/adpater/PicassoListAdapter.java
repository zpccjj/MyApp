package com.zzz.myapp.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zzz.myapp.R;

public class PicassoListAdapter extends ArrayAdapter {
    private Context mContext;

    private String[] mImageUrls;

    public PicassoListAdapter(Context context, String[] imageUrls) {
        super(context, R.layout.picasso_adapter,imageUrls);
        this.mContext = context;
        this.mImageUrls = imageUrls;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = View.inflate(mContext,R.layout.picasso_adapter,null);
        }
        ImageView imageView = (ImageView)convertView;
        if (TextUtils.isEmpty(mImageUrls[position])){
            Picasso.get().cancelRequest(imageView);
            imageView.setImageDrawable(null);
        }else{
            Picasso
                    .get()
                    .load(mImageUrls[position])
                    //.memoryPolicy(MemoryPolicy.NO_CACHE)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    //.centerCrop()
                    //.fit()
                    .into((ImageView) convertView);
        }
        //加载图片
//        Picasso.get().load(mImageUrls[position]).into((ImageView) convertView);

        return convertView;
    }
}
