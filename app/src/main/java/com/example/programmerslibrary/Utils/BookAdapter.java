package com.example.programmerslibrary.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.programmerslibrary.Items.Items;
import com.example.programmerslibrary.R;
import com.example.programmerslibrary.models.Book;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {

    private static final String TAG = "BookAdapter";

    private HashMap<Integer, Boolean> checkedMap = new HashMap<>();


    private List<Book> booksList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView author;
        TextView numberOfBooks;
        TextView bookStatus;
        ImageView cover;

        public MyViewHolder(View view) {
            super(view);
            this.title  = view.findViewById(R.id.textView_title);
            this.author = view.findViewById(R.id.textView_author);
            this.numberOfBooks = view.findViewById(R.id.textView_numberOfBooks);
            this.bookStatus = view.findViewById(R.id.textView_book_status);
            this.cover = view.findViewById(R.id.image_book);
        }
    }


    public BookAdapter(Context context, ArrayList<Book> objects) {
        booksList = objects;
        for(int i = 0; i < objects.size(); i++){
            checkedMap.put(i, false);
        }
    }

    @Override
    public BookAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_adapter_view_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Book book = booksList.get(position);

        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthors());
        holder.numberOfBooks.setText(Integer.toString(book.getNumberOfCopies()));
        holder.bookStatus.setText(book.getBookStatus().toString());
        Bitmap bitmap = BitmapFactory.decodeFile(book.getCover());
        holder.cover.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }

    public void toggleChecked(int position) {
        if (checkedMap.get(position)) {
            checkedMap.put(position, false);
        } else {
            checkedMap.put(position, true);
        }

        notifyDataSetChanged();
    }

    public ArrayList<Book> getCheckedItems() {
        ArrayList<Book> checkedItems = new ArrayList<>();

        for (int i = 0; i < checkedMap.size(); i++) {
            if (checkedMap.get(i)) {
                (checkedItems).add(Items.getListofBooks().get(i));
            }
        }

        return checkedItems;
    }
}