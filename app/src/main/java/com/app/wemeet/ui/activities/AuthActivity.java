package com.app.wemeet.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.app.wemeet.R;
import com.app.wemeet.ui.widgets.WeMeetTextView;
import com.app.wemeet.utils.UiUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This activity manages user authentication via Firebase
 *
 * @author Wan Clem
 **/
public class AuthActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.facebook_login_button)
    LoginButton facebookLogInButton;

    @BindView(R.id.facebook_login_button_delegate)
    Button facebookLoginButtonDelegate;

    @BindView(R.id.username_field)
    EditText usernameField;

    @BindView(R.id.password_field)
    EditText passwordField;

    @BindView(R.id.remember_me_check_container)
    CardView rememberMeCheckContainer;

    @BindView(R.id.remember_me_check)
    CheckBox rememberMeCheck;

    @BindView(R.id.remember_me_label)
    WeMeetTextView rememberMeCheckLabel;

    @BindView(R.id.email_login_label_container)
    CardView emailLoginLabelContainer;

    @BindView(R.id.email_login_label)
    WeMeetTextView emailLoginLabel;

    private FirebaseAuth firebaseAuth;

    private FirebaseAuth.AuthStateListener authStateListener;


    //Progress dialog for any authentication action
    private ProgressDialog progressDialog;

    CallbackManager mCallbackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);

        initProgressDialog();

        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    startMainActivity();
                }
            }

        };


        mCallbackManager = CallbackManager.Factory.create();

        facebookLogInButton.setReadPermissions("email", "public_profile");

        facebookLogInButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }

        });

        attachClickListeners();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void startMainActivity() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent mMainActivityIntent = new Intent(AuthActivity.this, MainActivity.class);
                startActivity(mMainActivityIntent);
                finish();
            }
        });
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }

    private void attachClickListeners() {
        facebookLoginButtonDelegate.setOnClickListener(this);
        rememberMeCheckContainer.setOnClickListener(this);
        rememberMeCheckLabel.setOnClickListener(this);
        emailLoginLabelContainer.setOnClickListener(this);
        emailLoginLabel.setOnClickListener(this);
        rememberMeCheck.setOnClickListener(this);
        rememberMeCheckLabel.setOnClickListener(this);
        rememberMeCheckContainer.setOnClickListener(this);
    }

    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        showProcessProgressDialog(getString(R.string.please_wait));
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            UiUtils.showSafeToast(getString(R.string.login_aborted));
                        }
                        dismissProgressDialog();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_login_label_container:
            case R.id.email_login_label:
                initEmailAuthentication();
                break;
            case R.id.facebook_login_button_delegate:
                initFacebookAuthentication();
                break;
            case R.id.remember_me_label:
            case R.id.remember_me_check_container:
                if (!rememberMeCheck.isChecked()){
                    rememberMeCheck.setChecked(true);
                }else {
                    rememberMeCheck.setChecked(false);
                }
                break;
        }
    }

    private void initEmailAuthentication() {
        if (validateForm()) {
            showProcessProgressDialog(getString(R.string.please_wait));
            attemptUserAuthenticationViaEmailAndPassword();
        } else {
            dismissProgressDialog();
        }
    }


    /***
     *
     * Utility method to help sign in or create a new account for a user.
     *
     * --First of all attempt to sign in a user with the provided username and password,
     * if there exists a username and password as provided in the auth fields then sign in,
     *
     *---If not create a new account with the provided auth values
     * ***/
    private void attemptUserAuthenticationViaEmailAndPassword() {
        signInWithUsernameAndPassword();
    }

    private void initFacebookAuthentication() {
        facebookLogInButton.performClick();
    }

    private void showProcessProgressDialog(String progressMessage) {
        progressDialog.setMessage(progressMessage);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = usernameField.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            usernameField.setError("Required.");
            valid = false;
        } else {
            usernameField.setError(null);
        }

        String password = passwordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Required.");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        return valid;
    }


    private void signInWithUsernameAndPassword() {

        //By default, firebase authention via password requires an email and a password.
        // Since that is not the case here ,lets simply prefix our users name with an @shixels suffix

        final String usernameValue = usernameField.getText().toString().trim() + "@shixels.com";
        final String passwordValue = passwordField.getText().toString().trim();

        firebaseAuth.signInWithEmailAndPassword(usernameValue, passwordValue).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user is handled in the listener.

                if (!task.isSuccessful()) {
                    //There was an error
                    Exception exception = task.getException();
                    if (exception instanceof FirebaseAuthUserCollisionException) {
                        //A user with the given email exists already
                        UiUtils.showSafeToast("Username taken");
                    } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                        //No such username is available
                        //Attempt to create an account here
                        FirebaseAuthInvalidCredentialsException firebaseAuthInvalidCredentialsException = (FirebaseAuthInvalidCredentialsException) exception;
                        UiUtils.showSafeToast(firebaseAuthInvalidCredentialsException.getMessage());
                    } else {
                        String message = exception.getMessage();
                        if (message.contains("There is no user record corresponding to this identifier")) {
                            //Create an account
                            createAccountWithUsernameAndPassword(usernameValue, passwordValue);
                        } else {
                            UiUtils.showSafeToast(message);
                        }

                    }

                }

                dismissProgressDialog();

            }

        });

    }

    private void createAccountWithUsernameAndPassword(String username, String password) {
        firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    UiUtils.showSafeToast(task.getException().getMessage());
                }
                dismissProgressDialog();
            }
        });
    }

}
