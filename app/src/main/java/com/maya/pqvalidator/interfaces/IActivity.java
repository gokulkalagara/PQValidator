package com.maya.pqvalidator.interfaces;

import android.app.Activity;

public interface IActivity
{
    public void changeTitle(String name);
    public void showSnackBar(String message,int type);
    public Activity activity();

}
