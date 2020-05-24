package com.example.practicaltouch.database;

import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.List;

class AppIdsListConverter {
    @TypeConverter
    public AppIdsList storedStringToAppIdsList(String value) {
        List<String> appIdsList = Arrays.asList(value.split("\\s*,\\s*"));
        return new AppIdsList(appIdsList);
    }

    @TypeConverter
    public String appIdsListToStoredString(AppIdsList list) {
        StringBuilder value = new StringBuilder();

        for (String appName : list.getListOfAppIds()) {
            value.append(appName).append(",");
        }

        return value.toString();
    }
}
