package net.perport.haberuygulamasi.News;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.perport.haberuygulamasi.Fragments.HomeFragmentDirections;
import net.perport.haberuygulamasi.MainActivity;
import net.perport.haberuygulamasi.R;
import net.perport.haberuygulamasi.Lib.ObjectID;

public class NewFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_list_element, container, false);
        final TextView title = view.findViewById(R.id.text_newTitle);
        final TextView author = view.findViewById(R.id.text_authorAndTime);
        final ImageView thumbnail = view.findViewById(R.id.image_new);
        NewFragmentArgs args = NewFragmentArgs.fromBundle(getArguments());

        title.setText(args.getTitle());
        author.setText(args.getAuthor() + " • " + DateUtils.getRelativeTimeSpanString(ObjectID.getTime(args.getID())));
        Glide.with(this).load(MainActivity.baseUrl + "haberKapak/" + args.getID()).into(thumbnail);

        final Button mainBtn = view.findViewById(R.id.button_new);
        final FloatingActionButton shareBtn = view.findViewById(R.id.button_share);

        mainBtn.setOnClickListener(l ->{
            NavHostFragment frag = (NavHostFragment) requireParentFragment();
            NavController controller = frag.getNavController();
            controller.navigate(HomeFragmentDirections.actionHomeFragmentToArticleFragment(args.getID(),args.getTitle(),MainActivity.baseUrl + "haberKapak/" + args.getID()));
        });

        shareBtn.setOnClickListener(l ->{
            Intent send = new Intent()
                    .setAction(Intent.ACTION_SEND)
                    .putExtra(Intent.EXTRA_TEXT,MainActivity.baseUrl+"haberIcerik/"+args.getID())
                    .setType("text/plain");
            Intent shareIntent = Intent.createChooser(send,null);
            startActivity(shareIntent);
        });

        return view;
    }
}
