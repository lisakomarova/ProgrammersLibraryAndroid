package com.example.programmerslibrary.models;

import androidx.annotation.NonNull;

import com.example.programmerslibrary.Enumerations.BookStatus;

import java.io.Serializable;
import java.util.Calendar;

public class Loan implements Serializable {
    public static final String TABLE_NAME = "loan";

    public static final String COLUMN_ID = "loan_id";
    public static final String COLUMN_READER_ID = "reader_id";
    public static final String COLUMN_BOOK_ID = "book_id";
    public static final String COLUMN_LOAN_DATE = "loan_date";
    public static final String COLUMN_IF_CLOSED = "if_closed";

    private int loan_id;
    private String reader;
    private String book;
    private int reader_id;
    private int book_id;
    private String loan_date;
    private Boolean if_closed;

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_READER_ID + " TEXT NOT NULL, "
                    + COLUMN_BOOK_ID + " TEXT  NOT NULL, "
                    + COLUMN_LOAN_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                    + COLUMN_IF_CLOSED + " TEXT CHECK( if_closed IN ('true','false') ) NOT NULL DEFAULT 'false', "
                    + "foreign key (" + COLUMN_READER_ID + ") references " + Reader.TABLE_NAME + "(" + Reader.COLUMN_ID + ")"
                    + " on update cascade on delete cascade, "
                    + "foreign key (" + COLUMN_BOOK_ID + ") references " + Book.TABLE_NAME + "(" + Book.COLUMN_ID + ")"
                    + " on update cascade on delete cascade "
                    + ")";
    public Loan (){

    }

    public Loan (Loan another){
        this.loan_id = another.loan_id;
        this.book = another.book;
        this.reader = another.reader;
        this.loan_date = another.loan_date;
        this.if_closed = another.if_closed;
    }

    public Loan(int loan_id, String book, String reader, String loan_date, Boolean if_closed){
        this.loan_id = loan_id;
        this.book = book;
        this.reader = reader;
        this.loan_date = loan_date;
        this.if_closed = if_closed;
    }


    public String getLoan_date() {
        return loan_date;
    }

    public void setLoan_date(String loan_date) {
        this.loan_date = loan_date;
    }

    public Boolean getIf_closed() {
        return if_closed;
    }

    public void setIf_closed(Boolean if_closed) {
        this.if_closed = if_closed;
    }

    public String getReader() {
        return reader;
    }

    public void setReader(String reader) {
        this.reader = reader;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }


    public int getReader_id() {
        return reader_id;
    }

    public void setReader_id(int reader_id) {
        this.reader_id = reader_id;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public int getLoan_id() {
        return loan_id;
    }

    public void setLoan_id(int loan_id) {
        this.loan_id = loan_id;
    }
}