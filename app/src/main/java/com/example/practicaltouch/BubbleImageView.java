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
    public boolean moved;

    public BubbleImageView(Context context, int px) {
        super(context);
        open = false;
        moved = false;
        setImageResource(R.drawable.logo);
        setLayoutParams(new ViewGroup.LayoutParams(px, px));
        setAlpha((float) 0.4);
        setOnLongClickListener(v -> {
            if(!moved) {
                context.startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                Toast.makeText(context, "Launching Practical Touch", Toast.LENGTH_SHORT).show();
            }
            return true;
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
