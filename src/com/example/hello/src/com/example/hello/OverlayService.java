package com.example.hello;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
public class OverlayService extends Service {
    private WindowManager wm;
    private LinearLayout root;
    @Override
    public IBinder onBind(Intent i) { return null; }
    @Override
    public void onCreate() {
        super.onCreate();
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(0xEE1A1A1A);
        root.setPadding(20, 20, 20, 20);
        LinearLayout bar = new LinearLayout(this);
        bar.setOrientation(LinearLayout.HORIZONTAL);
        bar.setGravity(Gravity.CENTER_VERTICAL);
        TextView title = new TextView(this);
        title.setText("KRUTYSH MENU");
        title.setTextColor(0xFF00FF00);
        title.setTextSize(18);
        title.setPadding(0, 0, 30, 0);
        bar.addView(title);
        Button x = new Button(this);
        x.setText("X");
        x.setTextColor(Color.WHITE);
        x.setBackgroundColor(0xFFFF0000);
        bar.addView(x);
        root.addView(bar);
        addToggle(root, "WallHack");
        addToggle(root, "Chams");
        addToggle(root, "ESP");
        addToggle(root, "Speed");
        addToggle(root, "Fly");
        addToggle(root, "No Recoil");
        int flag = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_PHONE;
        final WindowManager.LayoutParams p = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            flag, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        p.gravity = Gravity.TOP | Gravity.START;
        p.x = 50; p.y = 150;
        x.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { stopSelf(); }
        });
        bar.setOnTouchListener(new View.OnTouchListener() {
            private int ix, iy; private float tx, ty;
            @Override public boolean onTouch(View v, MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ix = p.x; iy = p.y; tx = e.getRawX(); ty = e.getRawY(); return true;
                    case MotionEvent.ACTION_MOVE:
                        p.x = ix + (int)(e.getRawX() - tx); p.y = iy + (int)(e.getRawY() - ty);
                        wm.updateViewLayout(root, p); return true;
                }
                return false;
            }
        });
        wm.addView(root, p);
    }
    private void addToggle(LinearLayout parent, String name) {
        Button b = new Button(this);
        b.setText(name + ": OFF");
        b.setTextColor(Color.WHITE);
        b.setBackgroundColor(0xFF444444);
        b.setTextSize(12);
        b.setOnClickListener(new View.OnClickListener() {
            private boolean on = false;
            @Override public void onClick(View v) {
                on = !on; Button btn = (Button) v;
                if (on) { btn.setText(name + ": ON"); btn.setBackgroundColor(0xFF00AA00); }
                else { btn.setText(name + ": OFF"); btn.setBackgroundColor(0xFF444444); }
            }
        });
        parent.addView(b);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (root != null) wm.removeView(root);
    }
                  }
