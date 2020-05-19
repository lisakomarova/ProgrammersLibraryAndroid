package com.example.programmerslibrary.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.programmerslibrary.Items.Items;
import com.example.programmerslibrary.R;
import com.example.programmerslibrary.models.Book;
import com.example.programmerslibrary.models.Loan;
import com.example.programmerslibrary.models.Reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.MyViewHolder> {

    private static final String TAG = "LoanAdapter";

    private HashMap<Integer, Boolean> checkedMap = new HashMap<>();


    private List<Loan> loansList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView reader;
        TextView book;
        TextView date;

        public MyViewHolder(View view) {
            super(view);
            this.reader  = view.findViewById(R.id.textView_loan_reader);
            this.book = view.findViewById(R.id.textView_loan_book);
            this.date = view.findViewById(R.id.textView_loan_date);
        }
    }


    public LoanAdapter(Context context, ArrayList<Loan> objects) {
        loansList = objects;
        for(int i = 0; i < objects.size(); i++){
            checkedMap.put(i, false);
        }
    }


    @Override
    public LoanAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.loan_adapter_view_layout, parent, false);

        return new LoanAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LoanAdapter.MyViewHolder holder, int position) {
        Loan loan = loansList.get(position);

        holder.reader.setText(loan.getReader());
        holder.book.setText(loan.getBook());
        holder.date.setText(loan.getLoan_date());
    }

    @Override
    public int getItemCount() {
        return loansList.size();
    }

}