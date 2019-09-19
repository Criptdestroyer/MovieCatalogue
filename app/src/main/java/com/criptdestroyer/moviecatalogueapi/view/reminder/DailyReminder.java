package com.criptdestroyer.moviecatalogueapi.view.reminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.criptdestroyer.moviecatalogueapi.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DailyReminder extends BroadcastReceiver {
    public static final String DAILY_REMINDER = "DailyReminder";
    public static final String EXTRA_TYPE = "type";
    private final int ID_DAILY = 101;
    private String dailyMessage = "Movie Catalogue inviting you!";


    public DailyReminder(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = "Movie Catalague";

        showToast(context, title, dailyMessage);

        showAlarmNotification(context, title, dailyMessage, ID_DAILY);
    }

    private void showToast(Context context, String title, String message){
        Toast.makeText(context, title+": "+message, Toast.LENGTH_LONG).show();
    }

    private void showAlarmNotification(Context context, String title, String message, int notifId){
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "AlarmManager channel";

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
            notificationManager.notify(notifId, notification);
        }
    }

    public void setDailyReminder(Context context, String type, String time){
        String TIME_FORMAT = "HH:mm";
        if(isDateInvalid(time, TIME_FORMAT)) return;


        Intent intent = new Intent(context, DailyReminder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        intent.putExtra(EXTRA_TYPE, type);

        String[] timeArray = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        if(alarmManager != null){
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        Toast.makeText(context, "Daily reminder set up", Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm(Context context, String type){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        Toast.makeText(context, "Daily reminder dibatalkan", Toast.LENGTH_SHORT).show();
    }

    public boolean isAlarmSet(Context context, String type){
        Intent intent = new Intent(context, DailyReminder.class);
        return PendingIntent.getBroadcast(context, ID_DAILY, intent, PendingIntent.FLAG_NO_CREATE) != null;
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


}
