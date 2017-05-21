package com.firozmemon.wallet.models;

/**
 * Created by firoz on 16/5/17.
 */

public class SignUp extends Login {

    private String email;

    public SignUp() {
        super();
    }

    public SignUp(String username, String email, String password) {
        super(username, password);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "SignUp{" +
                "email='" + email + '\'' +
                "super='" + super.toString() + '\'' +
                '}';
    }
}
