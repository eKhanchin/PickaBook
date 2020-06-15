package com.hitorus.pick_a_book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Random;

public class PickFragment extends Fragment {
    private TextView mBookNameTextView;
    private TextView mAuthorNameTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflates the layout for this fragment
        return inflater.inflate(R.layout.fragment_pick, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        mBookNameTextView = view.findViewById(R.id.pickedBookNameLabel);
        mAuthorNameTextView = view.findViewById(R.id.pickedAuthorNameLabel);

        Button approveButton = view.findViewById(R.id.readApproveButton);
        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptToReadBook();
            }
        });

        Button repickButton = view.findViewById(R.id.repickButton);
        repickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repickBook();
            }
        });

        final Button declineButton = view.findViewById(R.id.readDeclineButton);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                declineBook();
            }
        });

        getPickedBook();
    }

    /**
     * Picks random book. If there are no pending books, goes back to previous fragment
     */
    public void getPickedBook()
    {
        DbHandler dbHandler = new DbHandler(getActivity());
        ArrayList<Book> mBooks = dbHandler.loadBooks();
        ArrayList<Book> onlyPendingBooks = new ArrayList<>();

        for (Book book : mBooks)
        {
            if (String.valueOf(book.getStatus()).equals("PENDING"))
                onlyPendingBooks.add(book);
        }

        int size = onlyPendingBooks.size();

        if (size > 0) {
            Random rand = new Random();
            int randomIndex = rand.nextInt(size);

            Book pickedBook = onlyPendingBooks.get(randomIndex);

            mBookNameTextView.setText(pickedBook.getName());
            mAuthorNameTextView.setText(pickedBook.getAuthor());
        }
        else {
            Toast.makeText(getContext(), R.string.noBooksToast, Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    /**
     * When user accepts to read the book - changes status to READING
     */
    public void acceptToReadBook()
    {
        String name = mBookNameTextView.getText().toString();
        String author = mAuthorNameTextView.getText().toString();

        DbHandler dbHandler = new DbHandler(getActivity());
        Book book = dbHandler.findBook(name, author);

        String[] newValues = new String[3];
        newValues[0] = book.getName();
        newValues[1] = book.getAuthor();
        newValues[2] = "READING";

        dbHandler.updateBook(book, newValues);

        Toast.makeText(getContext(), R.string.pickBookToast, Toast.LENGTH_SHORT).show();
        getActivity().getSupportFragmentManager().popBackStack();
    }

    /**
     * Re-picks new book
     */
    public void repickBook()
    {
        this.getPickedBook();
    }

    /**
     * When user declines to read the book - returns to previous fragment
     */
    public void declineBook()
    {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
