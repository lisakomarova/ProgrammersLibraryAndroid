package com.example.programmerslibrary.ui.readers;

import android.content.Context;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.programmerslibrary.DataBase.MyDBHelper;
import com.example.programmerslibrary.Enumerations.BookStatus;
import com.example.programmerslibrary.MainActivity;
import com.example.programmerslibrary.R;
import com.example.programmerslibrary.models.Book;
import com.example.programmerslibrary.models.Reader;
import com.example.programmerslibrary.ui.books.BookListFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class AddReaderFragment extends Fragment {
    FragmentManager fragmentManager;

    CheckBox hasBook_check_box;
    EditText firstname_edit, lastname_edit, email_edit;

    Button buttonBack, buttonSave;

    String  firstname;
    String  lastname;
    String  email;
    Boolean hasBook;

    private MyDBHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_reader, container, false);

        db = MainActivity.getDb();

        fragmentManager = getActivity().getSupportFragmentManager();

        //hasBook_check_box = (CheckBox) view.findViewById(R.id.has_book_check_box);
        firstname_edit = (EditText) view.findViewById(R.id.firstname_edit_text);
        lastname_edit = (EditText) view.findViewById(R.id.lastname_edit_text);
        email_edit = (EditText) view.findViewById(R.id.email_edit_text);

        buttonBack = view.findViewById(R.id.addREaderBack);
        buttonSave = view.findViewById(R.id.addReaderSave);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create new fragment and transaction
                Fragment newFragment = new ReaderListFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack if needed
                transaction.replace(R.id.nav_host_fragment, newFragment);

                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                // Commit the transaction
                transaction.commit();
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstname = firstname_edit.getText().toString();
                lastname = lastname_edit.getText().toString();
                email = email_edit.getText().toString();
                //hasBook = hasBook_check_box.isChecked();

                if(firstname.equalsIgnoreCase("") || hasBook.equals(null))
                {
                    firstname_edit.setError("please enter firstname");//it gives user to info message //use any one //

                }
                else
                {
                    Reader newReader = new Reader();
                    newReader.setFirstName(firstname);
                    newReader.setLastName(lastname);
                    newReader.setEmail(email);
                    newReader.setHasBook(false);

                    createReader(newReader);

                    Fragment newFragment = new ReaderListFragment();
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

    /**
     * Inserting new book in db
     * and refreshing the list
     */
    private void createReader(Reader reader) {

        MyDBHelper db = MainActivity.getDb();
        // inserting book in db and getting
        // newly inserted book id
        long id = db.insertReader(reader);

    }

}
