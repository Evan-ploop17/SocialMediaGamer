package com.example.socialmediagamer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.socialmediagamer.R;
import com.example.socialmediagamer.fragments.ChatFragment;
import com.example.socialmediagamer.fragments.FilterFragment;
import com.example.socialmediagamer.fragments.HomeFragment;
import com.example.socialmediagamer.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    public static Object java;
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(new HomeFragment());
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.itemHome:
                            openFragment(new HomeFragment());
                            return true;
                        case R.id.itemFilters:
                            openFragment(new FilterFragment());
                            return true;
                        case R.id.itemChat:
                            openFragment(new ChatFragment());
                            return true;
                        case R.id.itemProfile:
                            openFragment(new ProfileFragment());
                            return true;
                    }
                    // Este true me permite mostrar el nombre del fragmento en bottom menu
                    return true;
                }
            };
}