package com.vinayak09.wsafety;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Extras.firebaseAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_main);

        var navHostFragment = getSupportFragmentManager().findFragmentById(R.id.mainFragment);
        if (navHostFragment != null)
            navController = ((NavHostFragment)navHostFragment).getNavController();
        else
            navController = Navigation.findNavController(this, R.id.mainFragment);

        if (Extras.firebaseAuth.getCurrentUser() != null)
            navController.navigate(R.id.mainMenu);

        Extras.progressDialog = new MaterialAlertDialogBuilder(this)
                .setView(R.layout.wait)
                .setCancelable(false)
                .create();

    }
}