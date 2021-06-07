package spa.sky.finapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DateCheck extends AppCompatActivity {
    Button take1, pay1, list1, graph1, find;
    GridLayout gridLayout;
    ListView myListView1;
    DBHelper databaseHelper;
    TextView home,dincome,dexpense;
    PieChart pieChart1;
    EditText start1,end1;
    String end;
    String start;


    @Override
    public void onBackPressed() {
        Intent intent = new Intent (getApplicationContext (),Dashboard.class);
        startActivity (intent);
        finish ();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_datecheck);

        databaseHelper = new DBHelper(this);
        databaseHelper.getDateExpenses (start,end);
        databaseHelper = new DBHelper(this);

        home = findViewById(R.id.homeTextViewT);
        dincome = findViewById(R.id.dincome);
        dexpense = findViewById(R.id.dexpense);
        gridLayout = findViewById(R.id.gridLayout);
        find = findViewById(R.id.find);
        start1 = findViewById(R.id.start1);
        end1 = findViewById(R.id.end1);
        take1 = findViewById (R.id.takeButtonT1);
        pay1 =  findViewById (R.id.payButtonT1);
        list1 =  findViewById (R.id.listViewTab1);
        graph1 =  findViewById (R.id.graphViewTab1);
        myListView1 = findViewById(R.id.transactionsListViewT1);
        pieChart1 = findViewById (R.id.pieChartExpenses);

        Calendar startCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new
                DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {

                        startCalendar.set(Calendar.YEAR, year);
                        startCalendar.set(Calendar.MONTH, monthOfYear);
                        startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }

                    private void updateLabel() {
                        String myFormat = "yyyy-MM-dd";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        start1.setText(sdf.format(startCalendar.getTime()));
                        start = sdf.format(startCalendar.getTime());
                    }
                };

        start1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(DateCheck.this, date, startCalendar
                        .get(Calendar.YEAR), startCalendar.get(Calendar.MONTH),
                        startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            };
        });

        Calendar endCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateEnd = new
                DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        endCalendar.set(Calendar.YEAR, year);
                        endCalendar.set(Calendar.MONTH, monthOfYear);
                        endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel1();
                    }

                    private void updateLabel1() {
                        String myFormat1 = "yyyy-MM-dd";
                        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
                        end1.setText(sdf1.format(endCalendar.getTime()));
                        end = sdf1.format(endCalendar.getTime());
                    }
                };

        end1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(DateCheck.this, dateEnd, endCalendar
                        .get(Calendar.YEAR), endCalendar.get(Calendar.MONTH),
                        endCalendar.get(Calendar.DAY_OF_MONTH)).show();
            };
        });

        pieChart1.setOnChartValueSelectedListener (new OnChartValueSelectedListener( ) {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry pe = (PieEntry) e;
                Log.i("Диаграмма",pe.getLabel());
                Snackbar snackbar = Snackbar.make (pieChart1, pe.getLabel (), Snackbar.LENGTH_SHORT);
                snackbar.show ();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        take1.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext (), IncomeActivity.class);
                startActivity (intent);
                finish ();
            }
        });


        pay1.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext (), ExpensesActivity.class);
                startActivity (intent);
                finish ();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext (), Dashboard.class);
                startActivity (intent);
                finish ();
            }
        });

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphView1(myListView1);
                setTList(start,end);
                SetAllDateExpense(start,end);
            }
        });
    }
    public void SetAllDateExpense(String start, String end){
        databaseHelper.setDateIncomeExpenses(start,end);
        gridLayout.setVisibility(View.VISIBLE);
        dincome.setText ("₽ " + Utils.dateincome);
        dexpense.setText ("₽ " + Utils.dateexpense);

    }



    private void setTList(String start,String end) {
        ArrayList<String> tran = databaseHelper.getDateTransaction (start,end);
        if (tran.size () == 0) {
            Toast.makeText (DateCheck.this, "Нет транзакций.", Toast.LENGTH_LONG).show ( );

        } else {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, tran);

            myListView1.setAdapter (arrayAdapter);
            final ArrayList<String> finalTransact1 = tran;
            myListView1.setOnItemClickListener (new AdapterView.OnItemClickListener ( ) {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Toast.makeText (DateCheck.this, "Транзакции  : " + finalTransact1.get (position), Toast.LENGTH_LONG).show ( );
                }
            });
        }

    }

    public void listView1 (View view) {
        graph1.setEnabled (true);
        list1.setEnabled (false);
        setTList (start,end);
        pieChart1.setVisibility (View.INVISIBLE);
        myListView1.setVisibility (View.VISIBLE);
    }

    public void graphView1(View view) {
        graph1.setEnabled (false);
        list1.setEnabled (true);
        pieChart1.setVisibility (View.VISIBLE);
        myListView1.setVisibility (View.INVISIBLE);
        setTList (start,end);
        pieChart1.setHoleRadius (15f);
        pieChart1.setTransparentCircleRadius (15f);

        HashMap<String, Integer> hash = databaseHelper.getDateExpenses (start,end);

        List<PieEntry> value = new ArrayList<>();
        for (Map.Entry h:hash.entrySet ()) {
            Integer val = (Integer) h.getValue ();
            String label = h.getKey ().toString ();

            value.add (new PieEntry (val, label));
        }

        PieDataSet pieDataSet = new PieDataSet (value, "Расходы") ;
        PieData pieData = new PieData (pieDataSet);
        pieChart1.setData (pieData);

        pieChart1.setCenterText ("Расходы");
        pieChart1.setCenterTextSize (10f);
        pieChart1.getLegend ().setEnabled(false);
        pieChart1.getDescription ().setEnabled (false);
        pieChart1.setEntryLabelColor (Color.BLACK);
        pieChart1.setEntryLabelTextSize (10f);
        pieChart1.animateXY (800,800);

        pieDataSet.setColors (ColorTemplate.MATERIAL_COLORS);
    }
}
