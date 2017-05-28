package com.firozmemon.wallet.ui.main;

import com.firozmemon.wallet.models.User_Credentials;

import java.util.List;

/**
 * Created by firoz on 18/5/17.
 */

public interface MainActivityView {

    void displayError(String msg);

    void displayNoCredentialsFound();

    void displayCredentials(List<User_Credentials> list);

    void goToCreateCredentialActivity();

    void goToCredentialDetailsActivity(User_Credentials credentials);

    void displayDeleteSuccess();
}
