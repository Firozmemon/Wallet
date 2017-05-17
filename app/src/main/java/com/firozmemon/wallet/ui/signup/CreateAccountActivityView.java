package com.firozmemon.wallet.ui.signup;

/**
 * Created by firoz on 16/5/17.
 */

public interface CreateAccountActivityView {

    void displaySuccessAndGoToLoginActivity(String successMessage);

    void displayError(String text);
}
