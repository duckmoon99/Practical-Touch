package com.example.practicaltouch.database;

import androidx.annotation.Nullable;

import java.util.List;

public class AppIdsList {
    private List<String> listOfAppIds;

    public AppIdsList(List<String> listOfAppIds) {
        this.listOfAppIds = listOfAppIds;
    }

    public List<String> getListOfAppIds() {
        return listOfAppIds;
    }

    public void setListOfAppIds(List<String> listOfAppIds) {
        this.listOfAppIds = listOfAppIds;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        } else {
            AppIdsList obj2 = (AppIdsList) obj;
            assert obj != null;
            List<String> list = ((AppIdsList) obj).getListOfAppIds();
            return listOfAppIds.equals(list);
        }
    }
}
