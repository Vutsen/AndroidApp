package spa.sky.finapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TransactionsActivity extends AppCompatActivity {
    Button take, pay, list, graph, date;
    ListView myListView;
    DBHelper databaseHelper;
    TextView home;
    PieChart pieChart;
    Button download;


    @Override
    public void onBackPressed() {
        Intent intent = new Intent (getApplicationContext (),Dashboard.class);
        startActivity (intent);
        finish ();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_transactions);

        databaseHelper = new DBHelper(this);
        databaseHelper.getExpenses ();

        databaseHelper = new DBHelper(this);
        take = findViewById (R.id.takeButtonT);
        pay =  findViewById (R.id.payButtonT);
        home = findViewById (R.id.home);
        date = findViewById(R.id.date);
        pieChart = findViewById (R.id.pieChartExpenses);
        download = findViewById (R.id.downloadButton);

        list =  findViewById (R.id.listViewTab);
        graph =  findViewById (R.id.graphViewTab);

        take.setVisibility (View.INVISIBLE);
        pay.setVisibility (View.INVISIBLE);

        myListView = findViewById(R.id.transactionsListViewT);
        TextView home = findViewById (R.id.homeTextViewT);

        graphView (myListView);
        setTList ();

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext (), DateCheck.class);
                startActivity (intent);
                finish ();
            }
        });

        pieChart.setOnChartValueSelectedListener (new OnChartValueSelectedListener ( ) {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                PieEntry pe = (PieEntry) e;
                Log.i("Диаграмма",pe.getLabel());
                Snackbar snackbar = Snackbar.make (pieChart, pe.getLabel (), Snackbar.LENGTH_SHORT);
                snackbar.show ();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext (), Pdf.class);
                startActivity(intent);
                finish ();

            }
        });


        take.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext (), IncomeActivity.class);
                startActivity (intent);
                finish ();
            }
        });


        pay.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext (), ExpensesActivity.class);
                startActivity (intent);
                finish ();
            }
        });


        home.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext (), Dashboard.class);
                startActivity (intent);
                finish ();
            }
        });

        home.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext (), Dashboard.class);
                startActivity (intent);
                finish ();
            }
        });

    }

    public  void setTList() {
        ArrayList<String> transact = databaseHelper.getTransactions ();
        if (transact.size () == 0) {
            Toast.makeText (TransactionsActivity.this, "Нет транзакций.", Toast.LENGTH_LONG).show ( );

        } else {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, transact);

            myListView.setAdapter (arrayAdapter);

            final ArrayList<String> finalTransact = transact;
            myListView.setOnItemClickListener (new AdapterView.OnItemClickListener ( ) {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Toast.makeText (TransactionsActivity.this, "Транзакции  : " + finalTransact.get (position), Toast.LENGTH_LONG).show ( );
                }
            });
        }
    }



    public void listView (View view) {
        graph.setEnabled (true);
        list.setEnabled (false);
        setTList ();
        pieChart.setVisibility (View.INVISIBLE);
        myListView.setVisibility (View.VISIBLE);

    }

    public void graphView(View view) {
        graph.setEnabled (false);
        list.setEnabled (true);
        pieChart.setVisibility (View.VISIBLE);
        myListView.setVisibility (View.INVISIBLE);
        setTList ();
        pieChart.setHoleRadius (15f);
        pieChart.setTransparentCircleRadius (15f);

        HashMap<String, Integer> hash = databaseHelper.getExpenses ();

        List<PieEntry> value = new ArrayList<>();
        for (Map.Entry h:hash.entrySet ()) {
            Integer val = (Integer) h.getValue ();
            String label = h.getKey ().toString ();

            value.add (new PieEntry (val, label));
        }

        PieDataSet pieDataSet = new PieDataSet (value, "Расходы") ;
        PieData pieData = new PieData (pieDataSet);
        pieChart.setData (pieData);

        pieChart.setCenterText ("Расходы");
        pieChart.setCenterTextSize (10f);
        pieChart.getLegend ().setEnabled(false);
        pieChart.getDescription ().setEnabled (false);
        pieChart.setEntryLabelColor (Color.BLACK);
        pieChart.setEntryLabelTextSize (10f);
        pieChart.animateXY (800,800);

        pieDataSet.setColors (ColorTemplate.MATERIAL_COLORS);
    }

}

