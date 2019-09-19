package com.criptdestroyer.moviecatalogueapi.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.criptdestroyer.moviecatalogueapi.R;
import com.criptdestroyer.moviecatalogueapi.view.reminder.DailyReminder;
import com.criptdestroyer.moviecatalogueapi.view.reminder.ReleaseReminder;

public class ReminderActivity extends AppCompatActivity {
    private DailyReminder dailyReminder;
    private int jobId = 10;
    private static final long ONE_DAY_INTERVAL = 24 * 60 * 60 * 1000L;

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
                    dailyReminder.setDailyReminder(getApplicationContext(), DailyReminder.DAILY_REMINDER, "06:20");
                }else{
                    dailyReminder.cancelAlarm(getApplicationContext(), DailyReminder.DAILY_REMINDER);
                }
            }
        });

        aSwitchReleaseReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    startJob();
                }else{
                    stopJob();
                }
            }
        });

        dailyReminder = new DailyReminder();
        if(dailyReminder.isAlarmSet(this, DailyReminder.DAILY_REMINDER)){
            aSwitchDailyReminder.setChecked(true);
        }else{
            aSwitchDailyReminder.setChecked(false);
        }
    }

    private void startJob(){
        ComponentName componentName = new ComponentName(this, ReleaseReminder.class);
        JobInfo.Builder builder = new JobInfo.Builder(jobId, componentName);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setRequiresDeviceIdle(false);
        builder.setRequiresCharging(false);

        builder.setPeriodic(ONE_DAY_INTERVAL);

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
        Toast.makeText(this, "Job Service started", Toast.LENGTH_SHORT).show();
    }

    private void stopJob(){
        JobScheduler tm = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        tm.cancel(jobId);
        Toast.makeText(this, "Job Service canceled", Toast.LENGTH_SHORT).show();
        finish();
    }
}
