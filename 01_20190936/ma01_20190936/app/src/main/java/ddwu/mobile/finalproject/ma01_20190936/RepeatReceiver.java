package ddwu.mobile.finalproject.ma01_20190936;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class RepeatReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        //기기가 다시 시작되면 알림 시작
        // Set the alarm here.

        // notification 생성
        // Notification 출력
        NotificationCompat.Builder builder
                = new NotificationCompat.Builder(context, context.getString(R.string.CHANNEL_ID))
                .setSmallIcon(R.drawable.ic_baseline_set_meal_24)
                .setShowWhen(true)
                .setContentTitle("푸드로그")
                .setContentText("내 푸드로그를 기록할 시간이에요.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        int notificationId = 100;

        notificationManager.notify(notificationId, builder.build());


    }


}
