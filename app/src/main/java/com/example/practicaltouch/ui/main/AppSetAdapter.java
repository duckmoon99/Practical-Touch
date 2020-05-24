package com.example.practicaltouch.ui.main;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practicaltouch.R;
import com.example.practicaltouch.database.AppSet;

import java.util.List;

public class AppSetAdapter extends ListAdapter<AppSet, AppSetAdapter.AppSetHolder> {

    private PackageManager packageManager;
    private LayoutInflater layoutInflater;

    AppSetAdapter(PackageManager packageManager, LayoutInflater layoutInflater) {
        super(DIFF_CALLBACK);
        this.packageManager = packageManager;
        this.layoutInflater = layoutInflater;
    }

    private static final DiffUtil.ItemCallback<AppSet> DIFF_CALLBACK = new DiffUtil.ItemCallback<AppSet>() {
        @Override
        public boolean areItemsTheSame(@NonNull AppSet oldItem, @NonNull AppSet newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull AppSet oldItem, @NonNull AppSet newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getAppIdsList().equals(newItem.getAppIdsList());
        }
    };

    @NonNull
    @Override
    public AppSetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.appset_item, parent, false);
        return new AppSetHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AppSetHolder holder, int position) {
        AppSet currentAppSet = getItem(position);

        holder.appTrayName.setText(currentAppSet.getName());

        List<String> listOfAppIds = currentAppSet.getAppIdsList().getListOfAppIds();
        int count = listOfAppIds.size();
        for (int i = 0; i < count; i++) {
            Drawable icon = null;
            try {
                icon = packageManager.getApplicationIcon(listOfAppIds.get(i));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            final ImageView view2 = (ImageView) layoutInflater.inflate(R.layout.appicon, holder.appTray, false);
            view2.setImageDrawable(icon);
            view2.setPadding(16,8,16,8);
            holder.appTray.addView(view2);
        }
    }

    AppSet getAppSetAt(int position) {
        return getItem(position);
    }

    class AppSetHolder extends RecyclerView.ViewHolder {
        private TextView appTrayName;
        private LinearLayout appTray;

        AppSetHolder(@NonNull View itemView) {
            super(itemView);
            appTrayName = itemView.findViewById(R.id.appset_name);
            appTray = itemView.findViewById(R.id.appset_linearlayout);

        }
    }

}
