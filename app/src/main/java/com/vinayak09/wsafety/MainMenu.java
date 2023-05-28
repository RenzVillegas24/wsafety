package com.vinayak09.wsafety;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.Map;


public class MainMenu extends Fragment {
    private static final String ARG_PARAM1 = "name";
    private static final String ARG_PARAM2 = "number";

    private String name;
    private String number;

    public MainMenu() {
        // Required empty public constructor
    }

    public static MainMenu newInstance(String param1, String param2) {
        MainMenu fragment = new MainMenu();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_PARAM1);
            number = getArguments().getString(ARG_PARAM2);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("MYID", "CHANNELFOREGROUND", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager m = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                m.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        var view = inflater.inflate(R.layout.fragment_main_menu, container, false);

        TextView textView =  view.findViewById(R.id.textNum);
        MaterialButton
                start = view.findViewById(R.id.startBtn),
                stop = view.findViewById(R.id.stopBtn),
                logout = view.findViewById(R.id.logoutBtn);

        start.setOnClickListener(view1 -> {
            if ((
                    ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED )) {
                Extras.progressDialog.show();

                Intent notificationIntent = new Intent(getContext(),ServiceMine.class);
                notificationIntent.setAction("Start");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    getContext().getApplicationContext().startForegroundService(notificationIntent);
                    Toast.makeText(getActivity(), "Service started running!", Toast.LENGTH_LONG).show();

                }

            } else {

                new MaterialAlertDialogBuilder(getActivity())
                        .setTitle("Permission must be granted.")
                        .setMessage("Would you like to grant the permission before continuing?")
                        .setPositiveButton("Continue", (dialog, id) -> {
                            Extras.progressDialog.show();

                            multiplePermissions.launch(new String[]{
                                    android.Manifest.permission.SEND_SMS,
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                            });


                        })
                        .setNegativeButton("Cancel", (dialog, id) -> {
                            Toast.makeText(getActivity(), "App permissions denied!", Toast.LENGTH_LONG).show();
                        })
                        .show();

            }
        });

        stop.setOnClickListener(view1 -> {
            Intent notificationIntent = new Intent(getActivity(), ServiceMine.class);
            notificationIntent.setAction("stop");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getContext().getApplicationContext().startForegroundService(notificationIntent);
                Toast.makeText(getActivity(), "Service stopped!", Toast.LENGTH_LONG).show();
            }
        });

        logout.setOnClickListener(view1 -> {

            new MaterialAlertDialogBuilder(getActivity())
                    .setTitle("Logout confirmation")
                    .setMessage("Would you like to logout this account?")
                    .setPositiveButton("Yes", (dialog, id) -> {
                        Extras.firebaseAuth.signOut();
                        Navigation.findNavController(getView()).navigate(R.id.action_mainMenu_to_registerNumber);
                    })
                    .setNegativeButton("No", (dialog, id) -> {
                        Toast.makeText(getActivity(), "Logging out cancelled.", Toast.LENGTH_SHORT).show();
                    })
                    .show();

        });


        textView.setText("will be sent to: " + Extras.firebaseAuth.getCurrentUser().getPhoneNumber());

        return view;
    }

    private ActivityResultLauncher<String[]> multiplePermissions = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
        @Override
        public void onActivityResult(Map<String, Boolean> result) {
            Intent notificationIntent = new Intent(getContext(),ServiceMine.class);
            notificationIntent.setAction("Start");

            Extras.progressDialog.dismiss();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getContext().getApplicationContext().startForegroundService(notificationIntent);
                Toast.makeText(getActivity(), "Service started running!", Toast.LENGTH_LONG).show();

            }

        }
    });

}