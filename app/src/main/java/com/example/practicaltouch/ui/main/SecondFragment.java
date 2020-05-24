package com.example.practicaltouch.ui.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.practicaltouch.MainActivity;
import com.example.practicaltouch.R;
import com.example.practicaltouch.database.AppIdsList;
import com.example.practicaltouch.database.AppSet;
import com.example.practicaltouch.database.AppSetViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SecondFragment extends Fragment {
    private static final String TAG = "SecondFragment";

    private LinearLayout appTray;
    private PackageManager packageManager;
    private Button launchButton;
    private AppSetViewModel appSetViewModel;

    private List<String> listOfAppIds = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_createnew_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appSetViewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(Objects.requireNonNull(this.getActivity()).getApplication()).create(AppSetViewModel.class);

        appTray = Objects.requireNonNull(getView()).findViewById(R.id.myLinearLayout);
        GridView appDrawer = getView().findViewById(R.id.myGrid);
        packageManager = Objects.requireNonNull(getActivity()).getPackageManager();

        final List<ResolveInfo> installedAppsList = getLaunchableApps();
        appDrawer.setAdapter(new AppAdapter(getActivity(), installedAppsList, packageManager));

        Point screenSize = new Point();
        appDrawer.getDisplay().getSize(screenSize);
        appDrawer.setNumColumns(screenSize.x/162);

        appDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Drawable icon = packageManager.getApplicationIcon(installedAppsList.get(position).activityInfo.applicationInfo);

                final String appId = installedAppsList.get(position).activityInfo.packageName;
                listOfAppIds.add(appId);

                LayoutInflater inflater = getLayoutInflater();
                final ImageView view2 = (ImageView) inflater.inflate(R.layout.appicon, parent, false);
                view2.setImageDrawable(icon);
                view2.setPadding(16,8,16,8);
                appTray.addView(view2);
                view2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        appTray.removeView(view2);
                        listOfAppIds.remove(appId);
                    }
                });
            }
        });


        launchButton = getView().findViewById(R.id.launchButton);
        launchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isListEmpty()) {
                    saveAppSet();

                    listOfAppIds = new ArrayList<>();
                    appTray.removeAllViews();

                    ((MainActivity) Objects.requireNonNull(getActivity())).start_stop();
                    Toast.makeText(getActivity(), "App launched!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private List<ResolveInfo> getLaunchableApps() {
        return packageManager.queryIntentActivities(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER), 0);
    }

    private boolean isListEmpty() {
        return appTray.getChildCount() == 0;
    }

    private void saveAppSet() {
        String defaultText = "My Apps";
        AppIdsList appIdsList = new AppIdsList(listOfAppIds);
        AppSet appSet = new AppSet(defaultText, appIdsList);
        appSetViewModel.insert(appSet);
    }
}
