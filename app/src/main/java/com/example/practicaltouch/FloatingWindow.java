package com.example.practicaltouch;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Objects;

public class FloatingWindow extends Service {
    final String tag = "floatingWindow";

    private static boolean started = false;
    WindowManager windowManager;
    LinearLayout frontLayer;
    LinearLayout backLayer;
    Point screenSize = new Point();
    ArrayList<String> appList;
    ImageView openapp;
    ImageView crossIcon;
    WindowManager.LayoutParams params;
    private PackageManager packageManager;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        started = true;

        appList = intent.getStringArrayListExtra("com.example.practicaltouch.addedApp");
        assert appList != null;
        openapp = new ImageView(this);
        try {
            openapp.setImageDrawable(packageManager.getApplicationIcon(appList.get(0)));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ViewGroup.LayoutParams butnparams = new ViewGroup.LayoutParams(
                150,150);
        openapp.setLayoutParams(butnparams);
        frontLayer.addView(openapp);
        windowManager.addView(frontLayer, params);

        openapp.setOnTouchListener(new View.OnTouchListener() {
            WindowManager.LayoutParams updatepar = params;
            double x;
            double y;
            double px;
            double py;

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
                        windowManager.updateViewLayout(frontLayer,updatepar);
                        if(Math.abs(screenSize.y - updatepar.y) <= 450 && Math.abs(updatepar.x - screenSize.x/2) <= 300) {
                            stopSelf();
                        } else {
                            if (updatepar.x >= screenSize.x / 2) {
                                updatepar.x = screenSize.x - 170;
                            } else {
                                updatepar.x = 16;
                            }
                            windowManager.updateViewLayout(frontLayer,updatepar);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        updatepar.x = (int) (x-(motionEvent.getRawX()-px));
                        updatepar.y = (int) (y+(motionEvent.getRawY()-py));
                        windowManager.updateViewLayout(frontLayer,updatepar);
                    default:
                        break;
                }
                return false;
            }
        });


        openapp.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view){
                return true;
            }
        });

        openapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                params.x = 16;
                params.y = 16;
                windowManager.updateViewLayout(frontLayer,params);
                Intent startApp = packageManager.getLaunchIntentForPackage(appList.get(0));
                startActivity(startApp);
//                Intent home = new Intent(FloatingWindow.this, MainActivity.class);
//                home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(home);
            }
        });

        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
        params.x = 16;
        params.y = 16;

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
        crossParam.x = 16;
        crossParam.y = 16;

        windowManager.addView(backLayer,crossParam);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        started = false;
        windowManager.removeView(backLayer);
        windowManager.removeView(frontLayer);
    }

    public static boolean hasStarted() {
        return started;
    }
}
