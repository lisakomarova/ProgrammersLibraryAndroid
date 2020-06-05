package com.example.programmerslibrary.ui.books;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.programmerslibrary.DataBase.MyAPIHelper;
import com.example.programmerslibrary.Enumerations.BookStatus;
import com.example.programmerslibrary.MainActivity;
import com.example.programmerslibrary.R;
import com.example.programmerslibrary.models.Book;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class AddBookFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    FragmentManager fragmentManager;

    final int REQUEST_CODE_GALLERY = 999;
    ImageView imageView;
    TextView cover_path;
    EditText title_edit, genre_edit, publ_date_edit, authors_edit;
    Spinner spinner;
    Button chooseCover, buttonBack, buttonSave;

    String user_id;
    String  title;
    String  genre;
    String  publ_date;
    String  authors;
    String  book_status;

    private MyAPIHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_book, container, false);

        db = MainActivity.getDb();

        fragmentManager = getActivity().getSupportFragmentManager();

        user_id = getArguments().getString("user");


        spinner = (Spinner)view.findViewById(R.id.book_status_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.book_status_array, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setEnabled(false);
        spinner.setClickable(false);
        int spinnerPosition = adapter.getPosition(BookStatus.AVAILABLE.toString());

        spinner.setSelection(spinnerPosition);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        title_edit = (EditText) view.findViewById(R.id.title_edit_text);
        genre_edit = (EditText) view.findViewById(R.id.genre_edit_text);
        publ_date_edit = (EditText) view.findViewById(R.id.publ_date_edit_text);
        authors_edit = (EditText) view.findViewById(R.id.authors_edit_text);
        chooseCover = (Button) view.findViewById(R.id.Choose);
        buttonBack = (Button) view.findViewById(R.id.addBookBack);
        buttonSave = (Button) view.findViewById(R.id.addBookSave);
        cover_path = view.findViewById(R.id.cover_path_textView);
        imageView = (ImageView) view.findViewById(R.id.cover_path_imageView);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create new fragment and transaction
                Fragment newFragment = new BookListFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack if needed
                transaction.replace(R.id.nav_host_fragment, newFragment);

                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                // Commit the transaction
                transaction.commit();
            }
        });
        chooseCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = title_edit.getText().toString();
                genre = genre_edit.getText().toString();
                publ_date = publ_date_edit.getText().toString();
                authors = authors_edit.getText().toString();
                int publ_year = Integer.valueOf(publ_date);

                if(title.equalsIgnoreCase("") || publ_date.equalsIgnoreCase(""))
                {
                    title_edit.setError("please enter username");//it gives user to info message //use any one //
                    publ_date_edit.setError("please enter username");//it gives user to info message //use any one //
                }
                else if (publ_year < 1500 || publ_year > Integer.valueOf(Year.now().toString()))
                    publ_date_edit.setError("please enter correct year");
                else
                {
                    Book newBook = new  Book();
                    newBook.setUserid(user_id);
                    newBook.setTitle(title);
                    newBook.setGenre(genre);
                    newBook.setPublication_year(Integer.parseInt(publ_date));
                    newBook.setAuthors(authors);
                    newBook.setBook_status(BookStatus.valueOf(book_status));
                    newBook.setCover(cover_path.getText().toString());
                    createBook(newBook);

                    Fragment newFragment = new BookListFragment();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                    // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack if needed
                    transaction.replace(R.id.nav_host_fragment, newFragment);

                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    // Commit the transaction
                    transaction.commit();
                }
            }
        });
        return view;
    }

    //for dropdown
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        book_status = parent.getItemAtPosition(pos).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_.png";
                String path = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).getAbsolutePath();
                File dir = new File(path, "ProgrammersLibrary");
                if (!dir.exists()) {
                    dir.mkdir();
                }
                File file = new File(dir, imageFileName);
                //checking if we can write to external memory
                String externalStorageState = Environment.getExternalStorageState();
                if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {

                    try (FileOutputStream out = new FileOutputStream(file)) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    } catch (IOException e) {
                        Log.d("CreateNew", e.getMessage());
                    }

                    cover_path.setText(file.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    /**
     * Inserting new book in db
     * and refreshing the list
     */
    private void createBook(Book book) {

        MyAPIHelper db = MainActivity.getDb();
        // inserting book in db and getting
        // newly inserted book id
        long id = db.insertBook(book);

    }

}
