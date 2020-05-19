package com.example.programmerslibrary.models;

import android.graphics.Picture;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.time.LocalDate;


public class Reader implements Serializable {

    public static final String TABLE_NAME = "reader";

    public static final String COLUMN_ID = "reader_id";
    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_LASTNAME = "lastname";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_HASBOOK = "has_book";

    @NonNull
    private int readerID;
    private String firstName;
    private String lastName;
    private   String email;
    private boolean hasBook;
    public boolean doesHaveBook(){return hasBook;}

    public void setHasBook (Boolean has){
        this.hasBook = has;
    }

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_FIRSTNAME + " TEXT NOT NULL, "
                    + COLUMN_LASTNAME + " TEXT, "
                    + COLUMN_EMAIL + " TEXT, "
                    + COLUMN_HASBOOK + " TEXT CHECK( has_book IN ('true','false') ) NOT NULL DEFAULT 'false' "
                    + ")";

    public Reader(){}
    public Reader(Reader another){
        this.readerID = another.readerID;
        this.firstName = another.firstName;
        this.lastName = another.lastName;
        this.email = another.email;
        this.hasBook = another.hasBook;
    }
    public Reader(Integer reader_id, @NonNull String firstName, @NonNull String lastName, @NonNull String email, Boolean hasBook){
        this.readerID = reader_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.hasBook = hasBook;
    }

    public int getReaderID() {
        return readerID;
    }

    public void setReaderID(int readerID) {
        this.readerID = readerID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
