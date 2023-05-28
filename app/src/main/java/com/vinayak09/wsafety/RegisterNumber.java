package com.vinayak09.wsafety;

import static android.content.Context.MODE_PRIVATE;

import static androidx.navigation.Navigation.findNavController;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterNumber extends Fragment {

    public RegisterNumber() {
        // Required empty public constructor
    }

    public static RegisterNumber newInstance() {
        RegisterNumber fragment = new RegisterNumber();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        var view = inflater.inflate(R.layout.fragment_register_number, container, false);;
        TextInputEditText
                name = view.findViewById(R.id.nameEdit),
                number = view.findViewById(R.id.numberEdit);
        MaterialButton confirm = view.findViewById(R.id.doneBtn);


        confirm.setOnClickListener(view1 -> {
            String numberString = number.getText().toString(), nameString = name.getText().toString();
            if( (number.length() == 10 || number.length() == 11 || number.length() == 13) && !nameString.isEmpty()){
                if (!(  number.length() == 13 && numberString.startsWith("+639") ||
                        number.length() == 11 && numberString.startsWith("09") ||
                        number.length() == 10 && numberString.startsWith("9"))
                ){
                    new MaterialAlertDialogBuilder(getActivity())
                            .setMessage("NUMBER INPUT!\nEnter a proper number (starts with +636 or 09).")
                            .setPositiveButton("Okay", (dialog, id) -> {})
                            .show();
                    return;
                }

                if (number.length() == 11) {
                    numberString = "+63" + numberString.substring(1);
                } else if (number.length() == 10) {
                    numberString = "+63" + numberString;
                }

                var bundle = new Bundle();
                bundle.putString("name", nameString);
                bundle.putString("number", numberString);

                Navigation.findNavController(view).navigate(R.id.action_registerNumber_to_OTPConf, bundle);

            }else {
                new MaterialAlertDialogBuilder(getActivity())
                        .setMessage("ERROR INPUT!\nFill-up the input properly.")
                        .setPositiveButton("Okay", (dialog, id) -> {})
                        .show();
            }

        });
        return view;

    }
}