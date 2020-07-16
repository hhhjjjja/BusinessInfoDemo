package com.hustar.businessinfodemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;


public class DetailInfoActivity extends AppCompatActivity implements OnMapReadyCallback {

    DrawerLayout drawerLayout;
    TextView nameView, addrView, priceView, menuView;
    Button sidebarBtn;
    ImageView sectorImage;

    Geocoder geocoder;
    GoogleMap gMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);

        geocoder = new Geocoder(this);

        sidebarBtn = findViewById(R.id.sidebarBtn);
        sectorImage = findViewById(R.id.sectorImage);

        nameView = findViewById(R.id.NameView);
        addrView = findViewById(R.id.AddrView);
        priceView = findViewById(R.id.PriceView);
        menuView = findViewById(R.id.MenuView);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();

        String sector = intent.getStringExtra("sector");
        nameView.setText(intent.getStringExtra("name"));
        addrView.setText("대구광역시 " + intent.getStringExtra("ad") + " " + intent.getStringExtra("addr"));
        priceView.setText(intent.getStringExtra("price")+"원");
        menuView.setText(intent.getStringExtra("menu"));


        if(sector.equals("이미용업")) {
            sectorImage.setImageResource(R.drawable.scissor_icon);
        } else if(sector.equals("한식")) {
            sectorImage.setImageResource(R.drawable.rice_icon);
        } else if(sector.equals("분식")) {
            sectorImage.setImageResource(R.drawable.hotdog_icon);
        } else if(sector.equals("중식")) {
            sectorImage.setImageResource(R.drawable.dimsum_icon);
        } else if(sector.equals("일식")) {
            sectorImage.setImageResource(R.drawable.sushi_icon);
        } else if(sector.equals("양식")) {
            sectorImage.setImageResource(R.drawable.pizza_icon);
        } else if(sector.equals("세탁업")) {
            sectorImage.setImageResource(R.drawable.laundry_icon);
        } else if(sector.equals("목욕업")) {
            sectorImage.setImageResource(R.drawable.bath_icon);
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        sidebarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();
                int id= item.getItemId();

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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        List <Address> addrList = null;
        String addrStr = addrView.getText().toString();
        try {
            addrList = geocoder.getFromLocationName(addrStr, 10);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(addrList != null) {
            if(addrList.size() == 0) {
                Toast.makeText(getApplicationContext(), "주소 정보 없음", Toast.LENGTH_SHORT).show();
            } else {
                Address addr = addrList.get(0);
                double lat = addr.getLatitude();
                double lon = addr.getLongitude();

                LatLng mak = new LatLng(lat, lon);

                gMap.addMarker(new MarkerOptions().position(mak).title(nameView.getText().toString()));
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mak, 18));
            }
        }
    }
}