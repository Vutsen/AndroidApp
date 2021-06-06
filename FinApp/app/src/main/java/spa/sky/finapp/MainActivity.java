package spa.sky.finapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity {

    private static FragmentManager fragmentManager;

    public DBHelper databaseHelper;

    {
        databaseHelper = new DBHelper(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if( getIntent().getBooleanExtra("Exit me", false)){
            finish();
            return;
        }
        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                    .replace(R.id.frameContainer, new Login_Fragment(),
                            Utils.Login_Fragment).commit();
        }

        findViewById(R.id.close_activity).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        finish();

                    }
                });

    }


    protected void replaceLoginFragment() {
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.frameContainer, new Login_Fragment(),
                        Utils.Login_Fragment).commit();
    }

    @Override
    public void onBackPressed() {

        Fragment SignUp_Fragment = fragmentManager
                .findFragmentByTag(Utils.SignUp_Fragment);

        if (SignUp_Fragment != null)
            replaceLoginFragment();
        else
            super.onBackPressed();
    }

    public void moveToDashboard() {
        Intent intent = new Intent (getApplicationContext (), Dashboard.class);
        startActivity (intent);
    }

}