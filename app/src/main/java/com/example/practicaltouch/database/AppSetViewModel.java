package com.example.practicaltouch.database;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class AppSetViewModel extends AndroidViewModel {

    private AppSetRepository repository;
    private LiveData<List<AppSet>> allAppSets;
    private MutableLiveData<Boolean> scrollUp;

    private PackageManager packageManager;
    private List<ResolveInfo> listOfInstalledApps;
    //private LiveData<List<String>> appTrayList;

    public AppSetViewModel(@NonNull Application application) {
        super(application);
        this.repository = new AppSetRepository(application);
        this.allAppSets = repository.getAllAppSets();
        this.scrollUp = new MutableLiveData<>(Boolean.FALSE);
        this.packageManager = application.getPackageManager();
        setUpResolveInfo();
    }

    public void insert(AppSet appSet) {
        repository.insert(appSet);
    }

    public void update(AppSet appSet) {
        repository.update(appSet);
    }

    public void delete(AppSet appSet) {
        repository.delete(appSet);
    }

    public void deleteAllAppSets() {
        repository.deleteAllAppSets();
    }

    public LiveData<List<AppSet>> getAllAppSets() {
        return allAppSets;
    }

    public MutableLiveData<Boolean> getScrollBool() {
        return scrollUp;
    }
    public void setScrollUpTrue() {
        scrollUp.setValue(Boolean.TRUE);
    }
    public void setScrollUpFalse() {
        scrollUp.setValue(Boolean.FALSE);
    }

    public List<ResolveInfo> getListOfInstalledApps() {
        return listOfInstalledApps;
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

}
