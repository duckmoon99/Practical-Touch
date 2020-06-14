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
import android.widget.TextView;

import com.example.practicaltouch.R;

import java.util.List;

public class AppAdapter extends BaseAdapter {

    private List<ResolveInfo> resolveList;
    private Activity context;
    private PackageManager packageManager;

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

    @Override
    public int getCount() {
        return resolveList.size();
    }

    @Override
    public ResolveInfo getItem(int position) {
        return resolveList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageView viewHolder;
        final TextView textHolder;
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.menu_item, parent, false);
            //convertView = inflater.inflate(R.layout.appicon, parent, false);

            viewHolder = convertView.findViewById(R.id.appIcon);
            textHolder = convertView.findViewById(R.id.appName);
            convertView.setTag(R.id.appIcon,viewHolder);
            convertView.setTag(R.id.appName,textHolder);
        } else {
            viewHolder = (ImageView) convertView.getTag(R.id.appIcon);
            textHolder = (TextView) convertView.getTag(R.id.appName);
        }

        ResolveInfo resolveInfo = getItem(position);

        Drawable appIcon = packageManager
                .getApplicationIcon(resolveInfo.activityInfo.applicationInfo);

        viewHolder.setImageDrawable(appIcon);
        textHolder.setText(resolveInfo.loadLabel(packageManager));

        return convertView;
    }

}
