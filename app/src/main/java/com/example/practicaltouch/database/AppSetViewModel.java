package com.example.practicaltouch.database;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class AppSetViewModel extends AndroidViewModel {
    private static final String TAG = "AppSetViewModel";
    
    private AppSetRepository repository;
    private LiveData<List<AppSet>> allAppSets;
    private MutableLiveData<Boolean> scrollUp;

    //private LiveData<List<String>> appTrayList;

    public AppSetViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG, "AppSetViewModel: hi");
        this.repository = new AppSetRepository(application);
        this.allAppSets = repository.getAllAppSets();
        this.scrollUp = new MutableLiveData<>(Boolean.FALSE);
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


}
