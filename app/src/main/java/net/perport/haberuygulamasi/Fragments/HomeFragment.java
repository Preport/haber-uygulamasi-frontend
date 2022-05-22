package net.perport.haberuygulamasi.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import net.perport.haberuygulamasi.APIEndpoints.Models.Haber;
import net.perport.haberuygulamasi.APIEndpoints.Responses;
import net.perport.haberuygulamasi.APIEndpoints.WrappedRequest;
import net.perport.haberuygulamasi.MainActivity;
import net.perport.haberuygulamasi.News.NewFragment;
import net.perport.haberuygulamasi.News.NewFragmentArgs;
import net.perport.haberuygulamasi.R;
import net.perport.haberuygulamasi.Lib.DelayedOnQueryTextListener;

import java.util.List;

import retrofit2.Call;


public class HomeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.topbar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);

        SearchView appBarSearch = (SearchView) searchItem.getActionView();
        appBarSearch.setMaxWidth(Integer.MAX_VALUE);
        appBarSearch.setOnQueryTextListener(new DelayedOnQueryTextListener() {
            public void onDelayedQueryTextChange(String s) {
                search(s);
                curQuery=s;
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() ->{
            appBarSearch.setQuery("", true);
            appBarSearch.clearFocus();
            appBarSearch.setIconified(true);
            if(lastArticle != null) getSince(lastArticle);
            else search(curQuery);
        });
    }
    private LinearLayout newsContainer;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Haber firstArticle;
    private Haber lastArticle;
    private String curQuery = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        newsContainer = view.findViewById(R.id.newsContainer);
        swipeRefreshLayout = view.findViewById(R.id.SwipeRefresh);

        swipeRefreshLayout.setRefreshing(true);
        search("");

        return view;
    }


    private void search(String query){
        query = query.trim();
        if(query.equals("")){
            Call<Responses.GetHaberResponse> res = MainActivity.API.getHaber(10);
            res.enqueue(new WrappedRequest<>(data -> setContent(data.body.items),()-> swipeRefreshLayout.setRefreshing(false),getContext()));
        } else {
            Call<Responses.GetHaberSearchResponse> res = MainActivity.API.searchHaber(query);
            res.enqueue(new WrappedRequest<>(data -> setContent(data.body),()->swipeRefreshLayout.setRefreshing(false),getContext()));
        }
    }


    private void getSince(Haber article){
        Call<Responses.GetHaberResponse> res = MainActivity.API.getHaberAfter(article._id, 10);
        res.enqueue(new WrappedRequest<>(data -> addContentToStart(data.body.items),()->swipeRefreshLayout.setRefreshing(false),getContext()));
    }
    private void getBefore(Haber article){
        Call<Responses.GetHaberResponse> res = MainActivity.API.getHaberBefore(article._id, 10);
        res.enqueue(new WrappedRequest<>(data -> addContentToEnd(data.body.items),()->swipeRefreshLayout.setRefreshing(false),getContext()));
    }
    private void setContent(List<? extends Haber> news){
        newsContainer.removeAllViews();
        FragmentTransaction fm = getParentFragmentManager().beginTransaction();
        for (int i = 0; i < news.size(); i++) {
            Haber article = news.get(i);
            if(i==0) lastArticle = article;
            else if(i == news.size()-1) firstArticle = article;

            NewFragmentArgs args = new NewFragmentArgs.Builder(article.isim, article.yazar, article._id).build();
            NewFragment frag = new NewFragment();
            frag.setArguments(args.toBundle());
            fm.add(newsContainer.getId(),frag);
        }
        fm.commit();
    }

    private void addContentToStart(List<Haber> news){
        FragmentTransaction fm = getParentFragmentManager().beginTransaction();

        for(int i = 0; i < news.size(); i++){
            Haber article = news.get(i);
            if(i == news.size()-1) firstArticle = article;

            NewFragmentArgs args = new NewFragmentArgs.Builder(article.isim, article.yazar, article._id).build();
            NewFragment frag = new NewFragment();
            frag.setArguments(args.toBundle());

            LinearLayout layout = new LinearLayout(newsContainer.getContext());
            layout.setId(View.generateViewId());
            newsContainer.addView(layout, 0);

            fm.add(layout.getId(), frag);
        }
    }

    private void addContentToEnd(List<Haber> news){
        //TODO: Create this fn
    }
}