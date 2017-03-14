package com.app.shixelsdating.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.app.shixelsdating.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by wan on 3/13/17.
 */

public class SplashActivity extends AppCompatActivity {

    public FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser == null) {
                    Intent mAppIntroAndLogInScreen = new Intent(SplashActivity.this, AuthActivity.class);
                    startActivity(mAppIntroAndLogInScreen);
                } else {
                    Intent mMainAppIntent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(mMainAppIntent);
                }

                if (!SplashActivity.this.isFinishing()) {
                    SplashActivity.this.finish();
                }

            }

        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

}
