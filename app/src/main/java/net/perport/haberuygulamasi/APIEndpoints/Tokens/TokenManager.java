package net.perport.haberuygulamasi.APIEndpoints.Tokens;


import android.os.Debug;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

import net.perport.haberuygulamasi.APIEndpoints.Responses;
import net.perport.haberuygulamasi.APIEndpoints.WrappedRequest;
import net.perport.haberuygulamasi.Login.LoginActivity;
import net.perport.haberuygulamasi.MainActivity;
import net.perport.haberuygulamasi.PREFERENCES;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;

public class TokenManager {
    private String refreshToken;
    private String accessToken;

    private RefreshToken refreshTokenPayload;
    private AccessToken accessTokenPayload;

    private static TokenManager manager;

    public static TokenManager get(){
        if(manager != null) return manager;
        else return manager = new TokenManager();
    }
    private TokenManager(){
        setRefreshToken(LoginActivity.preferences.getString(PREFERENCES.refreshToken.name(), ""));
        setAccessToken(LoginActivity.preferences.getString(PREFERENCES.accessToken.name(), ""));
    }

    private boolean alreadyRefreshingAccessToken=false;

    public static final long fiveMinutes = 1000 * 60 * 5;

    public void refreshAccessTokenIfNecessary(){
        long currentTime = new Date().getTime();
        if(currentTime + fiveMinutes*2 > this.accessTokenPayload.exp * 1000L) refreshAccessToken();
    }
    public void refreshAccessToken(){
        if(alreadyRefreshingAccessToken || !refreshTokenIsValid())return;
        alreadyRefreshingAccessToken=true;


        Call<Responses.RefreshTokenResponse> res = MainActivity.API.refreshAccessToken(getRefreshToken());

        Log.d("URL", res.request().url().toString());
        Log.d("URL", res.request().headers().toString());
        WrappedRequest<Responses.RefreshTokenResponse> Wrap = new WrappedRequest<>(data -> {
            Log.d("br","BRUH");
            setAccessToken(data.body.access_token);
        },null);

        Wrap.Catch(err ->{
            if(err.statusCode == 401) {
                Log.d("bruh", "actualBruh");
                LoginActivity.preferences.edit().remove(PREFERENCES.refreshToken.name()).remove(PREFERENCES.accessToken.name()).apply();
                //TODO: giriş sayfasına yönlendir
            }
            else refreshAccessToken();
        });
        res.enqueue(Wrap);
    }
    public void setAccessToken(String accessToken){
        if(accessToken.equals("")){
            refreshAccessToken();
            return;
        }
        Log.d("KEY",LoginActivity.preferences.getString(PREFERENCES.refreshToken.name(), ""));
        this.accessToken=accessToken;
        String decoded = new String(Base64.decode(accessToken.split("\\.")[1], Base64.NO_WRAP));

        this.accessTokenPayload = new Gson().fromJson(decoded, AccessToken.class);
        LoginActivity.preferences.edit().putString(PREFERENCES.accessToken.name(), accessToken).apply();
        long currentTime = new Date().getTime();
        if( currentTime + fiveMinutes > this.accessTokenPayload.exp * 1000L){
            refreshAccessToken();
        } else {
            new Handler().postDelayed(this::refreshAccessToken, this.accessTokenPayload.exp * 1000L - new Date().getTime() - fiveMinutes);
        }
    }

    public void setRefreshToken(String refreshToken){
        Log.d("refresh", refreshToken);
        if(refreshToken.equals("")){
            return;
        }
        this.refreshToken=refreshToken;
        String decoded = new String(Base64.decode(refreshToken.split("\\.")[1], Base64.NO_WRAP));
        LoginActivity.preferences.edit()
                .putString(PREFERENCES.refreshToken.name(), refreshToken)
                .putBoolean(PREFERENCES.isAnonymous.name(), false)
                .apply();
        this.refreshTokenPayload = new Gson().fromJson(decoded, RefreshToken.class);
        this.refreshAccessToken();
    }

    public boolean refreshTokenIsValid(){
        if(this.refreshTokenPayload != null) Log.d("b", ""+this.refreshTokenPayload.exp);
        boolean valid = this.refreshTokenPayload != null && this.refreshTokenPayload.exp * 1000L > new Date().getTime();
        if(!valid) {
            //TODO: Giriş sayfası
        }

        return valid;
    }
    public boolean accessTokenIsValid(){
        return this.accessTokenPayload != null && this.accessTokenPayload.exp * 1000L > new Date().getTime();
    }

    public AccessToken getAccessTokenPayload() {
        return accessTokenPayload;
    }

    public RefreshToken getRefreshTokenPayload() {
        return refreshTokenPayload;
    }

    public String getAccessToken() {
        return "Bearer " + accessToken;
    }

    public String getRefreshToken() {
        return "Bearer " + refreshToken;
    }

    public boolean isModerator(){
        return this.accessTokenPayload != null && this.accessTokenPayload.isModerator;
    }
    public String getUsername(){
        if(this.accessTokenPayload != null) return this.accessTokenPayload.username;
        return null;
    }
}
