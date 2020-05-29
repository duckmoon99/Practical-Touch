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
        setImageResource(R.mipmap.logo_foreground);
        setLayoutParams(new ViewGroup.LayoutParams(180, 180));
        setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view){
                return true;
            }
        });
    }

    public void toggle() {
        if (open) {
            setImageResource(R.mipmap.logo_foreground);
        } else {
            setImageResource(R.mipmap.collapse_foreground);
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
