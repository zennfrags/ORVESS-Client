package io.orvess.client;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FloatingMenuService extends Service {
    private static final String CHANNEL_ID = "orvess_overlay";
    private WindowManager windowManager;
    private View bubble;
    private View panel;
    private WindowManager.LayoutParams bubbleParams;
    private WindowManager.LayoutParams panelParams;
    private boolean menuVisible = false;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        startForeground(7, buildNotification());
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        addBubble();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private void addBubble() {
        if (bubble != null) return;
        TextView button = new TextView(this);
        button.setText("O");
        button.setTextColor(Color.WHITE);
        button.setTextSize(20);
        button.setTypeface(Typeface.DEFAULT_BOLD);
        button.setGravity(Gravity.CENTER);
        button.setBackgroundColor(Color.rgb(28, 28, 34));
        bubble = button;

        bubbleParams = new WindowManager.LayoutParams(dp(54), dp(54), overlayType(),
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        bubbleParams.gravity = Gravity.TOP | Gravity.START;
        bubbleParams.x = dp(16);
        bubbleParams.y = dp(180);

        button.setOnTouchListener(new View.OnTouchListener() {
            private int startX, startY;
            private float downX, downY;
            private boolean moved;
            @Override public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = bubbleParams.x; startY = bubbleParams.y;
                        downX = event.getRawX(); downY = event.getRawY(); moved = false;
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) (event.getRawX() - downX);
                        int dy = (int) (event.getRawY() - downY);
                        if (Math.abs(dx) > dp(4) || Math.abs(dy) > dp(4)) moved = true;
                        bubbleParams.x = Math.max(0, startX + dx);
                        bubbleParams.y = Math.max(0, startY + dy);
                        try { windowManager.updateViewLayout(bubble, bubbleParams); } catch (Exception ignored) {}
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (!moved) toggleMenu();
                        return true;
                }
                return true;
            }
        });
        windowManager.addView(bubble, bubbleParams);
    }

    private void toggleMenu() {
        if (menuVisible) { removePanel(); return; }
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(16), dp(12), dp(16), dp(12));
        box.setBackgroundColor(Color.rgb(17, 17, 21));

        TextView title = new TextView(this);
        title.setText("ORVESS MENU");
        title.setTextColor(Color.WHITE);
        title.setTextSize(15);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        box.addView(title);

        TextView info = new TextView(this);
        info.setText("Native overlay active\nGame data integration: not connected");
        info.setTextColor(Color.LTGRAY);
        info.setTextSize(12);
        LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(-2, -2);
        infoParams.topMargin = dp(8);
        infoParams.bottomMargin = dp(8);
        box.addView(info, infoParams);

        Button hide = new Button(this);
        hide.setText("Minimize menu");
        box.addView(hide);
        hide.setOnClickListener(v -> removePanel());

        Button stop = new Button(this);
        stop.setText("Stop ORVESS overlay");
        box.addView(stop);
        stop.setOnClickListener(v -> stopSelf());

        panel = box;
        panelParams = new WindowManager.LayoutParams(dp(240), -2, overlayType(),
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        panelParams.gravity = Gravity.TOP | Gravity.START;
        panelParams.x = bubbleParams.x;
        panelParams.y = bubbleParams.y + dp(60);
        windowManager.addView(panel, panelParams);
        menuVisible = true;
    }

    private void removePanel() {
        if (panel != null) {
            try { windowManager.removeView(panel); } catch (Exception ignored) {}
            panel = null;
        }
        menuVisible = false;
    }

    private int overlayType() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                : WindowManager.LayoutParams.TYPE_PHONE;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "ORVESS floating menu", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(channel);
        }
    }

    private Notification buildNotification() {
        Notification.Builder builder = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                ? new Notification.Builder(this, CHANNEL_ID) : new Notification.Builder(this);
        return builder.setContentTitle("ORVESS is running")
                .setContentText("Tap the floating O button to open the menu")
                .setSmallIcon(android.R.drawable.ic_menu_view)
                .build();
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override public void onDestroy() {
        removePanel();
        if (bubble != null) {
            try { windowManager.removeView(bubble); } catch (Exception ignored) {}
            bubble = null;
        }
        super.onDestroy();
    }

    @Override public IBinder onBind(Intent intent) { return null; }
}
