package com.example.programmerslibrary.ui.readers;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.programmerslibrary.DataBase.MyDBHelper;
import com.example.programmerslibrary.MainActivity;
import com.example.programmerslibrary.R;
import com.example.programmerslibrary.models.Reader;

public class AddReaderFragment extends Fragment {


    FragmentManager fragmentManager;

    EditText firstname_edit, lastname_edit, email_edit;

    Button buttonBack, buttonSave;

    String user_id;
    String  firstname;
    String  lastname;
    String  email;

    private MyDBHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_reader, container, false);
        user_id = getArguments().getString("user");

        db = MainActivity.getDb();

        fragmentManager = getActivity().getSupportFragmentManager();

        //hasBook_check_box = (CheckBox) view.findViewById(R.id.has_book_check_box);
        firstname_edit = view.findViewById(R.id.firstname_edit_text);
        lastname_edit = view.findViewById(R.id.lastname_edit_text);
        email_edit = view.findViewById(R.id.email_edit_text);

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

                if(firstname.equalsIgnoreCase("")) {
                    firstname_edit.setError("please enter firstname");//it gives user to info message //use any one //

                } else if (checkEmailEqual(email)) {
                    email_edit.setError("please enter unique e-mail");//it gives user to info message //use any one //
                } else {
                    Reader newReader = new Reader();
                    newReader.setUserid(user_id);
                    newReader.setFirst_name(firstname);
                    newReader.setLast_name(lastname);
                    newReader.setEmail(email);
                    newReader.setHas_book(false);

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

    private boolean checkEmailEqual(String email) {
        for (Reader r : db.getAllReaders(user_id)
        ) {
            if (r.getEmail().equals(email)) return true;
        }
        return false;
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
