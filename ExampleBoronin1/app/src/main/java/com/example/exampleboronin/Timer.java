package com.example.exampleboronin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import java.util.Locale;

public class Timer extends AppCompatActivity {

    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;
    // Идентификатор уведомления
    private static final int NOTIFY_ID = 101;
    // Идентификатор канала
    private static String CHANNEL_ID = "puch Channel";
    private TextView tv;
    private boolean b;
    private static final long START_TIME_IN_MILLIS = 10000;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;
    private long mEndTime;
    NotificationChannel notificationChannel;
    public String channel_name;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        tv = findViewById(R.id.textView);
    //для секундомера
        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("Time: %s");
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= 86400000) {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                }
            }
        });

        getSupportActionBar().setTitle("Таймер");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        channel_name = NotificationChannel.DEFAULT_CHANNEL_ID;


        createNotificationChannel(notificationChannel);
    }

    // запуск секундомера
    public void startChronometer(View v) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
            //проверка таймера
            if (mTimerRunning) {
                pauseTimer();
            } else {
                startTimer();
            }
        }
    }
    //пауза секундомера
    public void pauseChronometer(View v) {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
            pauseTimer();
        }
    }
    //сбросить секундомер
    public void resetChronometer(View v) {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
        resetTimer();
    }
    //запуск таймера
    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
    //остановка таймера
            public void onFinish() {
                mTimerRunning = false;
                tv.setText("Finished");
    //Создаём уведомление
                Intent notificationIntent = new Intent(Timer.this, Timer.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(Timer.this,
                        0, notificationIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT);
                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(Timer.this, CHANNEL_ID)
                                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000})
                                .setSmallIcon(R.drawable.ic_launcher_foreground)
                                .setContentTitle("Внимание")
                                .setContentText("Не порть зрение, пора отдохнуть")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(pendingIntent)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                        R.drawable.ic_launcher_foreground)) // большая картинка
                                .setAutoCancel(true); // автоматически закрыть уведомление после нажатия
                NotificationManagerCompat notificationManager =
                        NotificationManagerCompat.from(Timer.this);
                notificationManager.notify(NOTIFY_ID, builder.build());
    //зацикливаем
                resetTimer();
                startTimer();
            }
        }.start();
        mTimerRunning = true;
    }
    //остановка таймера
    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
    }
    //работа таймера
    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        tv.setText(timeLeftFormatted);
    }
    //сброс таймера
    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
    }
    //сохраннение данных в хранилище для работы в фоновом режиме
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);
        editor.putLong("chron", pauseOffset);
        editor.apply();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }
    //берём данные из хранилища
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        mTimeLeftInMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
        mTimerRunning = prefs.getBoolean("timerRunning", false);
        pauseOffset = prefs.getLong("chron", pauseOffset);
        updateCountDownText();
        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
            } else {
                startTimer();
            }
        }
    }
    //создание канала уведомления
    private void createNotificationChannel(NotificationChannel channel_name) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel";
            String description = "Уведомление";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // кнопка назад
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}