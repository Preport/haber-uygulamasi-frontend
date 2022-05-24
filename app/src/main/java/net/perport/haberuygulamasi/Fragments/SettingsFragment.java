package net.perport.haberuygulamasi.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import net.perport.haberuygulamasi.APIEndpoints.DTOs.updateUserDTO;
import net.perport.haberuygulamasi.APIEndpoints.Responses;
import net.perport.haberuygulamasi.APIEndpoints.Tokens.TokenManager;
import net.perport.haberuygulamasi.APIEndpoints.WrappedRequest;
import net.perport.haberuygulamasi.MainActivity;
import net.perport.haberuygulamasi.R;

import retrofit2.Call;


public class SettingsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        ChipGroup categories = view.findViewById(R.id.categoryChipGroup);

        EditText username = view.findViewById(R.id.editUsername);

        EditText password = view.findViewById(R.id.editPassword);

        ProgressBar loading = view.findViewById(R.id.progressBar2);

        ConstraintLayout settingsContent = view.findViewById(R.id.settingsContent);

        Button saveButton = view.findViewById(R.id.button);
        ProgressBar saveLoading = view.findViewById(R.id.saveProgressBar);

        TokenManager manager = TokenManager.get();
        Call<Responses.GetCurrentAccountResponse> call = MainActivity.API.getCurrentAccount(manager.getAccessToken());

        call.enqueue(new WrappedRequest<>(data->{
            username.setHint(data.body.kullaniciAdi);

            for(int i = 0; i<data.body.kategoriler.size(); i++){
                String kategori = data.body.kategoriler.get(i);
                Chip ch = (Chip) inflater.inflate(R.layout.chip_category, categories, false);
                ch.setText(kategori);
                Log.d("Category", ""+((1<<i) & data.body.kategoriSecimleri));
                ch.setChecked(((1<<i) & data.body.kategoriSecimleri) != 0);
                categories.addView(ch);
            }
            settingsContent.setVisibility(View.VISIBLE);
        }, ()->{
            loading.setVisibility(View.GONE);
        },getContext()));

        saveButton.setOnClickListener(l ->{

            saveButton.setEnabled(false);
            saveLoading.setVisibility(View.VISIBLE);
            Call<Responses.PatchAccountResponse> res =
                    MainActivity.API.updateAccount(manager.getAccessToken(),
                            manager.getAccessTokenPayload().sub, new updateUserDTO(username.getText().toString(), password.getText().toString(), categoriesToInt(categories)));

            res.enqueue(new WrappedRequest<>(data ->{

            },()->{
                saveButton.setEnabled(true);
                saveLoading.setVisibility(View.INVISIBLE);
            },getContext()));
        });
        return view;
    }
    private int categoriesToInt(ChipGroup group){
        int result = 0;
        for(int i = 0; i< group.getChildCount(); i++){
            View v = group.getChildAt(i);
            Chip ch = (Chip) v;
            if(ch.isChecked()) result = result | (1<<i);
        }
        return result;
    }
}