package com.criptdestroyer.moviecatalogueapi.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.criptdestroyer.moviecatalogueapi.R;
import com.criptdestroyer.moviecatalogueapi.view.reminder.Reminder;

public class ReminderActivity extends AppCompatActivity {
    private Reminder reminder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        Switch aSwitchDailyReminder = findViewById(R.id.switch_daily);
        Switch aSwitchReleaseReminder = findViewById(R.id.switch_release);

        aSwitchDailyReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    reminder.setReminder(getApplicationContext(), "07:00", Reminder.DAILY_REMINDER);
                }else{
                    reminder.cancelAlarm(getApplicationContext(), Reminder.DAILY_REMINDER);
                }
            }
        });

        aSwitchReleaseReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    reminder.setReminder(getApplicationContext(), "08:00", Reminder.RELEASE_REMINDER);
                }else{
                    reminder.cancelAlarm(getApplicationContext(), Reminder.RELEASE_REMINDER);
                }
            }
        });

        reminder = new Reminder();
        if(reminder.isAlarmSet(this, Reminder.DAILY_REMINDER)){
            aSwitchDailyReminder.setChecked(true);
        }else{
            aSwitchDailyReminder.setChecked(false);
        }

        if(reminder.isAlarmSet(this, Reminder.RELEASE_REMINDER)){
            aSwitchReleaseReminder.setChecked(true);
        }else{
            aSwitchReleaseReminder.setChecked(false);
        }
    }
}
