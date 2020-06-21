package com.example.practicaltouch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class BubbleImageView extends ImageView {
    private boolean open;

    public BubbleImageView(Context context) {
        super(context);
        open = false;
        setImageResource(R.drawable.logo);
        setLayoutParams(new ViewGroup.LayoutParams(150, 150));
        setOnLongClickListener(view -> true);
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
