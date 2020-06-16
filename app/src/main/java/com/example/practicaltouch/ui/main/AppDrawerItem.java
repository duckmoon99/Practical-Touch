package com.example.practicaltouch.ui.main;

import android.graphics.drawable.Drawable;

public class AppDrawerItem {
    private Drawable appIcon;
    private String appName;

    public AppDrawerItem(Drawable appIcon, String appName) {
        this.appIcon = appIcon;
        this.appName = appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
