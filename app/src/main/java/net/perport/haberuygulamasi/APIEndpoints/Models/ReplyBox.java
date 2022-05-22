package net.perport.haberuygulamasi.APIEndpoints.Models;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.perport.haberuygulamasi.R;

public class ReplyBox {

    private Button btn;

    private EditText content;

    private Animation shakeAnim;

    private View.OnClickListener listener;

    private View view;

    private String bText = "";

    public void setButtonAction(View.OnClickListener l){
        listener = l;
    }

    public void setButtonText(String text){
        bText = text;
        if(btn != null) btn.setText(bText);
    }

    public void flipVisibility(){
        view.setVisibility(view.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }

    public void setVisibility(int visibility){
        view.setVisibility(visibility);
    }

    public String getContent(){
        return content.getText().toString();
    }

    public void setContent(String txt){
        content.setText(txt);
    }

    private void playShakeAnim(View view){
        if(shakeAnim == null) shakeAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.shake);

        if(view.getAnimation() == null) view.startAnimation(shakeAnim);
    }

    private void playShakeAnim(View view, boolean restartAnim){
        if(!restartAnim){
            playShakeAnim(view);
            return;
        }
        if(shakeAnim == null) shakeAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.shake);

        view.clearAnimation();
        view.startAnimation(shakeAnim);
    }

    public void setViewData(View view){
        this.view = view;
        btn = view.findViewById(R.id.button_sendComment2);
        TextView commentLength = view.findViewById(R.id.text_remainingCharacters2);
        content = view.findViewById(R.id.editTextTextMultiLine2);

        btn.setOnClickListener(l ->{
            if(content.length()>200){
                playShakeAnim(commentLength, true);
                content.setError("Yorum 200 karakteri geçemez!");
            } else if(content.getText().toString().trim().length() == 0){
                content.setError("Yorum Alanı boş bırakılamaz");
            } else {
                listener.onClick(l);
            }
        });

        if(!bText.equals(""))setButtonText(bText);

        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("DefaultLocale")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int length = charSequence.length();
                if(length > 150) {
                    commentLength.setVisibility(View.VISIBLE);
                    commentLength.setText(String.format("%d", 200 - length));
                    if(length > 200) {
                        playShakeAnim(commentLength);
                        commentLength.setTextColor(Color.RED);
                    }
                    else commentLength.setTextColor(Color.GREEN);
                } else commentLength.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
