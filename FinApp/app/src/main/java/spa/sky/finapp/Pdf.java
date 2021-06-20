package spa.sky.finapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;



import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;


import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;

public class Pdf extends AppCompatActivity {
    private static String shit= "FinAppReport" + Utils.pdfNumber;
    public static Cell cell;


    ImageView dowlex;
    DBHelper databaseHelper;
    ArrayList<Transactions> list;
    Transactions transactions;
    Context context;
    String pdfname;
    TextView home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        dowlex = findViewById(R.id.downlex);
        context = this;
        transactions = new Transactions ();
        databaseHelper =new DBHelper (this);
        home = findViewById(R.id.home1);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext (), Dashboard.class);
                startActivity (intent);
                finish ();
            }
        });

        dowlex.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                HSSFWorkbook workbook = new HSSFWorkbook();
                HSSFSheet firstSheet = workbook.createSheet("sheet");
                HSSFRow rowA = firstSheet.createRow(Utils.rowNumber);

                cell = rowA.createCell(0);
                cell.setCellValue("Тэг расхода");

                cell = rowA.createCell(1);
                cell.setCellValue("Сумма в рублях");

                cell = rowA.createCell(2);
                cell.setCellValue("Дата транзакции");

                cell = rowA.createCell(3);
                cell.setCellValue("Расходы/Доходы");
                int num = 0;
                for (Transactions t : list = databaseHelper.getTransactionsPdf()) {
                    num = num + 1;
                    HSSFRow rowb = firstSheet.createRow(num);
                    String tag = t.getTag();
                    long amnt = t.getAmount();
                    String dat = t.getCreated_at();

                    String exin;
                    if (t.getExin() == 0) {
                        exin = "Расход";
                    } else
                        exin = "Доходы";
                    cell = rowb.createCell(0);
                    cell.setCellValue(tag);

                    cell = rowb.createCell(1);
                    cell.setCellValue(String.valueOf(amnt));

                    cell = rowb.createCell(2);
                    cell.setCellValue(dat);

                    cell = rowb.createCell(3);
                    cell.setCellValue(exin);
                }
                FileOutputStream fos = null;
                try {
                   /* String str_path = (Environment.getExternalStorageDirectory() +"/Documents").toString(); */
                    File file ;
                    file = new File(Environment.getExternalStorageDirectory() +"/Documents", shit + ".xls");
                    fos = new FileOutputStream(file);
                    workbook.write(fos);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.flush();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(Pdf.this, "Эксель файл создан", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(getApplicationContext (), SimpleMail.class);
                startActivity (intent);
                finish ();
                }
        });

    }
}



