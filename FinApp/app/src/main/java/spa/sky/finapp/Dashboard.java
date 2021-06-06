package spa.sky.finapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {
    int totals;
    DBHelper databaseHelper;
    Button take,pay;
    TextView income, expense,total;
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_dashboard);

        income =  findViewById (R.id.income);
        expense = findViewById (R.id.expense);
        take = findViewById (R.id.takeButton);
        total = findViewById(R.id.total);
        pay = findViewById (R.id.payButton);
        pieChart =  findViewById (R.id.pieChart);

        databaseHelper = new DBHelper(this);

        databaseHelper.setIncomeExpenses ();

        income.setText ("₽ " + Utils.income);
        expense.setText ("₽ " + Utils.expense);
        totals = Utils.income - Utils.expense;

        total.setText("₽ " + totals );

        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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



        TextView showAll =  findViewById (R.id.showAllTextView);
        showAll.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext (), TransactionsActivity.class);
                startActivity (intent);
                finish ();

            }
        });

        getTList();
        getTPieChart();

    }



    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Выйти", true);
        startActivity(intent);
        finish();
    }

    public void getTList() {
        ListView myListView = findViewById(R.id.transactionsListDash);

        ArrayList<String> transact = databaseHelper.getTransactions ();
        if (transact.size ( ) == 0) {
            Toast.makeText (Dashboard.this, "Нет транзакций", Toast.LENGTH_LONG).show ( );

        }

        else {
            List<String> transactMini = new ArrayList<> ( );
            transactMini.add (transact.get (0));
            if (transact.size () >= 2)
                transactMini.add (transact.get (1));

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, transactMini);

            myListView.setAdapter (arrayAdapter);

            myListView.setOnItemClickListener (new AdapterView.OnItemClickListener ( ) {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Toast.makeText (Dashboard.this, "Нажмите * Показать все *, чтобы просмотреть все транзакции.", Toast.LENGTH_LONG).show ( );
                }
            });

        }
    }
    public void getTPieChart() {

        List<PieEntry> value = new ArrayList<> ( );
        if (Utils.expense != 0 && Utils.income !=0) {

            pieChart.setHoleRadius (15f);
            pieChart.setTransparentCircleRadius (15f);

            value.add (new PieEntry (Utils.expense, "Расходы"));
            value.add (new PieEntry (Utils.income, "Доходы"));

            PieDataSet pieDataSet = new PieDataSet (value, "Транзакции");
            PieData pieData = new PieData (pieDataSet);
            pieChart.setData (pieData);

            pieChart.setContentDescription (null);
            pieChart.getLegend ( ).setEnabled (false);
            pieChart.getDescription ( ).setEnabled (false);
            pieChart.setEntryLabelTextSize (10f);
            pieChart.animateXY (1000, 1000);
            pieDataSet.setColors (ColorTemplate.MATERIAL_COLORS);
        }
    }
}
