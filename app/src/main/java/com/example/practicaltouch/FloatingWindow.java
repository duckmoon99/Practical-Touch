package com.example.practicaltouch;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.util.Log;

import androidx.annotation.Nullable;

import static java.lang.Math.max;

public class FloatingWindow extends Service {

    private final static String TAG1 = "openapp_position";
    private final static String TAG2 = "distance";
    WindowManager wm;
    LinearLayout ll;
    LinearLayout ll2;
    Point screenSize = new Point();
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(screenSize);
        ll = new LinearLayout(this);
        ll.setBackgroundColor(Color.TRANSPARENT);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setLayoutParams(layoutParams);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);


        params.gravity = Gravity.RIGHT | Gravity.TOP;
        params.x = 16;
        params.y = 16;

        final ImageView openapp = new ImageView(this);
        openapp.setImageResource(R.mipmap.ic_launcher_round);
        ViewGroup.LayoutParams butnparams = new ViewGroup.LayoutParams(
                150,150);
        openapp.setLayoutParams(butnparams);

        ll2 = new LinearLayout(this);
        ll2.setBackgroundColor(Color.TRANSPARENT);
        ll2.setLayoutParams(layoutParams);


        final WindowManager.LayoutParams crossParam = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        final ImageView crossIcon = new ImageView(this);
        crossIcon.setImageResource(R.mipmap.floating_cross_foreground);
        crossIcon.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        crossParam.gravity = Gravity.BOTTOM | Gravity.BOTTOM;
        crossParam.x = 16;
        crossParam.y = 16;

        ll.addView(openapp);
        wm.addView(ll2,crossParam);
        wm.addView(ll,params);

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
                        ll2.addView(crossIcon);
                        break;
                    case MotionEvent.ACTION_UP:
                        ll2.removeView(crossIcon);
                        wm.updateViewLayout(ll,updatepar);
                        Log.i(TAG2, String.valueOf(screenSize.y - updatepar.y));
                        Log.i(TAG2, String.valueOf(Math.abs(updatepar.x - screenSize.x/2)));
                        if(screenSize.y - updatepar.y <= 400 && Math.abs(updatepar.x - screenSize.x/2) <= 150) {
                            onDestroy();
                        } else {
                            if (updatepar.x >= screenSize.x / 2) {
                                updatepar.x = screenSize.x - 170;
                            } else {
                                updatepar.x = 16;
                            }
                            wm.updateViewLayout(ll,updatepar);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        updatepar.x = (int) (x-(motionEvent.getRawX()-px));
                        updatepar.y = (int) (y+(motionEvent.getRawY()-py));
                        wm.updateViewLayout(ll,updatepar);
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
                wm.updateViewLayout(ll,params);
                Intent home = new Intent(FloatingWindow.this,MainActivity.class);
                home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(home);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();

        try {
            wm.removeView(ll2);
        } catch (Exception e) {}
        wm.removeView(ll);
    }
}
