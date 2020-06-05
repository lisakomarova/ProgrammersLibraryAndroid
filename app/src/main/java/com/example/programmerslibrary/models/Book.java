package com.example.programmerslibrary.models;


import androidx.annotation.NonNull;

import com.example.programmerslibrary.Enumerations.BookStatus;

import java.io.Serializable;
import java.util.Calendar;

public class Book implements Serializable {

    private int book_id;
    private String title;
    private String genre;
    private int publication_year;
    private String authors;
    private BookStatus book_status;
    private String cover;
    private String userid;


    public Book (){

    }
    public Book (Book another){
        this.book_id = another.book_id;
        this.title = another.title;
        this.genre = another.genre;
        this.publication_year = another.publication_year;
        this.authors = another.authors;
        this.book_status = another.book_status;
        this.cover = another.cover;
        this.userid = another.userid;
    }
    public Book(@NonNull int book_id, String user_id, @NonNull String title, String genre, int publication_year,
                String authors, BookStatus book_status, String cover){
        if((publication_year < 1900) & (publication_year > Calendar.getInstance().get(Calendar.YEAR)))
            throw new IllegalArgumentException();
        this.book_id = book_id;
        this.title = title;
        this.genre = genre;
        this.publication_year = publication_year;
        this.authors = authors;
        this.book_status = book_status;
        this.cover = cover;
        this.userid = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getPublication_year() {
        return publication_year;
    }

    public void setPublication_year(int publication_year) {
        this.publication_year = publication_year;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }


    public BookStatus getBook_status() {
        return book_status;
    }

    public void setBook_status(BookStatus book_status) {
        this.book_status = book_status;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @NonNull
    @Override
    public String toString() {
        return title + " " + publication_year + " " + genre + " " + authors;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
