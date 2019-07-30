package com.hsic.qp.sz.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import bean.InfoItem;
import bean.ResponseData;
import util.PathFileUtils;
import util.print.Barcode;
import util.print.GPrinterCommand;
import util.print.PrintPic;
import util.print.PrintQueue;
import util.print.PrintUtils;
import util.print.PrinterUtils;

public class PrintfTask extends AsyncTask<String, Void, ResponseData> {
	ProgressDialog dialog;
	Context mContext;
	List<InfoItem> mData;
	String SaleID;
	String SignFile;

	public PrintfTask(Context context, List<InfoItem> data, String saleid, String signfile){
		this.mContext = context;
		this.mData = data;
		this.SaleID = saleid;
		this.SignFile = signfile;
	}

	@Override
	protected ResponseData doInBackground(String... params) {
		// TODO Auto-generated method stub
		ResponseData res = new ResponseData();

		String mac = mContext.getSharedPreferences("DeviceSetting", 0).getString("bluetooth_mac", "");
		try {
			Bitmap bitmap = Barcode.getBarcodeBitmap(SaleID, 400, 100);
			PrintPic printPic = PrintPic.getInstance();
			printPic.init(bitmap);
			if (null != bitmap) {
				if (bitmap.isRecycled()) {
					bitmap = null;
				} else {
					bitmap.recycle();
					bitmap = null;
				}
			}
			byte[] bytes = printPic.printDraw();


			PrinterUtils.initPrinter();
			PrinterUtils.feedPaperCutPartial();

			ArrayList<byte[]> printBytes = new ArrayList<byte[]>();
			for (int n = 0; n < 2; n++) {
				printBytes.add(GPrinterCommand.reset);
				printBytes.add(GPrinterCommand.center);
				printBytes.add(GPrinterCommand.bold);
				printBytes.add(GPrinterCommand.print);
				printBytes.add(PrintUtils.test("申中工业气体销售单据"));
				printBytes.add(GPrinterCommand.print);
				printBytes.add(GPrinterCommand.print);
				printBytes.add(GPrinterCommand.bold_cancel);
				printBytes.add(bytes);
				printBytes.add(GPrinterCommand.print);
				printBytes.add(GPrinterCommand.print);
				printBytes.add(GPrinterCommand.left);
				printBytes.add(GPrinterCommand.text_normal_size);
				printBytes.add(PrintUtils.test("订单号:"+SaleID));
				printBytes.add(GPrinterCommand.print);

				for (int i = 0; i < mData.size(); i++) {
					if(mData.get(i)!=null && mData.get(i).getKey()!=null && mData.get(i).getKey().length()>0){
						if(mData.get(i).getKey().equals("-1")){
							printBytes.add(PrintUtils.test("--------------------------------"));
							printBytes.add(GPrinterCommand.print);
						}else if(mData.get(i).getKey().equals("0")){
							printBytes.add(GPrinterCommand.print);
						}else if(mData.get(i).getKey().equals("1")){
							printBytes.add(PrintUtils.test(mData.get(i).getName()));
							printBytes.add(GPrinterCommand.print);
							PrintUtils.printText(mData.get(i).getName() + "\n");
						}else if(mData.get(i).getKey().equals("2")){
							if(mData.get(i).getName().length()>8){
								printBytes.add(PrintUtils.test(mData.get(i).getName()));
								printBytes.add(GPrinterCommand.print);
								printBytes.add(PrintUtils.test(PrintUtils.printTwoData("", mData.get(i).getValue())));
								printBytes.add(PrintUtils.test(PrintUtils.printTwoData(mData.get(i).getName(), mData.get(i).getValue())));
							}
							printBytes.add(GPrinterCommand.print);
						}else if(mData.get(i).getKey().equals("3")){
							if(mData.get(i).getName().length()>8){
								printBytes.add(PrintUtils.test(mData.get(i).getName()));
								printBytes.add(GPrinterCommand.print);
								printBytes.add(PrintUtils.test(PrintUtils.printThreeData("", mData.get(i).getValue(), mData.get(i).getValue2())));
							}else{
								printBytes.add(PrintUtils.test(PrintUtils.printThreeData(mData.get(i).getName(), mData.get(i).getValue(), mData.get(i).getValue2())));
							}

							printBytes.add(GPrinterCommand.print);
						}
					}
				}
				printBytes.add(GPrinterCommand.print);
				printBytes.add(PrintUtils.test("客户签字:"));
				if(SignFile==null && SignFile.length()>0){
					printBytes.add(GPrinterCommand.print);
					printBytes.add(GPrinterCommand.print);
					printBytes.add(GPrinterCommand.print);
					printBytes.add(GPrinterCommand.print);
					printBytes.add(GPrinterCommand.print);
				}else{
					try {
						Bitmap bitmap2 = BitmapFactory.decodeFile(SignFile, PathFileUtils.getBitmapOption(4));
						PrintPic printPic2 = PrintPic.getInstance();
						printPic2.init(bitmap2);
						if (null != bitmap2) {
							if (bitmap2.isRecycled()) {
								bitmap2 = null;
							} else {
								bitmap2.recycle();
								bitmap2 = null;
							}
						}
						byte[] bytes2 = printPic2.printDraw();
						printBytes.add(GPrinterCommand.print);
						printBytes.add(bytes2);
						printBytes.add(GPrinterCommand.print);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						printBytes.add(GPrinterCommand.print);
						printBytes.add(GPrinterCommand.print);
						printBytes.add(GPrinterCommand.print);
						printBytes.add(GPrinterCommand.print);
						printBytes.add(GPrinterCommand.print);
					}

				}
				printBytes.add(PrintUtils.test("--------------------------------"));
			}

			PrintQueue.getQueue(mContext, mac).add(printBytes);
		} catch (Exception ex) {
			ex.printStackTrace();
			res.setRespCode(1);
			res.setRespMsg("打印异常");
			return res;
		}

		res.setRespCode(0);
		res.setRespMsg("打印发送");
		return res;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog = new ProgressDialog(mContext);
		dialog.setMessage("正在打印销售单据...");
		dialog.setCancelable(false);
		dialog.show();
	}

	@Override
	protected void onPostExecute(ResponseData ret) {
		super.onPostExecute(ret);
		dialog.setCancelable(true);
		if(ret.getRespCode()==0)
			dialog.dismiss();
		else{
			dialog.setMessage(ret.getRespMsg());
		}
	}
}
