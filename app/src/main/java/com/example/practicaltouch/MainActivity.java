package com.example.practicaltouch;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.practicaltouch.database.AppIdsList;
import com.example.practicaltouch.database.AppSet;
import com.example.practicaltouch.database.AppSetViewModel;
import com.example.practicaltouch.databinding.ActivityMainBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.AppCompatActivity;

import com.example.practicaltouch.ui.main.EditAppSet;
import com.example.practicaltouch.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    AppSetViewModel appSetViewModel;
    AlertDialog alert;
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    public static final int EDIT_APPSET_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this);
        binding.viewPager.setAdapter(sectionsPagerAdapter);
        new TabLayoutMediator(binding.tabs, binding.viewPager, (tab, position) -> tab.setText(TAB_TITLES[position])).attach();

        setSupportActionBar(binding.toolbar);

        appSetViewModel = new ViewModelProvider(this).get(AppSetViewModel.class);
        if(FloatingWindow.hasStarted()){
            bindService(new Intent(this, FloatingWindow.class), connection, 0);
        }
    }

    FloatingWindow mService;
    private boolean mBound = false;

    public void start_stop(ArrayList<String> s) {
        if(!checkPermission()){
            reqPermission();
            return;
        }
        if (mBound) {
            mService.update(s);
        } else {
            Intent startIntent = new Intent(MainActivity.this, FloatingWindow.class).putStringArrayListExtra("com.example.practicaltouch.addedApp", s);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(startIntent);
            } else {
                startService(startIntent);
            }
            bindService(startIntent, connection, 0);
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            FloatingWindow.LocalBinder binder = (FloatingWindow.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    private void reqPermission(){
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Screen overlay detected");
        alertBuilder.setMessage("Enable 'Draw over other apps' in your system setting.");
        alertBuilder.setPositiveButton("OPEN SETTINGS", (dialog, which) -> {
            // warning below
            Intent intent = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
            }
            startActivityForResult(intent, RESULT_OK);
        });

        alert = alertBuilder.create();
        alert.show();
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                reqPermission();
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_appsets:
                // Maybe show a different toast message if theres no appsets?
                appSetViewModel.deleteAllAppSets();
                Toast.makeText(this, "All AppSets deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_APPSET_REQUEST && resultCode == RESULT_OK) {
            assert data != null;
            int id = data.getIntExtra(EditAppSet.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Something went wrong, AppSet can't be updated",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            String name = data.getStringExtra(EditAppSet.EXTRA_NAME);
            AppIdsList appIdsList = new AppIdsList(data.getStringArrayListExtra(EditAppSet.EXTRA_APPSETS));
            AppSet appSet = new AppSet(name, appIdsList);
            appSet.setId(id);
            appSetViewModel.update(appSet);

            Toast.makeText(this, "AppSet updated", Toast.LENGTH_SHORT).show();
        }
    }
  
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 80);
    }
  
    @Override
    protected void onPause() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(this, AppSetWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appset_listview_widget);
        super.onPause();
    }
}