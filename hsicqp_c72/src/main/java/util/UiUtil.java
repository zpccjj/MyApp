package util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.util.Log;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import bean.InfoItem;
import bean.Sale;

public class UiUtil {
	public static void CloseDiag(final ProgressDialog dialog){
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(3000);//让他显示n耗秒后，取消ProgressDialog
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dialog.dismiss();
			}
		});

		t.start();
	}

	public static void CloseKey(Activity activity){
//        try {
//            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//            boolean isOpen=imm.isActive();
//
//            if(isOpen){
//                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//        	e.printStackTrace();
//        }
	}

	public static void setDiagBtn(DialogInterface dialog, boolean isDiss){
		try {
			java.lang.reflect.Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, isDiss);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<InfoItem> getBluetoothList(Set<BluetoothDevice> devices){
		List<InfoItem> bList = new ArrayList<InfoItem>();

		if (devices.size()>0) {
			for(Iterator<BluetoothDevice> iterator=devices.iterator();iterator.hasNext();){
				BluetoothDevice bluetoothDevice=(BluetoothDevice)iterator.next();
				InfoItem bInfo = new InfoItem();
				bInfo.setKey(bluetoothDevice.getAddress());
				bInfo.setValue(bluetoothDevice.getName() + ":" + bluetoothDevice.getAddress());
				bList.add(bInfo);
			}
		}
		return bList;
	}

	public static boolean hasBluetoothInfoInDeviceList(List<InfoItem> bList, String Mac){
		boolean has = false;

		if(bList==null || bList.size()==0) return false;
		if(Mac==null || Mac.length()==0) return false;

		for (int i = 0; i < bList.size(); i++) {
			if(bList.get(i).getKey().equals(Mac)){
				has = true;
				break;
			}
		}

		return has;
	}

	public static List<InfoItem>getPrintInfo(Sale sale, String Truck, String Persons){
		List<InfoItem> list = new ArrayList<InfoItem>();
		InfoItem  info = new InfoItem();
		info.setKey("2");
		info.setName("客户编号:");
		info.setValue(sale.getCustomerID());
		list.add(info);

		info = new InfoItem();
		info.setKey("2");
		info.setName("客户名称:");
		info.setValue(sale.getCustomerName());
		list.add(info);

		info = new InfoItem();
		info.setKey("2");
		info.setName("客户类型:");
		info.setValue(sale.getCustomerType()==null ? "" : ( sale.getCustomerType().equals("CT01") ? "月结用户" : "个体客户") );
		list.add(info);

		info = new InfoItem();
		info.setKey("2");
		info.setName("联系电话:");
		info.setValue(sale.getCustomerTelephone());
		list.add(info);

		info = new InfoItem();
		info.setKey("2");
		info.setName("联系地址:");
		info.setValue(sale.getAddress());
		list.add(info);

		info = new InfoItem();
		info.setKey("2");
		info.setName("付费方式:");
		String PayType = "";
		if(sale.getPayType()!=null){
			if(sale.getPayType().equals("0")) PayType = "现金";
			else if(sale.getPayType().equals("1")) PayType = "气票";
			else if(sale.getPayType().equals("2")) PayType = "月结";
		}
		info.setValue(PayType);
		list.add(info);


		info = new InfoItem();
		info.setKey("2");
		info.setName("配送车牌号:");
		info.setValue(Truck);
		list.add(info);

		info = new InfoItem();
		info.setKey("2");
		info.setName("配送人员:");
		info.setValue(Persons);
		list.add(info);


		info = new InfoItem();
		info.setKey("0");
		list.add(info);

		info = new InfoItem();
		if(sale.getCustomerType()!=null && sale.getCustomerType().equals("CT01")){
			info.setKey("2");
			info.setName("发/收瓶信息");
			info.setValue("发/收数量");
		}else{
			info.setKey("3");
			info.setName("发/收瓶信息");
			info.setValue("发/收数量");
			info.setValue2("单价");
		}
		list.add(info);

		info = new InfoItem();
		info.setKey("-1");
		list.add(info);

		int totleR = 0; int totleS = 0; BigDecimal price = new BigDecimal("0.0");
		for (int i = 0; i < sale.getSaleDetail().size(); i++) {
			if(sale.getCustomerType()!=null && sale.getCustomerType().equals("CT01")){
				if(sale.getSaleDetail().get(i).getGoodsType()==1){
					info = new InfoItem();
					info.setKey("2");
					info.setName(sale.getSaleDetail().get(i).getGoodsName());
					if(sale.getSaleDetail().get(i).getIsJG()==1){
						info.setValue(String.valueOf(sale.getSaleDetail().get(i).getSendNum()) +"x"+String.valueOf(sale.getSaleDetail().get(i).getNum())
								+ " / " + String.valueOf(sale.getSaleDetail().get(i).getReceiveNum()) +"x"+ String.valueOf(sale.getSaleDetail().get(i).getNum()));
						totleR += sale.getSaleDetail().get(i).getReceiveNum() * sale.getSaleDetail().get(i).getNum();
						totleS += sale.getSaleDetail().get(i).getSendNum() * sale.getSaleDetail().get(i).getNum();
					}else{
						info.setValue(String.valueOf(sale.getSaleDetail().get(i).getSendNum()) + " / " + String.valueOf(sale.getSaleDetail().get(i).getReceiveNum()));
						totleR += sale.getSaleDetail().get(i).getReceiveNum();
						totleS += sale.getSaleDetail().get(i).getSendNum();
					}
					list.add(info);
				}else if(sale.getSaleDetail().get(i).getGoodsType()==6){
					info = new InfoItem();
					info.setKey("1");
					info.setName(sale.getSaleDetail().get(i).getGoodsName());
					list.add(info);
				}
			}else{
				if(sale.getSaleDetail().get(i).getGoodsType()==1){
					info = new InfoItem();
					info.setKey("3");
					info.setName(sale.getSaleDetail().get(i).getGoodsName());
					if(sale.getSaleDetail().get(i).getIsJG()==1){
						info.setValue(String.valueOf(sale.getSaleDetail().get(i).getSendNum())  +"x"+ String.valueOf(sale.getSaleDetail().get(i).getNum())
								+ " / " + String.valueOf(sale.getSaleDetail().get(i).getReceiveNum())  +"x"+ String.valueOf(sale.getSaleDetail().get(i).getNum()));
						totleR += sale.getSaleDetail().get(i).getReceiveNum() * sale.getSaleDetail().get(i).getNum();
						totleS += sale.getSaleDetail().get(i).getSendNum() * sale.getSaleDetail().get(i).getNum();
						if(sale.getSaleDetail().get(i).getSendNum()>0){
							info.setValue2(sale.getSaleDetail().get(i).getGoodsPrice().toString());
							price = price.add(sale.getSaleDetail().get(i).getGoodsPrice().multiply(new BigDecimal(sale.getSaleDetail().get(i).getSendNum() * sale.getSaleDetail().get(i).getNum())));
						}else{
							info.setValue2("-");
						}
					}else{
						info.setValue(String.valueOf(sale.getSaleDetail().get(i).getSendNum()) + " / " + String.valueOf(sale.getSaleDetail().get(i).getReceiveNum()));
						totleR += sale.getSaleDetail().get(i).getReceiveNum();
						totleS += sale.getSaleDetail().get(i).getSendNum();
						if(sale.getSaleDetail().get(i).getSendNum()>0){
							info.setValue2(sale.getSaleDetail().get(i).getGoodsPrice().toString());
							price = price.add(sale.getSaleDetail().get(i).getGoodsPrice().multiply(new BigDecimal(sale.getSaleDetail().get(i).getSendNum())));
						}else{
							info.setValue2("-");
						}
					}

					list.add(info);
				}else if(sale.getSaleDetail().get(i).getGoodsType()==6){
					info = new InfoItem();
					info.setKey("2");
					info.setName(sale.getSaleDetail().get(i).getGoodsName());
					info.setValue(sale.getSaleDetail().get(i).getGoodsPrice().toString());
					price = price.add(sale.getSaleDetail().get(i).getGoodsPrice());
					list.add(info);
				}
			}

		}
		info = new InfoItem();
		info.setKey("-1");
		list.add(info);


		if(sale.getCustomerType()!=null && sale.getCustomerType().equals("CT01")){
			info = new InfoItem();
			info.setKey("2");
			info.setName("合计:");
			info.setValue(String.valueOf(totleS) + " / " + String.valueOf(totleR));
			list.add(info);
		}else{
			info = new InfoItem();
			info.setKey("3");
			info.setName("合计:");
			info.setValue(String.valueOf(totleS) + " / " + String.valueOf(totleR));
			info.setValue2(price.toString()+ "元");
			list.add(info);
		}
		info = new InfoItem();
		info.setKey("-1");
		list.add(info);

		if(sale.getRemark()!=null && sale.getRemark().length()>0 && sale.getOtherPirce()!=null){
			info = new InfoItem();
			info.setKey("2");
			info.setName(sale.getRemark()+ ":");
			info.setValue(sale.getOtherPirce().toString() + "元");
			list.add(info);
		}

		info = new InfoItem();
		info.setKey("0");
		list.add(info);

		info = new InfoItem();
		info.setKey("2");
		info.setName("打印时间:");
		info.setValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		list.add(info);
		info = new InfoItem();
		info.setKey("-1");
		list.add(info);

		Log.e("打印",util.json.JSONUtils.toJsonWithGson(list));

		return list;
	}
}
