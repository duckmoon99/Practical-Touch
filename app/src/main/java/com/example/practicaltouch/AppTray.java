package com.example.practicaltouch;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

public class AppTray extends LinearLayout {
    public AppTray(final Context context, List<String> appList, final PackageManager packageManager) {
        super(context);
        for (final String s: appList) {
            ImageView current = new ImageView(context);
            try {
                current.setImageDrawable(packageManager.getApplicationIcon(s));
                current.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent startApp = packageManager.getLaunchIntentForPackage(s);
                        context.startActivity(startApp);
                    }
                });
                current.setLayoutParams(new ViewGroup.LayoutParams(180, 180));
                current.setPadding(16, 0, 16, 0);
                addView(current);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
