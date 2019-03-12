package com.example.blankapp1;

import androidx.appcompat.app.AppCompatActivity;
import co.chatsdk.core.session.ChatSDK;
import io.reactivex.android.schedulers.AndroidSchedulers;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.FirebaseApp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button signInButton;
    public static final int RC_SIGN_IN = 900;
    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        signInButton = (Button) findViewById(R.id.button);
    }

    public void startAuthenticationActivity () {

        ArrayList<AuthUI.IdpConfig> idps = new ArrayList<>();

        idps.add(new AuthUI.IdpConfig.EmailBuilder().build());

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(idps)
                        .build(),
                RC_SIGN_IN);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // RC_SIGN_IN is the request code you passed into  startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                // Success
            }
            else {
                // Handle Error
            }
        }
    }

    public void login_click (View v) {
        startAuthenticationActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        authenticateWithCachedToken();
    }

    protected void authenticateWithCachedToken () {
        signInButton.setEnabled(false);

        ChatSDK.auth().authenticate()
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    signInButton.setEnabled(true);
                })
                .subscribe(() -> {
                    ChatSDK.ui().startMainActivity(MainActivity.this);
                }, throwable -> {
                    // Setup failed
                });
    }
}
