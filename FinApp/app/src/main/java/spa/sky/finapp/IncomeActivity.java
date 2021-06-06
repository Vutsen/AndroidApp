package spa.sky.finapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class IncomeActivity extends AppCompatActivity {

    EditText take, tag, datetext;
    Button addIncome;
    DBHelper databaseHelper;
    TextView FinApp;
    String date1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_take);

        take =  findViewById (R.id.takeEditText);
        tag =  findViewById (R.id.tagEditTextTake);
        databaseHelper = new DBHelper(this);
        FinApp = findViewById (R.id.TitleTake);
        datetext = findViewById(R.id.dateTextViewTake);

        addIncome = findViewById (R.id.ButtonTake);
        findViewById(R.id.close_take).setOnClickListener(
                new View.OnClickListener () {

                    @Override
                    public void onClick(View arg0) {
                        onBackPressed ();
                    }
                });

        Calendar incomeCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date1 = new
                DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {

                        incomeCalendar.set(Calendar.YEAR, year);
                        incomeCalendar.set(Calendar.MONTH, monthOfYear);
                        incomeCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }

                    private void updateLabel() {
                        String myFormat2 = "yyyy-MM-dd";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat2, Locale.US);
                        datetext.setText(sdf.format(incomeCalendar.getTime()));
                    }
                };

        datetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(IncomeActivity.this, date1, incomeCalendar
                        .get(Calendar.YEAR), incomeCalendar.get(Calendar.MONTH),
                        incomeCalendar.get(Calendar.DAY_OF_MONTH)).show();
            };
        });

        addIncome.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                if (take.getText ().toString ().equals ("") || tag.getText ().toString ().equals ("")) {
                    Toast.makeText (getApplicationContext (),"Введите действительную сумму и тег.",Toast.LENGTH_SHORT).show ();
                }
                else
                    addTake();
            }
        });

        FinApp.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext (), Dashboard.class);
                startActivity (intent);
                finish ();
            }
        });

    }
    public void addTake () {
        Transactions t = new Transactions ();
        t.setExin (1);
        t.setAmount (Long.parseLong (take.getText ().toString ()));
        t.setTag (tag.getText ().toString ());
        t.setDate(datetext.getText().toString());
        t.setUid (Integer.parseInt (Utils.userId));
        databaseHelper.insertTransaction (t);
        Toast.makeText (getApplicationContext (),"Доход добавлен", Toast.LENGTH_SHORT).show ();
        databaseHelper.getTransactions ();
        Intent intent = new Intent (getApplicationContext (), TransactionsActivity.class);
        startActivity (intent);
        finish ();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent (getApplicationContext (),TransactionsActivity.class);
        startActivity (intent);
        finish ();
    }
}