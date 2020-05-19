package com.example.programmerslibrary.ui.readers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

public class EditReaderFragment extends Fragment {
    FragmentManager fragmentManager;

    CheckBox hasBook_check_box;
    EditText firstname_edit, lastname_edit, email_edit;

    Button buttonBack, buttonSave;

    String  firstname;
    String  lastname;
    String  email;
    Boolean hasBook;

    MyDBHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_reader, container, false);

        db = MainActivity.getDb();

        fragmentManager = getActivity().getSupportFragmentManager();


        hasBook_check_box = (CheckBox) view.findViewById(R.id.edit_has_book_check_box);
        firstname_edit = (EditText) view.findViewById(R.id.edit_firstname_edit_text);
        lastname_edit = (EditText) view.findViewById(R.id.edit_lastname_edit_text);
        email_edit = (EditText) view.findViewById(R.id.edit_email_edit_text);

        buttonBack = view.findViewById(R.id.editReaderBack);
        buttonSave = view.findViewById(R.id.editReaderSave);

        //Retrieve the value
        final int position = getArguments().getInt("position");

        Reader reader= db.getReader(position);
        firstname_edit.setText(reader.getFirstName());
        lastname_edit.setText(reader.getLastName());
        email_edit.setText(reader.getEmail());
        hasBook_check_box.setChecked(reader.doesHaveBook());

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
                hasBook = hasBook_check_box.isChecked();

                if(firstname.equalsIgnoreCase("") || hasBook.equals(null))
                {
                    firstname_edit.setError("please enter firstname");//it gives user to info message //use any one //
                    hasBook_check_box.setChecked(false);

                }
                else
                {
                    Reader newReader = new Reader();
                    newReader.setFirstName(firstname);
                    newReader.setLastName(lastname);
                    newReader.setEmail(email);
                    newReader.setHasBook(hasBook);

                    updateReader(newReader, position);

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
     * Updating reader in db and updating
     * item in the list by its position
     */
    private void updateReader(Reader reader, int position) {
        Reader n = db.getReader(position);
        // updating book info
        n.setFirstName(reader.getFirstName());
        n.setLastName(reader.getLastName());
        n.setEmail(reader.getEmail());
        n.setHasBook(reader.doesHaveBook());

        // updating note in db
        db.updateReader(n);

    }
}