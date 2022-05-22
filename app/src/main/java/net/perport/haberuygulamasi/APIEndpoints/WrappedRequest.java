package net.perport.haberuygulamasi.APIEndpoints;

import android.app.AlertDialog;
import android.content.Context;

import net.perport.haberuygulamasi.Lib.Catch;
import net.perport.haberuygulamasi.Lib.Finally;
import net.perport.haberuygulamasi.Lib.Then;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WrappedRequest<T> implements Callback<T> {

    Then<T> ret;
    Finally fin;
    Context popupCtx;

    ArrayList<Catch> catches = new ArrayList<>();
    public WrappedRequest(Then<T> response, Context popupContext){
        this.ret=response;
        this.popupCtx=popupContext;
    }
    public WrappedRequest(Then<T> response, Finally _finally, Context popupContext){
        this.ret = response;
        this.fin = _finally;
        this.popupCtx = popupContext;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if(response.isSuccessful()){
            ret.Data(response.body());
        }else {
            try {
                Responses.ErrorResponse err = new Gson().fromJson(response.errorBody().string(), Responses.ErrorResponse.class);
                callCatches(err);
                popUp(err.message);
            } catch (IOException e) {
                popUp(e.getMessage());
                callCatches(e.getMessage());
                e.printStackTrace();
            } catch(JsonSyntaxException e){
                popUp("Sunucuyla Bağlantı Kurulamadı.");
                callCatches("Sunucuyla Bağlantı Kurulamadı.");
                e.printStackTrace();
            }
        }
        if(fin != null)fin.Fn();
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if(!call.isCanceled())popUp("Sunucuyla Bağlantı Kurulamadı.");
        callCatches("Sunucuyla Bağlantı Kurulamadı.");
        if(fin != null)fin.Fn();
    }

    public WrappedRequest<T> Catch(Catch err){
        catches.add(err);
        return this;
    }

    private void callCatches(String err){
        if(catches.size()==0) return;
        callCatches(new Gson().fromJson("{success:false,statusCode:500,message:\""+err+"\"}", Responses.ErrorResponse.class));
    }
    private void callCatches(Responses.ErrorResponse err){
        catches.forEach(c -> {
            c.Data(err);
        });
    }
    private void popUp(String message){
        if(popupCtx == null)return;
        AlertDialog.Builder builder = new AlertDialog.Builder(popupCtx)
                .setCancelable(false)
                .setTitle("Hata:")
                .setMessage(message)
                .setPositiveButton("Onayla", (dialogInterface, i) -> {

                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
