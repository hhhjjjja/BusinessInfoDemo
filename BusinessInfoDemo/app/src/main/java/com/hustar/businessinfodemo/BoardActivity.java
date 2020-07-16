package com.hustar.businessinfodemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class BoardActivity extends AppCompatActivity {

    Button sidebarBtn;

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        sidebarBtn = findViewById(R.id.sidebarBtn);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();

                int id= item.getItemId();
                String title = item.getTitle().toString();

                if(id == R.id.home) {
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if(id == R.id.search) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if(id == R.id.board) {
                    Intent intent = new Intent(getApplicationContext(), BoardActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                return true;
            }
        });

        sidebarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }
}