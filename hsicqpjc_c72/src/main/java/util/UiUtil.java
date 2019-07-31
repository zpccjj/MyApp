package util;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.inputmethod.InputMethodManager;

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

}
