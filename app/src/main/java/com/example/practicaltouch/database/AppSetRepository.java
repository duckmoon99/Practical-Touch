package com.example.practicaltouch.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

class AppSetRepository {
    private AppSetDAO appSetDAO;
    private LiveData<List<AppSet>> allAppSets;

    AppSetRepository(Application application) {
        AppSetDatabase database = AppSetDatabase.getInstance(application);
        appSetDAO = database.appSetDAO();
        allAppSets = appSetDAO.getAllAppSets();
    }

    void insert(AppSet appSet) {
        new InsertAppSetAsyncTask(appSetDAO).execute(appSet);
    }

    void update(AppSet appSet) {
        new UpdateAppSetAsyncTask(appSetDAO).execute(appSet);
    }

    void delete(AppSet appSet) {
        new DeleteAppSetAsyncTask(appSetDAO).execute(appSet);
    }

    void deleteAllAppSets() {
        new DeleteAllAppSetsAsyncTask(appSetDAO).execute();
    }

    LiveData<List<AppSet>> getAllAppSets() {
        return allAppSets;
    }

    private static class InsertAppSetAsyncTask extends AsyncTask<AppSet, Void, Void> {
        private AppSetDAO appSetDAO;

        private InsertAppSetAsyncTask(AppSetDAO appSetDAO) {
            this.appSetDAO = appSetDAO;
        }

        @Override
        protected Void doInBackground(AppSet... appSets) {
            appSetDAO.insert(appSets[0]);
            return null;
        }
    }
    private static class UpdateAppSetAsyncTask extends AsyncTask<AppSet, Void, Void> {
        private AppSetDAO appSetDAO;

        private UpdateAppSetAsyncTask(AppSetDAO appSetDAO) {
            this.appSetDAO = appSetDAO;
        }

        @Override
        protected Void doInBackground(AppSet... appSets) {
            appSetDAO.update(appSets[0]);
            return null;
        }
    }
    private static class DeleteAppSetAsyncTask extends AsyncTask<AppSet, Void, Void> {
        private AppSetDAO appSetDAO;

        private DeleteAppSetAsyncTask(AppSetDAO appSetDAO) {
            this.appSetDAO = appSetDAO;
        }

        @Override
        protected Void doInBackground(AppSet... appSets) {
            appSetDAO.delete(appSets[0]);
            return null;
        }
    }
    private static class DeleteAllAppSetsAsyncTask extends AsyncTask<AppSet, Void, Void> {
        private AppSetDAO appSetDAO;

        private DeleteAllAppSetsAsyncTask(AppSetDAO appSetDAO) {
            this.appSetDAO = appSetDAO;
        }

        @Override
        protected Void doInBackground(AppSet... appSets) {
            appSetDAO.deleteAllAppSets();
            return null;
        }
    }
}
