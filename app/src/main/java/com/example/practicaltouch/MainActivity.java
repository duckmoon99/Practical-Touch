package com.example.practicaltouch;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.practicaltouch.database.AppSetViewModel;
import com.example.practicaltouch.databinding.ActivityMainBinding;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.AppCompatActivity;

import com.example.practicaltouch.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    ActivityMainBinding binding;
    AppSetViewModel appSetViewModel;
    AlertDialog alert;
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this);
        binding.viewPager.setAdapter(sectionsPagerAdapter);
        new TabLayoutMediator(binding.tabs, binding.viewPager, (tab, position) -> tab.setText(TAB_TITLES[position])).attach();

        setSupportActionBar(binding.toolbar);

        appSetViewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication()).create(AppSetViewModel.class);
    }

    public void start_stop(ArrayList<String> s) {
        if (checkPermission()) {
            if (FloatingWindow.hasStarted()) {
                stopService(new Intent(MainActivity.this, FloatingWindow.class));
            }
            Toast.makeText(this, "Application Set launched!", Toast.LENGTH_SHORT).show();
            Intent startIntent = new Intent(MainActivity.this, FloatingWindow.class).putStringArrayListExtra("com.example.practicaltouch.addedApp", s);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(
                        startIntent);
            } else {
                startService(startIntent);
            }

        } else {
            reqPermission();
        }
    }

    private void reqPermission(){
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Screen overlay detected");
        alertBuilder.setMessage("Enable 'Draw over other apps' in your system setting.");
        alertBuilder.setPositiveButton("OPEN SETTINGS", (dialog, which) -> {
            // warning below
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent,RESULT_OK);
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

    public AppSetViewModel getAppSetViewModel() {
        return appSetViewModel;
    }
}