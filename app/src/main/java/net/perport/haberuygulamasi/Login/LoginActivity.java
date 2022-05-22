package net.perport.haberuygulamasi.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import net.perport.haberuygulamasi.APIEndpoints.Tokens.TokenManager;
import net.perport.haberuygulamasi.MainActivity;
import net.perport.haberuygulamasi.PREFERENCES;
import net.perport.haberuygulamasi.R;

public class LoginActivity extends AppCompatActivity {

    public static SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("default",Context.MODE_PRIVATE);

        Log.d("KEY",preferences.getString(PREFERENCES.refreshToken.name(), ""));
        TokenManager manager = TokenManager.get();
        if(preferences.getBoolean("isAnonymous",false) || manager.refreshTokenIsValid()){
            Intent main = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(main);
            return;
        }

        setContentView(R.layout.login_activity);
    }

    public static boolean Validate(EditText kullaniciAdi, EditText eposta, EditText sifre){
        boolean rt = true;
        if(eposta.length() == 0){
            eposta.setError("Bu alan boş bırakılamaz");
            rt= false;
        }

        else if(eposta.length() < 3){
            eposta.setError("Bu alan en az 3 karakter olmalı");
            rt= false;
        }

        if(sifre.length() == 0){
            sifre.setError("Bu alan boş bırakılamaz");
            rt= false;
        }
        else if(sifre.length() < 8){
            sifre.setError("Şifre en az 8 karakter olmalı");
            rt= false;
        }

        if(kullaniciAdi != null){
            if(kullaniciAdi.length() == 0){
                kullaniciAdi.setError("Bu alan boş bırakılamaz");
                rt= false;
            }

            else if(kullaniciAdi.length() < 3 || kullaniciAdi.length()>20){
                kullaniciAdi.setError("Bu alan 3 ila 20 karakter arasında olmalı");
                rt= false;
            }
        }

        return rt;
    }
    public static boolean Validate(EditText eposta, EditText sifre){
        return Validate(null,eposta,sifre);
    }
}
