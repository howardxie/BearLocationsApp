package com.example.cs160_sp18.prog3;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnItemTouchListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LocationListener, BearViewHolder.OnBearListener {
    public static Location currLoc;
    private FusedLocationProviderClient fusedLocationClient;
    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Bear> mBears = new ArrayList<>();
    private String username;

    // UI elements
//    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.bear_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        locOnSucc();
        refreshLocation();

        swipe_refresh = findViewById(R.id.swipe_refresh);
        setOnRefreshForSwipe();

        getSupportActionBar().setTitle("Berkeley Bears"); // for set actionbar title
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
        }
        makeBearList();
        setAdapterAndUpdateData();
    }

    private void makeBearList() {
        Bear b1 = new Bear(R.drawable.mlk_bear, "Class of 1927 Bear", 37.869288, -122.260125);
        Bear b2 = new Bear(R.drawable.outside_stadium, "Stadium Entrance Bear", 37.871305, -122.252516);
        Bear b3 = new Bear(R.drawable.macchi_bears, "Macchi Bears", 37.874118, -122.258778);
        Bear b4 = new Bear(R.drawable.les_bears, "Les Bears", 37.871707, -122.253602);
        Bear b5 = new Bear(R.drawable.strawberry_creek, "Strawberry Creek Topiary Bear", 37.869861, -122.261148);
        Bear b6 = new Bear(R.drawable.south_hall, "South Hall Little Bear", 37.871382, -122.258355);
        Bear b7 = new Bear(R.drawable.bell_bears, "Great Bear Bell Bears", 37.872061599999995, -122.2578123);
        Bear b8 = new Bear(R.drawable.bench_bears, "Campanile Esplanade Bears", 37.87233810000001, -122.25792999999999);
        mBears.add(b1);
        mBears.add(b2);
        mBears.add(b3);
        mBears.add(b4);
        mBears.add(b5);
        mBears.add(b6);
        mBears.add(b7);
        mBears.add(b8);
    }

    private void setOnRefreshForSwipe() {
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLocation();
                setAdapterAndUpdateData();
                swipe_refresh.setRefreshing(false);
            }
        });
    }

    private void setAdapterAndUpdateData() {
        // create a new adapter with the updated mComments array
        // this will "refresh" our recycler view
        mAdapter = new BearAdapter(this, mBears, this);
        mRecyclerView.setAdapter(mAdapter);

    }

    @SuppressLint("MissingPermission")
    private void refreshLocation() {
        LocationManager lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        lm.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
        Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        currLoc = loc;
    }


    @Override
    public void onBearClick(int position) {
        Intent intent = new Intent(this, CommentFeedActivity.class);
        Bear b = mBears.get(position);
        float d = Float.valueOf(b.getDistance());
        if (d <= 10.0) {
            intent.putExtra("bear", b);
            intent.putExtra("username", this.username);
            startActivity(intent);
        } else {
            CharSequence text = "Not within 10m!";
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(this, text, duration).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
