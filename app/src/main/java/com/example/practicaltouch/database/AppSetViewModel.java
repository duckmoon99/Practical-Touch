package com.example.practicaltouch.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class AppSetViewModel extends AndroidViewModel {

    private AppSetRepository repository;
    private LiveData<List<AppSet>> allAppSets;
    private MutableLiveData<Boolean> scrollUp;
    private boolean loaded;

    public boolean isLoaded(){
        return loaded;
    }

    public void loaded(){
        loaded = true;
    }

    private ArrayList<String> listOfAppIds = new ArrayList<>();

    public ArrayList<String> getListOfAppIds() {
        return listOfAppIds;
    }

    public AppSetViewModel(@NonNull Application application) {
        super(application);
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
