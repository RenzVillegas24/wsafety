package com.vinayak09.wsafety;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPConfigure extends Fragment {

    private static final String ARG_PARAM1 = "number";
    private static final String ARG_PARAM2 = "name";
    private String number;
    private String name;
    private String authCode;

    public OTPConfigure() {
        // Required empty public constructor
    }

    public static OTPConfigure newInstance(String number, String name) {
        OTPConfigure fragment = new OTPConfigure();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, number);
        args.putString(ARG_PARAM2, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            number = getArguments().getString(ARG_PARAM1);
            name = getArguments().getString(name);
        }


        sendOTP(number);


        Extras.progressDialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        var view = inflater.inflate(R.layout.fragment_otp_conf, container, false);

        TextInputEditText
                OTP = view.findViewById(R.id.otpTxt);
        MaterialButton confirm = view.findViewById(R.id.confirmBtn);

        confirm.setOnClickListener(view1 -> {
            if (!OTP.getText().toString().isEmpty())
                verifyOTP(OTP.getText().toString());
        });


        return view;
    }

    private void sendOTP(String num){
        PhoneAuthProvider.verifyPhoneNumber(
                PhoneAuthOptions.newBuilder(Extras.firebaseAuth )
                        .setPhoneNumber(num)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(getActivity())
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                Toast.makeText(getActivity(), "The code was being sent.", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Extras.progressDialog.dismiss();
                                new MaterialAlertDialogBuilder(getActivity())
                                        .setMessage(e.getMessage().split(".")[0])
                                        .setPositiveButton("Retry", (dialog, id) -> {
                                            Navigation.findNavController(getView()).popBackStack();
                                        })
                                        .show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                Extras.progressDialog.dismiss();

                                authCode = s;
                                Toast.makeText(getActivity(), "Code was sent.", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .build()
        );
    }

    private void verifyOTP(String code){
        Extras.progressDialog.show();
        signInWithOTP( PhoneAuthProvider.getCredential(authCode, code));
    }

    private void signInWithOTP(PhoneAuthCredential credential){
        Extras.firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Extras.progressDialog.hide();
                        Toast.makeText(getActivity(), "Phone verified!", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(getView()).navigate(R.id.action_OTPConf_to_mainMenu);
                    }
                });
    }
}
