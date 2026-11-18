package com.example.thuchanhapp3;

import android.app.Activity;
import android.view.View;

/** Giữ nội dung bên trong thanh hệ thống trên các phiên bản Android mới. */
public abstract class BaseActivity extends Activity {
    @Override public void setContentView(int layoutResId) {
        super.setContentView(layoutResId);
        View content = findViewById(android.R.id.content);
        content.setOnApplyWindowInsetsListener((view, insets) -> {
            view.setPadding(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(),
                    insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom());
            return insets.consumeSystemWindowInsets();
        });
        content.requestApplyInsets();
    }
}
