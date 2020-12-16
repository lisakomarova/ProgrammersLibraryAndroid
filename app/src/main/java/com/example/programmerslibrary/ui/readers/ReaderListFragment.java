package com.example.programmerslibrary.ui.readers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.programmerslibrary.DataBase.MyDBHelper;
import com.example.programmerslibrary.MainActivity;
import com.example.programmerslibrary.R;
import com.example.programmerslibrary.SignUpActivity;
import com.example.programmerslibrary.Utils.ReaderAdapter;
import com.example.programmerslibrary.Utils.RecyclerTouchListener;
import com.example.programmerslibrary.models.Reader;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ReaderListFragment extends Fragment {

    MyDBHelper db;
    private ArrayList<Reader> readerList= new ArrayList<>();
    private ReaderAdapter mAdapter;
    private RecyclerView recyclerView;
    private TextView noReadersView;
    String user_id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
        toggleEmptyNotes();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_readers);
        noReadersView = view.findViewById(R.id.empty_readers_view);

        user_id = MainActivity.getUserID();

        db = MainActivity.getDb();

        readerList.addAll(db.getAllReaders(user_id));

        FloatingActionButton fab = view.findViewById(R.id.fab_readers);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create new fragment and transaction
                Fragment newFragment = new AddReaderFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                Bundle args = new Bundle();
                args.putString("user", user_id);

                newFragment.setArguments(args);
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack if needed
                transaction.replace(R.id.nav_host_fragment, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });

        mAdapter = new ReaderAdapter(getContext(), readerList);
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

            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));

        setHasOptionsMenu(true);

        Toolbar toolbar = view.findViewById(R.id.toolbar_readers);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        return view;
    }


    /**
     * Opens dialog with Edit - Delete options
     * Edit - 0
     * Delete - 0
     */
    private void showActionsDialog(final int position) {
        List<String> actions = new ArrayList<>();
        actions.add("Edit");

        if (!readerList.get(position).doesHaveBook()) {
            actions.add("Delete");
        }
        if (readerList.get(position).doesHaveBook()) {
            actions.add("Send message");
        }

        final CharSequence[] charActions = actions.toArray(new CharSequence[actions.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose option");
        builder.setItems(charActions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (charActions[which].equals("Edit")) {
                    // Create new fragment and transaction
                    Fragment newFragment = new EditReaderFragment();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                    Bundle args = new Bundle();
                    args.putInt("position", readerList.get(position).getReader_id());
                    newFragment.setArguments(args);

                    // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack if needed
                    transaction.replace(R.id.nav_host_fragment, newFragment);

                    // Commit the transaction
                    transaction.commit();
                } else if (charActions[which].equals("Delete")) {
                    deleteReader(position);
                } else {
                    sendEmail(position);
                }
            }

        });
        builder.show();
    }

    private void sendEmail(int position) {
        String to = db.getReader(readerList.get(position).getReader_id()).getEmail();
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        email.putExtra(Intent.EXTRA_SUBJECT, "Book");
        email.putExtra(Intent.EXTRA_TEXT, "Верни книгу!");

        //need this to prompts email client only
        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }


    /**
     * Deleting book from db and removing the
     * item from the list by its position
     */
    private void deleteReader(int position) {
        // deleting the book from db
        db.deleteReader(readerList.get(position));

        // removing the book from the list
        readerList.remove(position);
        mAdapter.notifyItemRemoved(position);

        toggleEmptyNotes();
    }

    /**
     * Toggling list and empty notes view
     */
    private void toggleEmptyNotes() {
        // you can check notesList.size() > 0

        if (db.getReadersCount(user_id) > 0) {
            noReadersView.setVisibility(View.GONE);
        } else {
            noReadersView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.appbar_reader_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        ArrayList<Reader> newReaderList;
        switch (item.getItemId()){
            case R.id.item_has_book:
                readerList.clear();
                readerList.addAll(db.getAllReaders(user_id));
                newReaderList = new ArrayList<>();
                for (Reader r: readerList
                     ) {
                    if(r.doesHaveBook()){
                        newReaderList.add(r);
                    }
                }
                readerList.retainAll(newReaderList);
                mAdapter.notifyDataSetChanged();

                return true;
            case R.id.item_has_not_book:
                readerList.clear();
                readerList.addAll(db.getAllReaders(user_id));
                newReaderList = new ArrayList<>();
                for (Reader r: readerList
                ) {
                    if(!r.doesHaveBook()){
                        newReaderList.add(r);
                    }
                }
                readerList.retainAll(newReaderList);
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.item_all:
                readerList.clear();
                readerList.addAll(db.getAllReaders(user_id));
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.item_logout:
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getActivity(), SignUpActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
                return true;
            default:
                return false;
        }
    }

}