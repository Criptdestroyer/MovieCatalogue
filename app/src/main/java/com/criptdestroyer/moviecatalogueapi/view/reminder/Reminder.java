package com.criptdestroyer.moviecatalogueapi.view.reminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.criptdestroyer.moviecatalogueapi.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Reminder extends BroadcastReceiver {
    public static final String DAILY_REMINDER = "daily";
    public static final String RELEASE_REMINDER = "release";
    public static final String EXTRA_TYPE = "type";
    private final int ID_DAILY = 101;
    private final int ID_RELEASE = 100;
    private int jobId = 10;
    private static final long ONE_DAY_INTERVAL = 24 * 60 * 60 * 1000L;

    public Reminder(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(EXTRA_TYPE);

        assert type != null;
        if(type.equalsIgnoreCase(DAILY_REMINDER)){
            String title = "Movie Catalague";
            String message = "Movie Catalogue inviting you!";
            showAlarmNotification(context, title, message);
        }else if(type.equalsIgnoreCase(RELEASE_REMINDER)){
            startJob(context);
        }


    }

    private void showAlarmNotification(Context context, String title, String message){
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "Reminder channel";

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_movie_filter_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setAutoCancel(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000,1000,1000,1000,1000});

            builder.setChannelId(CHANNEL_ID);

            if(notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if(notificationManager != null){
            notificationManager.notify(ID_DAILY, notification);
        }
    }

    public void setReminder(Context context, String time, String type){
        String TIME_FORMAT = "HH:mm";
        if(isDateInvalid(time, TIME_FORMAT)) return;

        Intent intent = new Intent(context, Reminder.class);
        intent.putExtra(EXTRA_TYPE, type);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        String[] timeArray = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        int id;
        if(type.equals(DAILY_REMINDER)){
            id = ID_DAILY;
        }else{
            id = ID_RELEASE;
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if(alarmManager != null){
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void cancelAlarm(Context context, String type){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Reminder.class);

        int id;
        if(type.equals(DAILY_REMINDER)){
            id = ID_DAILY;
        }else{
            id = ID_RELEASE;
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            if(id == ID_RELEASE){
                stopJob(context);
            }
        }
    }

    public boolean isAlarmSet(Context context, String type){
        Intent intent = new Intent(context, Reminder.class);

        int id;
        if(type.equals(DAILY_REMINDER)){
            id = ID_DAILY;
        }else{
            id = ID_RELEASE;
        }

        return PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_NO_CREATE) != null;
    }

    public boolean isDateInvalid(String date, String format) {
        try {
            DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
            df.setLenient(false);
            df.parse(date);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }

    private void startJob(Context context){
        ComponentName componentName = new ComponentName(context, ReleaseReminder.class);
        JobInfo.Builder builder = new JobInfo.Builder(jobId, componentName);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setRequiresDeviceIdle(false);
        builder.setRequiresCharging(false);

        builder.setPeriodic(ONE_DAY_INTERVAL);

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null) {
            jobScheduler.schedule(builder.build());
            Log.d("JOB","start");
        }

    }

    private void stopJob(Context context){
        JobScheduler tm = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (tm != null) {
            tm.cancel(jobId);
            Log.d("JOB","stop");
        }
    }


}
