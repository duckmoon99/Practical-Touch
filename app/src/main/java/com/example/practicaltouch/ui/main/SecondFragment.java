package com.example.practicaltouch.ui.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.practicaltouch.MainActivity;
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

        packageManager = Objects.requireNonNull(getActivity()).getPackageManager();
        final List<ResolveInfo> installedAppsList = getLaunchableApps();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            installedAppsList.sort((a,b) -> a.loadLabel(packageManager).toString().compareTo(b.loadLabel(packageManager).toString()));
        }

        AppAdapter appAdapter = new AppAdapter(getActivity(), installedAppsList, packageManager, binding);
        binding.appDrawer.setAdapter(appAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        binding.appDrawer.setLayoutManager(gridLayoutManager);

        //Point screenSize = new Point();
        //binding.appDrawer.getDisplay().getSize(screenSize);
        //Log.i(TAG, String.valueOf(binding.appDrawer.getColumnWidth()));

        binding.launchButton.setOnClickListener(view1 -> {
            ArrayList<String> listOfAppIds = appAdapter.getListOfAppIds();
            if (!listOfAppIds.isEmpty()) {
                saveAppSet(listOfAppIds);
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
                binding.scrollbar.fullScroll(View.FOCUS_RIGHT);
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

    private void saveAppSet(ArrayList<String> listOfAppIds) {
        String defaultText = "My Apps";
        AppIdsList appIdsList = new AppIdsList(listOfAppIds);
        AppSet appSet = new AppSet(defaultText, appIdsList);
        appSetViewModel.insert(appSet);
    }
}

