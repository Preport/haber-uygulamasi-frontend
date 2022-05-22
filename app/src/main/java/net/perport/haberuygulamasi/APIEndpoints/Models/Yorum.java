package net.perport.haberuygulamasi.APIEndpoints.Models;

import net.perport.haberuygulamasi.APIEndpoints.DTOs.createCommentDTO;
import net.perport.haberuygulamasi.APIEndpoints.Responses;
import net.perport.haberuygulamasi.APIEndpoints.Tokens.TokenManager;
import net.perport.haberuygulamasi.APIEndpoints.WrappedRequest;
import net.perport.haberuygulamasi.MainActivity;
import net.perport.haberuygulamasi.R;
import net.perport.haberuygulamasi.Lib.ObjectID;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.asynclayoutinflater.view.AsyncLayoutInflater;

import com.google.gson.Gson;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;

public class Yorum {
    public String _id;

    public String haberID;

    public String yorum;

    public String zaman;

    public boolean editlenmiş;

    public boolean silinmiş;

    public String ustYorum;

    public String kullaniciAdi;

    public List<Yorum> altYorumlar;

    private final AtomicReference<ReplyBox> replyBox = new AtomicReference<>();
    public void setViewData(View view, Context ctx){
        TextView username = view.findViewById(R.id.username);
        TextView content = view.findViewById(R.id.content);
        TextView timeAgo = view.findViewById(R.id.timeAgo);
        TextView editedDeleted = view.findViewById(R.id.editedOrDeletedAt);

        username.setText(kullaniciAdi);
        content.setText(yorum);

        CharSequence dateStr = DateUtils.getRelativeTimeSpanString(ObjectID.getTime(_id), new Date().getTime(), DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL);
        timeAgo.setText(String.format(" • %s", dateStr.toString().startsWith("0 ") ? "az önce" : dateStr));

        if(editlenmiş){
            try {
                editedDeleted.setText(String.format(" • %s", DateUtils.getRelativeTimeSpanString(MainActivity.dateFormat.parse(zaman).getTime())));
            } catch (ParseException err){
                editedDeleted.setVisibility(View.GONE);
            }
        }else editedDeleted.setVisibility(View.GONE);

        AsyncLayoutInflater inf = new AsyncLayoutInflater(ctx);
        LinearLayout commentContainer = view.findViewById(R.id.comContainer);

        //replyButton
        Button replyBtn = view.findViewById(R.id.replyButton);
        replyBtn.setOnClickListener(l -> {
            if(replyBox.get() == null){
                 inf.inflate(R.layout.item_replybox, commentContainer, (View cView, int resid, ViewGroup parent)->{
                     ReplyBox box = new ReplyBox();
                     box.setViewData(cView);
                     box.setVisibility(View.VISIBLE);
                     box.setButtonAction(z -> sendComment(ctx, commentContainer,new createCommentDTO(haberID, box.getContent(), _id)));
                     replyBox.set(box);
                     commentContainer.addView(cView,0);
                 });
            } else {
                replyBox.get().flipVisibility();
            }
        });

        //add sub comments
        if(altYorumlar == null || altYorumlar.size()==0)return;



        for(Yorum comment:altYorumlar){
            inf.inflate(R.layout.item_comment, commentContainer, (View cView, int resid, ViewGroup parent)->{
                Log.d("comment", "shouldGetAddedToTheContainer");
                comment.setViewData(cView, ctx);
                commentContainer.addView(cView);
            });
        }

    }

    private void sendComment(Context ctx, LinearLayout commentContainer, createCommentDTO dto){
        Call<Responses.PostCommentResponse> res = MainActivity.API.createComment(TokenManager.get().getAccessToken(), dto);

        Log.d("test", new Gson().toJson(dto));
        res.enqueue(new WrappedRequest<>(data -> {
            ReplyBox box = replyBox.get();
            box.setContent("");
            box.setVisibility(View.GONE);
            createComment(ctx, commentContainer,data.body.toYorum(TokenManager.get().getUsername()));
        }, ctx));
    }

    private void createComment(Context ctx, LinearLayout commentContainer, Yorum comment){
        AsyncLayoutInflater inf = new AsyncLayoutInflater(ctx);
        inf.inflate(R.layout.item_comment, commentContainer, (View view, int resid, ViewGroup parent)->{
            comment.setViewData(view, ctx);
            commentContainer.addView(view, 0);
        });
    }
}
