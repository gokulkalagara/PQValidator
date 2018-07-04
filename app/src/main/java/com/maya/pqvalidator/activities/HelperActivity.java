package com.maya.pqvalidator.activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.maya.pqvalidator.R;
import com.maya.pqvalidator.interfaces.IActivity;

public class HelperActivity extends AppCompatActivity implements IActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);
    }

    @Override
    public void changeTitle(String name) {

    }

    @Override
    public void showSnackBar(String message, int type) {

    }


    @Override
    public Activity activity() {
        return this;
    }
}
