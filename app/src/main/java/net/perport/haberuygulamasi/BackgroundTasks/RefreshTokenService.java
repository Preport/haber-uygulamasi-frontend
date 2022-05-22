package net.perport.haberuygulamasi.BackgroundTasks;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import net.perport.haberuygulamasi.APIEndpoints.Tokens.TokenManager;

import java.util.Date;

public class RefreshTokenService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        loop();
        return START_STICKY;
    }

    private static final long fiveMinutes = 1000 * 60 * 5;
    private void loop(){
        new Handler().postDelayed(()-> {
            TokenManager.get().refreshAccessTokenIfNecessary();
            loop();
        }, fiveMinutes);
    }
}
