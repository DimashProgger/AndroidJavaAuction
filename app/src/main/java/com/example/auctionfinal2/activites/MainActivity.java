package com.example.auctionfinal2.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.auctionfinal2.Config.Config;
import com.example.auctionfinal2.R;
import com.example.auctionfinal2.fragments.CreateAuctionFragment;
import com.example.auctionfinal2.fragments.CreateFragment;
import com.example.auctionfinal2.fragments.HomeFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.paypal.android.sdk.payments.PayPalConfiguration;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static final int PAYPAL_REQUEST_CODE = 7171;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) // use bcs of test
            .clientId(Config.PAYPAL_CLIENT_ID);

    Button btnPayNow;


    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar, toolbar2;
    NavigationView navigationView;

    Fragment homeFragment;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        drawerLayout = findViewById(R.id.drawer);

        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView=findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.sidebar_name);
        navUsername.setText(auth.getCurrentUser().getDisplayName());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

//        homeFragment = new HomeFragment();
//        loadFragment(homeFragment);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {




                int id = menuItem.getItemId();
                Fragment fragment = null;
                switch (id)
                {
                    case R.id.search:
                        fragment = new HomeFragment();
                        loadFragment2(fragment);
                        break;
                    case R.id.basket:
                        fragment = new CreateFragment();
                        loadFragment2(fragment);
                        break;
                    case R.id.favorite:
                        fragment = new CreateAuctionFragment();
                        loadFragment2(fragment);
                        break;
//                    case R.id.promo_code:
//                        break;
//                    case R.id.orders:
//                        break;
//                    case R.id.setting:
//                        break;
//                    case R.id.support:
//                        break;
                    default:
                        return true;
                }
                return true;
            }
        });


        if (savedInstanceState == null) {
            navigationView.getMenu().performIdentifierAction(R.id.search, 0);
        }
    }

    private void loadFragment(Fragment homeFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_container, homeFragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void loadFragment2(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment).commit();
        drawerLayout.closeDrawer(GravityCompat.START);
        fragmentTransaction.addToBackStack(null);
    }





    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menu_logout) {

            auth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();

        } else if (id == R.id.menu_my_cart){

            startActivity(new Intent(MainActivity.this, CartActivity.class));

        }

        return true;
    }
}