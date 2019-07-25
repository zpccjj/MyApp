package util;

import android.graphics.BitmapFactory.Options;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PathFileUtils {
	public static String getPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/sz/photo/";
	}
	
	public static String getSignPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/sz/sign/";
	}
	
	public static String getSalePath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/sz/Sale/";
	}
	
	public static List<String> getFileList(String SaleID, String Path){
		List<String> ret = new ArrayList<String>();
		
		File rootFile = new File(Path);
		
		File[] files = rootFile.listFiles();
		
		if (files != null && files.length>0) {
			for (File file : files) {
				if (!file.isDirectory()){
					if(file.getName().substring(file.getName().length()-4, file.getName().length()).equals(".jpg")){
						String[] tmpStrings = file.getName().split("\\_");
						if(tmpStrings[0].equals(SaleID)) ret.add(file.getName());
					};
				}
			}
		}
		
		return ret;
	}
	
	public static Options getBitmapOption(int inSampleSize)	{
	        System.gc();
	        Options options = new Options();
	        options.inPurgeable = true;
	        options.inSampleSize = inSampleSize;
	        return options;
	}
}
