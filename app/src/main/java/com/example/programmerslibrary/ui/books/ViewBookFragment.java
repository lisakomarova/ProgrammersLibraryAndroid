package com.example.programmerslibrary.ui.books;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.programmerslibrary.DataBase.MyAPIHelper;
import com.example.programmerslibrary.MainActivity;
import com.example.programmerslibrary.R;
import com.example.programmerslibrary.models.Book;


public class ViewBookFragment extends Fragment {

    FragmentManager fragmentManager;

    ImageView imageView;
    EditText title_edit, genre_edit, publ_date_edit, authors_edit;
    Spinner spinner;
    Button buttonBackEdit, buttonSaveEdit;

    MyAPIHelper db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_book, container, false);


        db = MainActivity.getDb();

        fragmentManager = getActivity().getSupportFragmentManager();

        spinner = (Spinner) view.findViewById(R.id.view_book_status_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.book_status_array, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setEnabled(false);
        spinner.setClickable(false);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        title_edit = (EditText) view.findViewById(R.id.view_title_edit_text);
        genre_edit = (EditText) view.findViewById(R.id.view_genre_edit_text);
        publ_date_edit = (EditText) view.findViewById(R.id.view_publ_date_edit_text);
        authors_edit = (EditText) view.findViewById(R.id.view_authors_edit_text);
        buttonBackEdit = (Button) view.findViewById(R.id.viewBookBack);
        buttonSaveEdit = (Button) view.findViewById(R.id.ViewBookEdit);
        imageView = (ImageView) view.findViewById(R.id.view_cover_image);

        //Retrieve the value
        final int position = getArguments().getInt("position");

        final Book book = db.getBook(position);
        title_edit.setText(book.getTitle());
        genre_edit.setText(book.getGenre());
        publ_date_edit.setText(Integer.toString(book.getPublication_year()));
        authors_edit.setText(book.getAuthors());
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

        buttonSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create new fragment and transaction
                Fragment newFragment = new EditBookFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                Bundle args = new Bundle();
                args.putInt("position", book.getBook_id());
                newFragment.setArguments(args);

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack if needed
                transaction.replace(R.id.nav_host_fragment, newFragment);

                // Commit the transaction
                transaction.commit();

            }
        });
        return view;
    }
}