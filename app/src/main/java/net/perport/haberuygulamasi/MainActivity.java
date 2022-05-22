package net.perport.haberuygulamasi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import net.perport.haberuygulamasi.APIEndpoints.Tokens.TokenManager;
import net.perport.haberuygulamasi.APIEndpoints.WebService;
import net.perport.haberuygulamasi.Login.LoginActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static String baseUrl = "https://haber.perport.net/";
    public static WebService API = new Retrofit.Builder()
            .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
            .build()
                .create(WebService.class);

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    AppBarConfiguration appBarCfg;
    NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("default",Context.MODE_PRIVATE);
        setContentView(R.layout.main_activity);

        //Nav
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        //TopBar
        Toolbar toolbar = findViewById(R.id.toolbar);

        appBarCfg = new AppBarConfiguration.Builder(navController.getGraph()).build();

        setSupportActionBar(toolbar);

        NavigationUI.setupWithNavController(toolbar, navController,appBarCfg);
        //BottomNav
        BottomNavigationView navView = findViewById(R.id.bottom_nav_view);
        NavigationUI.setupWithNavController(navView, navController);

        boolean isAnonymous = preferences.getBoolean(PREFERENCES.isAnonymous.name(),false);

        TokenManager manager = TokenManager.get();
        if(!isAnonymous && !manager.refreshTokenIsValid()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        };

    }

}
