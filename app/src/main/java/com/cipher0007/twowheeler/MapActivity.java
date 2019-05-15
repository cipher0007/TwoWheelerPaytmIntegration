package com.cipher0007.twowheeler;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cipher0007.twowheeler.Helpers.FetchURL;
import com.cipher0007.twowheeler.Helpers.TaskLoadedCallback;
import com.cipher0007.twowheeler.Network.Adapter.BikeNoAdapter;
import com.cipher0007.twowheeler.Network.Adapter.RateAdapter;
import com.cipher0007.twowheeler.Network.ApiClient;
import com.cipher0007.twowheeler.Network.ApiServices;
import com.cipher0007.twowheeler.Network.Models.Rate;
import com.cipher0007.twowheeler.OtpVerification.SharedPrefManager;
import com.codemybrainsout.ratingdialog.RatingDialog;
import com.facebook.shimmer.ShimmerFrameLayout;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private RecyclerView rateRecyclerView;
    private ShimmerFrameLayout mShimmerViewContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        TextView txtbook = findViewById(R.id.txtbookride);


//        final RatingDialog ratingDialog = new RatingDialog.Builder(this)
//
//                .onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
//                    @Override
//                    public void onFormSubmitted(String feedback) {
//
//                    }
//                }).build();
//
//        ratingDialog.show();
//        Intent i=new Intent(getApplicationContext(),BookingCurrentTrip.class);
//        startActivity(i);
//        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#20111111"));
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow();
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                w.setStatusBarColor(Color.parseColor("#20111111"));
//            }
//        }
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.startShimmer();
        mShimmerViewContainer.setVisibility(View.VISIBLE);


//        test();


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
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        rateRecyclerView = findViewById(R.id.rateRecyclerView);
        rateRecyclerView.setHasFixedSize(true);
        // LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        rateRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        //rateRecyclerView.setLayoutManager(manager);
        NetworkCall();

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
        //initButton();


        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setCheckable(false);
        }
    }

//    private void test() {
//        String response="status=success:orderId=a089f02724ed4a8db6c069f6d30b3245:txnId=None:paymentId=MOJO7918005A76494611:token=qyFwLidQ0aBNNWlsmwHx1gHFhlt6A1";
//
//        String rearray[] = response.split(":");
//        String orderid = rearray[1].substring(rearray[1].indexOf("=") + 1);
//        //Toast.makeText(getApplicationContext(), orderid, Toast.LENGTH_LONG).show();
//
//
//        String paymentId =rearray[3].substring(rearray[3].indexOf("=")+1);
//        String token =rearray[4].substring(rearray[4].indexOf("=")+1);
//
//
//        Log.d("BKC","Orderid "+orderid+"\npaymentid "+paymentId+"\ntoken "+token);
//        Log.d("BKC",response);
//
//        Toast.makeText(getApplicationContext(), "Orderid "+orderid+"\npaymentid "+paymentId+"\ntoken "+token, Toast.LENGTH_SHORT).show();
//
//    }

    private void NetworkCall() {

        ApiServices apiService = ApiClient.getClient(getApplicationContext()).create(ApiServices.class);

        Call<List<Rate>> call = apiService.BikeRates(new SharedPrefManager(getApplicationContext()).getPhoneNumber());
        call.enqueue(new Callback<List<Rate>>() {
            @Override
            public void onResponse(Call<List<Rate>> call, Response<List<Rate>> response) {
                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);
                List<Rate> rates = response.body();
                Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                rateRecyclerView.setAdapter(new RateAdapter(getApplicationContext(), rates));
            }

            @Override
            public void onFailure(Call<List<Rate>> call, Throwable t) {
                Toast.makeText(MapActivity.this, "No timing are available!", Toast.LENGTH_SHORT).show();
                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        place1 = new MarkerOptions().position(new LatLng(30.2653, 78.0110)).title("Easy Scooter").icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("sc", 100, 100)));
        place2 = new MarkerOptions().position(new LatLng(30.355977, 78.085342)).title("Easy Scooter").icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("sc", 100, 100)));


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

//    private void initButton() {
//        SharedPrefManager save = new SharedPrefManager(getApplicationContext());
//        btn2h = findViewById(R.id.btn2hRide);
//        btn4h = findViewById(R.id.btn4hRide);
//        btn6h = findViewById(R.id.btn6hRide);
//        btnfull = findViewById(R.id.btnFullDayRide);
//
//        btn2h.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                save.Ridetime("2");
//                Intent intent = new Intent(MapActivity.this, BikeNo.class);
//                //   intent.putExtra("price", "90");
//                startActivity(intent);
//                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//
////                Intent intent = new Intent(MapActivity.this, ConfirmBookingFinal.class);
////                intent.putExtra("price", "180");
////                startActivity(intent);
////                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//            }
//
//        });
//
//        btn4h.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                save.Ridetime("4");
//                Intent intent = new Intent(MapActivity.this, ConfirmBookingFinal.class);
//                intent.putExtra("price", "180");
//                startActivity(intent);
//                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//            }
//        });
//
//        btn6h.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                save.Ridetime("6");
//                Intent intent = new Intent(MapActivity.this, ConfirmBookingFinal.class);
//                intent.putExtra("price", "250");
//                startActivity(intent);
//                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//            }
//        });
//        btnfull.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                save.Ridetime("0");
//                Intent intent = new Intent(MapActivity.this, ConfirmBookingFinal.class);
//                intent.putExtra("price", "450");
//                startActivity(intent);
//                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//            }
//        });
//    }

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

        if (id == R.id.nav_your_trips) {
            Intent intent = new Intent(getApplicationContext(), YourBooking.class);

            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else if (id == R.id.nav_share) {

            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            String shareBodyText = "Install Easy Scooter From play store and GET FIRST FREE RIDE, Book you vehicle and ride on mountains!" +
                    "http://easyscooter.co.in";
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject/Title");
            intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(intent, "Choose sharing method"));
        }
//        } else if (id == R.id.nav_rate_us) {
//            final RatingDialog ratingDialog = new RatingDialog.Builder(getApplicationContext())
//
//                .onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
//                    @Override
//                    public void onFormSubmitted(String feedback) {
//
//                    }
//                }).build();
//
//        ratingDialog.show();
//        }
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