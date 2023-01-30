package ddwu.mobile.finalproject.ma01_20190936;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AlarmActivity extends Activity {

    TextView tvTime;
    TimePicker timePicker;
    int hour, min;
    AlarmManager alarmManager;

    EditText etTargetCal;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        tvTime = findViewById(R.id.tvTime);
        timePicker = findViewById(R.id.timePicker);
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        etTargetCal = findViewById(R.id.etTargetCal);


        //목표칼로리
        SharedPreferences spf = this.getSharedPreferences("targetNoti", 0);
        String targetCal = spf.getString("targetCal", null);

        if (targetCal != null)
            etTargetCal.setText(targetCal);

        createNotificationChannel();
    }

    public void onClick(View v) {
        Intent intent = null;
        PendingIntent sender = null;
        SharedPreferences spf = null;
        SharedPreferences.Editor editor = null;

        switch (v.getId()) {
            case R.id.btnSet:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    hour = timePicker.getHour();
                    min = timePicker.getMinute();
                } else {
                    hour = timePicker.getCurrentHour();
                    min = timePicker.getCurrentMinute();
                }

                tvTime.setText(hour + "시" + min +"분");

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, min);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                intent = new Intent(this, RepeatReceiver.class);
                sender = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//			60초당 한번 알람 등록 --> 최소 1분 정도로 반복을 설정하여야 함
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, sender);

//			정확도가 떨어지는 반복 알람 설정 시
//			alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 3000,
//					AlarmManager.INTERVAL_FIFTEEN_MINUTES, sender);

                break;
            case R.id.btnCancelAlarm:
                intent = new Intent(this, RepeatReceiver.class);
                sender = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                if (sender != null) {
                    alarmManager.cancel(sender);
                    tvTime.setText(R.string.no_alarm_set);
                }
                break;
            case R.id.btnCalSet:
                //목표 칼로리 설정: sharedPreferences 사용
                String targetCal = etTargetCal.getText().toString();
                spf = getSharedPreferences("targetNoti", 0);
                editor = spf.edit();
                editor.putString("targetCal", targetCal);
                editor.commit();


                break;
            case R.id.btnCalCancel:
                spf = getSharedPreferences("targetNoti", 0);
                editor = spf.edit();
                editor.clear();
                editor.commit();
                etTargetCal.setText("");
                break;
        }

    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);       // strings.xml 에 채널명 기록
            String description = getString(R.string.channel_description);       // strings.xml에 채널 설명 기록
            int importance = NotificationManager.IMPORTANCE_DEFAULT;    // 알림의 우선순위 지정
            NotificationChannel channel = new NotificationChannel(getString(R.string.CHANNEL_ID), name, importance);    // CHANNEL_ID 지정
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);  // 채널 생성
            notificationManager.createNotificationChannel(channel);
        }
    }
}
