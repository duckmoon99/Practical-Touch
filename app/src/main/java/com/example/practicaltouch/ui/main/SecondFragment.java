package com.example.practicaltouch.ui.main;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

    private FragmentCreatenewTabBinding binding;
    private AppSetViewModel appSetViewModel;
    private PackageManager packageManager;
    private ArrayList<String> listOfAppIds;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreatenewTabBinding.inflate(inflater, container, false);
        appSetViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(AppSetViewModel.class);
        listOfAppIds = new ArrayList<>();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        packageManager = Objects.requireNonNull(getActivity()).getPackageManager();

        AppAdapter appAdapter = new AppAdapter(getActivity(),
                AppsList.getInstance(packageManager).getListOfAppDrawerItems(), this);
        binding.appDrawer.setAdapter(appAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        //binding.appDrawer.setLayoutManager(new GridLayoutManager(getActivity(),calculateNoOfColumns(getActivity())));
        binding.appDrawer.setLayoutManager(gridLayoutManager);
        binding.appDrawer.setHasFixedSize(true);

        binding.launchButton.setOnClickListener(view1 -> {
            if (listOfAppIds.isEmpty()) {
                Toast.makeText(getActivity(), "Please select at least an application", Toast.LENGTH_SHORT).show();
            }
            else if (binding.inputName.getText().toString().trim().equals("")) {
                Toast.makeText(getActivity(), "Please input a title", Toast.LENGTH_SHORT).show();
            }
            else {
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
        String appSetName = binding.inputName.getText().toString();
        AppIdsList appIdsList = new AppIdsList(listOfAppIds);
        AppSet appSet = new AppSet(appSetName, appIdsList);
        appSetViewModel.insert(appSet);
        binding.inputName.setText(R.string.my_apps);
    }

    /*
    public int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }
     */

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

        final ImageView view2 = (ImageView) getLayoutInflater().inflate(R.layout.appicon, binding.appTray, false);
        view2.setImageDrawable(icon);
        view2.setOnClickListener(v2 -> {
            binding.appTray.removeView(view2);
            listOfAppIds.remove(appId);
        });
        binding.appTray.addView(view2);
        listOfAppIds.add(appId);
    }
}

