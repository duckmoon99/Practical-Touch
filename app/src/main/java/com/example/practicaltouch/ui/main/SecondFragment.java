package com.example.practicaltouch.ui.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.practicaltouch.MainActivity;
import com.example.practicaltouch.R;
import com.example.practicaltouch.database.AppIdsList;
import com.example.practicaltouch.database.AppSet;
import com.example.practicaltouch.database.AppSetViewModel;
import com.example.practicaltouch.databinding.FragmentCreatenewTabBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SecondFragment extends Fragment {

    private FragmentCreatenewTabBinding binding;
    private AppSetViewModel appSetViewModel;

    private PackageManager packageManager;
    private ArrayList<String> listOfAppIds = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreatenewTabBinding.inflate(inflater, container, false);
        appSetViewModel = ((MainActivity) Objects.requireNonNull(getActivity())).getAppSetViewModel();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        Log.d(TAG, "onViewCreated: BEFORE VIEWMODEL created");
//        appSetViewModel = ViewModelProvider.AndroidViewModelFactory
//                .getInstance(Objects.requireNonNull(this.getActivity()).getApplication()).create(AppSetViewModel.class);
//        Log.d(TAG, "onViewCreated: AFTER VIEWMODEL created");

        packageManager = Objects.requireNonNull(getActivity()).getPackageManager();
        final HorizontalScrollView scroll = binding.scrollbar;

        final List<ResolveInfo> installedAppsList = getLaunchableApps();
        binding.appDrawer.setAdapter(new AppAdapter(getActivity(), installedAppsList, packageManager));

        binding.appDrawer.setNumColumns(4);

        binding.appDrawer.setOnItemClickListener((parent, view12, position, id) -> {
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
                binding.appTray.addView(view2);
                view2.setOnClickListener(v -> {
                    binding.appTray.removeView(view2);
                    listOfAppIds.remove(appId);
                });
                listOfAppIds.add(appId);
            }
        });

        binding.launchButton.setOnClickListener(view1 -> {
            if (!listOfAppIds.isEmpty()) {
                saveAppSet();
                ((MainActivity) Objects.requireNonNull(getActivity())).start_stop(listOfAppIds);
                listOfAppIds.clear();
                binding.appTray.removeAllViews();
                Toast.makeText(getActivity(), "App launched!", Toast.LENGTH_SHORT).show();
                appSetViewModel.setScrollUpTrue();
            } else {
                Toast.makeText(getActivity(), "Please select at least an application.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.appTray.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            int newRight = right - oldRight;
            if (newRight > 0) {
                scroll.fullScroll(View.FOCUS_RIGHT);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private List<ResolveInfo> getLaunchableApps() {
        return packageManager.queryIntentActivities(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER), 0);
    }

    /*
    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    private List<PackageInfo> getInstalledApps() {
        List<PackageInfo> packageList = packageManager
                .getInstalledPackages(PackageManager.GET_PERMISSIONS);

        final List<PackageInfo> packageList1 = new ArrayList<>();

        //To filter out System apps
        for (PackageInfo pi : packageList) {
            if(!isSystemPackage(pi)){
                packageList1.add(pi);
            }
        }

        return packageList1;
    }
    */

    private void saveAppSet() {
        String defaultText = "My Apps";
        AppIdsList appIdsList = new AppIdsList(listOfAppIds);
        AppSet appSet = new AppSet(defaultText, appIdsList);
        appSetViewModel.insert(appSet);
    }

}

