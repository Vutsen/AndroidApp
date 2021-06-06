package spa.sky.finapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;

public class SimpleMail extends Activity {
    DBHelper databaseHelper;
    Button send;
    TextView home;


    public SimpleMail() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        databaseHelper = new DBHelper(this);
        home = findViewById(R.id.homeTextViewT1);
        send = findViewById(R.id.email);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext (), Dashboard.class);
                startActivity (intent);
                finish ();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getmail();
            }
        });
    }

    public void getmail(){
        String address = databaseHelper.getEmail();
        String subject = "Отчет";
        String list = "Отчет из приложения FinApp";
        Intent emailIntent = new Intent("android.intent.action.SEND");
        Utils.pdfNumber = Utils.pdfNumber - 1;
        emailIntent.setType("plain/text");
        emailIntent.putExtra("android.intent.extra.EMAIL", new String[]{address});
        emailIntent.putExtra("android.intent.extra.SUBJECT", subject);
        emailIntent.putExtra("android.intent.extra.TEXT", list);
        emailIntent.putExtra("android.intent.extra.STREAM",
                FileProvider.getUriForFile(this,BuildConfig.APPLICATION_ID +".provider",
                        new File( Environment.getExternalStorageDirectory() + "/Documents/FinAppReport"+Utils.pdfNumber + ".pdf")));
        emailIntent.setType("text/document");
        try {
            SimpleMail.this.startActivity(Intent.createChooser(emailIntent, "Отправка письма..."));
        }catch(android.content.ActivityNotFoundException ex) {
            Toast.makeText(SimpleMail.this, "Почтовый клиент не установлен.", Toast.LENGTH_SHORT).show();
        };
    }
}
