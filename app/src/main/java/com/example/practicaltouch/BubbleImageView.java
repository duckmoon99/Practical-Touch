package com.example.practicaltouch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint("AppCompatCustomView")
public class BubbleImageView extends ImageView {
    private boolean open;

    public BubbleImageView(Context context) {
        super(context);
        open = false;
        setImageResource(R.drawable.logo);
        setLayoutParams(new ViewGroup.LayoutParams(150, 150));
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                context.startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                Toast.makeText(context, "Switching App Set", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    public void toggle() {
        if (open) {
            setImageResource(R.drawable.logo);
        } else {
            setImageResource(R.drawable.collapse);
        }
        open = !open;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    public boolean isOpen() {
        return open;
    }

}
