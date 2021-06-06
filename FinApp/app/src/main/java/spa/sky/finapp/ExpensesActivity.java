package spa.sky.finapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExpensesActivity extends AppCompatActivity {
    EditText pay, tag;
    DBHelper databaseHelper;
    Button addExpense;
    TextView Finapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_pay);


        databaseHelper = new DBHelper(this);
        pay = findViewById (R.id.payEditText);
        tag = findViewById (R.id.tagEditText);
        addExpense =  findViewById (R.id.ButtonPay);

        Finapp = findViewById (R.id.TitlePay);
        findViewById(R.id.close_pay).setOnClickListener(
                new View.OnClickListener () {

                    @Override
                    public void onClick(View arg0) {

                        onBackPressed ();

                    }
                });

        Intent intent = getIntent ();
        if (intent.getStringExtra ("ocr") != null) {
            String str = intent.getStringExtra ("ocr");
            pay.setText (str);
        }

        TextView dateTextView =  findViewById (R.id.dateTextViewPay);
        String date = new SimpleDateFormat("MMM dd", Locale.getDefault()).format(new Date());
        dateTextView.setText (date + ", Expense");

        addExpense.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                if (pay.getText ().toString ().equals ("") || tag.getText ().toString ().equals ("")) {
                    Toast.makeText (getApplicationContext (),"Введите действительную сумму и тег.",Toast.LENGTH_SHORT).show ();
                }
                else
                    addPay ();
            }
        });


        Finapp.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext (), Dashboard.class);
                startActivity (intent);
                finish ();
            }
        });


        findViewById (R.id.transportImageViewP).setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                tag.setText ("Транспорт");
            }
        });
        findViewById (R.id.travelImageViewP).setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                tag.setText ("Путешествие");
            }
        });
        findViewById (R.id.foodImageViewP).setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                tag.setText ("Еда");
            }
        });
        findViewById (R.id.billsImageViewP).setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                tag.setText ("Счета");
            }
        });
        findViewById (R.id.sportsImageViewP).setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                tag.setText ("Спорт");
            }
        });
        findViewById (R.id.homeImageViewP).setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                tag.setText ("Дом");
            }
        });
        findViewById (R.id.petsImageViewP).setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                tag.setText ("Питомцы");
            }
        });
        findViewById (R.id.educationImageViewP).setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                tag.setText ("Образование");
            }
        });
        findViewById (R.id.beautyImageViewP).setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                tag.setText ("Косметология");
            }
        });
        findViewById (R.id.kidsImageViewP).setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                tag.setText ("Дети");
            }
        });
        findViewById (R.id.healthImageViewP).setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                tag.setText ("Здоровье");
            }
        });
        findViewById (R.id.movieImageViewP).setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                tag.setText ("Кино");
            }
        });
    }

    public void addPay () {
        Transactions t = new Transactions ();
        t.setExin (0);
        Double d = Double.parseDouble (pay.getText ().toString ());
        Log.i("SSS", d.toString ());
        long l = Math.round (d);
        t.setAmount (l);
        t.setTag (tag.getText ().toString ());
        t.setUid (Integer.parseInt (Utils.userId));
        databaseHelper.insertTransaction (t);
        databaseHelper.getTransactions ();
        databaseHelper.setIncomeExpenses ();

        Intent intent = new Intent (getApplicationContext ( ), TransactionsActivity.class);
        startActivity (intent);
        finish ( );
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent (getApplicationContext (),TransactionsActivity.class);
        startActivity (intent);
        finish ();
    }



}
