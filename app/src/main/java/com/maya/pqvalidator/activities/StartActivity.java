package com.maya.pqvalidator.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;


import com.maya.pqvalidator.R;
import com.maya.pqvalidator.constants.Permission;
import com.maya.pqvalidator.fragments.home.HomeFragment;
import com.maya.pqvalidator.fragments.start.AllowPermissionFragment;
import com.maya.pqvalidator.interfaces.IActivity;
import com.maya.pqvalidator.utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartActivity extends AppCompatActivity implements IActivity
{


    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setUp();
    }



    private void setUp()
    {
        toolbar.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(activity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, AllowPermissionFragment.newInstance(null,null)).commit();
            }
            else
            {
                openQRCodeScan();
            }
        }
        else
        {
            openQRCodeScan();
        }

    }

    @Override
    public void changeTitle(String name) {

    }

    @Override
    public void showSnackBar(String message, int type)
    {
        Utility.showSnackBar(activity(),coordinatorLayout,message,type);
    }


    @Override
    public Activity activity()
    {
        return this;
    }


    private boolean checkPermissions(Permission permission)
    {
        switch (permission)
        {

            case CAMERA:
                if (ContextCompat.checkSelfPermission(activity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(activity(), new String[]{android.Manifest.permission.CAMERA}, Utility.generateRequestCodes().get("REQUEST_CAMERA"));
                    return false;
                }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == Utility.generateRequestCodes().get("REQUEST_CAMERA"))
        {
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED)
            {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if(shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA) == false)
                    {
                        Toast.makeText(this, "Open permissions and give all the permissions in order to access the app", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                    else
                    {
                        checkPermissions(Permission.CAMERA);
                    }
                }
            }
            else
            {
                openQRCodeScan();
            }
        }
    }

    public void openQRCodeScan()
    {
        if(checkPermissions(Permission.CAMERA))
        {
            toolbar.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, HomeFragment.newInstance(null,null)).commit();
        }
    }
}
