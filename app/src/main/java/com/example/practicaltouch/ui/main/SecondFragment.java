package com.example.practicaltouch.ui.main;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.practicaltouch.MainActivity;
import com.example.practicaltouch.R;
import com.example.practicaltouch.database.AppIdsList;
import com.example.practicaltouch.database.AppSet;
import com.example.practicaltouch.database.AppSetViewModel;
import com.example.practicaltouch.database.AppsList;
import com.example.practicaltouch.databinding.FragmentCreatenewTabBinding;

import java.util.ArrayList;
import java.util.Objects;

public class SecondFragment extends Fragment implements AppAdapter.OnAppListener {

    private static String TAG = "second_fragment";
    private FragmentCreatenewTabBinding binding;
    private AppSetViewModel appSetViewModel;
    private PackageManager packageManager;
    private ArrayList<String> listOfAppIds;
    private String dynamicName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreatenewTabBinding.inflate(inflater, container, false);
        appSetViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(AppSetViewModel.class);
        listOfAppIds = appSetViewModel.getListOfAppIds();
        return binding.getRoot();
    }

    private void loadList() {
        for (String appId : listOfAppIds) {
            addApp(appId);
        }
    }

    //adds an app to the tray (make sure to check if it is already in listOfAppId)
    private void addApp(String appId) {
        try {
            Drawable icon = packageManager.getApplicationIcon(appId);
            final ImageView view2 = (ImageView) getLayoutInflater().inflate(R.layout.appicon, binding.appTray, false);
            view2.setImageDrawable(icon);
            view2.setOnClickListener(v2 -> {
                binding.appTray.removeView(view2);
                listOfAppIds.remove(appId);
            });
            binding.appTray.addView(view2);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(!appSetViewModel.isLoaded()){
            Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
            appSetViewModel.loaded();
        }
        packageManager = Objects.requireNonNull(getActivity()).getPackageManager();
        loadList();
        new Thread(() -> {
            AppAdapter appAdapter = new AppAdapter(getActivity(),
                    AppsList.getInstance(packageManager).getListOfAppDrawerItems(), SecondFragment.this);
            if(binding != null) {
                binding.appDrawer.post(() -> {
                    binding.appDrawer.setAdapter(appAdapter);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), MainActivity.calculateNoOfColumns(getActivity()));
                    binding.appDrawer.setLayoutManager(gridLayoutManager);
                    binding.appDrawer.setHasFixedSize(true);
                });
            }
        }).start();


        binding.launchButton.setOnClickListener(view1 -> {
            if (listOfAppIds.isEmpty()) {
                Toast.makeText(getActivity(), "Please select at least an app", Toast.LENGTH_SHORT).show();
            } else {
                if (binding.inputName.getText().toString().trim().equals("")) {
                    StringBuilder s = new StringBuilder();
                    int count = Math.min(listOfAppIds.size(), 3);
                    for (int i = 0; i < count; i++) {
                        ApplicationInfo ai = null;
                        try {
                            ai = packageManager.getApplicationInfo(listOfAppIds.get(i), 0);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        if(i > 0) s.append(", ");
                        if (ai != null) {
                            s.append((String) packageManager.getApplicationLabel(ai));
                        } else {
                            s.append("Unknown");
                        }
                    }
                    dynamicName = s.toString().trim();
                } else {
                    dynamicName = binding.inputName.getText().toString().trim();
                }
                saveAppSet(listOfAppIds);
                ((MainActivity) Objects.requireNonNull(getActivity())).start_stop(listOfAppIds);
                listOfAppIds.clear();
                binding.appTray.removeAllViews();
                appSetViewModel.setScrollUpTrue();
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

    private void saveAppSet(ArrayList<String> listOfAppIds) {
        AppIdsList appIdsList = new AppIdsList(listOfAppIds);
        AppSet appSet = new AppSet(dynamicName, appIdsList);
        appSetViewModel.insert(appSet);
        binding.inputName.setText("");
    }


    @Override
    public void onAppClick(int position) {
        ResolveInfo resolveInfo = AppsList.getInstance(packageManager).getResolveInfoAt(position);
        String appId = resolveInfo.activityInfo.packageName;
        if (listOfAppIds.contains(appId)) return;
        listOfAppIds.add(appId);
        addApp(appId);
    }
}

