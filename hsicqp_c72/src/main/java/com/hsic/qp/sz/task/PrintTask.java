package com.hsic.qp.sz.task;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Bitmap;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import bean.InfoItem;
import util.PrintUtils;
import util.print.Barcode;
import util.print.GPrinterCommand;
import util.print.PrintPic;

public class PrintTask {

    public static void PrintSale(Context mContext, List<InfoItem> mData, String SaleID){
        ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.setMessage("开始打印销售单据...");
        dialog.setCancelable(false);
        dialog.show();


        BluetoothSocket bluetoothSocket = null;
        OutputStream outputStream;

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!bluetoothAdapter.isEnabled()){
            dialog.setMessage("蓝牙未打开");
            dialog.setCancelable(false);

            return;
        }

        UUID uuid = UUID.fromString("0001101-0000-1000-8000-00805F9B34FB");

        bluetoothAdapter.cancelDiscovery();
        //连接
        try {
            String mac = mContext.getSharedPreferences("DeviceSetting", 0).getString("bluetooth_mac", "");
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(mac);
            bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();
        } catch (Exception e) {
            // TODO: handle exception
            try {
                if(bluetoothSocket!=null)
                    bluetoothSocket.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            e.printStackTrace();
            dialog.setMessage("连接打印设备异常");
            dialog.setCancelable(false);
            return ;
        }


        dialog.setMessage("正在打印销售单据...");
        //打印
        try {
            PrintUtils.setOutputStream(outputStream);
            PrintUtils.selectCommand(GPrinterCommand.reset);
            PrintUtils.selectCommand(GPrinterCommand.center);
            PrintUtils.selectCommand(GPrinterCommand.bold);
            PrintUtils.printText("申中工业气体销售单据");
            PrintUtils.selectCommand(GPrinterCommand.print);
            PrintUtils.selectCommand(GPrinterCommand.bold_cancel);

            //


            PrintUtils.selectCommand(GPrinterCommand.print);
            PrintUtils.selectCommand(GPrinterCommand.text_normal_size);
            PrintUtils.selectCommand(GPrinterCommand.left);
            PrintUtils.printText("订单号:"+SaleID);
            PrintUtils.selectCommand(GPrinterCommand.print);
            PrintUtils.selectCommand(GPrinterCommand.print);


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
            }
            PrintUtils.selectCommand(GPrinterCommand.print);
            PrintUtils.printText("客户签字:");
            PrintUtils.selectCommand(GPrinterCommand.print);
            PrintUtils.selectCommand(GPrinterCommand.print);
            PrintUtils.selectCommand(GPrinterCommand.print);
            PrintUtils.selectCommand(GPrinterCommand.print);
            PrintUtils.selectCommand(GPrinterCommand.print);

            PrintUtils.selectCommand(GPrinterCommand.reset);
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
            outputStream.write(bytes, 0, bytes.length);

            dialog.setMessage("打印完成");
            dialog.setCancelable(false);
            //    dialog.dismiss();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            dialog.setMessage("打印出现异常");
            dialog.setCancelable(false);
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
    }
}
