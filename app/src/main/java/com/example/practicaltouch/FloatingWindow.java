package com.example.practicaltouch;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;

public class FloatingWindow extends Service {
    final String tag = "floatingWindow";
    private static String CHANNEL_ID = "com.example.practicaltouch.channel";

    private static boolean started = false;
    WindowManager windowManager;
    LinearLayout frontLayer;
    LinearLayout backLayer;
    LinearLayout trayLayer;
    LinearLayout appTray;
    HorizontalScrollView appTrayContainer;

    Point screenSize = new Point();
    ArrayList<String> appList;
    BubbleImageView openapp;
    ImageView crossIcon;
    WindowManager.LayoutParams params;
    WindowManager.LayoutParams updatepar;
    private PackageManager packageManager;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        started = true;

        NotificationChannel channel = null;
        Intent homeIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, homeIntent, 0);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            int importance = NotificationManager.IMPORTANCE_LOW;
            String name = "Status";
            String description = "Shows if Practical Touch is running.";
            channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.mipmap.logo_foreground)
                .setContentTitle("Practical Touch")
                .setContentText("Practical Touch is running.")
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        appList = intent.getStringArrayListExtra("com.example.practicaltouch.addedApp");
        assert appList != null;
        openapp = new BubbleImageView(this);

        frontLayer.addView(openapp);
        windowManager.addView(frontLayer, params);

        openapp.setOnTouchListener(new View.OnTouchListener() {
            double x;
            double y;
            double px;
            double py;
            boolean moved = false;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        x = updatepar.x;
                        y = updatepar.y;
                        px = motionEvent.getRawX();
                        py = motionEvent.getRawY();
                        backLayer.addView(crossIcon);
                        break;
                    case MotionEvent.ACTION_UP:
                        backLayer.removeView(crossIcon);
                        windowManager.updateViewLayout(frontLayer, updatepar);
                        if (Math.abs(screenSize.y - updatepar.y) <= 350 && Math.abs(updatepar.x - screenSize.x / 2) <= 300) {
                            stopSelf();
                        } else {
                            if (updatepar.x >= screenSize.x / 2) {
                                updatepar.x = screenSize.x - 150;
                            } else {
                                updatepar.x = 0;
                            }
                            windowManager.updateViewLayout(frontLayer, updatepar);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        updatepar.x = (int) (x-(motionEvent.getRawX()-px));
                        updatepar.y = (int) (y+(motionEvent.getRawY()-py));
                        windowManager.updateViewLayout(frontLayer, updatepar);
                    default:
                        break;
                }
                return false;
            }
        });

        openapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (openapp.isOpen()) {
                    appTrayContainer.removeView(appTray);
                } else {
                    appTrayContainer.addView(appTray);
                }
                openapp.toggle();
            }
        });

        appTray = new AppTray(this, appList, packageManager);
        appTrayContainer = new HorizontalScrollView(this);

        trayLayer.addView(appTrayContainer);
        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        windowManager.getDefaultDisplay().getSize(screenSize);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        packageManager = getPackageManager();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        assert windowManager != null;

        windowManager.getDefaultDisplay().getSize(screenSize);
        frontLayer = new LinearLayout(this);
        frontLayer.setBackgroundColor(Color.TRANSPARENT);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        frontLayer.setLayoutParams(layoutParams);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                        ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                        : WindowManager.LayoutParams.TYPE_PHONE),
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.END | Gravity.TOP;
        updatepar = params;

        backLayer = new LinearLayout(this);
        backLayer.setBackgroundColor(Color.TRANSPARENT);
        backLayer.setLayoutParams(layoutParams);
        
        final WindowManager.LayoutParams crossParam = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                        ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                        : WindowManager.LayoutParams.TYPE_PHONE),
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        crossIcon = new ImageView(this);
        crossIcon.setImageResource(R.mipmap.floating_cross_foreground);
        crossIcon.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        crossParam.gravity = Gravity.BOTTOM;

        windowManager.addView(backLayer, crossParam);

        trayLayer = new LinearLayout(this);
        trayLayer.setBackgroundColor(Color.TRANSPARENT);
        trayLayer.setLayoutParams(layoutParams);

        final WindowManager.LayoutParams trayParam = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                        ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                        : WindowManager.LayoutParams.TYPE_PHONE),
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        trayParam.gravity = Gravity.TOP | Gravity.END;

        windowManager.addView(trayLayer, trayParam);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        started = false;
        windowManager.removeView(backLayer);
        windowManager.removeView(frontLayer);
        windowManager.removeView(trayLayer);
    }

    public static boolean hasStarted() {
        return started;
    }
}
