package com.firozmemon.wallet.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.firozmemon.wallet.models.Login;
import com.firozmemon.wallet.models.SignUp;
import com.firozmemon.wallet.models.User_Credentials;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;

/**
 * Created by firoz on 18/5/17.
 */

public class MyWalletDatabaseHelper extends SQLiteOpenHelper implements DatabaseRepository {

    private static final String DATABASE_NAME = "WALLET_DB";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USER = "user";
    private static final String USER_COLUMN_ID = "_id";
    private static final String USER_COLUMN_USERNAME = "user_name";
    private static final String USER_COLUMN_EMAIL = "email";
    private static final String USER_COLUMN_PASSWORD = "password";

    private static final String TABLE_USER_CREDENTIALS = "user_credentials";
    private static final String USER_CREDENTIALS_COLUMN_ID = "_id";
    private static final String USER_CREDENTIALS_COLUMN_USER_ID = "user_id";
    private static final String USER_CREDENTIALS_COLUMN_SITE_NAME = "site_name";
    private static final String USER_CREDENTIALS_COLUMN_EMAIL = "email";
    private static final String USER_CREDENTIALS_COLUMN_USERNAME = "user_name";
    private static final String USER_CREDENTIALS_COLUMN_PASSWORD = "password";
    private static final String USER_CREDENTIALS_COLUMN_DESCRIPTION = "description";


    private static MyWalletDatabaseHelper helper = null;

    public static MyWalletDatabaseHelper getInstance(Context context) {
        if (helper == null)
            helper = new MyWalletDatabaseHelper(context);
        return helper;
    }

    private MyWalletDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + "( " +
                USER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_COLUMN_USERNAME + " text, " + USER_COLUMN_EMAIL + " text, " +
                USER_COLUMN_PASSWORD + " text not null" +
                ");";

        String CREATE_TABLE_USER_CREDENTIALS = "CREATE TABLE " + TABLE_USER_CREDENTIALS + "( " +
                USER_CREDENTIALS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_CREDENTIALS_COLUMN_USER_ID + " INTEGER, " +
                USER_CREDENTIALS_COLUMN_SITE_NAME + " text, " +
                USER_CREDENTIALS_COLUMN_EMAIL + " text, " + USER_CREDENTIALS_COLUMN_USERNAME + " text, " +
                USER_CREDENTIALS_COLUMN_PASSWORD + " text not null, " +
                USER_CREDENTIALS_COLUMN_DESCRIPTION + " text " +
                ");";


        sqLiteDatabase.execSQL(CREATE_TABLE_USER);
        sqLiteDatabase.execSQL(CREATE_TABLE_USER_CREDENTIALS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    @Override
    public Single<Integer> checkLoginCredentials(final Login login) {
        return Single.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                if (login != null) {
                    String username = login.getUsername();
                    String password = login.getPassword();

                    if ("".equals(username) || "".equals(password))
                        return -1;

                    SQLiteDatabase db = null;
                    Cursor cursor = null;
                    try {
                        db = MyWalletDatabaseHelper.this.getReadableDatabase();

                        cursor = db.rawQuery("SELECT " + USER_COLUMN_ID + " FROM " + TABLE_USER +
                                " WHERE " + USER_COLUMN_USERNAME + " = '" + username + "' AND " +
                                USER_COLUMN_PASSWORD + " = '" + password + "'", null);
                        if (cursor != null && cursor.getCount() == 1 && cursor.moveToFirst()) {
                            return cursor.getInt(cursor.getColumnIndex(USER_COLUMN_ID));
                        } else {
                            return -1;
                        }
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                        if (db != null) {
                            db.close();
                        }
                    }

                } else {
                    return -1;
                }
            }
        });
    }

    @Override
    public Single<Boolean> createUser(final SignUp signUpData) {
        return Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {

                if (signUpData != null) {
                    String username = signUpData.getUsername();
                    String email = signUpData.getEmail();
                    String password = signUpData.getPassword();

                    if (!("".equals(username)) && !("".equals(email)) && !("".equals(password))) {
                        ContentValues values = new ContentValues();
                        values.put(USER_COLUMN_USERNAME, username);
                        values.put(USER_COLUMN_EMAIL, email);
                        values.put(USER_COLUMN_PASSWORD, password);

                        SQLiteDatabase db = null;
                        try {
                            db = MyWalletDatabaseHelper.this.getWritableDatabase();
                            if (db.insert(TABLE_USER, null, values) > 0)
                                return Boolean.TRUE;
                            else
                                return Boolean.FALSE;
                        } finally {
                            if (db != null) {
                                db.close();
                            }
                        }
                    }
                }
                return Boolean.FALSE;
            }
        });
    }

    @Override
    public Single<List<User_Credentials>> getCredentials(final int userId) {
        return Single.fromCallable(new Callable<List<User_Credentials>>() {
            @Override
            public List<User_Credentials> call() throws Exception {
                if (userId != -1) {
                    SQLiteDatabase db = null;
                    Cursor cursor = null;
                    try {
                        db = MyWalletDatabaseHelper.this.getReadableDatabase();

                        cursor = db.rawQuery("SELECT * FROM " + TABLE_USER_CREDENTIALS +
                                        " WHERE " + USER_CREDENTIALS_COLUMN_USER_ID + " = '" + userId + "'",
                                null);

                        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                            List<User_Credentials> list = new ArrayList<User_Credentials>();
                            do {
                                User_Credentials credentials = new User_Credentials();
                                credentials.setId(cursor.getString(cursor.getColumnIndex(USER_CREDENTIALS_COLUMN_ID)));
                                credentials.setUser_id(cursor.getString(cursor.getColumnIndex(USER_CREDENTIALS_COLUMN_USER_ID)));
                                credentials.setSite_name(cursor.getString(cursor.getColumnIndex(USER_CREDENTIALS_COLUMN_SITE_NAME)));
                                credentials.setEmail(cursor.getString(cursor.getColumnIndex(USER_CREDENTIALS_COLUMN_EMAIL)));
                                credentials.setUser_name(cursor.getString(cursor.getColumnIndex(USER_CREDENTIALS_COLUMN_USERNAME)));
                                credentials.setPassword(cursor.getString(cursor.getColumnIndex(USER_CREDENTIALS_COLUMN_PASSWORD)));
                                credentials.setDescription(cursor.getString(cursor.getColumnIndex(USER_CREDENTIALS_COLUMN_DESCRIPTION)));

                                list.add(credentials);
                            } while (cursor.moveToNext());

                            return list;
                        } else {
                            return Collections.EMPTY_LIST;
                        }
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                        if (db != null) {
                            db.close();
                        }
                    }
                }
                return Collections.EMPTY_LIST;
            }
        });
    }

    @Override
    public Single<Boolean> createCredentials(final int userId, final User_Credentials credentials) {
        return Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                String site_name = credentials.getSite_name();
                String user_name = credentials.getUser_name();
                String email = credentials.getEmail();
                String password = credentials.getPassword();
                String description = credentials.getDescription();

                // Validate credentials
                if (userId > 0
                        &&
                        site_name != null && !site_name.equalsIgnoreCase("")
                        &&
                        password != null && !password.equalsIgnoreCase("")) {

                    SQLiteDatabase db = null;
                    try {
                        ContentValues values = new ContentValues();
                        values.put(USER_CREDENTIALS_COLUMN_USER_ID, userId);
                        values.put(USER_CREDENTIALS_COLUMN_SITE_NAME, site_name);
                        values.put(USER_CREDENTIALS_COLUMN_USERNAME, user_name);
                        values.put(USER_CREDENTIALS_COLUMN_EMAIL, email);
                        values.put(USER_CREDENTIALS_COLUMN_PASSWORD, password);
                        values.put(USER_CREDENTIALS_COLUMN_DESCRIPTION, description);

                        db = MyWalletDatabaseHelper.this.getWritableDatabase();
                        if (db.insert(TABLE_USER_CREDENTIALS, null, values) > 0)
                            return Boolean.TRUE;
                        else
                            return Boolean.FALSE;
                    } finally {
                        if (db != null) {
                            db.close();
                        }
                    }

                } else
                    return Boolean.FALSE;
            }
        });
    }

    @Override
    public Single<User_Credentials> updateCredentials(final User_Credentials credentials) {
        return Single.fromCallable(new Callable<User_Credentials>() {
            @Override
            public User_Credentials call() throws Exception {
                int id = Integer.parseInt(credentials.getId());
                int userId = Integer.parseInt(credentials.getUser_id());
                String site_name = credentials.getSite_name();
                String user_name = credentials.getUser_name();
                String email = credentials.getEmail();
                String password = credentials.getPassword();
                String description = credentials.getDescription();

                if (id > 0 && userId > 0
                        &&
                        site_name != null && !site_name.equalsIgnoreCase("")
                        &&
                        password != null && !password.equalsIgnoreCase("")) {
                    SQLiteDatabase db = null;
                    try {
                        ContentValues values = new ContentValues();
                        values.put(USER_CREDENTIALS_COLUMN_ID, id);
                        values.put(USER_CREDENTIALS_COLUMN_USER_ID, userId);
                        values.put(USER_CREDENTIALS_COLUMN_SITE_NAME, site_name);
                        values.put(USER_CREDENTIALS_COLUMN_USERNAME, user_name);
                        values.put(USER_CREDENTIALS_COLUMN_EMAIL, email);
                        values.put(USER_CREDENTIALS_COLUMN_PASSWORD, password);
                        values.put(USER_CREDENTIALS_COLUMN_DESCRIPTION, description);

                        db = MyWalletDatabaseHelper.this.getWritableDatabase();

                        if (db.update(TABLE_USER_CREDENTIALS, values,
                                USER_CREDENTIALS_COLUMN_ID + "=?", new String[]{String.valueOf(id)}) > 0)
                            return credentials;
                        else
                            return new User_Credentials();
                    } finally {
                        if (db != null) {
                            db.close();
                        }
                    }
                } else
                    return new User_Credentials();
            }
        });
    }
}
