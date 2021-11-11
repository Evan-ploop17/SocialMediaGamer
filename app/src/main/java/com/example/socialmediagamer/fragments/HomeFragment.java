package com.example.socialmediagamer.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.socialmediagamer.R;
import com.example.socialmediagamer.activities.MainActivity;
import com.example.socialmediagamer.activities.PostActivity;
import com.example.socialmediagamer.providers.AuthProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {

    View mView;
    FloatingActionButton mFab;
    Toolbar mToolbar;
    AuthProvider mAuthProvider;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        mFab = mView.findViewById(R.id.fab);
        mToolbar = mView.findViewById(R.id.toolbar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.publications);
        setHasOptionsMenu(true);
        mAuthProvider = new AuthProvider();



        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPost();
            }
        });
        return mView;
    }

    private void goToPost() {
        Intent intent = new Intent(getContext(), PostActivity.class);
        startActivity(intent); 
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.itemLogout){
            logOut();
        }
        return true;
    }

    private void logOut() {
        mAuthProvider.logout();
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags( intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(intent);
    }
}