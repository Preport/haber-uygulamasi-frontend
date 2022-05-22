package net.perport.haberuygulamasi.Login.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.perport.haberuygulamasi.APIEndpoints.WrappedRequest;
import net.perport.haberuygulamasi.APIEndpoints.DTOs.createUserDTO;
import net.perport.haberuygulamasi.Login.LoginActivity;
import net.perport.haberuygulamasi.MainActivity;
import net.perport.haberuygulamasi.R;
import net.perport.haberuygulamasi.APIEndpoints.Responses;

import retrofit2.Call;


public class SignUpFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        TextView loginText = view.findViewById(R.id.girisYapinText);
        final SharedPreferences preferences = container.getContext().getSharedPreferences("default", Context.MODE_PRIVATE);



        Button btn = view.findViewById(R.id.button_signup);
        EditText email = view.findViewById(R.id.input_email);
        EditText username = view.findViewById(R.id.input_username);
        EditText password = view.findViewById(R.id.input_password);
        ProgressBar bar = view.findViewById(R.id.progressBar);

        CharSequence defaultBtnText = btn.getText();

        String receivedEmail = SignUpFragmentArgs.fromBundle(getArguments()).getEmail();

        if(receivedEmail.contains("@")){
            email.setText(receivedEmail);
        } else username.setText(receivedEmail);

        loginText.setOnClickListener(cl -> {
            NavHostFragment.findNavController(this).navigate(SignUpFragmentDirections.actionSignupFragmentToLoginFragment().setEmail(email.getText().toString()));
        });
        btn.setOnClickListener(_btnView -> {
            if(!LoginActivity.Validate(username, email, password))return;

            bar.setVisibility(View.VISIBLE);
            btn.setEnabled(false);
            btn.setText("");

            createUserDTO dto = new createUserDTO(username.getText().toString(),email.getText().toString(),password.getText().toString());

            Call<Responses.PostAccountResponse> res = MainActivity.API.createAccount(dto);

            res.enqueue(new WrappedRequest<>(data -> {
                new AlertDialog.Builder(getContext())
                        .setCancelable(false)
                        .setTitle("Başarılı:")
                        .setMessage(data.body)
                        .setPositiveButton("Onayla", (dialogInterface, i) -> {
                            NavHostFragment.findNavController(this).navigate(SignUpFragmentDirections.actionSignupFragmentToLoginFragment().setEmail(email.getText().toString()));
                        }).create().show();

            },() -> {
                bar.setVisibility(View.INVISIBLE);
                btn.setEnabled(true);
                btn.setText(defaultBtnText);
            }, container.getContext()));
        });
        return view;
    }
}