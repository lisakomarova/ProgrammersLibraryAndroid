package com.example.programmerslibrary.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.programmerslibrary.Items.Items;
import com.example.programmerslibrary.R;
import com.example.programmerslibrary.models.Book;
import com.example.programmerslibrary.models.Reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ReaderAdapter extends RecyclerView.Adapter<ReaderAdapter.MyViewHolder> {

private static final String TAG = "ReaderAdapter";

private HashMap<Integer, Boolean> checkedMap = new HashMap<>();


private List<Reader> readersList;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView firstName;
    TextView lastName;
    TextView email;
    ImageView hasBook;

    public MyViewHolder(View view) {
        super(view);
        this.firstName  = view.findViewById(R.id.textView_firstname);
        this.lastName = view.findViewById(R.id.textView_lastname);
        this.email = view.findViewById(R.id.textView_email);
        this.hasBook = view.findViewById(R.id.image_has_book);
    }
}


    public ReaderAdapter(Context context, ArrayList<Reader> objects) {
        readersList = objects;
        for(int i = 0; i < objects.size(); i++){
            checkedMap.put(i, false);
        }
    }

    @Override
    public ReaderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reader_adapter_view_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReaderAdapter.MyViewHolder holder, int position) {
        Reader reader= readersList.get(position);

        holder.firstName.setText(reader.getFirstName());
        holder.lastName.setText(reader.getLastName());
        holder.email.setText(reader.getEmail());
        if(reader.doesHaveBook())
            holder.hasBook.setImageResource(R.drawable.ic_lens_red_24dp);
        else
            holder.hasBook.setImageResource(R.drawable.ic_lens_green_24dp);
    }

    @Override
    public int getItemCount() {
        return readersList.size();
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