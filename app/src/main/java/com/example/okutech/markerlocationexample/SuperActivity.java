package com.example.okutech.markerlocationexample;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Description
 *
 * @author Abhilash Chikara
 * @version 1.0
 * @since 8/1/17
 */

public class SuperActivity extends AppCompatActivity {

    protected ProgressDialog dialog;

    protected void showDialog(String message) {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
        }
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if ((dialog != null) && dialog.isShowing())
            dialog.dismiss();
        dialog = null;
    }

    protected void hideDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }
}
