package com.firozmemon.wallet.ui.details;

import com.firozmemon.wallet.models.User_Credentials;

/**
 * Created by firoz on 21/5/17.
 */

public interface CredentialDetailsActivityView {

    void enterEditMode();

    void displayError(String msg);

    void displaySuccess(User_Credentials user_credentials);
}
