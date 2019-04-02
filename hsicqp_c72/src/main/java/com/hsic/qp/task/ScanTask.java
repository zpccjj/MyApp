package com.hsic.qp.task;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.rscja.deviceapi.RFIDWithUHF;

import util.RfidUtils;


public class ScanTask  extends AsyncTask<RFIDWithUHF, Void, String> {
	Handler mHandler;
	public ScanTask(Handler handler){
		mHandler = handler;
	}

	@Override
	protected String doInBackground(RFIDWithUHF... params) {
		// TODO Auto-generated method stubparams[0].setEPCTIDMode(true);
		//
		//		params[0].startInventoryTag((byte)0, (byte)0);


		while (true) {
			if(isCancelled()){
				break;
			}
			String[] res = null;
			String strTid;
			String strEpc;
			res = params[0].readTagFromBuffer();
			if (res != null) {
				strTid = res[0];
				if (!strTid.equals("0000000000000000")&&!strTid.equals("000000000000000000000000")) {

				} else {
					strTid = "";
				}

				strEpc = params[0].convertUiiToEPC(res[1]);
				Log.e("Tid="+strTid, "Epc="+strEpc);
				//
				String ret = RfidUtils.getDataFromEPC(strEpc);
				if(ret!=null){
					Message msg = new Message() ;
					msg.what = 1 ;
					msg.obj = ret;
					mHandler.sendMessage(msg);

					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}
		params[0].stopInventory();
		Message msg = new Message() ;
		msg.what = 2 ;
		mHandler.sendMessage(msg);
		return "";
	}

//	private String getDataFromEPC(String epc){
//		try {
//			String epcX34 = RfidUtils.xorHex(epc, "34");
//			
//			byte[] Data = RfidUtils.hexStringToBytes(epcX34);
//			String bitString = "";
//			for (int i = 0; i < Data.length; i++) {
//				bitString+=RfidUtils.byteToBit(Data[i]);
//			}
//			
//			Rfid rfid = new Rfid();
//			rfid.setEPC(epc);
//			rfid.setVersion(bitString.substring(0, 4));//0101 标签类别 + 规范版本
//			rfid.setCQDW(String.format("%04d", RfidUtils.binaryToDecimal(bitString.substring(4, 18))));//4位单位代码
//			rfid.setLabelNo(String.format("%07d", RfidUtils.binaryToDecimal(bitString.substring(18, 42))));//7位追溯码
//			rfid.setCZJZCode(String.format("%05d", RfidUtils.binaryToDecimal(bitString.substring(48, 64))));//5位充装介质
//			rfid.setNextCheckDate(String.format("%04d", RfidUtils.binaryToDecimal(bitString.substring(64, 78))));//4位下次检验日期
//			rfid.setState(bitString.substring(78, 80)); //钢瓶状态 00合格 01 报废 10 停用
//			//bitString.substring(80, 82);//气瓶种类 00散瓶01集格02集格内瓶
//			return util.json.JSONUtils.toJsonWithGson(rfid);
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//			return null;
//		}


//		return bitString.substring(0, 4) //0101 标签类别 + 规范版本
//				+ String.format("%04d", binaryToDecimal(bitString.substring(4, 18))) //4位单位代码
//				+ String.format("%07d", binaryToDecimal(bitString.substring(18, 42)))//7位追溯码
//				+ String.format("%05d", binaryToDecimal(bitString.substring(48, 64)))//5充装介质
//				+ String.format("%04d", binaryToDecimal(bitString.substring(64, 78)))//4下次检验日期
//				+ bitString.substring(82, 84) ; //钢瓶状态 00合格 01 报废 10 停用
//	}

}
