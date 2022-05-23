package net.perport.haberuygulamasi.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.fragment.app.Fragment;


import net.perport.haberuygulamasi.APIEndpoints.Models.Notification;
import net.perport.haberuygulamasi.APIEndpoints.Responses;
import net.perport.haberuygulamasi.APIEndpoints.Tokens.TokenManager;
import net.perport.haberuygulamasi.APIEndpoints.WrappedRequest;
import net.perport.haberuygulamasi.MainActivity;
import net.perport.haberuygulamasi.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


public class NotificationsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        LinearLayout notificationContainer = view.findViewById(R.id.notificationContainer);
        Call<Responses.GetNotificationsResponse> res = MainActivity.API.getNotifications(TokenManager.get().getAccessToken(), false);

        Log.d("Deb", res.request().url().toString());
        Log.d("Deb", res.request().headers().toString());
        res.enqueue(new WrappedRequest<>(data ->{
            createComments(data.body.items, notificationContainer);
        }, getContext()));

        return view;
    }

    private void createComments(List<Notification> notifications, LinearLayout container){
        if(notifications.size() == 0)return;
        AsyncLayoutInflater inf = new AsyncLayoutInflater(getContext());
        for(Notification not:notifications){
            inf.inflate(R.layout.item_comment, container, (View view, int resid, ViewGroup parent)->{
                not.setViewData(view);
                container.addView(view);
            });
        }
    }
}