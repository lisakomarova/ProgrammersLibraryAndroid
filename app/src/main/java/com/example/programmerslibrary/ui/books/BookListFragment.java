package com.example.programmerslibrary.ui.books;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.programmerslibrary.DataBase.MyDBHelper;
import com.example.programmerslibrary.Enumerations.BookStatus;
import com.example.programmerslibrary.MainActivity;
import com.example.programmerslibrary.R;
import com.example.programmerslibrary.Utils.BookAdapter;
import com.example.programmerslibrary.Utils.RecyclerTouchListener;
import com.example.programmerslibrary.models.Book;
import com.example.programmerslibrary.models.Loan;
import com.example.programmerslibrary.ui.loan.IssueBookFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class BookListFragment extends Fragment {

    MyDBHelper db;
    FragmentManager fragmentManager;
    private ArrayList<Book> bookList = new ArrayList<>();
    private BookAdapter mAdapter;
    private RecyclerView recyclerView;
    private TextView noNotesView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        fragmentManager = getActivity().getSupportFragmentManager();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
        toggleEmptyNotes();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_book_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        noNotesView = view.findViewById(R.id.empty_notes_view);


        db = MainActivity.getDb();

        bookList.addAll(db.getAllBooks());

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create new fragment and transaction
                Fragment newFragment = new AddBookFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack if needed
                transaction.replace(R.id.nav_host_fragment, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });

        mAdapter = new BookAdapter(getContext(), bookList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        toggleEmptyNotes();

        /**
         * On long press on RecyclerView item, open alert dialog
         * with options to choose
         * Edit and Delete
         * */
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                viewBook(position);
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        return view;
    }


    /**
     * Inserting new book in db
     * and refreshing the list
     */
    private void createBook(Book book) {
        // inserting book in db and getting
        // newly inserted book id
        long id = db.insertBook(book);

        // get the newly inserted book from db
        Book n = db.getBook(id);

        if (n != null) {
            // adding new note to array list at 0 position
            bookList.add(0, n);

            // refreshing the list
            mAdapter.notifyDataSetChanged();

            toggleEmptyNotes();
        }
    }


    /**
     * Deleting book from SQLite and removing the
     * item from the list by its position
     */
    private void deleteBook(int position) {
        // deleting the book from db
        db.deleteBook(bookList.get(position));

        // removing the book from the list
        bookList.remove(position);
        mAdapter.notifyItemRemoved(position);

        toggleEmptyNotes();
    }

    /**
     * Opens dialog with Edit - Delete options
     * Edit - 0
     * Delete - 0
     */
    private void showActionsDialog(final int position) {
        List<String> actions = new ArrayList<>();
        actions.add("Edit");
        actions.add("Delete");
        actions.add("View");
        //CharSequence actions[] = new CharSequence[]{"Edit", "Delete", "View"};

        if(bookList.get(position).getBookStatus().toString().equals("AVAILABLE") && bookList.get(position).getNumberOfCopies() > 0)
            actions.add("Issue book");
        if(bookList.get(position).getBookStatus().toString().equals("LOANED"))
            actions.add("Return book");
        if(!bookList.get(position).getBookStatus().toString().equals("DISPOSED"))
            actions.add("Dispose book");
        final CharSequence[] charActions = actions.toArray(new CharSequence[actions.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose option");
        builder.setItems(charActions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // Create new fragment and transaction
                    Fragment newFragment = new EditBookFragment();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                    Bundle args = new Bundle();
                    args.putInt("position", bookList.get(position).getIdBook());
                    newFragment.setArguments(args);

                    // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack if needed
                    transaction.replace(R.id.nav_host_fragment, newFragment);
                    // Commit the transaction
                    transaction.commit();
                }
                else if (which == 1) {
                    deleteBook(position);
                }
                else if (which == 2) {
                    viewBook(position);
                }
                else if (charActions[which].equals("Issue book")){
                    Fragment newFragment = new IssueBookFragment();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                    Bundle args = new Bundle();
                    args.putInt("position", bookList.get(position).getIdBook());

                    newFragment.setArguments(args);

                    // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack if needed
                    transaction.replace(R.id.nav_host_fragment, newFragment);
                    // Commit the transaction
                    transaction.commit();

                }
                else if (charActions[which].equals("Return book")){

                }
                else if (charActions[which].equals("Dispose book")){

                }
            }
        });
        builder.show();
    }

    private void viewBook(int position) {
        // Create new fragment and transaction
        Fragment newFragment = new ViewBookFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Bundle args = new Bundle();
        args.putInt("position", bookList.get(position).getIdBook());
        newFragment.setArguments(args);

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack if needed
        transaction.replace(R.id.nav_host_fragment, newFragment);

        // Commit the transaction
        transaction.commit();
    }


    /**
     * Toggling list and empty notes view
     */
    private void toggleEmptyNotes() {
        // you can check notesList.size() > 0

        if (db.getBooksCount() > 0) {
            noNotesView.setVisibility(View.GONE);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();
        inflater.inflate(R.menu.appbar_book_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        ArrayList<Book> newBookList;
        switch (item.getItemId()){
            case R.id.item_show_available:
                newBookList = new ArrayList<>();
                bookList.clear();
                bookList.addAll(db.getAllBooks());
                for (Book b: bookList
                     ) {
                    if(b.getBookStatus().toString().equals("AVAILABLE")){
                        newBookList.add(b);
                    }
                }
                bookList.retainAll(newBookList);
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.item_show_landed:
                newBookList = new ArrayList<>();
                bookList.clear();
                bookList.addAll(db.getAllBooks());
                for (Book b: bookList
                ) {
                    if(b.getBookStatus().toString().equals("LOANED")){
                        newBookList.add(b);
                    }
                }
                bookList.retainAll(newBookList);
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.item_show_disposed:
                bookList.clear();
                bookList.addAll(db.getAllBooks());
                newBookList = new ArrayList<>();
                for (Book b: bookList
                ) {
                    if(b.getBookStatus().toString().equals("DISPOSED")){
                        newBookList.add(b);
                    }
                }
                bookList.retainAll(newBookList);
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.item_show_all:
                bookList.clear();
                bookList.addAll(db.getAllBooks());
                mAdapter.notifyDataSetChanged();
                return true;
            default:
                return false;
        }
    }
}