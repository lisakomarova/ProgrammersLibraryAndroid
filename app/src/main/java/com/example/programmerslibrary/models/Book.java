package com.example.programmerslibrary.models;


import androidx.annotation.NonNull;

import com.example.programmerslibrary.Enumerations.BookStatus;

import java.io.Serializable;
import java.util.Calendar;

public class Book implements Serializable {
    public static final String TABLE_NAME = "book";

    public static final String COLUMN_ID = "book_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_GENRE = "genre";
    public static final String COLUMN_PUBLICATION_YEAR = "publication_year";
    public static final String COLUMN_AUTHORS = "authors";
    public static final String COLUMN_NUMBER_OF_COPIES = "number_of_copies";
    public static final String COLUMN_BOOK_STATUS= "book_status";
    public static final String COLUMN_COVER= "cover";

    private int idBook;
    private String title;
    private String genre;
    private int publicationYear;
    private String authors;
    private int numberOfCopies;
    private BookStatus bookStatus;
    private String cover;


    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_TITLE + " TEXT NOT NULL, "
                    + COLUMN_GENRE + " TEXT, "
                    + COLUMN_PUBLICATION_YEAR + " INTEGER NOT NULL, "
                    + COLUMN_AUTHORS + " TEXT, "
                    + COLUMN_NUMBER_OF_COPIES + " INTEGER NOT NULL, "
                    + COLUMN_BOOK_STATUS + " TEXT CHECK( book_status IN ('AVAILABLE','LOANED', 'DISPOSED') ) NOT NULL DEFAULT 'AVAILABLE', "
                    + COLUMN_COVER + " TEXT"
                    + ")";

    public Book (){

    }
    public Book (Book another){
        this.idBook = another.idBook;
        this.title = another.title;
        this.genre = another.genre;
        this.publicationYear = another.publicationYear;
        this.authors = another.authors;
        this.numberOfCopies = another.numberOfCopies;
        this.bookStatus = another.bookStatus;
        this.cover = another.cover;
    }
    public Book(@NonNull int idBook, @NonNull String title, String genre, int publicationYear,
                String authors, int numberOfCopies, BookStatus bookStatus, String cover){
        if((publicationYear < 1900) & (publicationYear > Calendar.getInstance().get(Calendar.YEAR)))
            throw new IllegalArgumentException();
        this.idBook = idBook;
        this.title = title;
        this.genre = genre;
        this.publicationYear = publicationYear;
        this.authors = authors;
        this.numberOfCopies = numberOfCopies;
        this.bookStatus = bookStatus;
        this.cover = cover;
    }

    public void incrementNumberOfCopies(){
        numberOfCopies++;
    }
    public void decrementNumberOfCopies(){
        numberOfCopies--;
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

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public int getNumberOfCopies() {
        return numberOfCopies;
    }

    public void setNumberOfCopies(int numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }

    public BookStatus getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(BookStatus bookStatus) {
        this.bookStatus = bookStatus;
    }

    public int getIdBook() {
        return idBook;
    }

    public void setIdBook(int idBook) {
        this.idBook = idBook;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
