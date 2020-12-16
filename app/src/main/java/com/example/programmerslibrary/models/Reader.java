package com.example.programmerslibrary.models;

import androidx.annotation.NonNull;

import java.io.Serializable;


public class Reader implements Serializable {

    public static final String TABLE_NAME = "reader";

    public static final String COLUMN_ID = "reader_id";
    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_LASTNAME = "lastname";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_HASBOOK = "has_book";
    public static final String COLUMN_USER_ID = "user_id";

    @NonNull
    private int reader_id;
    private String first_name;
    private String last_name;
    private   String email;
    private boolean has_book;
    private  String userid;
    public boolean doesHaveBook(){return has_book;}

    public void setHas_book(Boolean has){
        this.has_book = has;
    }

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_USER_ID + " TEXT NOT NULL, "
                    + COLUMN_FIRSTNAME + " TEXT NOT NULL, "
                    + COLUMN_LASTNAME + " TEXT, "
                    + COLUMN_EMAIL + " TEXT, "
                    + COLUMN_HASBOOK + " TEXT CHECK( has_book IN ('true','false') ) NOT NULL DEFAULT 'false' "
                    + ")";

    public Reader(){}
    public Reader(Reader another){
        this.reader_id = another.reader_id;
        this.first_name = another.first_name;
        this.last_name = another.last_name;
        this.email = another.email;
        this.has_book = another.has_book;
        this.userid = another.userid;
    }
    public Reader(Integer reader_id, String user_id, @NonNull String first_name, @NonNull String last_name,
                  @NonNull String email, Boolean has_book){
        this.reader_id = reader_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.has_book = has_book;
        this.userid = user_id;
    }

    public int getReader_id() {
        return reader_id;
    }

    public void setReader_id(int reader_id) {
        this.reader_id = reader_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
