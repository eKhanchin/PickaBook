package com.hitorus.pick_a_book;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {
    private static ArrayList<Book> mBooks;
    private Context mContext;
    private static AllBooksFragment mFragment;

    // Provides a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView bookNameItem;
        public TextView bookStatusItem;
        public TextView authorNameItem;

        public ViewHolder(View v) {
            super(v);

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mFragment.showBookPopupMenu(view, getAdapterPosition());
                    return true;
                }
            });

            bookNameItem = v.findViewById(R.id.bookNameItem);
            bookStatusItem = v.findViewById(R.id.bookStatusItem);
            authorNameItem = v.findViewById(R.id.authorNameItem);
        }

        public TextView getBookNameItem() {
            return bookNameItem;
        }

        public TextView getBookStatusItem() {
            return bookStatusItem;
        }

        public TextView getAuthorNameItem() {
            return authorNameItem;
        }
    }

    public BooksAdapter (ArrayList<Book> books, Context context, AllBooksFragment fragment) {
        mBooks = books;
        mContext = context;
        mFragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Creates a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView bookNameTextField = holder.getBookNameItem();
        TextView authorTextField = holder.getAuthorNameItem();
        TextView statusTextField = holder.getBookStatusItem();

        bookNameTextField.setText(mBooks.get(position).getName());
        authorTextField.setText(mBooks.get(position).getAuthor());

        String status = String.valueOf(mBooks.get(position).getStatus());
        if(status.equals("PENDING"))
            statusTextField.setText("");
        else
            statusTextField.setText(status);
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }
}
