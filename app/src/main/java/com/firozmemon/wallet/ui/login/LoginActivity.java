package com.firozmemon.wallet.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.firozmemon.wallet.ui.signup.CreateAccountActivity;
import com.firozmemon.wallet.R;
import com.firozmemon.wallet.WalletApplication;
import com.firozmemon.wallet.models.Login;
import com.firozmemon.wallet.models.sharedpreferences.SharedPreferencesRepository;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class LoginActivity extends AppCompatActivity implements LoginActivityView {

    @Inject
    Login login;
    @Inject
    SharedPreferencesRepository sharedPreferencesRepository;
    @Inject
    SharedPreferences sharedPreferences;

    @BindView(R.id.usernameTextInputLayout) TextInputLayout usernameTextInputLayout;
    @BindView(R.id.passwordTextInputLayout) TextInputLayout passwordTextInputLayout;

    @BindView(R.id.usernameEditText) EditText usernameEditText;
    @BindView(R.id.passwordEditText) EditText passwordEditText;

    @OnClick(R.id.signInButton)
    void signInButton() {
        login.setUsername(usernameEditText.getText().toString());
        login.setPassword(passwordEditText.getText().toString());

        presenter.signInClicked(login);
    }

    @OnClick(R.id.createAccountTextView)
    void createAccountTextView() {
        presenter.createAccountClicked();
    }

    LoginActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ((WalletApplication) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter = new LoginActivityPresenter(this, sharedPreferencesRepository,
                sharedPreferences, AndroidSchedulers.mainThread());
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.unsubscribe();
    }

    @Override
    public void goToCreateAccountActivity() {
        Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
        startActivity(intent);
    }

    @Override
    public void goToNextActivity() {
        Toast.makeText(LoginActivity.this, "GOTO Next Activity", Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayError(String text) {
        Toast.makeText(LoginActivity.this, "ERROR: " + text, Toast.LENGTH_LONG).show();
    }

    // Handling Orientation Changes
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("username",usernameEditText.getText().toString());
        outState.putString("password",passwordEditText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String username = savedInstanceState.getString("username", "");
        String password = savedInstanceState.getString("password", "");

        usernameEditText.setText(username);
        passwordEditText.setText(password);
    }
}
