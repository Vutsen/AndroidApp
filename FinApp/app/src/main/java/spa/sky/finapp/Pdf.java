package spa.sky.finapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class Pdf extends AppCompatActivity {
    private static final String TAG = "ExelCreatorActivity";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;

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
                try {
                    createPdfWrapper();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void createPdfWrapper() throws IOException, DocumentException {

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("Вам необходимо разрешить доступ к хранилищу данных",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
        } else {
            createPdf();
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.download)
                            .setContentTitle(pdfname)
                            .setContentText("Загрузка завершена.");

            Intent notificationIntent = new Intent(this, SimpleMail.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent);

            // Add as notification
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, builder.build());

            Intent intent1 = new Intent (getApplicationContext (), SimpleMail.class);
            Toast.makeText (getApplicationContext (), "Отчет в формате PDF успешно загружен.",Toast.LENGTH_SHORT).show ();
            startActivity (intent1);
            finish ();
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("ОК", okListener)
                .setNegativeButton("Закрыть", null)
                .create()
                .show();
    }

    private void createPdf() throws IOException, DocumentException {

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "Создан новый каталог для PDF");
        }

        final String FONT = "/assets/fonts/arial.ttf";
        BaseFont z=BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font c = new Font(z,16f,Font.NORMAL);

        pdfname = "FinAppReport"+Utils.pdfNumber+".pdf";
        Utils.pdfNumber = Utils.pdfNumber + 1;
        File pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
        OutputStream output = new FileOutputStream (pdfFile);
        Document document = new Document(PageSize.A4);
        PdfPTable table = new PdfPTable(new float[]{3, 3, 3, 3});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(50);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(new PdfPCell(new Phrase("Тег транзакции",c)));
        table.addCell(new PdfPCell(new Phrase("Сумма в рублях",c)));
        table.addCell(new PdfPCell(new Phrase("Дата транзакции",c)));
        table.addCell(new PdfPCell(new Phrase("Доходы / Расходы",c)));
        table.setHeaderRows(1);



        list = databaseHelper.getTransactionsPdf ();
        for (Transactions t : list) {

            String tag = t.getTag ();
            long amnt = t.getAmount ();
            String dat = t.getCreated_at ();


            String exin;
            if(t.getExin () == 0) {
                exin = "Расходы";
            }
            else
                exin = "Доходы";
            Log.i("pdf",t.toString ());
            table.addCell(new PdfPCell(new Phrase(tag,c)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(amnt),c)));
            table.addCell(new PdfPCell(new Phrase(dat,c)));
            table.addCell(new PdfPCell(new Phrase(exin,c)));

        }

        PdfWriter.getInstance(document, output);
        document.open();
        document.add(new Paragraph (" Транзакции  " + Utils.userName + "\n\n", c));
        document.add(table);
        document.close();

    }
}


