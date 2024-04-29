package com.amar.photostyle.custom_views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class CustomImageView extends AppCompatImageView {

    public static Bitmap currentBitmap;

    public CustomImageView(@NonNull Context context) {
        super(context);
    }

    public CustomImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        currentBitmap=bm;
    }

    @Override
    public void setImageURI(@Nullable Uri uri) {
        super.setImageURI(uri);
    }

    @Override
    public void setBackgroundResource(int resId) {
        super.setBackgroundResource(resId);
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(background);
    }
    public static Bitmap getCurrentBitmap(){
        Bitmap bitmap= currentBitmap;
        return bitmap;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = getDrawable();
        if (d != null) {
            int w = MeasureSpec.getSize(widthMeasureSpec);
            int h = w * d.getIntrinsicHeight() / d.getIntrinsicWidth();
            setMeasuredDimension(w, h);
        } else super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
