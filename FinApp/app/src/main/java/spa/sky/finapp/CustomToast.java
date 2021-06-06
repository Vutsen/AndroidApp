package spa.sky.finapp;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class CustomToast {
    public void Show_Toast(Context context, View view, String error) {


        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.error_layout,
                (ViewGroup) view.findViewById(R.id.toast_root));


        TextView text = (TextView) layout.findViewById(R.id.error);
        text.setText(error);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);// Set


        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);

        toast.show();
    }
}
