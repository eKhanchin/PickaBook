package com.hitorus.pick_a_book;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

public class BookRenameDialogFragment extends DialogFragment {
    private Book mBook;
    private EditText mNewBookNameEditText;
    private EditText mNewAuthorNameEditText;

    public static BookRenameDialogFragment newInstance(Book book) {
        BookRenameDialogFragment fragment = new BookRenameDialogFragment();
        Bundle args = new Bundle();
        fragment.setBook(book);
        fragment.setArguments(args);
        return fragment;
    }

    public interface BookRenameDialogListener { void renameBook(BookRenameDialogFragment dialog); }

    // Uses this instance of the interface to deliver action events
    BookRenameDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_rename_book, null);

        this.addBookString(view);

        builder.setView(view)
                .setPositiveButton(R.string.renameButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.renameBook(BookRenameDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.cancelButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        BookRenameDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    // Overrides the Fragment.onAttach() method to instantiate the BookRenameDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verifies that the host activity implements the callback interface
        try {
            // Instantiates the BookRenameDialogListener so the events can be sent to the host
            listener = (BookRenameDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throws exception
            throw new ClassCastException(context.toString()
                    + " must implement BookRenameDialogListener");
        }
    }

    /**
     * Adds a book's name and author's name as a string to EditText
     * @param view - layout view
     */
    public void addBookString(View view) {
        String bookName = mBook.getName();
        mNewBookNameEditText = view.findViewById(R.id.newBookNameEditText);
        mNewBookNameEditText.setText(bookName);

        String authorName = mBook.getAuthor();
        mNewAuthorNameEditText = view.findViewById(R.id.newAuthorNameEditText);
        mNewAuthorNameEditText.setText(authorName);
    }

    public void setBook(Book book) { mBook = book; }

    public EditText getBookRenameEditText() { return mNewBookNameEditText; }

    public EditText getAuthorRenameEditText() { return mNewAuthorNameEditText; }
}
