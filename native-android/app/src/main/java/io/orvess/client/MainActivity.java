package io.orvess.client;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private int white = Color.WHITE;
    private int muted = Color.rgb(180, 180, 190);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showScreen();
    }

    private void showScreen() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(dp(28), dp(28), dp(28), dp(28));
        root.setBackgroundColor(Color.rgb(10, 10, 12));

        TextView title = new TextView(this);
        title.setText("ORVESS");
        title.setTextColor(white);
        title.setTextSize(30);
        title.setGravity(Gravity.CENTER);
        title.setLetterSpacing(0.12f);
        root.addView(title, matchWrap());

        TextView subtitle = new TextView(this);
        subtitle.setText("Native floating-menu prototype");
        subtitle.setTextColor(muted);
        subtitle.setTextSize(14);
        subtitle.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams subParams = matchWrap();
        subParams.topMargin = dp(8);
        subParams.bottomMargin = dp(28);
        root.addView(subtitle, subParams);

        Button start = new Button(this);
        start.setText("Enable and start floating menu");
        root.addView(start, matchWrap());
        start.setOnClickListener(v -> startOverlay());

        Button stop = new Button(this);
        stop.setText("Stop floating menu");
        LinearLayout.LayoutParams stopParams = matchWrap();
        stopParams.topMargin = dp(10);
        root.addView(stop, stopParams);
        stop.setOnClickListener(v -> {
            stopService(new Intent(this, FloatingMenuService.class));
            Toast.makeText(this, "ORVESS floating menu stopped", Toast.LENGTH_SHORT).show();
        });

        TextView note = new TextView(this);
        note.setText("The floating menu is an Android overlay. It does not modify Minecraft or read game data.");
        note.setTextColor(muted);
        note.setTextSize(12);
        note.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams noteParams = matchWrap();
        noteParams.topMargin = dp(24);
        root.addView(note, noteParams);

        setContentView(root);
    }

    private void startOverlay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent permissionIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivity(permissionIntent);
            Toast.makeText(this, "Allow ORVESS to display over other apps, then return and tap Start again.", Toast.LENGTH_LONG).show();
            return;
        }
        Intent service = new Intent(this, FloatingMenuService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(service);
        else startService(service);
        Toast.makeText(this, "ORVESS floating menu started", Toast.LENGTH_SHORT).show();
    }

    private LinearLayout.LayoutParams matchWrap() {
        return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }
}
