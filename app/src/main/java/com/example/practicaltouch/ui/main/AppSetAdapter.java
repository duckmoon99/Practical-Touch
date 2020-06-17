package com.example.practicaltouch.ui.main;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practicaltouch.MainActivity;
import com.example.practicaltouch.R;
import com.example.practicaltouch.database.AppSet;

import java.util.ArrayList;

public class AppSetAdapter extends ListAdapter<AppSet, AppSetAdapter.AppSetHolder> {

    private PackageManager packageManager;
    private LayoutInflater layoutInflater;
    private MainActivity activity;

    AppSetAdapter(PackageManager packageManager, LayoutInflater layoutInflater, FragmentActivity activity) {
        super(DIFF_CALLBACK);
        this.packageManager = packageManager;
        this.layoutInflater = layoutInflater;
        this.activity = (MainActivity) activity;
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

        holder.appTray.removeAllViews();
        holder.appTrayName.setText(currentAppSet.getName());

        final ArrayList<String> listOfAppIds = new ArrayList<>(currentAppSet.getAppIdsList().getListOfAppIds());
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
            holder.appTray.addView(view2);
        }

        holder.dropDownMenu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(activity, holder.dropDownMenu);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.edit_menu_item:
                        Toast.makeText(activity, "Edit!", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.delete_menu_item:
                        activity.getAppSetViewModel().delete(currentAppSet);
                        Toast.makeText(activity, "Appset deleted", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            });
            popupMenu.show();
        });

        holder.launchButton.setOnClickListener(v -> activity.start_stop(listOfAppIds));
    }

    AppSet getAppSetAt(int position) {
        return getItem(position);
    }

    class AppSetHolder extends RecyclerView.ViewHolder {
        private TextView appTrayName;
        private LinearLayout appTray;
        private Button launchButton;
        private ImageView dropDownMenu;

        AppSetHolder(@NonNull View itemView) {
            super(itemView);
            appTrayName = itemView.findViewById(R.id.appset_name);
            appTray = itemView.findViewById(R.id.appset_linearlayout);
            launchButton = itemView.findViewById(R.id.appset_launchbutton);
            dropDownMenu = itemView.findViewById(R.id.dropdown_menu_button);
        }
    }

}
