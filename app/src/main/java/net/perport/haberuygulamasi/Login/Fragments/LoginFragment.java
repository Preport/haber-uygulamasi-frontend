package net.perport.haberuygulamasi.Login.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import net.perport.haberuygulamasi.APIEndpoints.Tokens.TokenManager;
import net.perport.haberuygulamasi.APIEndpoints.WrappedRequest;
import net.perport.haberuygulamasi.APIEndpoints.DTOs.loginDTO;

import net.perport.haberuygulamasi.Login.LoginActivity;
import net.perport.haberuygulamasi.MainActivity;
import net.perport.haberuygulamasi.PREFERENCES;
import net.perport.haberuygulamasi.R;
import net.perport.haberuygulamasi.APIEndpoints.Responses;

import retrofit2.Call;


public class LoginFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        final SharedPreferences preferences = container.getContext().getSharedPreferences("default", Context.MODE_PRIVATE);

        TextView signupText = view.findViewById(R.id.kayitOlunText);


        Button btn = view.findViewById(R.id.button_giris);
        EditText email = view.findViewById(R.id.input_email);
        EditText password = view.findViewById(R.id.input_password);
        ProgressBar bar = view.findViewById(R.id.progressBar);

        CharSequence defaultBtnText = btn.getText();


        String receivedEmail = SignUpFragmentArgs.fromBundle(getArguments()).getEmail();

        email.setText(receivedEmail);


        signupText.setOnClickListener(cl -> {
            NavHostFragment.findNavController(this).navigate(LoginFragmentDirections.actionLoginFragmentToSignupFragment().setEmail(email.getText().toString()));
        });
        btn.setOnClickListener(_btnView -> {
            if(!LoginActivity.Validate(email,password))return;

            bar.setVisibility(View.VISIBLE);
            btn.setEnabled(false);
            btn.setText("");

            loginDTO dto = new loginDTO(email.getText().toString(),password.getText().toString());

            Call<Responses.LoginResponse> res = MainActivity.API.login(dto);

            res.enqueue(new WrappedRequest<>(data -> {
                TokenManager manager = TokenManager.get();
                manager.setRefreshToken(data.body.refresh_token);
                Log.d("Key",data.body.refresh_token);
                Intent main = new Intent(view.getContext(), MainActivity.class);
                startActivity(main);
            },() -> {
                bar.setVisibility(View.INVISIBLE);
                btn.setEnabled(true);
                btn.setText(defaultBtnText);
            }, container.getContext()));
        });
        return view;
    }
}