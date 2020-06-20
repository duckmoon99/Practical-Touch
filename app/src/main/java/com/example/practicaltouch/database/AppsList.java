package com.example.practicaltouch.database;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

import com.example.practicaltouch.ui.main.AppDrawerItem;

import java.util.ArrayList;
import java.util.List;

public class AppsList {
    private static volatile AppsList instance;
    private List<ResolveInfo> listOfInstalledApps;
    private List<AppDrawerItem> listOfAppDrawerItem;
    private PackageManager packageManager;

    private AppsList(PackageManager packageManager) {
        if (instance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
        this.packageManager = packageManager;
        setUpResolveInfo();
        setUpAppSets();
        this.packageManager = null;
    }

    public static AppsList getInstance(PackageManager packageManager) {
        if (instance == null) {
            synchronized (AppsList.class) {
                if (instance == null) instance = new AppsList(packageManager);
            }

        }
        return instance;
    }

    private void setUpResolveInfo() {
        final List<ResolveInfo> installedAppsList = getLaunchableApps();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            installedAppsList.sort((a,b) -> a.loadLabel(packageManager).toString().compareTo(b.loadLabel(packageManager).toString()));
        }
        this.listOfInstalledApps = installedAppsList;
    }

    private List<ResolveInfo> getLaunchableApps() {
        return packageManager.queryIntentActivities(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER), 0);
    }

    public List<AppDrawerItem> getListOfAppDrawerItems() {
        return listOfAppDrawerItem;
    }
    public ResolveInfo getResolveInfoAt(int position) {
        return listOfInstalledApps.get(position);
    }

    private void setUpAppSets() {
        int count = listOfInstalledApps.size();
        listOfAppDrawerItem = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ResolveInfo resolveInfo = listOfInstalledApps.get(i);
            listOfAppDrawerItem.add(new AppDrawerItem
                    (packageManager.getApplicationIcon(resolveInfo.activityInfo.applicationInfo),
                            (String) resolveInfo.loadLabel(packageManager)));
        }

    }
}
