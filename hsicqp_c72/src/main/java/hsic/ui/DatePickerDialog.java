package hsic.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hsic.qp.sz.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

public class DatePickerDialog extends Dialog {
    Context context;
    EditText editText;
    private WheelView day;
    private WheelView month;
    private WheelView year;
    private WheelView Hour;
    private WheelView Minute;
    private WheelView Second;
    private Button button_1;
    private Button button_2;

    private static final int MIN_YEAR = 1980;
    private static final int MAX_YEAR = 2050;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

    @SuppressWarnings("deprecation")
    public DatePickerDialog(final Context context, final EditText editText, final boolean isDateTime) {
        super(context);
        this.context = context;
        this.editText = editText;
        this.setTitle("选择日期");
        this.setContentView(R.layout.timestamp_picker_dialog);
        this.setCancelable(false);

        this.button_1 = (Button) findViewById(R.id.button1);
        this.button_2 = (Button) findViewById(R.id.button2);

        day = (WheelView) findViewById(R.id.day);
        month = (WheelView) findViewById(R.id.month);
        year = (WheelView) findViewById(R.id.year);
        Hour = (WheelView) findViewById(R.id.hour);
        Minute = (WheelView) findViewById(R.id.minute);
        Second = (WheelView) findViewById(R.id.second);

        if(!isDateTime){
            Hour.setVisibility(View.GONE);
            Minute.setVisibility(View.GONE);
            Second.setVisibility(View.GONE);
        }

        final Calendar c = Calendar.getInstance();
        int nYear = c.get(Calendar.YEAR); // 获取当前年份
        int nMonth = c.get(Calendar.MONTH)+1;// 获取当前月份
        int nDay = c.get(Calendar.DAY_OF_MONTH);// 获取当前月份的日期号码
        int nHour = c.get(Calendar.HOUR_OF_DAY);
        int nMinute = c.get(Calendar.MINUTE);
        int nSecond = c.get(Calendar.SECOND);

        try {
            Date d;
            if(isDateTime){
                d = this.sdf.parse(this.editText.getText().toString());
                nHour = d.getHours();
                nMinute = d.getMinutes();
                nSecond = d.getSeconds();
            }else{
                d = this.sdf2.parse(this.editText.getText().toString());
            }
            nYear = d.getYear() + 1900;
            nMonth = d.getMonth() + 1;
            nDay = d.getDate();

        } catch (Exception e) {
        }

        year.setViewAdapter(new NumericWheelAdapter(context, MIN_YEAR, MAX_YEAR));
        year.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int n = getMonthDays(newValue, month.getCurrentItem());
                int m = day.getCurrentItem();
                day.setViewAdapter(new NumericWheelAdapter(context, 1, n, "%02d"));
                day.setCurrentItem((n - 1) < m ? (n - 1) : m);
            }
        });
        year.setCyclic(true);

        month.setViewAdapter(new NumericWheelAdapter(context, 1, 12, "%02d"));
        month.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int n = getMonthDays(year.getCurrentItem(),newValue);
                int m = day.getCurrentItem();
                day.setViewAdapter(new NumericWheelAdapter(context, 1, n, "%02d"));
                day.setCurrentItem((n - 1) < m ? (n - 1) : m);
            }
        });
        month.setCyclic(true);

        int n = getMonthDays(year.getCurrentItem()+MIN_YEAR,nMonth);
        day.setViewAdapter(new NumericWheelAdapter(context, 1, n, "%02d"));
        day.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
            }
        });
        day.setCyclic(true);

        Hour.setViewAdapter(new NumericWheelAdapter(context, 0, 23, "%02d"));
        Hour.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
            }
        });
        Hour.setCyclic(true);

        Minute.setViewAdapter(new NumericWheelAdapter(context, 0, 59, "%02d"));
        Minute.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
            }
        });
        Minute.setCyclic(true);

        Second.setViewAdapter(new NumericWheelAdapter(context, 0, 59, "%02d"));
        Second.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
            }
        });
        Second.setCyclic(true);

        year.setCurrentItem(nYear - MIN_YEAR);
        month.setCurrentItem(nMonth - 1);
        day.setCurrentItem(nDay - 1);
        Hour.setCurrentItem(nHour);
        Minute.setCurrentItem(nMinute);
        Second.setCurrentItem(nSecond);

        this.button_1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(isDateTime){
                    String _ymd = String.format(
                            "%04d-%02d-%02d %02d:%02d:%02d",
                            getYear(), getMonth(),
                            getDay(), getHour(),
                            getMinute(), getSecond());
                    editText.setText(_ymd);
                }else{
                    String _ymd = String.format(
                            "%04d-%02d-%02d",
                            getYear(), getMonth(),
                            getDay());
                    editText.setText(_ymd);
                }


                DatePickerDialog.this.dismiss();
            }
        });
        this.button_2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                DatePickerDialog.this.dismiss();
            }
        });
    }


    private int getYear() {
        return year.getCurrentItem() + MIN_YEAR;
    }

    private int getMonth() {
        return month.getCurrentItem() + 1;
    }

    private int getDay() {
        return day.getCurrentItem() + 1;
    }

    private int getHour() {
        return Hour.getCurrentItem();
    }

    private int getMinute() {
        return Minute.getCurrentItem();
    }

    private int getSecond() {
        return Second.getCurrentItem();
    }

    private int getMonthDays(int year, int month)
    {
        month++;
        year += MIN_YEAR;
        switch (month)
        {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
            {
                return 31;
            }
            case 4:
            case 6:
            case 9:
            case 11:
            {
                return 30;
            }
            case 2:
            {
                if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
                    //       Log.i("day count","29");
                    return 29;
                } else {
                    return 28;
                }
            }
        }
        return 0;
    }
}
