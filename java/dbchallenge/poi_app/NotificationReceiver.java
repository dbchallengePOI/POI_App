package dbchallenge.poi_app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import java.util.Date;

public class NotificationReceiver extends BroadcastReceiver {
  public static String name;
  public static String location;
  public static String tips;
  public static String subinfo;
  public static String imgurl;

  @Override
  public void onReceive(Context context, Intent intent) {
    name = intent.getStringExtra("name");
    location = intent.getStringExtra("location");
    tips = intent.getStringExtra("tips");
    subinfo = intent.getStringExtra("subinfo");
    imgurl = intent.getStringExtra("imgurl");

    //generate ID
    long time = new Date().getTime();
    String tmpStr = String.valueOf(time);
    String last4Str = tmpStr.substring(tmpStr.length() - 5);
    int notificationId = Integer.valueOf(last4Str);

    // Use NotificationCompat.Builder to set up our notification.
    NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setPriority(NotificationCompat.PRIORITY_MAX);

    //icon appears in device notification bar and right hand corner of notification
    builder.setSmallIcon(R.mipmap.db_final_icon);

    Intent intentView = new Intent(context, open_Information.class);

    intentView.putExtra("name", name);
    intentView.putExtra("location", location);
    intentView.putExtra("tips", tips);
    intentView.putExtra("imgurl", imgurl);
    // Set the intent that will fire when the user taps the notification.
    builder.setContentIntent(PendingIntent.getActivity(context, notificationId, intentView, 0));


    // Content title, which appears in large type at the top of the notification
    builder.setContentTitle("POI IN DER NÄHE");

    // Content text, which appears in smaller text below the title
    builder.setContentText(intent.getStringExtra("content"));

    System.out.println("ALARMMANAGER: " + intent.getStringExtra("content"));

    // The subtext, which appears under the text on newer devices.
    builder.setSubText("Station: " + subinfo);

    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    // Will display the notification in the notification bar
    notificationManager.notify(notificationId, builder.build());
    }
}
