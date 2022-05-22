package net.perport.haberuygulamasi.News;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;

import net.perport.haberuygulamasi.APIEndpoints.Responses;
import net.perport.haberuygulamasi.APIEndpoints.WrappedRequest;
import net.perport.haberuygulamasi.Comments.CommentsFragment;
import net.perport.haberuygulamasi.Comments.CommentsFragmentArgs;
import net.perport.haberuygulamasi.MainActivity;
import net.perport.haberuygulamasi.R;


import io.noties.markwon.Markwon;
import retrofit2.Call;


public class ArticleFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_article, container, false);


        ArticleFragmentArgs args = ArticleFragmentArgs.fromBundle(getArguments());

        ((Toolbar)getActivity().findViewById(R.id.toolbar)).setTitle(args.getTitle());

        final ImageView image = view.findViewById(R.id.image_article_header);
        final TextView title = view.findViewById(R.id.text_article_title);
        final TextView content = view.findViewById(R.id.text_article_content);

        final Markwon md = Markwon.create(view.getContext());


        Glide.with(this).load(args.getImageUrl()).into(image);
        title.setText(args.getTitle());

        md.setMarkdown(content, "**İçerik yükleniyor**");

        Call<Responses.GetSingleHaberResponse> res = MainActivity.API.getHaber(args.getId());
        Log.d("requestURL",res.request().url().toString());
        res.enqueue(new WrappedRequest<Responses.GetSingleHaberResponse>(data -> {
            md.setMarkdown(content, data.body.icerik);
        }, null).Catch(err -> {
            md.setMarkdown(content, "**İçerik yüklenirken bir hata oluştu: " + err.message + "**");
        }));


        CommentsFragment frag = new CommentsFragment();
        frag.setArguments(new CommentsFragmentArgs.Builder(args.getId()).build().toBundle());
        getParentFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerView2, frag).commit();
        return view;
    }
}
