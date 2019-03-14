package hsic.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

public class EditDate extends EditText {
    private Context context;
    protected String dateFormat = "";

    public EditDate(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.setFocusable(false);

        this.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {

                DatePickerDialog dpd = new DatePickerDialog(context, EditDate.this, true);
                dpd.show();
                
            }
        });
    }

}
