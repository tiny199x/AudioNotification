package com.example.administrator.audionotification;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Intent volume;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button t = (Button) findViewById(R.id.Active);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNotification(v);
            }
        });
        volume = new Intent(this,VolumeControl.class);
        volume.setAction("StartVolumeControl");
        Button t2 = (Button) findViewById(R.id.DeActive);
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destroy(v);
            }
        });

    }
    public void createNotification(View v){
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int result = getResources().getDimensionPixelSize(resourceId);
        volume.putExtra("height",result);

        startService(volume);

    }
    public void destroy(View v){
        if(volume!=null){
            stopService(volume);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        volume = new Intent(this,VolumeControl.class);
        volume.setAction("StartVolumeControl");
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int result = getResources().getDimensionPixelSize(resourceId);
        volume.putExtra("height",(int) (result*0.66));
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        Toast.makeText(getApplicationContext(),display.getWidth()+" "+display.getHeight(),Toast.LENGTH_SHORT).show();
        startService(volume);
    }
}
