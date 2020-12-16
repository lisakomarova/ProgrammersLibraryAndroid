package com.example.programmerslibrary.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.programmerslibrary.Enumerations.BookStatus;
import com.example.programmerslibrary.models.Book;
import com.example.programmerslibrary.models.Loan;
import com.example.programmerslibrary.models.Reader;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MyDBHelper extends SQLiteOpenHelper {

    private static final String password = "password12345678";
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "library_db";

    public MyDBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase.loadLibs(context);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        //db.setForeignKeyConstraintsEnabled(true);
        super.onConfigure(db);
        db.execSQL("PRAGMA foreign_keys = ON;");
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create book table
        db.execSQL(Book.CREATE_TABLE);
        // create reader table
        db.execSQL(Reader.CREATE_TABLE);
        // create loans table
        db.execSQL(Loan.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Book.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Reader.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Loan.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertBook(Book book) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `book status` will be inserted automatically.
        // no need to add them
        values.put(Book.COLUMN_USER_ID, book.getUserid());
        values.put(Book.COLUMN_TITLE, book.getTitle());
        values.put(Book.COLUMN_GENRE, book.getGenre());
        values.put(Book.COLUMN_PUBLICATION_YEAR, book.getPublication_year());
        values.put(Book.COLUMN_AUTHORS, book.getAuthors());
        values.put(Book.COLUMN_BOOK_STATUS, book.getBook_status().toString());
        values.put(Book.COLUMN_COVER, book.getCover());

        // insert row
        long id = db.insert(Book.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public long insertReader(Reader reader) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `book status` will be inserted automatically.
        // no need to add them
        values.put(Reader.COLUMN_FIRSTNAME, reader.getFirst_name());
        values.put(Reader.COLUMN_USER_ID, reader.getUserid());
        values.put(Reader.COLUMN_LASTNAME, reader.getLast_name());
        values.put(Reader.COLUMN_EMAIL, reader.getEmail());
        if (reader.doesHaveBook())
            values.put(Reader.COLUMN_HASBOOK, "true");
        else
            values.put(Reader.COLUMN_HASBOOK, "false");

        // insert row
        long id = db.insert(Reader.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public long insertLoan(Loan loan) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `book status` will be inserted automatically.
        // no need to add them
        values.put(Loan.COLUMN_USER_ID, loan.getUserID());
        values.put(Loan.COLUMN_READER_ID, loan.getReader_id());
        values.put(Loan.COLUMN_BOOK_ID, loan.getBook_id());
        if (loan.getIf_closed())
            values.put(Loan.COLUMN_IF_CLOSED, "true");
        else
            values.put(Loan.COLUMN_IF_CLOSED, "false");

        // insert row
        long id = db.insert(Loan.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Book getBook(long id) {
        // get readable database
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Book.TABLE_NAME,
                new String[]{Book.COLUMN_ID, Book.COLUMN_USER_ID, Book.COLUMN_TITLE, Book.COLUMN_GENRE, Book.COLUMN_PUBLICATION_YEAR,
                        Book.COLUMN_AUTHORS, Book.COLUMN_BOOK_STATUS,
                        Book.COLUMN_COVER},
                Book.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Book note = new Book(
                cursor.getInt(cursor.getColumnIndex(Book.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Book.COLUMN_USER_ID)),
                cursor.getString(cursor.getColumnIndex(Book.COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndex(Book.COLUMN_GENRE)),
                cursor.getInt(cursor.getColumnIndex(Book.COLUMN_PUBLICATION_YEAR)),
                cursor.getString(cursor.getColumnIndex(Book.COLUMN_AUTHORS)),
                BookStatus.valueOf(cursor.getString(cursor.getColumnIndex(Book.COLUMN_BOOK_STATUS))),
                cursor.getString(cursor.getColumnIndex(Book.COLUMN_COVER)));

        // close the db connection
        cursor.close();

        return note;
    }

    public Reader getReader(long id) {
        // get readable database
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Reader.TABLE_NAME,
                new String[]{Reader.COLUMN_ID, Reader.COLUMN_USER_ID, Reader.COLUMN_FIRSTNAME, Reader.COLUMN_LASTNAME, Reader.COLUMN_EMAIL,
                        Reader.COLUMN_HASBOOK},
                Reader.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare reader object
        Reader reader = new Reader(
                cursor.getInt(cursor.getColumnIndex(Reader.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Reader.COLUMN_USER_ID)),
                cursor.getString(cursor.getColumnIndex(Reader.COLUMN_FIRSTNAME)),
                cursor.getString(cursor.getColumnIndex(Reader.COLUMN_LASTNAME)),
                cursor.getString(cursor.getColumnIndex(Reader.COLUMN_EMAIL)),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(Reader.COLUMN_HASBOOK))));

        // close the db connection
        cursor.close();

        return reader;
    }
    public Loan getLoan(long id) {
        // get readable database
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Loan.TABLE_NAME,
                new String[]{Loan.COLUMN_ID, Loan.COLUMN_USER_ID, Loan.COLUMN_READER_ID, Loan.COLUMN_BOOK_ID, Loan.COLUMN_LOAN_DATE,
                        Loan.COLUMN_IF_CLOSED},
                Loan.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Cursor cursorReader = db.query(Reader.TABLE_NAME,
                new String[]{Reader.COLUMN_FIRSTNAME, Reader.COLUMN_LASTNAME},
                Reader.COLUMN_ID + "=?",
                new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex(Loan.COLUMN_READER_ID)))}, null, null,
                null, null);
        Cursor cursorBook = db.query(Book.TABLE_NAME,
                new String[]{Book.COLUMN_TITLE},
                Book.COLUMN_ID + "=?",
                new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex(Loan.COLUMN_BOOK_ID)))}, null, null,
                null, null);
        if (cursorBook != null)
            cursorBook.moveToFirst();
        if (cursorReader != null)
            cursorReader.moveToFirst();

        String bookTitle = cursorBook.getString(0);
        String readerFullname = cursorReader.getString(0) + " " + cursorReader.getString(1);

        // prepare loan object
        Loan loan = new Loan(
                cursor.getInt(cursor.getColumnIndex(Loan.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Loan.COLUMN_READER_ID)),
                bookTitle,
                readerFullname,
                cursor.getString(cursor.getColumnIndex(Loan.COLUMN_LOAN_DATE)),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(Loan.COLUMN_IF_CLOSED))));
        loan.setReader_id(cursor.getInt(cursor.getColumnIndex(Loan.COLUMN_READER_ID)));
        loan.setBook_id(cursor.getInt(cursor.getColumnIndex(Loan.COLUMN_BOOK_ID)));
        // close the db connection
        cursor.close();

        return loan;
    }

    public List<Book> getAllBooks(String user_id) {
        List<Book> books = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Book.TABLE_NAME +
                " WHERE " + Book.COLUMN_USER_ID + "= " + user_id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(Book.TABLE_NAME,
                new String[]{Book.COLUMN_ID, Book.COLUMN_USER_ID, Book.COLUMN_TITLE, Book.COLUMN_GENRE, Book.COLUMN_PUBLICATION_YEAR,
                        Book.COLUMN_AUTHORS, Book.COLUMN_BOOK_STATUS, Book.COLUMN_COVER},
                null, null, null, null,
                null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.setBook_id(cursor.getInt(cursor.getColumnIndex(Book.COLUMN_ID)));
                book.setUserid(cursor.getString(cursor.getColumnIndex(Book.COLUMN_USER_ID)));
                book.setTitle(cursor.getString(cursor.getColumnIndex(Book.COLUMN_TITLE)));
                book.setGenre(cursor.getString(cursor.getColumnIndex(Book.COLUMN_GENRE)));
                book.setPublication_year(cursor.getInt(cursor.getColumnIndex(Book.COLUMN_PUBLICATION_YEAR)));
                book.setAuthors(cursor.getString(cursor.getColumnIndex(Book.COLUMN_AUTHORS)));
                book.setBook_status(BookStatus.valueOf(cursor.getString(cursor.getColumnIndex(Book.COLUMN_BOOK_STATUS))));
                book.setCover(cursor.getString(cursor.getColumnIndex(Book.COLUMN_COVER)));

                books.add(book);
            } while (cursor.moveToNext());
        }

        //
        cursor.close();
        // close db connection
        db.close();

        // return notes list
        return books;
    }

    public List<Reader> getAllReaders(String user_id) {
        List<Reader> readers = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Reader.TABLE_NAME +
                " WHERE " + Reader.COLUMN_USER_ID + "= " + user_id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(Reader.TABLE_NAME,
                new String[]{Reader.COLUMN_ID, Reader.COLUMN_USER_ID, Reader.COLUMN_FIRSTNAME, Reader.COLUMN_LASTNAME, Reader.COLUMN_EMAIL,
                        Reader.COLUMN_HASBOOK},
                null, null, null, null,
                null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Reader reader = new Reader();
                reader.setReader_id(cursor.getInt(cursor.getColumnIndex(Reader.COLUMN_ID)));
                reader.setUserid(cursor.getString(cursor.getColumnIndex(Reader.COLUMN_USER_ID)));
                reader.setFirst_name(cursor.getString(cursor.getColumnIndex(Reader.COLUMN_FIRSTNAME)));
                reader.setLast_name(cursor.getString(cursor.getColumnIndex(Reader.COLUMN_LASTNAME)));
                reader.setEmail(cursor.getString(cursor.getColumnIndex(Reader.COLUMN_EMAIL)));
                reader.setHas_book(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(Reader.COLUMN_HASBOOK))));

                readers.add(reader);
            } while (cursor.moveToNext());
        }

        //
        cursor.close();
        // close db connection
        db.close();

        // return notes list
        return readers;
    }

    public List<Loan> getAllLoans(String user_id) {
        List<Loan> loans = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Loan.TABLE_NAME +
                " WHERE " + Loan.COLUMN_USER_ID + "= " + user_id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(Loan.TABLE_NAME,
                new String[]{Loan.COLUMN_ID, Loan.COLUMN_USER_ID, Loan.COLUMN_READER_ID, Loan.COLUMN_BOOK_ID, Loan.COLUMN_LOAN_DATE,
                        Loan.COLUMN_IF_CLOSED},
                null, null, null, null,
                null, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                // prepare reader object
                Cursor cursorReader = db.query(Reader.TABLE_NAME,
                        new String[]{Reader.COLUMN_FIRSTNAME, Reader.COLUMN_LASTNAME},
                        Reader.COLUMN_ID + "=?",
                        new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex(Loan.COLUMN_READER_ID)))}, null, null,
                        null, null);
                Cursor cursorBook = db.query(Book.TABLE_NAME,
                        new String[]{Book.COLUMN_TITLE},
                        Book.COLUMN_ID + "=?",
                        new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex(Loan.COLUMN_BOOK_ID)))}, null, null,
                        null, null);
                if (cursorBook != null)
                    cursorBook.moveToFirst();
                if (cursorReader != null)
                    cursorReader.moveToFirst();

                String bookTitle = cursorBook.getString(0);
                String readerFullname = cursorReader.getString(0) + " " + cursorReader.getString(1);

                // prepare loan object
                Loan loan = new Loan(
                        cursor.getInt(cursor.getColumnIndex(Loan.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(Loan.COLUMN_READER_ID)),
                        bookTitle,
                        readerFullname,
                        cursor.getString(cursor.getColumnIndex(Loan.COLUMN_LOAN_DATE)),
                        Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(Loan.COLUMN_IF_CLOSED))));
                loan.setReader_id(cursor.getInt(cursor.getColumnIndex(Loan.COLUMN_READER_ID)));
                loan.setBook_id(cursor.getInt(cursor.getColumnIndex(Loan.COLUMN_BOOK_ID)));
                loans.add(loan);
            } while (cursor.moveToNext());
        }

        //
        cursor.close();
        // close db connection
        db.close();

        // return notes list
        return loans;
    }


    public int getBooksCount(String user_id) {
        return getAllBooks(user_id).size();
    }

    public int getReadersCount(String user_id) {
        return getAllReaders(user_id).size();
    }

    public int getLoansCount(String user_id) {
        return getAllLoans(user_id).size();
    }

    public void updateBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Book.COLUMN_TITLE, book.getTitle());
        values.put(Book.COLUMN_GENRE, book.getGenre());
        values.put(Book.COLUMN_PUBLICATION_YEAR, book.getPublication_year());
        values.put(Book.COLUMN_AUTHORS, book.getAuthors());
        values.put(Book.COLUMN_BOOK_STATUS, book.getBook_status().toString());
        values.put(Book.COLUMN_COVER, book.getCover());

        // updating row
        db.update(Book.TABLE_NAME, values, Book.COLUMN_ID + " = ?",
                new String[]{String.valueOf(book.getBook_id())});
    }

    public void updateReader(Reader reader) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Reader.COLUMN_FIRSTNAME, reader.getFirst_name());
        values.put(Reader.COLUMN_LASTNAME, reader.getLast_name());
        values.put(Reader.COLUMN_EMAIL, reader.getEmail());
        if (reader.doesHaveBook())
            values.put(Reader.COLUMN_HASBOOK, "true");
        else
            values.put(Reader.COLUMN_HASBOOK, "false");


        // updating row
        db.update(Reader.TABLE_NAME, values, Reader.COLUMN_ID + " = ?",
                new String[]{String.valueOf(reader.getReader_id())});
    }

    public void updateLoan(Loan loan) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        if (loan.getIf_closed())
            values.put(Loan.COLUMN_IF_CLOSED, "true");
        else
            values.put(Loan.COLUMN_IF_CLOSED, "false");


        // updating row
        db.update(Loan.TABLE_NAME, values, Loan.COLUMN_ID + " = ?",
                new String[]{String.valueOf(loan.getLoan_id())});
    }

    public void deleteBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Book.TABLE_NAME, Book.COLUMN_ID + " = ?",
                new String[]{String.valueOf(book.getBook_id())});
        db.close();
    }

    public void deleteReader(Reader reader) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Reader.TABLE_NAME, Reader.COLUMN_ID + " = ?",
                new String[]{String.valueOf(reader.getReader_id())});
        db.close();
    }

    public void deleteLoan(Loan loan) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Loan.TABLE_NAME, Loan.COLUMN_ID + " = ?",
                new String[]{String.valueOf(loan.getLoan_id())});
        db.close();
    }

    public Loan getLoansByBookID(int id) {
        // get readable database
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Loan.TABLE_NAME,
                new String[]{Loan.COLUMN_ID, Loan.COLUMN_USER_ID, Loan.COLUMN_READER_ID, Loan.COLUMN_BOOK_ID, Loan.COLUMN_LOAN_DATE,
                        Loan.COLUMN_IF_CLOSED},
                Loan.COLUMN_BOOK_ID + "=? and " + Loan.COLUMN_IF_CLOSED + "=?",
                new String[]{String.valueOf(id), "false"}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Cursor cursorReader = db.query(Reader.TABLE_NAME,
                new String[]{Reader.COLUMN_FIRSTNAME, Reader.COLUMN_LASTNAME},
                Reader.COLUMN_ID + "=?",
                new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex(Loan.COLUMN_READER_ID)))}, null, null,
                null, null);
        Cursor cursorBook = db.query(Book.TABLE_NAME,
                new String[]{Book.COLUMN_TITLE},
                Book.COLUMN_ID + "=?",
                new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex(Loan.COLUMN_BOOK_ID)))}, null, null,
                null, null);
        if (cursorBook != null)
            cursorBook.moveToFirst();
        if (cursorReader != null)
            cursorReader.moveToFirst();

        String bookTitle = cursorBook.getString(0);
        String readerFullname = cursorReader.getString(0) + " " + cursorReader.getString(1);

        // prepare loan object
        Loan loan = new Loan(
                cursor.getInt(cursor.getColumnIndex(Loan.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Loan.COLUMN_USER_ID)),
                bookTitle,
                readerFullname,
                cursor.getString(cursor.getColumnIndex(Loan.COLUMN_LOAN_DATE)),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(Loan.COLUMN_IF_CLOSED))));
        loan.setReader_id(cursor.getInt(cursor.getColumnIndex(Loan.COLUMN_READER_ID)));
        loan.setBook_id(cursor.getInt(cursor.getColumnIndex(Loan.COLUMN_BOOK_ID)));

        // close the db connection
        cursor.close();

        return loan;
    }

    public String getReaderNameByReaderID(long id) {
        Reader r = getReader(id);

        return r.getFirst_name() + r.getLast_name();
    }

    public String getBookTitleByBookID(long id) {
        return getBook(id).getTitle();
    }

    public SQLiteDatabase getReadableDatabase() {
        return (super.getReadableDatabase(password));
    }

    public SQLiteDatabase getWritableDatabase() {
        return (super.getWritableDatabase(password));
    }
}