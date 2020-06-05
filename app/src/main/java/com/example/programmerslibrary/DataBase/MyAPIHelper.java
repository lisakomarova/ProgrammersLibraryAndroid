package com.example.programmerslibrary.DataBase;

import android.content.Context;
import android.widget.Toast;

import com.example.programmerslibrary.models.Book;
import com.example.programmerslibrary.models.Loan;
import com.example.programmerslibrary.models.Reader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyAPIHelper {

    Context context;

    public MyAPIHelper(Context context){
        this.context = context;
    }
    private static final String BASE_URL = "https://libraryapi.azurewebsites.net/";

    public long insertBook(Book book) {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("userid", book.getUserid())
                .add("title", book.getTitle())
                .add("genre", book.getGenre())
                .add("publication_year", String.valueOf(book.getPublication_year()))
                .add("authors", book.getAuthors())
                .add("book_status", book.getBook_status().toString())
                .add("cover", book.getCover())
                .build();

        final Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .url(BASE_URL + "api/Library/InsertBook")
                .post(formBody)
                .build();
        Response response = null;
        Call call = client.newCall(request);
        try {
            response = call.execute();
        } catch (IOException e) {
            e.getMessage();
        }

        Integer id = 0;
        if(response.code() == 200)
        {
            Toast.makeText(context, "Added",Toast.LENGTH_SHORT).show();
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson g = gsonBuilder.create();
            try {
                String a = response.body().string();
                a = a.replaceAll("\\[", "").replaceAll("\\]","").replaceAll("\"","");
                id = Double.valueOf(a).intValue();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return id;
        }
        else {
            Toast.makeText(context, response.toString(),Toast.LENGTH_SHORT).show();

        }
        return id;
    }

    public long insertReader(Reader reader) {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("userid", reader.getUserid())
                .add("first_name", reader.getFirst_name())
                .add("last_name", reader.getLast_name())
                .add("email", reader.getEmail())
                .add("has_book", Boolean.toString(false))
                .build();

        final Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .url(BASE_URL + "api/Library/InsertReader")
                .post(formBody)
                .build();
        Response response = null;
        Call call = client.newCall(request);
        try {
            response = call.execute();
        } catch (IOException e) {
            e.getMessage();
        }

        int id = 0;
        if(response.code() == 200)
        {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson g = gsonBuilder.create();
            try {
                String a = response.body().string();
                a = a.replaceAll("\\[", "").replaceAll("\\]","").replaceAll("\"","");
                id = Double.valueOf(a).intValue();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return id;
        }
        return id;
    }

    public long insertLoan(Loan loan) {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("userid", loan.getUserID())
                .add("reader_id", String.valueOf(loan.getReader_id()))
                .add("book_id", String.valueOf(loan.getBook_id()))
                .add("loan_date", loan.getLoan_date())
                .add("if_closed", Boolean.toString(loan.getIf_closed()))
                .build();

        final Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .url(BASE_URL + "api/Library/InsertLoan")
                .post(formBody)
                .build();
        Response response = null;
        Call call = client.newCall(request);
        try {
            response = call.execute();
        } catch (IOException e) {
            e.getMessage();
        }

        Integer id = 0;
        if(response.code() == 200)
        {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson g = gsonBuilder.create();
            try {
                String a = response.body().string();
                a = a.replaceAll("\\[", "").replaceAll("\\]","").replaceAll("\"","");
                id = Double.valueOf(a).intValue();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return id;
        }
        return id;
    }

    public Book getBook(long id) {
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(BASE_URL + "api/Library/GetBookByID/" + id)
                .get()
                .build();

        Call call = client.newCall(request);

        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson g = gsonBuilder.create();

        Book[] books = new Book[0];

        String a = null;
        Book book = new Book();
        try {
            a = validate(response.body().string());
            books = g.fromJson(a, Book[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<Book>(Arrays.asList(books)).get(0);
    }

    public Reader getReader(long id) {
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(BASE_URL + "api/Library/GetReaderByID/" + id)
                .get()
                .build();

        Call call = client.newCall(request);

        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson g = gsonBuilder.create();


        String a = null;
        Reader[] reader = new Reader[0];
        try {
            a = validate(response.body().string());
            reader  = g.fromJson(a,  Reader[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<Reader>(Arrays.asList(reader)).get(0);
    }

    public Loan getLoan(long id) {
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(BASE_URL + "api/Library/GetLoanByID/" + id)
                .get()
                .build();

        Call call = client.newCall(request);

        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson g = gsonBuilder.create();


        String a = null;
        Loan loan = new Loan();
        try {
            a = validate(response.body().string());
            loan  = g.fromJson(a,  Loan.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loan;
    }

    public List<Book> getAllBooks(String user_id) {
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(BASE_URL + "api/Library/"+ user_id +"/GetAllBooks")
                .get()
                .build();

        Call call = client.newCall(request);

        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Book[] books = new Book[0];

        if(response.code() == 200){
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson g = gsonBuilder.create();
            String a = null;
            try {
                a = validate(response.body().string());
                books = g.fromJson(a, Book[].class);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return new ArrayList<Book>(Arrays.asList(books));
    }

    public List<Reader> getAllReaders(String user_id) {
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(BASE_URL + "api/Library/" + user_id + "/GetAllReaders")
                .get()
                .build();

        Call call = client.newCall(request);

        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson g = gsonBuilder.create();


        String a = null;
        Reader[] readers = new Reader[0];
        try {
            a = validate(response.body().string());
            readers = g.fromJson(a, Reader[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<Reader>(Arrays.asList(readers));
    }

    public List<Loan> getAllLoans(String user_id) {
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(BASE_URL + "api/Library/" + user_id + "/GetAllLoans")
                .get()
                .build();

        Call call = client.newCall(request);

        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson g = gsonBuilder.create();


        String a = null;
        Loan[] loans = new Loan[0];
        try {
            a = validate(response.body().string());
            loans = g.fromJson(a, Loan[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<Loan>(Arrays.asList(loans));
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
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("book_id", String.valueOf(book.getBook_id()))
                .add("userid", book.getUserid())
                .add("title", book.getTitle())
                .add("genre", book.getGenre())
                .add("publication_year", String.valueOf(book.getPublication_year()))
                .add("authors", book.getAuthors())
                .add("book_status", book.getBook_status().toString())
                .add("cover", book.getCover())
                .build();

        final Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .url(BASE_URL + "api/Library/UpdateBook")
                .put(formBody)
                .build();
        Response response = null;
        Call call = client.newCall(request);
        try {
            response = call.execute();
        } catch (IOException e) {
            e.getMessage();
        }

        if(response.code() == 200)
        {
            System.out.println("Book issued and updated");
        }
    }

    public void updateReader(Reader reader) {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("reader_id", String.valueOf(reader.getReader_id()))
                .add("userid", reader.getUserid())
                .add("first_name", reader.getFirst_name())
                .add("last_name", reader.getLast_name())
                .add("email", reader.getEmail())
                .add("has_book", Boolean.toString(reader.doesHaveBook()))
                .build();

        final Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .url(BASE_URL + "api/Library/UpdateReader")
                .put(formBody)
                .build();
        Response response = null;
        Call call = client.newCall(request);
        try {
            response = call.execute();
        } catch (IOException e) {
            e.getMessage();
        }

        if(response.code() == 200)
        {
            System.out.println("Reader got book and updated");
        }
    }

    public void updateLoan(Loan loan) {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("loan_id", String.valueOf(loan.getLoan_id()))
                .add("userid", loan.getUserID())
                .add("reader_id", String.valueOf(loan.getReader_id()))
                .add("book_id", String.valueOf(loan.getBook_id()))
                .add("loan_date", loan.getLoan_date())
                .add("if_closed", Boolean.toString(loan.getIf_closed()))
                .build();

        final Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .url(BASE_URL + "api/Library/UpdateLoan")
                .put(formBody)
                .build();
        Response response = null;
        Call call = client.newCall(request);
        try {
            response = call.execute();
        } catch (IOException e) {
            e.getMessage();
        }

        if (response.code() == 200) {
            System.out.println("Loan is updated");
        }
    }

    public void deleteBook(int id) {
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(BASE_URL + "api/Library/DeleteBook/" + id)
                .delete()
                .build();

        Call call = client.newCall(request);

        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response.code() == 200) {
        }
    }

    public void deleteReader(int id) {
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(BASE_URL + "api/Library/DeleteReader/" + id)
                .delete()
                .build();

        Call call = client.newCall(request);

        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response.code() == 200) {
        }
    }

    public boolean deleteLoan(int id) {
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(BASE_URL + "api/Library/DeleteLoan/" + id)
                .delete()
                .build();

        Call call = client.newCall(request);

        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response.code() == 200) {
            return true;
        }
        else
            return false;
    }

    public Loan getLoansByBookID(int book_id){
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(BASE_URL + "api/Library/GetLoanByBookID/" + book_id)
                .get()
                .build();

        Call call = client.newCall(request);

        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson g = gsonBuilder.create();


        String a = null;
        Loan loan[] = new Loan[0];
        try {
            a = validate(response.body().string());
            loan  = g.fromJson(a,  Loan[].class);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<Loan>(Arrays.asList(loan)).get(0);
    }

    public String getReaderNameByReaderID(int reader_id){
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(BASE_URL + "api/Library/GetReaderNameByReaderID/" + reader_id)
                .get()
                .build();

        Call call = client.newCall(request);

        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String a = null;
        try {
            a = response.body().string();
            a = a.replaceAll("\\[", "").replaceAll("\\]","").replaceAll("\"","").replaceAll("\\\\","");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return a;
    }

    public String getBookTitleByBookID(int book_id){
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(BASE_URL + "api/Library/GetBookTitleByBookID/" + book_id)
                .get()
                .build();

        Call call = client.newCall(request);

        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String a = null;
        try {
            a = response.body().string();
            a = a.replaceAll("\\[", "").replaceAll("\\]","").replaceAll("\"","").replaceAll("\\\\","");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return a;
    }
    public String validate(String s) {
        final char dm = (char) 34;
        String b = s.substring(1, s.length() - 1);
        b = b.replaceAll("\\\\", "");
        return b;
    }
}