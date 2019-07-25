package hsic.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

public class EditTime extends EditText {
    private Context context;
    protected String dateFormat = "";

    public EditTime(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.setFocusable(false);

        this.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {

                DatePickerDialog dpd = new DatePickerDialog(context, EditTime.this, true);
                dpd.show();

            }
        });
    }
}
