package com.example.programmerslibrary;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.programmerslibrary.DataBase.MyDBHelper;
import com.example.programmerslibrary.Utils.ShieldActivity;
import com.example.programmerslibrary.ui.books.BookListFragment;
import com.example.programmerslibrary.ui.loan.LoanListFragment;
import com.example.programmerslibrary.ui.readers.ReaderListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    static MyDBHelper db;
    private static String userID;
    private final int YOUR_PERMISSION_STATIC_CODE_IDENTIFIER=15;

    public static MyDBHelper getDb() {
        return db;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        auth();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);
        db = new MyDBHelper(getApplicationContext());

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                YOUR_PERMISSION_STATIC_CODE_IDENTIFIER);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_users, R.id.navigation_books)
                .build();
        BottomNavigationView bottomNav = findViewById(R.id.nav_view);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

    }

    private void auth() {
        Intent intent = new Intent(this, ShieldActivity.class);
        startActivity(intent);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
/*
                    Bundle args = new Bundle();
                    args.putString("user", userID);*/

                    switch (item.getItemId()) {
                        case R.id.navigation_users:
                            selectedFragment = new ReaderListFragment();
                            /*selectedFragment.setArguments(args);*/
                            break;
                        case R.id.navigation_books:
                            selectedFragment = new BookListFragment();
                            /*selectedFragment.setArguments(args);*/
                            break;
                        case R.id.navigation_loans:
                            selectedFragment = new LoanListFragment();
                         /*   selectedFragment.setArguments(args);*/
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                            selectedFragment).commit();
                    return true;
                }
            };


    public static String getUserID(){
    return userID; }

}
