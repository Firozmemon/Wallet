package com.firozmemon.wallet.ui.signup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.firozmemon.wallet.R;
import com.firozmemon.wallet.WalletApplication;
import com.firozmemon.wallet.models.SignUp;
import com.firozmemon.wallet.models.sharedpreferences.SharedPreferencesRepository;
import com.firozmemon.wallet.ui.login.LoginActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class CreateAccountActivity extends AppCompatActivity implements CreateAccountActivityView {

    @Inject
    SharedPreferencesRepository sharedPreferencesRepository;
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    SignUp signUp;

    @BindView(R.id.nameTextInputLayout)
    TextInputLayout nameTextInputLayout;
    @BindView(R.id.emailTextInputLayout)
    TextInputLayout emailTextInputLayout;
    @BindView(R.id.passwordTextInputLayout)
    TextInputLayout passwordTextInputLayout;

    @BindView(R.id.nameEditText)
    EditText nameEditText;
    @BindView(R.id.emailEditText)
    EditText emailEditText;
    @BindView(R.id.passwordEditText)
    EditText passwordEditText;

    @OnClick(R.id.joinButton)
    void joinButton() {
        signUp.setUsername(nameEditText.getText().toString());
        signUp.setEmail(emailEditText.getText().toString());
        signUp.setPassword(passwordEditText.getText().toString());

        presenter.performSignUp(signUp);
    }

    CreateAccountActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Displaying back icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.gray)));

        ((WalletApplication) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter = new CreateAccountActivityPresenter(this, sharedPreferencesRepository, sharedPreferences, AndroidSchedulers.mainThread());
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.unsubscribe();
    }

    @Override
    public void displaySuccessAndGoToLoginActivity(String successMessage) {
        Toast.makeText(CreateAccountActivity.this, successMessage, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void displayError(String text) {
        Toast.makeText(CreateAccountActivity.this, "Error: " + text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
