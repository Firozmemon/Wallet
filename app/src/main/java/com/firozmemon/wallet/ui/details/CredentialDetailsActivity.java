package com.firozmemon.wallet.ui.details;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.firozmemon.wallet.R;
import com.firozmemon.wallet.WalletApplication;
import com.firozmemon.wallet.database.DatabaseRepository;
import com.firozmemon.wallet.models.User_Credentials;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class CredentialDetailsActivity extends AppCompatActivity implements CredentialDetailsActivityView {

    @Inject
    DatabaseRepository databaseRepository;

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
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.checkBoxLinearLayout)
    LinearLayout checkBoxLinearLayout;
    @BindView(R.id.checkBox)
    CheckBox checkBox;

    @OnClick(R.id.fab)
    public void fabEditClicked() {
        User_Credentials tempUser_credentials = new User_Credentials();
        tempUser_credentials.setId(user_credentials.getId());
        tempUser_credentials.setUser_id(user_credentials.getUser_id());
        tempUser_credentials.setSite_name(siteNameEditText.getText().toString());
        tempUser_credentials.setUser_name(userNameEditText.getText().toString());
        tempUser_credentials.setEmail(emailEditText.getText().toString());
        tempUser_credentials.setPassword(passwordEditText.getText().toString());
        tempUser_credentials.setDescription(descriptionEditText.getText().toString());

        if (isInEditMode) {

            if (user_credentials.equals(tempUser_credentials)) {
                exitEditMode();
                Snackbar.make(coordinatorLayout, R.string.nothingChanged, Snackbar.LENGTH_LONG)
                        .show();
            } else {
                presenter.editClicked(isInEditMode, tempUser_credentials);
            }
        } else {
            presenter.editClicked(isInEditMode, tempUser_credentials);
        }
    }

    @OnCheckedChanged(R.id.checkBox)
    public void checkChanged(boolean isChecked) {
        if (!isChecked) {
            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
    }

    boolean isInEditMode = false;
    User_Credentials user_credentials;
    CredentialDetailsActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credential_details);

        ((WalletApplication) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user_credentials = getIntent().getParcelableExtra("DETAILS");

        isInEditMode = false;
        setUpEditTextData();

    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter = new CredentialDetailsActivityPresenter(this, databaseRepository, AndroidSchedulers.mainThread());
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.unsubscribe();
    }

    private void setUpEditTextData() {
        if (user_credentials != null) {
            siteNameEditText.setText(user_credentials.getSite_name());
            userNameEditText.setText(user_credentials.getUser_name());
            emailEditText.setText(user_credentials.getEmail());
            passwordEditText.setText(user_credentials.getPassword());
            descriptionEditText.setText(user_credentials.getDescription());
        } else {
            onBackPressed();
        }
    }

    private void shouldEnableEditTexts(boolean enable) {
        siteNameEditText.setEnabled(enable);
        userNameEditText.setEnabled(enable);
        emailEditText.setEnabled(enable);
        passwordEditText.setEnabled(enable);
        descriptionEditText.setEnabled(enable);

        checkBox.setChecked(false); // Reset checkBox

        // If in Edit Mode, do not display checkBox
        if (enable) {
            checkBoxLinearLayout.setVisibility(View.GONE);
        } else {
            checkBoxLinearLayout.setVisibility(View.VISIBLE);
        }
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

    @Override
    public void onBackPressed() {
        if (isInEditMode)
            exitEditMode();
        else
            super.onBackPressed();
    }

    public void exitEditMode() {
        isInEditMode = false;
        shouldEnableEditTexts(isInEditMode);

        setUpEditTextData();

        fab.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_edit));
    }

    @Override
    public void enterEditMode() {
        isInEditMode = true;
        shouldEnableEditTexts(isInEditMode);

        fab.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_save));
    }

    @Override
    public void displayError(String msg) {
        Snackbar.make(coordinatorLayout, "Error: " + msg, Snackbar.LENGTH_LONG)
                .show();
        exitEditMode();
    }

    @Override
    public void displaySuccess(User_Credentials credentials) {
        Snackbar.make(coordinatorLayout, R.string.dataUpdated, Snackbar.LENGTH_LONG)
                .show();
        user_credentials = credentials;
        exitEditMode();
    }
}
