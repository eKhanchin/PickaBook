package com.hitorus.pick_a_book;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.content.Context.CLIPBOARD_SERVICE;

public class AllBooksFragment extends Fragment{
    private EditText mSearchEditText;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Book> mBooks;
    private Book mSelectedBook;
    private int mBookPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflates the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_books, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // Gets view's fields
        mSearchEditText = view.findViewById(R.id.searchEditText);
        Button mSearchButton = view.findViewById(R.id.searchButton);
        RecyclerView mRecyclerView = view.findViewById(R.id.allBooksRecyclerView);

        // Sets onClick listener
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchBooksTable();
            }
        });

        // Improves performance, layout size of the RecyclerView does not change
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        final DbHandler dbHandler = new DbHandler(getActivity());
        mBooks = dbHandler.loadBooks();

        if (mBooks.size() > 0) {
            sortBooksList(mBooks);

            mAdapter = new BooksAdapter(mBooks, view.getContext(), this);
            mRecyclerView.setAdapter(mAdapter);

            displayFoundBooksNumber();
        }
        else {
            Toast.makeText(getContext(), R.string.noBooksToast, Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStack();
        }

    }

    /**
     * Searches for books and/or authors that match the search query
     */
    public void searchBooksTable() {
        String searchQuery = mSearchEditText.getText().toString().toLowerCase();
        if (!searchQuery.equals("")) {
            // Search query is not empty
            ArrayList<Book> requiredBooks = new ArrayList<>();
            for (Book book : mBooks) {
                if (book.getName().toLowerCase().contains(searchQuery)
                        || book.getAuthor().toLowerCase().contains(searchQuery)) {
                    // Matching the search query
                    requiredBooks.add(book);
                }
            }

            if (requiredBooks.size() == 0) {
                Toast.makeText(getContext(), R.string.noSearchResultsToast, Toast.LENGTH_SHORT).show();
            }
            else {
                // Replace old data with a new one
                mBooks.clear();
                mBooks.addAll(requiredBooks);
                mAdapter.notifyDataSetChanged();
                displayFoundBooksNumber();
            }
        }
    }

    /**
     * Sorts list of books so that finished books are at the end of the list.
     * @param mBooks - list of books.
     */
    private void sortBooksList(ArrayList<Book> mBooks)
    {
        ArrayList<Book> statusFinishedBooks = new ArrayList<>();
        ArrayList<Book> statusPendingAndReadingBooks = new ArrayList<>();

        for (Book book : mBooks)
        {
            if (book.getStatus() == Book.Status.FINISHED) statusFinishedBooks.add(book);
            else statusPendingAndReadingBooks.add(book);
        }

        statusPendingAndReadingBooks.addAll(statusFinishedBooks);

        this.mBooks = statusPendingAndReadingBooks;
    }

    /**
     * Saves selected book and it's position in a list and opens popup menu
     * @param view - current selected view in RecyclerView
     * @param position - position of a view in a list
     */
    public void showBookPopupMenu(View view, int position)
    {
        mSelectedBook = mBooks.get(position);
        mBookPosition = position;

        Context wrapper = new ContextThemeWrapper(getContext(), R.style.pickABookPopupMenuStyle);
        PopupMenu popup = new PopupMenu(wrapper, view);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                bookMenuItemClick(item);
                return true;
            }
        });
        popup.inflate(R.menu.book_popup_menu);
        popup.show();
    }

    /**
     * Menu item press callback
     * @param item - pressed menu item
     */
    public void bookMenuItemClick(MenuItem item) {

        DbHandler dbHandler = new DbHandler(getContext());

        switch (item.getItemId())
        {
            case R.id.startReadingOption:
                startReading(dbHandler);
                break;

            case R.id.finishReadingOption:
                finishReading(dbHandler);
                break;

            case R.id.clearStatusOption:
                clearStatus(dbHandler);
                break;

            case R.id.copyNameOption:
                copyBookAuthorNames();
                break;

            case R.id.renameOption:
                displayBookRenameDialog();
                break;

            case R.id.deleteBookOption:
                displayDeleteBookDialog();
                break;

            default:
                break;
        }
    }

    /**
     * Changes book's status to READING
     * @param dbHandler - database handler
     */
    public void startReading(DbHandler dbHandler) {
        changeStatus(dbHandler, "READING", R.string.changedStatusToReadingToast);
    }

    /**
     * Changes book's status to FINISHED
     * @param dbHandler - database handler
     */
    public void finishReading(DbHandler dbHandler) {
        changeStatus(dbHandler, "FINISHED", R.string.changedStatusToFinishedToast);
    }

    /**
     * Clear book's status - changes it to PENDING
     * @param dbHandler - handlers of a database
     */
    public void clearStatus(DbHandler dbHandler) {
        changeStatus(dbHandler, "PENDING", R.string.changedStatusToPendingToast);
    }

    /**
     * Changes book's status
     * @param dbHandler - database handler
     * @param status - book's new status
     * @param toastStringId - id of a string to display as toast
     */
    public void changeStatus(DbHandler dbHandler, String status, int toastStringId) {

        String[] newValues = new String[3];
        newValues[0] = mSelectedBook.getName();
        newValues[1] = mSelectedBook.getAuthor();
        newValues[2] = status;

        boolean results = dbHandler.updateBook(mSelectedBook, newValues);

        if (results) {
            mSelectedBook.setStatus(Book.Status.valueOf(status));
            this.mBooks.set(mBookPosition, mSelectedBook);
            this.mAdapter.notifyDataSetChanged();
            Toast.makeText(getContext(), toastStringId, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Copies book's or author's name to the clipboard
     */
    public void copyBookAuthorNames()
    {
        final android.content.ClipboardManager clipboardManager =
                (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);

        ClipData clipData = ClipData.newPlainText("Source Text",
                "\"" + mBooks.get(mBookPosition).getName() + "\", "
                        + mBooks.get(mBookPosition).getAuthor());

        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(getContext(), R.string.copyObjectToast,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Displays a dialog that allows to rename selected book
     */
    public void displayBookRenameDialog() {
        DialogFragment dialog = BookRenameDialogFragment.newInstance(mSelectedBook);
        dialog.show(getActivity().getSupportFragmentManager(), "BookRenameDialogFragment");
    }

    /**
     * Renames selected book
     * @param dialog - a dialog that allows to enter a new name
     */
    public void renameBook(BookRenameDialogFragment dialog) {
        EditText newBookNameEditText = dialog.getBookRenameEditText();
        EditText newAuthorNameEditText = dialog.getAuthorRenameEditText();

        String bookName = newBookNameEditText.getText().toString();
        String authorName = newAuthorNameEditText.getText().toString();

        String[] newValues = new String[3];
        newValues[0] = bookName;
        newValues[1] = authorName;
        newValues[2] = mSelectedBook.getStatus().toString();

        DbHandler dbHandler = new DbHandler(getActivity());
        boolean results = dbHandler.updateBook(mSelectedBook, newValues);

        if (results) {
            mSelectedBook.setName(bookName);
            mSelectedBook.setAuthor(authorName);
            this.mBooks.set(mBookPosition, mSelectedBook);
            this.mAdapter.notifyDataSetChanged();
            Toast.makeText(getContext(), R.string.bookRenameToast, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Displays a dialog that allows to delete selected book
     */
    public void displayDeleteBookDialog() {
        DialogFragment dialog = DeleteBookDialogFragment.newInstance(mSelectedBook);
        dialog.show(getActivity().getSupportFragmentManager(), "DeleteBookDialogFragment");
    }

    /**
     * Deletes selected book
     */
    public void deleteBook() {
        DbHandler dbHandler = new DbHandler(getActivity());
        boolean result = dbHandler.deleteBook(mBooks.get(mBookPosition).getName(),
                mBooks.get(mBookPosition).getAuthor());

        if (result) {
            this.mBooks.remove(mBookPosition);
            this.mAdapter.notifyDataSetChanged();
            Toast.makeText(getContext(), R.string.deleteBookToast, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Displays a current number of books in a list as a toast
     */
    public void displayFoundBooksNumber() {
        String toast = "Found " + mBooks.size() + " books";
        Toast.makeText(getContext(), toast, Toast.LENGTH_SHORT).show();
    }
}
