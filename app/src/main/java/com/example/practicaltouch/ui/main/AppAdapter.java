package com.example.practicaltouch.ui.main;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.practicaltouch.R;

import java.util.List;

public class AppAdapter extends BaseAdapter {
    //private List<PackageInfo> packageList;
    private List<ResolveInfo> resolveList;
    private Activity context;
    private PackageManager packageManager;
    private static final String TAG = "AppAdapter";

    AppAdapter(Activity context, List<ResolveInfo> resolveList,
                      PackageManager packageManager) {
        super();
        this.context = context;
        this.resolveList = resolveList;
        this.packageManager = packageManager;
    }

    /*
        private class ViewHolder {
            ImageView imageView;
            ViewHolder(final ImageView imageView) {
                this.imageView = imageView;
            }
        }
     */
    public int getCount() {
        return resolveList.size();
    }

    public ResolveInfo getItem(int position) {
        return resolveList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageView viewHolder;
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.appicon, parent,false);

            viewHolder = convertView.findViewById(R.id.appIconHolder);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ImageView) convertView.getTag();
        }

        ResolveInfo resolveInfo = getItem(position);

        Drawable appIcon = packageManager
                .getApplicationIcon(resolveInfo.activityInfo.applicationInfo);

        viewHolder.setImageDrawable(appIcon);
        viewHolder.setPadding(16,8,16,8);

        return convertView;
    }

}
