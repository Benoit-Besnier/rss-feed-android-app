package com.example.feeded;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.feeded.client.AuthenticationClient;
import com.example.feeded.element.custom.Credentials;
import com.example.feeded.element.custom.UserSession;
import com.example.feeded.handler.error.ConflictErrorHandler;
import com.example.feeded.handler.error.ForbiddenErrorHandler;
import com.example.feeded.service.SessionManager;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.http.ResponseEntity;

/**
 * A login screen that offers login via username/password.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {

    @ViewById
    AutoCompleteTextView login;

    @ViewById
    EditText password;

    @RestService
    AuthenticationClient authenticationClient;

    @Bean
    SessionManager sessionManager;

    @Bean
    ForbiddenErrorHandler forbiddenErrorHandler;

    @Bean
    ConflictErrorHandler conflictErrorHandler;

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Session Manager
        sessionManager = new SessionManager(getApplicationContext());
        this.startFeedActivity();
    }

    @AfterInject
    void afterInject() {
        authenticationClient.setRestErrorHandler(forbiddenErrorHandler);
        authenticationClient.setRestErrorHandler(conflictErrorHandler);
    }

    @Click({R.id.sign_in_button})
    void authentication() {
        Log.d("DEBUG", login.getText().toString() + " : " + password.getText().toString());
        this.connect(login.getText().toString(), password.getText().toString());
        Log.d("DEBUG", String.valueOf(sessionManager.isLoggedIn()));
    }

    @Click({R.id.register_button})
    void registration() {
        Log.d("DEBUG", login.getText().toString() + " : " + password.getText().toString());
        this.registration(login.getText().toString(), password.getText().toString());
        Log.d("DEBUG", String.valueOf(sessionManager.isLoggedIn()));
    }

    @Background
    void connect(final String username, final String password) {
        final Credentials credentials = new Credentials(username, password);
        final ResponseEntity<UserSession> responseEntity = this.authenticationClient.signin(credentials);

        if (responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful()) {
            final UserSession userSession = responseEntity.getBody();

            Log.d("DEBUG", userSession.getUsername() + " : " + userSession.getToken());
            sessionManager.createLoginSession(userSession.getUsername(), userSession.getToken());
            this.showToast("Signin succeed.");
            this.startFeedActivity();
        } else {
            this.showToast("Signin failed.");
        }
    }

    @Background
    void registration(final String username, final String password) {
        final Credentials credentials = new Credentials(username, password);
        final ResponseEntity responseEntity = this.authenticationClient.register(credentials);

        if (responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful()) {
            this.showToast("Your account is registered! You can now signin.");
        } else {
            this.showToast("We couldn't create your account.");
        }
    }

    void startFeedActivity() {
        if (this.sessionManager.isLoggedIn()) {
            final Intent intent = new Intent(this, FeedActivity_.class);

            this.startActivity(intent);
        } else {
            this.showToast("User is not connected.");
        }
    }

    @UiThread
    void showToast(final String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

}

