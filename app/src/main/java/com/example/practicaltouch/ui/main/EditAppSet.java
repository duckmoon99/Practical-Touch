package com.example.practicaltouch.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.practicaltouch.MainActivity;
import com.example.practicaltouch.R;

import com.example.practicaltouch.database.AppsList;
import com.example.practicaltouch.databinding.EditAppsetBinding;

import java.util.ArrayList;
import java.util.Objects;

public class EditAppSet extends AppCompatActivity implements AppAdapter.OnAppListener {
    public static final String EXTRA_ID = "com.example.practicaltouch.ui.main.EXTRA_ID";
    public static final String EXTRA_NAME = "com.example.practicaltouch.ui.main.EXTRA_NAME";
    public static final String EXTRA_APPSETS = "com.example.practicaltouch.ui.main.EXTRA_APPSETS";

    private EditAppsetBinding binding;
    private ArrayList<String> listOfAppIds;
    private PackageManager packageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(this, "Loading app menu", Toast.LENGTH_SHORT).show();

        binding = EditAppsetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        packageManager = getPackageManager();

        binding.editInputName.setText(intent.getStringExtra(EXTRA_NAME));
        listOfAppIds = intent.getStringArrayListExtra(EXTRA_APPSETS);

        assert listOfAppIds != null;
        int count = listOfAppIds.size();
        for (int i = 0; i < count; i++) {
            String appId = listOfAppIds.get(i);
            Drawable icon = null;
            try {
                icon = getPackageManager().getApplicationIcon(listOfAppIds.get(i));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            final ImageView view2 = (ImageView) getLayoutInflater().inflate(R.layout.appicon, binding.editAppTray, false);
            view2.setImageDrawable(icon);
            view2.setOnClickListener(v -> {
                binding.editAppTray.removeView(view2);
                listOfAppIds.remove(appId);
            });
            binding.editAppTray.addView(view2);
        }

        new Thread(() -> {
            AppAdapter appAdapter = new AppAdapter(EditAppSet.this,
                    AppsList.getInstance(getPackageManager()).getListOfAppDrawerItems(), EditAppSet.this);
            binding.editAppDrawer.post(() -> {
                binding.editAppDrawer.setAdapter(appAdapter);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(EditAppSet.this, MainActivity.calculateNoOfColumns(getApplicationContext()));
                binding.editAppDrawer.setLayoutManager(gridLayoutManager);
                binding.editAppDrawer.setHasFixedSize(true);
            });
        }).start();

        binding.editAppTray.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            int newRight = right - oldRight;
            if (newRight > 0) {
                binding.editScrollbar.fullScroll(View.FOCUS_RIGHT);
            }
        });


        setSupportActionBar(binding.editToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        setTitle("Edit AppSet");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_appset_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_appset:
                saveAppSet();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveAppSet() {
        String appSetName = binding.editInputName.getText().toString().trim();
        if (listOfAppIds.isEmpty()) {
            Toast.makeText(this, "Please select at least an application", Toast.LENGTH_SHORT).show();
        }
        else if (appSetName.equals("")) {
            Toast.makeText(this, "Please input a title", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent data = new Intent();
            data.putExtra(EXTRA_NAME, appSetName);
            data.putExtra(EXTRA_APPSETS, listOfAppIds);
            int id = getIntent().getIntExtra(EXTRA_ID, -1);
            if (id != -1) {
                data.putExtra(EXTRA_ID, id);
            }
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    public void onAppClick(int position) {
        ResolveInfo resolveInfo = AppsList.getInstance(packageManager).getResolveInfoAt(position);
        String appId = resolveInfo.activityInfo.packageName;

        if (listOfAppIds.contains(appId)) return;

        Drawable icon = null;
        try {
            icon = packageManager.getApplicationIcon(appId);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        final ImageView view2 = (ImageView) getLayoutInflater().inflate(R.layout.appicon, binding.editAppTray, false);
        view2.setImageDrawable(icon);
        view2.setOnClickListener(v2 -> {
            binding.editAppTray.removeView(view2);
            listOfAppIds.remove(appId);
        });
        binding.editAppTray.addView(view2);
        listOfAppIds.add(appId);
    }
}