package com.example.practicaltouch.ui.main;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practicaltouch.R;
import com.example.practicaltouch.databinding.AppDrawerItemBinding;
import com.example.practicaltouch.databinding.FragmentCreatenewTabBinding;

import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {

    private List<ResolveInfo> resolveList;
    private LayoutInflater layoutInflater;
    private PackageManager packageManager;
    private ArrayList<String> listOfAppIds = new ArrayList<>();
    private FragmentCreatenewTabBinding fragmentBinding;

    AppAdapter(Activity context, List<ResolveInfo> resolveList, PackageManager packageManager, FragmentCreatenewTabBinding fragmentBinding) {
        this.resolveList = resolveList;
        this.packageManager = packageManager;
        this.layoutInflater = context.getLayoutInflater();
        this.fragmentBinding = fragmentBinding;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private AppDrawerItemBinding binding;

        public ViewHolder(@NonNull AppDrawerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(v -> {
                String appId = resolveList.get(getAdapterPosition()).activityInfo.packageName;
                if (listOfAppIds.contains(appId)) return;

                Drawable icon = null;
                try {
                    icon = packageManager.getApplicationIcon(appId);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                final ImageView view2 = (ImageView) layoutInflater.inflate(R.layout.appicon, fragmentBinding.appTray, false);
                view2.setImageDrawable(icon);
                view2.setOnClickListener(v2 -> {
                    fragmentBinding.appTray.removeView(view2);
                    listOfAppIds.remove(appId);
                });
                fragmentBinding.appTray.addView(view2);
                listOfAppIds.add(appId);
            });
        }
        public void bind(AppDrawerItem appDrawerItem) {
            binding.setAppDrawerItem(appDrawerItem);
            binding.executePendingBindings();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AppDrawerItemBinding binding = AppDrawerItemBinding.inflate(layoutInflater, parent, false);
        //View view = layoutInflater.inflate(R.layout.app_drawer_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResolveInfo resolveInfo = resolveList.get(position);
        AppDrawerItem appDrawerItem = new AppDrawerItem
                (packageManager.getApplicationIcon(resolveInfo.activityInfo.applicationInfo),
                        (String) resolveInfo.loadLabel(packageManager));
        holder.bind(appDrawerItem);
        //ResolveInfo resolveInfo = resolveList.get(position);
        //holder.binding.appName.setText(resolveInfo.loadLabel(packageManager));
        //holder.binding.appIcon.setImageDrawable(packageManager.getApplicationIcon(resolveInfo.activityInfo.applicationInfo));
    }

    @Override
    public int getItemCount() {
        return resolveList.size();
    }

    public ArrayList<String> getListOfAppIds() {
        return listOfAppIds;
    }

}
