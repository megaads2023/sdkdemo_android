package com.example.adssample;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

public class BaseActivity extends Activity {

    public void printInfo(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        Log.e("ads-sample", msg);
    }
}
