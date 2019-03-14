package util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import bean.InfoItem;
import bean.Sale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class UiUtil {
	public static void CloseDiag(final ProgressDialog dialog){
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(5000);//������ʾn�����ȡ��ProgressDialog
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
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);  
            boolean isOpen=imm.isActive();
            
            if(isOpen){
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); 
            }
        } catch (Exception e) {
            // TODO: handle exception
        	e.printStackTrace();
        }
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
				System.out.println("-�豸��"+bluetoothDevice.getName() + " " + bluetoothDevice.getAddress()+ " " + bluetoothDevice.getBondState());
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
		
		Log.e("hasBluetoothInfoInDeviceList", "="+has);
		
		return has;
	}
	
	public static List<InfoItem>getPrintInfo(Sale sale, String Truck, String Persons){
		List<InfoItem> list = new ArrayList<InfoItem>();
		InfoItem  info = new InfoItem();
		info.setKey("2");
		info.setName("�ͻ����:");
		info.setValue(sale.getCustomerID());
		list.add(info);
		
		info = new InfoItem();
		info.setKey("2");
		info.setName("�ͻ�����:");
		info.setValue(sale.getCustomerName());
		list.add(info);
		
		info = new InfoItem();
		info.setKey("2");
		info.setName("�ͻ�����:");
		info.setValue(sale.getCustomerType()==null ? "" : ( sale.getCustomerType().equals("CT01") ? "�½��û�" : "����ͻ�") );
		list.add(info);
		
		info = new InfoItem();
		info.setKey("2");
		info.setName("��ϵ�绰:");
		info.setValue(sale.getCustomerTelephone());
		list.add(info);
		
		info = new InfoItem();
		info.setKey("2");
		info.setName("��ϵ��ַ:");
		info.setValue(sale.getAddress());
		list.add(info);
		
		info = new InfoItem();
		info.setKey("2");
		info.setName("���ѷ�ʽ:");
		String PayType = "";
		if(sale.getPayType()!=null){
			if(sale.getPayType().equals("0")) PayType = "�ֽ�";
			else if(sale.getPayType().equals("1")) PayType = "��Ʊ";
			else if(sale.getPayType().equals("2")) PayType = "�½�";
		}
		info.setValue(PayType);
		list.add(info);
		
		
		info = new InfoItem();
		info.setKey("2");
		info.setName("���ͳ��ƺ�:");
		info.setValue(Truck);
		list.add(info);
		
		info = new InfoItem();
		info.setKey("2");
		info.setName("������Ա:");
		info.setValue(Persons);
		list.add(info);
		
		
		info = new InfoItem();
		info.setKey("0");
		list.add(info);
		
		info = new InfoItem();
		if(sale.getCustomerType()!=null && sale.getCustomerType().equals("CT01")){
			info.setKey("2");
			info.setName("��ƿ��Ϣ");
			info.setValue("����");
		}else{
			info.setKey("3");
			info.setName("��ƿ��Ϣ");
			info.setValue("����");
			info.setValue2("���");
		}
		list.add(info);
		
		info = new InfoItem();
		info.setKey("-1");
		list.add(info);
		
		int totle = 0; BigDecimal price = new BigDecimal("0.0");
		for (int i = 0; i < sale.getSaleDetail().size(); i++) {
			if(sale.getCustomerType()!=null && sale.getCustomerType().equals("CT01")){
				if(sale.getSaleDetail().get(i).getGoodsType()==1 && sale.getSaleDetail().get(i).getSendNum()>0){
					info = new InfoItem();
					info.setKey("2");
					info.setName(sale.getSaleDetail().get(i).getGoodsName());
					info.setValue(String.valueOf(sale.getSaleDetail().get(i).getSendNum()));
					totle += sale.getSaleDetail().get(i).getSendNum();
					list.add(info);
				}else if(sale.getSaleDetail().get(i).getGoodsType()==6){
					info = new InfoItem();
					info.setKey("1");
					info.setName(sale.getSaleDetail().get(i).getGoodsName());
					list.add(info);
				}
			}else{
				if(sale.getSaleDetail().get(i).getGoodsType()==1 && sale.getSaleDetail().get(i).getSendNum()>0){
					info = new InfoItem();
					info.setKey("3");
					info.setName(sale.getSaleDetail().get(i).getGoodsName());
					info.setValue(String.valueOf(sale.getSaleDetail().get(i).getSendNum()));
					totle += sale.getSaleDetail().get(i).getSendNum();
					info.setValue2(sale.getSaleDetail().get(i).getGoodsPrice().toString());
					price = price.add(sale.getSaleDetail().get(i).getGoodsPrice());
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
			info.setName("�ϼ�:");
			info.setValue(String.valueOf(totle));
			list.add(info);
		}else{
			info = new InfoItem();
			info.setKey("3");
			info.setName("�ϼ�:");
			info.setValue(String.valueOf(totle));
			info.setValue2(price.toString());
			list.add(info);
		}
		
		info = new InfoItem();
		info.setKey("0");
		list.add(info);
		
		info = new InfoItem();
		info.setKey("2");
		info.setName("��ƿ��Ϣ");
		info.setValue("����");
		list.add(info);
		
		info = new InfoItem();
		info.setKey("-1");
		list.add(info);
		
		int totle2 = 0;
		for (int i = 0; i < sale.getSaleDetail().size(); i++) {
			if(sale.getSaleDetail().get(i).getGoodsType()==1 && sale.getSaleDetail().get(i).getReceiveNum()>0){
				info = new InfoItem();
				info.setKey("2");
				info.setName(sale.getSaleDetail().get(i).getGoodsName());
				info.setValue(String.valueOf(sale.getSaleDetail().get(i).getReceiveNum()));
				totle2 += sale.getSaleDetail().get(i).getReceiveNum();
				list.add(info);
			}
		}
		
		if(sale.getGoodsList()!=null && sale.getGoodsList().size()>0){
			for (int i = 0; i < sale.getGoodsList().size(); i++) {
				info = new InfoItem();
				info.setKey("2");
				info.setName(sale.getGoodsList().get(i).getGoodsName());
				info.setValue(String.valueOf(sale.getGoodsList().get(i).getGoodsNum()));
				totle2 += sale.getGoodsList().get(i).getGoodsNum();
				list.add(info);
			}
		}
		
		info = new InfoItem();
		info.setKey("-1");
		list.add(info);
		
		info = new InfoItem();
		info.setKey("2");
		info.setName("�ϼ�:");
		info.setValue(String.valueOf(totle2));
		list.add(info);
		
		info = new InfoItem();
		info.setKey("0");
		list.add(info);
		
		info = new InfoItem();
		info.setKey("2");
		info.setName("��ӡʱ��:");
		info.setValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		list.add(info);
		
		Log.e("=====", util.json.JSONUtils.toJsonWithGson(list));
		
		return list;
	}
	
	
	
	
}
