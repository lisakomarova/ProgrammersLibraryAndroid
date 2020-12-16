package com.example.programmerslibrary.ui.loan;

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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.programmerslibrary.DataBase.MyDBHelper;
import com.example.programmerslibrary.MainActivity;
import com.example.programmerslibrary.R;
import com.example.programmerslibrary.SignUpActivity;
import com.example.programmerslibrary.Utils.LoanAdapter;
import com.example.programmerslibrary.Utils.RecyclerTouchListener;
import com.example.programmerslibrary.models.Loan;
import com.example.programmerslibrary.models.Reader;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class LoanListFragment extends Fragment {

    MyDBHelper db;
    private ArrayList<Loan> loanList = new ArrayList<>();
    private LoanAdapter mAdapter;
    private RecyclerView recyclerView;
    private TextView noLoansView;
    String user_id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
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

        View view = inflater.inflate(R.layout.fragment_loan_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_loans);
        noLoansView = view.findViewById(R.id.empty_loans_view);

        user_id = MainActivity.getUserID();

        db = MainActivity.getDb();

        loanList.addAll(db.getAllLoans(user_id));

        for (Loan l : loanList
             ) {
            l.setReader(db.getReaderNameByReaderID(l.getReader_id()));
            l.setBook(db.getBookTitleByBookID(l.getBook_id()));
        }

        mAdapter = new LoanAdapter(getContext(), loanList);
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
                 //showActionsDialog(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        Toolbar toolbar = view.findViewById(R.id.toolbar_loans);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        return view;
    }


    /**
     * Toggling list and empty notes view
     */
    private void toggleEmptyNotes() {
        // you can check notesList.size() > 0

        if (db.getLoansCount(user_id) > 0) {
            noLoansView.setVisibility(View.GONE);
        } else {
            noLoansView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.appbar_loans_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        ArrayList<Reader> newReaderList;
        switch (item.getItemId()) {
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