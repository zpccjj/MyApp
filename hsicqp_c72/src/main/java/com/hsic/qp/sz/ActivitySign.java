package com.hsic.qp.sz;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hsic.qp.sz.listener.WsListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import bll.MyApplication;
import hsic.ui.SignatureView;
import util.PathFileUtils;
import util.ToastUtil;

public class ActivitySign extends Activity implements WsListener{
    private SignatureView mSignaturePad;
    private Button mClearButton;
    private Button mSaveButton;

    private String SaleID;

    private Context getContext(){
        return ActivitySign.this;
    }

    private MyApplication getApp(){
        return (MyApplication) super.getApplication();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign);

        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("客户签字");

        SaleID = getIntent().getExtras().getString("SaleID", "");

        mSignaturePad = (SignatureView) findViewById(R.id.signature_pad);

        mSignaturePad.setBackgroundResource(R.drawable.a);

        mSignaturePad.setOnSignedListener(new SignatureView.OnSignedListener() {
            @Override
            public void onSigned() {
                mSaveButton.setEnabled(true);
                mClearButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                mSaveButton.setEnabled(false);
                mClearButton.setEnabled(false);
            }
        });

        mClearButton = (Button) findViewById(R.id.clear_button);
        mSaveButton = (Button) findViewById(R.id.save_button);

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePad.clear();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                if (addSignatureToGallery(signatureBitmap)) {
                    //	ToastUtil.showToast(getContext(), "已保成功");
                    List<String> list = new ArrayList<String>();
                    list.add(SaleID);
                    //	new UploadTask(getContext(), getApp().getLogin().getUserID(), ActivitySign.this, true).execute(list);

                    finish();
                } else {
                    ToastUtil.showToast(getContext(), "已保失败");
                }
            }
        });
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    public boolean addSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
            String path = PathFileUtils.getSignPath();
            File file = new File(path);
            if(!file.exists()){
                file.mkdirs();
            }

//			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//	        String filename = SaleID + "_" + sdf.format(new Date()) +".jpg";
            String filename = SaleID +"_sign.jpg";
            File photo = new File(path + filename);
            saveBitmapToJPG(signature, photo);
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(photo);
            mediaScanIntent.setData(contentUri);
            getContext().sendBroadcast(mediaScanIntent);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void WsFinish(boolean isSuccess, int code, String retData) {
        // TODO Auto-generated method stub
        if(isSuccess){
            finish();
        }else{

        }
    }
}
