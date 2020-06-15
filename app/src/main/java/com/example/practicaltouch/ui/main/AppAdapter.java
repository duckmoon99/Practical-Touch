package com.example.practicaltouch.ui.main;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practicaltouch.R;
import com.example.practicaltouch.databinding.FragmentCreatenewTabBinding;

import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {

    private List<ResolveInfo> resolveList;
    private LayoutInflater layoutInflater;
    private PackageManager packageManager;
    private ArrayList<String> listOfAppIds = new ArrayList<>();
    private FragmentCreatenewTabBinding binding;

    AppAdapter(Activity context, List<ResolveInfo> resolveList, PackageManager packageManager, FragmentCreatenewTabBinding binding) {
        this.resolveList = resolveList;
        this.packageManager = packageManager;
        this.layoutInflater = context.getLayoutInflater();
        this.binding = binding;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView appIcon;
        TextView appName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            appIcon = itemView.findViewById(R.id.appIcon);
            appName = itemView.findViewById(R.id.appName);
            itemView.setOnClickListener(v -> {
                String appId = resolveList.get(getAdapterPosition()).activityInfo.packageName;
                if (listOfAppIds.contains(appId)) return;

                Drawable icon = null;
                try {
                    icon = packageManager.getApplicationIcon(appId);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                final ImageView view2 = (ImageView) layoutInflater.inflate(R.layout.appicon, binding.appTray, false);
                view2.setImageDrawable(icon);
                view2.setPadding(16, 8, 16, 8);
                view2.setOnClickListener(v2 -> {
                    binding.appTray.removeView(view2);
                    listOfAppIds.remove(appId);
                });
                binding.appTray.addView(view2);
                listOfAppIds.add(appId);
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.app_drawer_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResolveInfo resolveInfo = resolveList.get(position);
        holder.appName.setText(resolveInfo.loadLabel(packageManager));
        holder.appIcon.setImageDrawable(packageManager.getApplicationIcon(resolveInfo.activityInfo.applicationInfo));
    }

    @Override
    public int getItemCount() {
        return resolveList.size();
    }

    public ArrayList<String> getListOfAppIds() {
        return listOfAppIds;
    }

}
