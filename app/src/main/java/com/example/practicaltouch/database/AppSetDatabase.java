package com.example.practicaltouch.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {AppSet.class}, version = 2)
public abstract class AppSetDatabase extends RoomDatabase {
    private static AppSetDatabase instance;

    public abstract AppSetDAO appSetDAO();

    public static synchronized AppSetDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppSetDatabase.class, "appset_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private AppSetDAO appSetDAO;

        private PopulateDBAsyncTask(AppSetDatabase database) {
            appSetDAO = database.appSetDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Insert prelaunched appsets
            return null;
        }
    }
}
