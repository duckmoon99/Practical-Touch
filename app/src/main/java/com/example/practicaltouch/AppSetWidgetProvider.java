package com.example.practicaltouch;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.ArrayList;

public class AppSetWidgetProvider extends AppWidgetProvider {
    public static final String ACTION_LAUNCH = "com.example.practicaltouch.actionLaunch";
    public static final String EXTRA_ITEM_POSITION = "com.example.practicaltouch.extraItemPosition";
    public static final String EXTRA_ITEM_APPSET = "com.example.practicaltouch.extraItemAppset";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Intent mainIntent = new Intent(context, MainActivity.class);
            PendingIntent mainPendingIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);

            Intent serviceIntent = new Intent(context, AppSetWidgetService.class);
            //serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            //serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

            Intent clickIntent = new Intent(context, AppSetWidgetProvider.class);
            clickIntent.setAction(ACTION_LAUNCH);
            PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appset_widget);
            views.setOnClickPendingIntent(R.id.appset_textview_widget, mainPendingIntent);
            views.setRemoteAdapter(R.id.appset_listview_widget, serviceIntent);
            views.setPendingIntentTemplate(R.id.appset_listview_widget, clickPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_LAUNCH.equals(intent.getAction())) {
            //int clickPosition = intent.getIntExtra(EXTRA_ITEM_POSITION, 0);
            ArrayList<String> s = intent.getStringArrayListExtra(EXTRA_ITEM_APPSET);
            if (FloatingWindow.hasStarted()) {
                context.stopService(new Intent(context, FloatingWindow.class));
            }
            Toast.makeText(context, "Application Set launched!", Toast.LENGTH_SHORT).show();
            Intent startIntent = new Intent(context, FloatingWindow.class).putStringArrayListExtra("com.example.practicaltouch.addedApp", s);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(startIntent);
            } else {
                context.startService(startIntent);
            }

        }
        super.onReceive(context, intent);
    }
}
