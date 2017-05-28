package com.firozmemon.wallet.ui.create;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.firozmemon.wallet.R;
import com.firozmemon.wallet.WalletApplication;
import com.firozmemon.wallet.database.DatabaseRepository;
import com.firozmemon.wallet.models.User_Credentials;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class CreateCredentialsActivity extends AppCompatActivity implements CreateCredentialsActivityView {

    @Inject
    DatabaseRepository databaseRepository;
    @Inject
    User_Credentials credentials;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.siteNameTextInputLayout)
    TextInputLayout siteNameTextInputLayout;
    @BindView(R.id.userNameTextInputLayout)
    TextInputLayout userNameTextInputLayout;
    @BindView(R.id.emailTextInputLayout)
    TextInputLayout emailTextInputLayout;
    @BindView(R.id.passwordTextInputLayout)
    TextInputLayout passwordTextInputLayout;
    @BindView(R.id.descriptionTextInputLayout)
    TextInputLayout descriptionTextInputLayout;
    @BindView(R.id.siteNameEditText)
    EditText siteNameEditText;
    @BindView(R.id.userNameEditText)
    EditText userNameEditText;
    @BindView(R.id.emailEditText)
    EditText emailEditText;
    @BindView(R.id.passwordEditText)
    EditText passwordEditText;
    @BindView(R.id.descriptionEditText)
    EditText descriptionEditText;

    @OnClick(R.id.fab)
    public void fabClicked() {
        int userId = ((WalletApplication) getApplication()).getLoggedInUserId();

        credentials.setUser_id(String.valueOf(userId));
        credentials.setSite_name(siteNameEditText.getText().toString());
        credentials.setUser_name(userNameEditText.getText().toString());
        credentials.setEmail(emailEditText.getText().toString());
        credentials.setPassword(passwordEditText.getText().toString());
        credentials.setDescription(descriptionEditText.getText().toString());

        presenter.createCredentials(userId, credentials);
    }

    CreateCredentialsActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_credentials);

        ((WalletApplication) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter = new CreateCredentialsActivityPresenter(this, databaseRepository, AndroidSchedulers.mainThread());
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.unsubscribe();
    }

    @Override
    public void displaySuccess() {
        Snackbar.make(coordinatorLayout, R.string.credentialCreatedSuccess, Snackbar.LENGTH_LONG)
                .show();
        onBackPressed();
    }

    @Override
    public void displayError(String msg) {
        Snackbar.make(coordinatorLayout, "Error: " + msg, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
