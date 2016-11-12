package com.egci428.a10074.fortunecookies;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class ActivityNewCookie extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private View view;
    private long lastUpdate;
    boolean deviceShaking = false;
    boolean detectShake = true;
    TextView shaked;
    private String[] cookies = {"gradea","lucky","surprise","panic","work"};
    private String[] cookieMsg = {"You will get A","You're Lucky","Something surprise you today","Don't Panic","Work Harder"};
    int i = 0;
    private float[] accel = new float[10];

    ImageView saveButton;
    boolean startDetectShake = false;

    public final static String cookieID = "message";
    public final static String cookieDate = "date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cookie);

        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        shaked = (TextView) findViewById(R.id.shakeSave);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();

        saveButton = (ImageView) findViewById(R.id.saveShakeButton);
        shaked.setText("Press Me");
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetectShake = true;
                shaked.setText("Shake");
            }
        });

    }

    @Override
    public void onSensorChanged(SensorEvent event){
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            if (startDetectShake) getAccelerometer(event);
        }
    }

    private void getAccelerometer(SensorEvent event){
        float[] values = event.values;

        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = System.currentTimeMillis();

        i++;
        if(i >= 10) {i = 0;}
        accel[i] = accelationSquareRoot;
        float meanAccel = 0;
        for (int j = 0;j < 10; j++) {
            meanAccel = meanAccel + accel[j];
        }
        meanAccel = meanAccel/10;
        System.out.println(meanAccel);
        if (meanAccel >= 2) //
        {
            if (actualTime - lastUpdate < 1) {
                return;
            }
            if(detectShake) {
                lastUpdate = actualTime;
                shaked.setText("Shaking");
                deviceShaking = true;
            }
        }
        else{
            if (deviceShaking){
                deviceShaking = false;
                detectShake = false;
                deviceShook();
            }
        }
    }

    private void deviceShook(){

        shaked.setText("Save");
        Random rand = new Random();
        final String timeStamp = new SimpleDateFormat("dd-MMM-yyyy HH:mm").format(Calendar.getInstance().getTime());
        final int  n = rand.nextInt(4);
        int res = getResources().getIdentifier("opened_cookie_"+cookies[n],"drawable",getPackageName());

        TextView dateTxt = (TextView)findViewById(R.id.shakeDate);
        TextView cookieTxt = (TextView)findViewById(R.id.shakeResults);
        ImageView cookieIm = (ImageView)findViewById(R.id.shakeImage);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ActivityNewCookie.this,MainActivity.class);

                intent2.putExtra(cookieID,n);
                intent2.putExtra(cookieDate,timeStamp);

                startActivity(intent2);
            }
        });

        if(n > 2) cookieTxt.setTextColor(Color.RED);
        else cookieTxt.setTextColor(Color.BLUE);

        dateTxt.setText("Date: "+ timeStamp);
        System.out.println(cookieMsg[n]);
        cookieTxt.setText("Results: "+ cookieMsg[n]);
        cookieIm.setImageResource(res);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register this class as a listener for the orientation and
        // accelerometer sensors
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        sensorManager.unregisterListener(this); // sensor will still be sending data! must stop!
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
