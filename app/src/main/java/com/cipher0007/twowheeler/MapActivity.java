package com.cipher0007.twowheeler;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cipher0007.twowheeler.Helpers.FetchURL;
import com.cipher0007.twowheeler.Helpers.TaskLoadedCallback;
import com.cipher0007.twowheeler.OtpVerification.SharedPrefManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback, NavigationView.OnNavigationItemSelectedListener {
    LatLng clementtown, Mylocation;
    private Polyline currentPolyline;
    private MarkerOptions place1, place2;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LinearLayout layoutBottomSheet;
    private BottomSheetBehavior sheetBehavior;
    // ArrayList<LatLng> MarkerPoints;
    private LinearLayout btn2h, btn4h, btn6h, btnfull;
    private TextView txtHeaderName, txtHeaderNo;
    private FrameLayout navicon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        TextView txtbook = findViewById(R.id.txtbookride);
        // TextView txtprice = findViewById(R.id.txtprice);
        Typeface bold = Typeface.createFromAsset(getAssets(),
                "Montserrat-Regular.otf");
        txtbook.setTypeface(bold);
//        txtprice.setTypeface(bold);
        navicon = findViewById(R.id.navIcon);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
        layoutBottomSheet = findViewById(R.id.bottom_sheet);

        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);


//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
        // toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        //TextView navUsername = (TextView) headerView.findViewById(R.id.navUsername);
        //navUsername.setText("Your Text Here");
        SharedPrefManager sharedPrefManager = new SharedPrefManager(MapActivity.this);
        txtHeaderName = headerView.findViewById(R.id.txtHeaderName);
        txtHeaderNo = headerView.findViewById(R.id.txtHeaderNo);
        txtHeaderName.setText(sharedPrefManager.getFirstName() + " " + sharedPrefManager.getLastName());
        txtHeaderNo.setText(sharedPrefManager.getPhoneNumber());

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // RelativeLayout bookNow = findViewById(R.id.btnBookNow);

//        bookNow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                    if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
////                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
////                        //btnBottomSheet.setText("Close sheet");
////                    } else {
////                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
////                       // btnBottomSheet.setText("Expand sheet");
////                    }
//
////                Intent intent = new Intent(MapActivity.this, ConfirmBookingFinal.class);
////                startActivity(intent);
//
//            }
//        });

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        // btnBottomSheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        // btnBottomSheet.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


        place1 = new MarkerOptions().position(new LatLng(30.2653, 78.0110)).title("Easy Scooter").icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("sc", 100, 100)));
        place2 = new MarkerOptions().position(new LatLng(30.355977, 78.085342)).title("Easy Scooter").icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("sc", 100, 100)));


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //   MarkerPoints = new ArrayList<>();
        initButton();
    }

    private void initButton() {

        btn2h = findViewById(R.id.btn2hRide);
        btn4h = findViewById(R.id.btn4hRide);
        btn6h = findViewById(R.id.btn6hRide);
        btnfull = findViewById(R.id.btnFullDayRide);

        btn2h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, ConfirmBookingFinal.class);
                intent.putExtra("price", "90");
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        btn4h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, ConfirmBookingFinal.class);
                intent.putExtra("price", "180");
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        btn6h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, ConfirmBookingFinal.class);
                intent.putExtra("price", "250");
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        btnfull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, ConfirmBookingFinal.class);
                intent.putExtra("price", "450");
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MapActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(MapActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        Mylocation = new LatLng(location.getLatitude(), location.getLongitude());

                        mMap.addMarker(new MarkerOptions().position(Mylocation).title("It's Me").icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("pin", 100, 100))));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(Mylocation));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f));
                        if (location != null) {

                        }
                    }
                });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                new FetchURL(MapActivity.this).execute(getUrl(marker.getPosition(), Mylocation, "driving"), "driving");
                return false;
            }
        });

        // Add a marker in Sydney and move the camera
//        clementtown = new LatLng(30.2653, 78.0110);
//        cmttown1 = new LatLng(30.271881, 77.998128);
//        LatLng itpark = new LatLng(30.355977, 78.085342);

//        Geocoder geocoder=new Geocoder(getApplicationContext());
////        List<Address>list=geocoder.getFromLocationName(clementtown,1);
//        mMap.addMarker(new MarkerOptions().position(clementtown).title("Two Wheeler").icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("helmet", 100, 100))));
//
//        mMap.addMarker(new MarkerOptions().position(cmttown1).title("Two Wheeler").icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("helmet", 100, 100))));
//        mMap.addMarker(new MarkerOptions().position(itpark).title("Two Wheeler").icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("helmet", 100, 100))));

        mMap.addMarker(place1);
        mMap.addMarker(place2);


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}