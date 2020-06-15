package com.hitorus.pick_a_book;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class DeleteBookDialogFragment extends DialogFragment {
    private Book mBook;

    public static DeleteBookDialogFragment newInstance(Book book) {
        DeleteBookDialogFragment fragment = new DeleteBookDialogFragment();
        Bundle args = new Bundle();
        fragment.setBook(book);
        fragment.setArguments(args);
        return fragment;
    }

    public interface DeleteBookDialogListener { void deleteBook(); }

    // Uses this instance of the interface to deliver action events
    DeleteBookDialogFragment.DeleteBookDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        String message = getResources().getString(R.string.deleteBookMessage) + " \""
                + mBook.getName() + "\", by " + mBook.getAuthor() + "?";
        builder.setMessage(message);

        builder.setPositiveButton(R.string.yesButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.deleteBook();
                    }
                })
                .setNegativeButton(R.string.noButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DeleteBookDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    // Overrides the Fragment.onAttach() method to instantiate the DeleteBookDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verifies that the host activity implements the callback interface
        try {
            // Instantiates the DeleteBookDialogListener so the events can be sent to the host
            listener = (DeleteBookDialogFragment.DeleteBookDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throws exception
            throw new ClassCastException(context.toString()
                    + " must implement DeleteBookDialogListener");
        }
    }

    public void setBook(Book book) { mBook = book; }
}
