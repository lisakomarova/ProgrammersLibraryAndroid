package com.example.programmerslibrary.ui.loan;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

import com.example.programmerslibrary.DataBase.MyDBHelper;
import com.example.programmerslibrary.MainActivity;
import com.example.programmerslibrary.R;
import com.example.programmerslibrary.models.Book;
import com.example.programmerslibrary.models.Loan;
import com.example.programmerslibrary.models.Reader;
import com.example.programmerslibrary.ui.books.BookListFragment;

import java.util.ArrayList;


public class IssueBookFragment extends Fragment {

    FragmentManager fragmentManager;

    Spinner spinnerBook, spinnerReader;
    Button buttonIssueBook, buttonCancel;

    String bookTitle;
    String readerName;

    MyDBHelper db;

    public IssueBookFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_issue_book, container, false);

        db = MainActivity.getDb();

        fragmentManager = getActivity().getSupportFragmentManager();

        spinnerBook = (Spinner) view.findViewById(R.id.spinnerBooksLoanFragment);
        spinnerReader = (Spinner) view.findViewById(R.id.spinnerReadersLoanFragment);

        spinnerBook.setOnItemSelectedListener(new SpinnerBookClass());
        spinnerReader.setOnItemSelectedListener(new SpinnerReaderClass());

        ArrayList<String> books = new ArrayList<>();
        for (Book b : db.getAllBooks()
        ) {
            books.add(b.getTitle());
        }

        ArrayList<String> readers = new ArrayList<>();
        for (Reader r : db.getAllReaders()
        ) {
            if (!r.doesHaveBook())
            readers.add(r.getFirstName() + " " + r.getLastName());
        }


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapterBook = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, books);
        // Specify the layout to use when the list of choices appears
        adapterBook.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerBook.setAdapter(adapterBook);

        //setting book title
        final int position = getArguments().getInt("position");
        int spinnerBookPosition = adapterBook.getPosition(db.getBook(position).getTitle());

        spinnerBook.setSelection(spinnerBookPosition);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapterReader = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, readers);
        // Specify the layout to use when the list of choices appears
        adapterReader.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerReader.setAdapter(adapterReader);

        buttonIssueBook = view.findViewById(R.id.textButton_issue_book);
        buttonCancel = view.findViewById(R.id.textButton_issue_book_cancel);


        buttonCancel.setOnClickListener(new View.OnClickListener() {
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

        buttonIssueBook.setEnabled(false);
        buttonIssueBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Loan loan = new Loan();
                Book book = new Book();
                Reader reader = new Reader();
                int book_id = 0, reader_id = 0;
                int updated_number_f_copies = 0;
                String name;

                //getting ids of chosen book and reader
                //getting number of copies and decremment it
                for (Book b : db.getAllBooks()
                ) {
                    if ( b.getTitle().equals(bookTitle)){
                        book_id = b.getIdBook();
                        book = new Book(b);
                        updated_number_f_copies = b.getNumberOfCopies() - 1;
                    }
                }

                for (Reader r : db.getAllReaders()
                ) {
                    name = r.getFirstName() + " " + r.getLastName();
                    if(name.equals(readerName)){
                        reader = new Reader(r);
                        reader_id = r.getReaderID();
                    }
                }
                loan.setBook_id(book_id);
                loan.setReader_id(reader_id);
                loan.setIf_closed(false);
                if(book!=null && updated_number_f_copies!=-1){
                    book.setNumberOfCopies(updated_number_f_copies);
                    db.updateBook(book);
                }
                if(reader!=null){
                    reader.setHasBook(true);
                    db.updateReader(reader);
                }
                db.insertLoan(loan);
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

        return view;
    }

    private class SpinnerBookClass implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            bookTitle = parent.getItemAtPosition(pos).toString();
            buttonIssueBook.setEnabled(true);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

            buttonIssueBook.setEnabled(false);
        }
    }

    private class SpinnerReaderClass implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            readerName = parent.getItemAtPosition(pos).toString();
            buttonIssueBook.setEnabled(true);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            buttonIssueBook.setEnabled(false);
        }
    }
}