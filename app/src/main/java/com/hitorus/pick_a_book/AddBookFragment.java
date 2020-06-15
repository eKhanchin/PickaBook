package com.hitorus.pick_a_book;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


public class AddBookFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflates the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_book, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // Finds addBookButton view and sets its on click listener
        Button addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addBookOperation();
            }
        });
    }

    /* Adds a book to a Book table */
    public void addBookOperation() {
        String bookName;
        String authorName;

        EditText bookNameEditText = getActivity().findViewById(R.id.bookNameEditText);
        EditText authorNameEditText = getActivity().findViewById(R.id.authorNameEditText);

        bookName = bookNameEditText.getText().toString().trim();
        authorName = authorNameEditText.getText().toString().trim();

        if (bookName.equals("") || authorName.equals("")) {
            // Some of the names were not given
            Toast.makeText(getActivity(), R.string.absentNameAuthorToast, Toast.LENGTH_SHORT).show();
        }
        else {
            DbHandler dbHandler = new DbHandler(getActivity());
            Book book;

            // Checks whether it already exists
            book = dbHandler.findBook(bookName, authorName);

            if (book != null) {
                // Book already exists
                Toast.makeText(getActivity(), R.string.bookAlreadyExist, Toast.LENGTH_SHORT).show();
            }
            else {
                // Adds a book
                book = new Book(bookName, authorName);
                dbHandler.addBook(book);

                bookNameEditText.setText("");
                authorNameEditText.setText("");

                Toast.makeText(getActivity(), R.string.addedBookToast, Toast.LENGTH_SHORT).show();
            }
        } //else
    }
}
