package net.perport.haberuygulamasi.BackgroundTasks;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import net.perport.haberuygulamasi.APIEndpoints.Models.Notification;
import net.perport.haberuygulamasi.APIEndpoints.Responses;
import net.perport.haberuygulamasi.APIEndpoints.Tokens.TokenManager;
import net.perport.haberuygulamasi.MainActivity;
import net.perport.haberuygulamasi.NOTIFICATION;
import net.perport.haberuygulamasi.PREFERENCES;
import net.perport.haberuygulamasi.R;

import retrofit2.Call;
import retrofit2.Response;

public class NotificationService extends Service {

    public static NotificationService get;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        get=this;
        loop();
        return START_STICKY;
    }

    private static final long fiveMinutes = 1000 * 60 * 5;
    private void loop(){
        new Handler().postDelayed(()->{
            TokenManager manager = TokenManager.get();
            manager.refreshAccessTokenIfNecessary();

            if(manager.accessTokenIsValid()) {
                Call<Responses.GetNotificationsResponse> call = MainActivity.API.getNotifications(manager.getAccessToken(), true);

                try{
                    Response<Responses.GetNotificationsResponse> res = call.execute();
                    if(res.isSuccessful()){

                        for(Notification notification: res.body().body.items){
                            this.createNotification(
                                    notification.bildirimTipi == NOTIFICATION.HABER ?
                                            notification.hedef : notification.hedef + " yorumunuza cevap verdi",
                                    notification.icerik
                            );
                        }
                    }
                }catch (Exception ignored){}
            }

            loop();
        }, fiveMinutes);
    }

    private int counter = 0;
    public void createNotification(String title, String content){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notifications")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat.from(this).notify(counter++, builder.build());
    }

}
