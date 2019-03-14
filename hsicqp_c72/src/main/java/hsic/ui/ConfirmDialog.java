package hsic.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

public class ConfirmDialog extends AlertDialog {
	private static final String confirmButton = "确定";
	private static final String cancelButton = "取消";
	public Context mContext;

	public ConfirmDialog(Context context) {
		super(context);
		mContext = context;
		setCancelButton();
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	public void setConfirmButton(
			DialogInterface.OnClickListener onConfirmClickListener) {
		super.setButton(BUTTON_POSITIVE, confirmButton, onConfirmClickListener);
	}

	public void setCancelButton(
			DialogInterface.OnClickListener onCancelClickListener) {
		super.setButton(BUTTON_NEGATIVE, cancelButton, onCancelClickListener);
	}

	public void setCancelButton() {
		super.setButton(BUTTON_NEGATIVE, cancelButton,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
	}

	public void setTitle(String title) {
		super.setTitle(title);
	}
}
