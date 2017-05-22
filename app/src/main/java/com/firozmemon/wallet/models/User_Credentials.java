package com.firozmemon.wallet.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by firoz on 20/5/17.
 */

public class User_Credentials implements Parcelable {

    String id, user_id, site_name, email, user_name, password, description;

    public User_Credentials() {
        this.id = "";
        this.user_id = "";
        this.site_name = "";
        this.email = "";
        this.user_name = "";
        this.password = "";
        this.description = "";
    }

    private User_Credentials(Parcel parcel) {
        this.id = parcel.readString();
        this.user_id = parcel.readString();
        this.site_name = parcel.readString();
        this.email = parcel.readString();
        this.user_name = parcel.readString();
        this.password = parcel.readString();
        this.description = parcel.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "User_Credentials{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", site_name='" + site_name + '\'' +
                ", email='" + email + '\'' +
                ", user_name='" + user_name + '\'' +
                ", password='" + password + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Storing data to Parcel object
     *
     * @param parcel
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(user_id);
        parcel.writeString(site_name);
        parcel.writeString(email);
        parcel.writeString(user_name);
        parcel.writeString(password);
        parcel.writeString(description);
    }

    public static final Parcelable.Creator<User_Credentials> CREATOR = new Creator<User_Credentials>() {
        @Override
        public User_Credentials createFromParcel(Parcel parcel) {
            return new User_Credentials(parcel);
        }

        @Override
        public User_Credentials[] newArray(int i) {
            return new User_Credentials[0];
        }
    };

    public boolean equals(User_Credentials credentials) {
        return (
                (this.id.equals(credentials.getId())) &&
                        (this.user_id.equals(credentials.getUser_id())) &&
                        (this.site_name.equals(credentials.getSite_name())) &&
                        (this.user_name.equals(credentials.getUser_name())) &&
                        (this.email.equals(credentials.getEmail())) &&
                        (this.password.equals(credentials.getPassword())) &&
                        (this.description.equals(credentials.getDescription()))
        );
    }
}
