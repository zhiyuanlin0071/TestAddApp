package com.mktech.testaddapp.entity;

import android.graphics.drawable.Drawable;

import static android.R.attr.id;

/**
 * Created by ken on 2017/2/10.
 */

public class HotAppInfo {

    private static final String TAG = HotAppInfo.class.getSimpleName();

    private String name;

    private String packageName;

    private Drawable drawableIcon;


    public Drawable getDrawableIcon() {
        return drawableIcon;
    }

    public void setDrawableIcon(Drawable drawableIcon) {
        this.drawableIcon = drawableIcon;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public HotAppInfo( String name, String packageName, Drawable drawableIcon) {
        this.name = name;
        this.packageName = packageName;
        this.drawableIcon = drawableIcon;
    }

    public HotAppInfo() {
    }
  
}
