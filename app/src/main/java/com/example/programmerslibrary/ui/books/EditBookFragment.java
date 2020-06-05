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
import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class EditBookFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    FragmentManager fragmentManager;

    final int REQUEST_CODE_GALLERY = 999;
    ImageView imageView;
    TextView cover_path;
    EditText title_edit, genre_edit, publ_date_edit, authors_edit;
    Spinner spinner;
    Button chooseCoverEdit, buttonBackEdit, buttonSaveEdit;

    String title;
    String genre;
    String publ_date;
    String authors;
    String book_status;

    MyAPIHelper db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_book, container, false);

        db = MainActivity.getDb();

        fragmentManager = getActivity().getSupportFragmentManager();

        spinner = (Spinner) view.findViewById(R.id.edit_book_status_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.book_status_array, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setEnabled(false);
        spinner.setClickable(false);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        title_edit = (EditText) view.findViewById(R.id.edit_title_edit_text);
        genre_edit = (EditText) view.findViewById(R.id.edit_genre_edit_text);
        publ_date_edit = (EditText) view.findViewById(R.id.edit_publ_date_edit_text);
        authors_edit = (EditText) view.findViewById(R.id.edit_authors_edit_text);
        chooseCoverEdit = (Button) view.findViewById(R.id.edit_Choose);
        buttonBackEdit = (Button) view.findViewById(R.id.editBookBack);
        buttonSaveEdit = (Button) view.findViewById(R.id.editBookSave);
        cover_path = view.findViewById(R.id.edit_cover_path_textView);
        imageView = (ImageView) view.findViewById(R.id.edit_cover_path_imageView);

        //Retrieve the value
        final int position = getArguments().getInt("position");

        Book book = db.getBook(position);
        title_edit.setText(book.getTitle());
        genre_edit.setText(book.getGenre());
        publ_date_edit.setText(Integer.toString(book.getPublication_year()));
        authors_edit.setText(book.getAuthors());
        cover_path.setText(book.getCover());
        Bitmap bitmap = BitmapFactory.decodeFile(book.getCover());
        imageView.setImageBitmap(bitmap);
        int spinnerPosition = adapter.getPosition(book.getBook_status().toString());
        spinner.setSelection(spinnerPosition);



        buttonBackEdit.setOnClickListener(new View.OnClickListener() {
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
        chooseCoverEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
        });
        buttonSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = title_edit.getText().toString();
                genre = genre_edit.getText().toString();
                publ_date = publ_date_edit.getText().toString();
                authors = authors_edit.getText().toString();

                if (title.equalsIgnoreCase("") || publ_date.equalsIgnoreCase("")) {
                    title_edit.setError("please enter username");//it gives user to info message //use any one //
                    publ_date_edit.setError("please enter username");//it gives user to info message //use any one //
                } else {
                    Book newBook = new Book();
                    newBook.setBook_id(position);
                    newBook.setTitle(title);
                    newBook.setGenre(genre);
                    newBook.setPublication_year(Integer.parseInt(publ_date));
                    newBook.setAuthors(authors);
                    newBook.setBook_status(BookStatus.valueOf(book_status));
                    newBook.setCover(cover_path.getText().toString());

                    updateBook(newBook, position);

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
     * Updating note in db and updating
     * item in the list by its position
     */
    private void updateBook(Book book, int position) {
        Book n = db.getBook(position);
        // updating book info
        n.setBook_id(book.getBook_id());
        n.setTitle(book.getTitle());
        n.setGenre(book.getGenre());
        n.setPublication_year(book.getPublication_year());
        n.setAuthors(book.getAuthors());
        n.setBook_status(book.getBook_status());
        n.setCover(book.getCover());
        // updating note in db
        db.updateBook(n);

    }
}