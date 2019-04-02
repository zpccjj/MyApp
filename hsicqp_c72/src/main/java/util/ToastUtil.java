package util;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtil {
	static Toast toast = null;
	public static void showToast(Context context, String info){		
		if (toast==null) {
			toast = Toast.makeText(context.getApplicationContext(), info, Toast.LENGTH_SHORT);
            //toast.setGravity(Gravity.CENTER, 0, 0);
			LinearLayout layout = (LinearLayout) toast.getView();
		   layout.setBackgroundColor(Color.parseColor("#FFFFFF"));//°×
		   //layout.getBackground().setAlpha(0);//Í¸Ã÷
		   TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
		   v.setTextColor(Color.RED); 
		   v.setTextSize(20);
		}else {
			toast.setText(info);
		}
		toast.show();
	} 
}
