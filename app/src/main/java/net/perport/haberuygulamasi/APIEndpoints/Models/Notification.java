package net.perport.haberuygulamasi.APIEndpoints.Models;

import android.view.View;
import android.widget.TextView;

import net.perport.haberuygulamasi.NOTIFICATION;
import net.perport.haberuygulamasi.R;

public class Notification {
    public String _id;
    public String kullaniciID;
    public NOTIFICATION bildirimTipi;
    public String icerik;
    public String hedef;


    public void setViewData(View view) {
        TextView text = view.findViewById(R.id.text_notification);
        text.setText(icerik);
    }
}
