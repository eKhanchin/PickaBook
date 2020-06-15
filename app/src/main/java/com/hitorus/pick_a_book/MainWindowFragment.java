package com.hitorus.pick_a_book;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainWindowFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflates the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_window, container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // Finds addBookButton view and sets its on click listener
        Button addBookButton = view.findViewById(R.id.addBookButton);
        addBookButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Opens AddBookFragment
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                AddBookFragment fragment = new AddBookFragment();
                fragmentTransaction.replace(R.id.mainActivity, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        Button allBooksButton = view.findViewById(R.id.allBooksButton);
        allBooksButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Opens AllBooksFragment
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                AllBooksFragment fragment = new AllBooksFragment();
                fragmentTransaction.replace(R.id.mainActivity, fragment, "AllBooksFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        Button pickButton = view.findViewById(R.id.pickButton);
        pickButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Opens PickFragment
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                PickFragment fragment = new PickFragment();
                fragmentTransaction.replace(R.id.mainActivity, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }
}
