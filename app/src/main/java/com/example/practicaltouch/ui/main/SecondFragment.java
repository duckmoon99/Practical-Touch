package com.example.practicaltouch.ui.main;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
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

    private LinearLayout appTray;
    private PackageManager packageManager;
    private AppSetViewModel appSetViewModel;

    private ArrayList<String> listOfAppIds = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_createnew_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button launchButton = Objects.requireNonNull(getView()).findViewById(R.id.launchButton);
        launchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!listOfAppIds.isEmpty()) {
                    saveAppSet();
                    ((MainActivity) Objects.requireNonNull(getActivity())).start_stop(listOfAppIds);
                    listOfAppIds.clear();
                    appTray.removeAllViews();
                    Toast.makeText(getActivity(), "App launched!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Please select at least an application.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        appSetViewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(Objects.requireNonNull(this.getActivity()).getApplication()).create(AppSetViewModel.class);

        appTray = Objects.requireNonNull(getView()).findViewById(R.id.myLinearLayout);
        GridView appDrawer = getView().findViewById(R.id.myGrid);
        packageManager = Objects.requireNonNull(getActivity()).getPackageManager();
        final HorizontalScrollView scroll = getView().findViewById(R.id.scroll);

        final List<ResolveInfo> installedAppsList = getLaunchableApps();
        appDrawer.setAdapter(new AppAdapter(getActivity(), installedAppsList, packageManager));

        Point screenSize = new Point();
        appDrawer.getDisplay().getSize(screenSize);
        appDrawer.setNumColumns(screenSize.x/162);

        appDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String appId = installedAppsList.get(position).activityInfo.packageName;

                if(!listOfAppIds.contains(appId)) {
                    Drawable icon = null;
                    try {
                        icon = packageManager.getApplicationIcon(appId);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    LayoutInflater inflater = getLayoutInflater();
                    final ImageView view2 = (ImageView) inflater.inflate(R.layout.appicon, parent, false);
                    view2.setImageDrawable(icon);
                    view2.setPadding(16, 8, 16, 8);
                    appTray.addView(view2);
                    view2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            appTray.removeView(view2);
                            listOfAppIds.remove(appId);
                        }
                    });
                    listOfAppIds.add(appId);
                }
            }
        });

        appTray.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int lengthChange = right - oldRight;
                if (lengthChange > 0) {
                    scroll.fullScroll(View.FOCUS_RIGHT);
                }
            }
        });
    }

    private List<ResolveInfo> getLaunchableApps() {
        return packageManager.queryIntentActivities(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER), 0);
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    private List<PackageInfo> getInstalledApps() {
        List<PackageInfo> packageList = packageManager
                .getInstalledPackages(PackageManager.GET_PERMISSIONS);

        final List<PackageInfo> packageList1 = new ArrayList<>();

        /*To filter out System apps*/
        for(PackageInfo pi : packageList) {
            if(!isSystemPackage(pi)){
                packageList1.add(pi);
            }
        }

        return packageList1;
    }

    private void saveAppSet() {
        String defaultText = "My Apps";
        AppIdsList appIdsList = new AppIdsList(listOfAppIds);
        AppSet appSet = new AppSet(defaultText, appIdsList);
        appSetViewModel.insert(appSet);
    }
}

