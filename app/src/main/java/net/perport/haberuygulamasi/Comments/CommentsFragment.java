package net.perport.haberuygulamasi.Comments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.fragment.app.Fragment;

import net.perport.haberuygulamasi.APIEndpoints.DTOs.createCommentDTO;
import net.perport.haberuygulamasi.APIEndpoints.Models.ReplyBox;
import net.perport.haberuygulamasi.APIEndpoints.Models.Yorum;
import net.perport.haberuygulamasi.APIEndpoints.Responses;
import net.perport.haberuygulamasi.APIEndpoints.Tokens.TokenManager;
import net.perport.haberuygulamasi.APIEndpoints.WrappedRequest;
import net.perport.haberuygulamasi.MainActivity;
import net.perport.haberuygulamasi.R;

import java.util.List;

import retrofit2.Call;

public class CommentsFragment extends Fragment {

    LinearLayout commentContainer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comments, container, false);

        CommentsFragmentArgs args = CommentsFragmentArgs.fromBundle(getArguments());

        TextView loadingText = view.findViewById(R.id.text_commentLoading);
        commentContainer = view.findViewById(R.id.commentContainer);




        Call<Responses.GetCommentsResponse> res = MainActivity.API.getComments(args.getId());

        res.enqueue(new WrappedRequest<Responses.GetCommentsResponse>(data ->{
            if(data.body.size() > 0) createComments(data.body);
            loadingText.setVisibility(View.GONE);
        },null).Catch(err ->{
            loadingText.setText(err.message);
        }));

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        CommentsFragmentArgs args = CommentsFragmentArgs.fromBundle(getArguments());

        TextView commentAs = view.findViewById(R.id.text_commentAs);

        AsyncLayoutInflater inflater = new AsyncLayoutInflater(getContext());

        LinearLayout container = view.findViewById(R.id.repBox);
        inflater.inflate(R.layout.item_replybox, container, (View cView, int resid, ViewGroup parent)->{
            String username = TokenManager.get().getUsername();
            ReplyBox replyBox = new ReplyBox();
            replyBox.setViewData(cView);
            if(username == null){
                commentAs.setText("Yorum yapabilmek için lütfen giriş yapın.");
                commentAs.setGravity(Gravity.CENTER);

                replyBox.setVisibility(View.GONE);
            } else {
                commentAs.setText(String.format("%s olarak yorum yapılıyor", username));
                replyBox.setButtonText("Yorum Yap");
                replyBox.setButtonAction(l -> sendComment(getContext(), new createCommentDTO(args.getId(), replyBox.getContent())));
            }
            container.addView(cView);
        });
    }

    private void sendComment(Context ctx, createCommentDTO dto){
        Call<Responses.PostCommentResponse> res = MainActivity.API.createComment(TokenManager.get().getAccessToken(), dto);

        res.enqueue(new WrappedRequest<>(data -> {
            createComment(data.body.toYorum(TokenManager.get().getUsername()));

        }, ctx));
    }

    private void createComments(List<Yorum> comments){
        AsyncLayoutInflater inf = new AsyncLayoutInflater(getContext());
        for (Yorum comment:comments) {
            inf.inflate(R.layout.item_comment, commentContainer, (View view, int resid, ViewGroup parent)->{
                Log.d("comment", "shouldGetAddedToTheContainer");
                comment.setViewData(view, getContext());
                commentContainer.addView(view);
            });
        }
    }

    private void createComment(Yorum comment){
        AsyncLayoutInflater inf = new AsyncLayoutInflater(getContext());

        inf.inflate(R.layout.item_comment, commentContainer, (View view, int resid, ViewGroup parent)->{
            comment.setViewData(view, getContext());
            commentContainer.addView(view);
        });
    }
}
