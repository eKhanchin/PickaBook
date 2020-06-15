package com.hitorus.pick_a_book;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity
        implements BookRenameDialogFragment.BookRenameDialogListener,
            DeleteBookDialogFragment.DeleteBookDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Adds a MainWindowFragment on start
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        MainWindowFragment fragment = new MainWindowFragment();
        fragmentTransaction.add(R.id.mainActivity, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void renameBook(BookRenameDialogFragment dialog) {
        AllBooksFragment fragment = (AllBooksFragment) getSupportFragmentManager()
                .findFragmentByTag("AllBooksFragment");

        fragment.renameBook(dialog);
    }


    @Override
    public void deleteBook() {
        AllBooksFragment fragment = (AllBooksFragment) getSupportFragmentManager()
                .findFragmentByTag("AllBooksFragment");

        fragment.deleteBook();
    }
}