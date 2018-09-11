package notifications;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;


import com.microsoft.windowsazure.notifications.NotificationsHandler;


import android.net.Uri;
import android.media.RingtoneManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import amurrider.rider.com.amur.amurrider.R;

public class MyHandler extends NotificationsHandler {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    Context ctx;

    @Override
    public void onReceive(Context context, Bundle bundle) {
        ctx = context;
        String nhMessage = bundle.getString("message");
                Log.e("==meesagenotification=",nhMessage);
        try {
            String result = java.net.URLDecoder.decode(nhMessage, "UTF-8");
            sendNotification(result);

           //     MainActivity.mainActivity.ToastNotify(result);
            managenotification(result);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    private void sendNotification(String msg) {
        String msgd=null;
        Intent intent=null;
        try {
            String result = java.net.URLDecoder.decode(msg, "UTF-8");
            JSONObject jsonObject1 = new JSONObject(result);
            JSONObject jsonpayload=jsonObject1.getJSONObject("Payload");
            String status=jsonpayload.getString("STATUS");
            if(status.equals("Success")){
                String stats=jsonpayload.getString("STATUS");
                msgd=jsonpayload.getString("MESSAGE");
                Log.v("Message Text",msgd);
                String title=jsonpayload.getString("TITLE");
                String type=jsonpayload.getString("TYPE");
                String imgurl=jsonpayload.getString("IMAGEURL");
                String sbtitle=jsonpayload.getString("SUBTITLE");
                if(type.equals("GN-FRIENDSMONEY")){

                }else if(type.equals("GN-HISTORY")){

                }else{

                }

        mNotificationManager = (NotificationManager)
                ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);



                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(ctx)
                                .setSmallIcon(R.mipmap.ic_launcher_round)
                                .setContentTitle("AMUR")
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(msgd))
                                .setSound(defaultSoundUri)
                                .setContentText(msgd);

                mBuilder.setContentIntent(contentIntent);
                mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

public void managenotification(String message) {
        try{
    JSONObject jsonObject1 = new JSONObject(message);
    JSONObject jsonpayload = jsonObject1.getJSONObject("Payload");
    String status = jsonpayload.getString("STATUS");

        String stats = jsonpayload.getString("STATUS");
        String msgd = jsonpayload.getString("MESSAGE");
        String title = jsonpayload.getString("TITLE");
        String type = jsonpayload.getString("TYPE");
        String sbtitle = jsonpayload.getString("SUBTITLE");

        if(type.equals("TNFRESHDAILY")){
            if (status.equals("Success")) {

        }else {

        }
        }else if(type.equals("TNAIRTIME")){
            if (status.equals("Success")) {


            }else {

            }
        }else if(type.equals("GN-HOME")){
            if (status.equals("Success")) {

          }
        }else if(type.equals("GN-FRESHDAILY")){
            if (status.equals("Success")) {
              }
        }
        else if(type.equals("GN-AIRTIMEBILL")){
            if (status.equals("Success")) {
               }
        }    else if(type.equals("GN-TRAVEL")){
            if (status.equals("Success")) {
               }
        }else if(type.equals("GN-DOCTORS")){
            if (status.equals("Success")) {
               }
        }else if(type.equals("GN-MERCHANTPROMO")){
            if (status.equals("Success")) {
              }
        }

    }catch (Exception e){
            Log.e("",e.getMessage());
        }
}
}