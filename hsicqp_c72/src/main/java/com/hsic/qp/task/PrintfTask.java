package com.hsic.qp.task;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;

import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import bean.InfoItem;
import bean.ResponseData;
import util.PrintUtils;
import util.SaleCode;

public class PrintfTask extends AsyncTask<String, Void, ResponseData> {
	ProgressDialog dialog;
	Context mContext;
	List<InfoItem> mData;
	String SaleID;
	public PrintfTask(Context context, List<InfoItem> data, String saleid){
		this.mContext = context;
		this.mData = data;
		this.SaleID = saleid;
	}

	@Override
	protected ResponseData doInBackground(String... params) {
		// TODO Auto-generated method stub
		ResponseData res = new ResponseData();

		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(!bluetoothAdapter.isEnabled()){
			res.setRespCode(1);
			res.setRespMsg("蓝牙未打开");
			return res;
		}

		UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
		BluetoothSocket bluetoothSocket;
		OutputStream outputStream;
		//连接
		try {
			String mac = mContext.getSharedPreferences("DeviceSetting", 0).getString("bluetooth_mac", "");
			BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(mac);
			bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid);
			bluetoothSocket.connect();
			outputStream = bluetoothSocket.getOutputStream();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			res.setRespCode(1);
			res.setRespMsg("连接打印设备异常");
			return res;
		}

		//打印
		try {
			PrintUtils.setOutputStream(outputStream);
			PrintUtils.selectCommand(PrintUtils.RESET);
			PrintUtils.selectCommand(PrintUtils.LINE_SPACING_DEFAULT);
			PrintUtils.selectCommand(PrintUtils.ALIGN_CENTER);
			PrintUtils.selectCommand(PrintUtils.BOLD);
			PrintUtils.printText("申中工业气体销售单据\n\n");
			PrintUtils.selectCommand(PrintUtils.BOLD_CANCEL);

			PrintUtils.selectCommand(SaleCode.getSaleCode(SaleID));
			PrintUtils.printText("订单号:"+SaleID+"\n\n");

			PrintUtils.selectCommand(PrintUtils.NORMAL);
			PrintUtils.selectCommand(PrintUtils.ALIGN_LEFT);

			for (int i = 0; i < mData.size(); i++) {
				if(mData.get(i)!=null && mData.get(i).getKey()!=null && mData.get(i).getKey().length()>0){
					if(mData.get(i).getKey().equals("-1")){
						PrintUtils.printText("--------------------------------\n");
					}else if(mData.get(i).getKey().equals("0")){
						PrintUtils.printText("\n");
					}else if(mData.get(i).getKey().equals("1")){
						PrintUtils.printText(mData.get(i).getName() + "\n");
					}else if(mData.get(i).getKey().equals("2")){
						PrintUtils.printText(PrintUtils.printTwoData(mData.get(i).getName(), mData.get(i).getValue()+"\n"));
					}else if(mData.get(i).getKey().equals("3")){
						PrintUtils.printText(PrintUtils.printThreeData(mData.get(i).getName(), mData.get(i).getValue(), mData.get(i).getValue2()+"\n"));
					}
				}
				//PrintUtils.printText(PrintUtils.printTwoData(mData.get(i).getName(), mData.get(i).getValue()+"\n"));
				//PrintUtils.printText(mData.get(i).getName() + mData.get(i).getValue()+"\n");
			}
			PrintUtils.printText("\n");
			PrintUtils.printText("客户签字:\n\n\n\n\n\n");

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			res.setRespCode(1);
			res.setRespMsg("打印出现异常");
			return res;
		}

		//关闭
		try {
			if (bluetoothSocket != null) {
				bluetoothSocket.close();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if (outputStream != null) {
				outputStream.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		res.setRespCode(0);
		res.setRespMsg("打印完成");
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
