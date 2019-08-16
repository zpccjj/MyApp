package com.hsic.qp.sz;

import android.app.ActionBar;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.reflect.TypeToken;
import com.hsic.qp.sz.adapter.PrintAdapter;
import com.hsic.qp.sz.listener.WsListener;
import com.hsic.qp.sz.task.PrintfTask;
import com.hsic.qp.sz.task.UploadTask;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import bean.InfoItem;
import hsic.ui.HsicActivity;
import util.ActivityUtils;
import util.PathFileUtils;
import util.ToastUtil;
import util.print.PrintQueue;

public class ActivityPrint extends HsicActivity implements WsListener{
	private final static String TITLE = "单据打印";

	static class mView{
		TextView txt;
		ListView list;
		Button printBtn;
		Button backBtn;
		Button photoBtn;
		Button uploadBtn;
		Button signBtn;

		ImageView image;
		LinearLayout printView;
	}
	mView mV;

	List<InfoItem> infoList ;
	String SaleID;
	int num = 0;
	int num2 = 0;

	private Context getContext(){
		return ActivityPrint.this;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_print);

		infoList = util.json.JSONUtils.toListWithGson(getIntent().getExtras().getString("PrintListInfo"),  new TypeToken<List<InfoItem>>(){}.getType());
		SaleID = getIntent().getExtras().getString("SaleID");


		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setTitle(TITLE);

		initViews();
		setListener();

		String btMac = getContext().getSharedPreferences("DeviceSetting", 0).getString("bluetooth_mac", "");
		if(btMac==null || btMac.length()==0)
			mV.printBtn.setEnabled(false);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		setPhotoNum();
	}

	private void initViews(){
		mV = new mView();
		mV.txt = (TextView) findViewById(R.id.print_1);
		mV.list = (ListView) findViewById(R.id.print_list);
		mV.printBtn = (Button) findViewById(R.id.print_btn);
		mV.backBtn = (Button) findViewById(R.id.print_back);
		mV.photoBtn = (Button) findViewById(R.id.print_photo);
		mV.uploadBtn = (Button) findViewById(R.id.print_upload);
		mV.signBtn = (Button) findViewById(R.id.print_sign);

		mV.image = (ImageView) findViewById(R.id.image);
		mV.printView = (LinearLayout) findViewById(R.id.print_0);

		if(BluetoothAdapter.getDefaultAdapter()==null){
			mV.printBtn.setEnabled(false);
		}
		mV.txt.setText("订单号:"+SaleID);
		mV.list.setAdapter(new PrintAdapter(getContext(), infoList));

	}

	private void setPhotoNum(){
		num = PathFileUtils.getFileList(SaleID, PathFileUtils.getPath()).size();
		mV.photoBtn.setText(getResources().getString(R.string.btn_string_24) + "("+String.valueOf(num)+")");

		List<String> filesList = PathFileUtils.getFileList(SaleID, PathFileUtils.getSignPath());
		num2 = filesList.size();
		mV.signBtn.setText(getResources().getString(R.string.btn_string_26) + "("+String.valueOf(num2)+")");

		if(filesList.size()>0){
			String signFile = PathFileUtils.getSignPath() + filesList.get(filesList.size()-1);
			Bitmap bitmap2 = BitmapFactory.decodeFile(signFile, PathFileUtils.getBitmapOption(4));
			mV.image.setImageBitmap(bitmap2);
		}
	}

	private void setListener(){
		mV.backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteSign();
				finish();
			}
		});

		mV.photoBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(SaleID==null || SaleID.length()==0){
					ToastUtil.showToast(getContext(), "无订单号");
					return ;
				}

				camera();
			}
		});

		mV.uploadBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				List<String> list = new ArrayList<String>();
				list.add(SaleID);

				boolean hasSale = false;
				boolean hasPhoto = false;
				if(num2>0){
					viewSaveToImage(mV.printView);
					hasSale = true;
				}
				if(num > 0){
					hasPhoto = true;
				}

				if(hasSale || hasPhoto){
					new UploadTask(getContext(), getApp().getLogin().getUserID(), ActivityPrint.this, hasSale, hasPhoto).execute(list);
				}else{
					ToastUtil.showToast(getContext(), "无电子销售单或照片上传");
					return ;
				}
			}
		});

		mV.printBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(SaleID==null || SaleID.length()==0){
					ToastUtil.showToast(getContext(), "无订单号");
					return ;
				}
				if(infoList==null || infoList.size()==0){
					ToastUtil.showToast(getContext(), "无打印信息");
					return ;
				}
				List<String> filesList = PathFileUtils.getFileList(SaleID, PathFileUtils.getSignPath());
				String signFile = null;
				if(filesList!=null && filesList.size()>0){
					signFile = PathFileUtils.getSignPath() + filesList.get(filesList.size()-1);
				}


				new PrintfTask(getContext(), infoList, SaleID, signFile).execute();
				//PrintTask.PrintSale(getContext(), infoList, SaleID);

			}
		});

		mV.signBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ActivityUtils.JumpToSign(getContext(), SaleID);
			}
		});
	}

	private void deleteSign(){
		if(num2>0){
			List<String> filesList = PathFileUtils.getFileList(SaleID, PathFileUtils.getSignPath());

			try {
				for (int i = 0; i < filesList.size(); i++) {
					File del = new File(PathFileUtils.getSignPath() + filesList.get(i));
					Log.e("=delete file", PathFileUtils.getSignPath() + filesList.get(i)+ ":" + String.valueOf(del.delete()));
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	private void viewSaveToImage(View view) {
		view.setDrawingCacheEnabled(true);
		view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
		view.setDrawingCacheBackgroundColor(Color.WHITE);
		Bitmap cachebmp = loadBitmapFromView(view);

		String path = PathFileUtils.getSalePath();
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		FileOutputStream fos;
		try {
			File jpg = new File(PathFileUtils.getSalePath(), SaleID+"_Sale.jpg");
			fos = new FileOutputStream(jpg);

			cachebmp.compress(Bitmap.CompressFormat.JPEG, 90, fos);

			fos.flush();
			fos.close();

			Log.e("imagePath=",jpg.getName());
		} catch (Exception e) {
			// TODO: handle exception
		}
		view.destroyDrawingCache();
	}

	private Bitmap loadBitmapFromView(View v) {
		int w = v.getWidth();
		int h = v.getHeight();

		Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bmp);

		c.drawColor(Color.WHITE);
		/** 如果不设置canvas画布为白色，则生成透明 */

		v.layout(0, 0, w, h);
		v.draw(c);

		return bmp;
	}

	private void camera(){
		String path = PathFileUtils.getPath();
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String filename = SaleID + "_" + sdf.format(new Date()) +".jpg";

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path, filename)));
		startActivityForResult(intent, 1);
	}

	//系统照相机拍摄返回
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode == 1){
			if(resultCode == RESULT_OK){
				camera();
			}
		}
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		String mac = getSharedPreferences("DeviceSetting", 0).getString("bluetooth_mac", "");
		PrintQueue.getQueue(getApplicationContext(), mac).disconnect();
		super.onDestroy();
	}

	@Override
	public void WsFinish(boolean isSuccess, int code, String retData) {
		// TODO Auto-generated method stub
		setPhotoNum();
	}

}
