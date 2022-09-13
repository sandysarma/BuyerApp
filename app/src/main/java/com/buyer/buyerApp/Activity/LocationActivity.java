package com.buyer.buyerApp.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.buyer.buyerApp.Class.GPSTracker;
import com.buyer.buyerApp.Class.NetworkHelper;
import com.buyer.buyerApp.Location.LocationProvider;
import com.buyer.buyerApp.Location.MapRequestResponse;
import com.buyer.buyerApp.Location.MapServices;
import com.buyer.buyerApp.Location.MapUtility;
import com.buyer.buyerApp.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationActivity extends AppCompatActivity implements LocationProvider.LocationCallback, OnMapReadyCallback {


    private LocationProvider locationProvider;
    private final String TAG = LocationActivity.class.getSimpleName();
    private String userAddress = "", city, state;
    private double mLatitude, mLongitude;
    private GoogleMap mMap;
    private EditText txtUserAddress;
    private ImageView imgCurrentloc, imgSearch;
    private TextView txtSelectLocation;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 2;
    private boolean mLocationPermissionGranted;
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 8088;
    private boolean isFirst = true;
    public static String country, place = "", addressLineDrops = "", strLatitude = "",
            strLongitude = "";
    RelativeLayout mapRelative;
    List<Address> addressList;
    GPSTracker gps;
    double longitude_new, latitude_new;

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private boolean currentMap = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        txtUserAddress = findViewById(R.id.txtUserAddress);
        imgCurrentloc = findViewById(R.id.imgCurrentloc);
        txtSelectLocation = findViewById(R.id.txtSelectLocation);
        imgSearch = findViewById(R.id.imgSearch);
        mapRelative = findViewById(R.id.mapRelative);
        getLocationPermission();
        checkAndRequestPermissions();
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationProvider = new LocationProvider(LocationActivity.this, LocationActivity.this);
        locationProvider.connect();

        place = getIntent().getStringExtra("place");

        gps = new GPSTracker(LocationActivity.this);
        if (gps.canGetLocation()) {
            latitude_new = gps.getLatitude();
            longitude_new = gps.getLongitude();

        } else {
            gps.showSettingsAlert();
        }

        strLatitude = getIntent().getStringExtra("Latitude");
        strLongitude = getIntent().getStringExtra("Longitude");

        txtUserAddress.setFocusable(false);
        imgSearch.setOnClickListener(view -> {

        });

        txtSelectLocation.setOnClickListener(view -> {
            if (place == null) {
                place = txtUserAddress.getText().toString().trim();
            } else {
                place = txtUserAddress.getText().toString().trim();
            }
            finish();
        });

        imgCurrentloc.setOnClickListener(view -> CurrentLocation());



    }


    private boolean checkAndRequestPermissions() {
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void CurrentLocation() {
        if (MapUtility.currentLocation != null) {
            LatLng lng = new LatLng(MapUtility.currentLocation.getLatitude(), MapUtility.currentLocation.getLongitude());
            mLatitude = MapUtility.currentLocation.getLatitude();
            mLongitude = MapUtility.currentLocation.getLongitude();
            addMarker(lng);
            new getAddressForLocation(mLatitude, mLongitude).execute();
        }
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            if (NetworkHelper.isNetworkAvailable(LocationActivity.this)) {
                if (currentMap) {
                    currentMap = false;
                    Toast.makeText(LocationActivity.this, "Internet Connected", Toast.LENGTH_LONG).show();
                }

                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();

                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            } else {
                if (currentMap == false) {
                    currentMap = true;
                    Toast.makeText(LocationActivity.this, "No Internet Connection !", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    private void addMarker(LatLng coordinate) {
        mLatitude = coordinate.latitude;
        mLongitude = coordinate.longitude;
        if (mMap != null) {
            LatLng latLng = new LatLng(mLatitude, mLongitude);
            mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getMyLocation();

        mMap.clear();
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (mMap.isIndoorEnabled()) {
            mMap.setIndoorEnabled(false);
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                mLatitude = latLng.latitude;
                mLongitude = latLng.longitude;
                addMarker(latLng);
                new getAddressForLocation(mLatitude, mLongitude).execute();
            }
        });

        if (checkLocationPermission()) {
            checkAndRequestPermissions();
        } else {
            return;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void handleNewLocation(Location location) {
        if (location != null) {

            MapUtility.currentLocation = location;
            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();
            if (isFirst) {
                isFirst = false;
                if (place == null) {
                    new getAddressForLocation(MapUtility.currentLocation.getLatitude(), MapUtility.currentLocation.getLongitude()).execute();
                } else {
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        addressList = geocoder.getFromLocationName(place, 5);
                        Address locations = addressList.get(0);
                        addressLineDrops = String.valueOf(locations.getAddressLine(0));
                        txtUserAddress.setText("" + place);
                        LatLng latLng = new LatLng(locations.getLatitude(), locations.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng).title(place));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void loc(Double latitude, Double longitude, String addressLineDrop) {
        MarkerOptions markerOptions;
        try {
            mMap.clear();
            txtUserAddress.setText("" + addressLineDrop);

            markerOptions = new MarkerOptions().position(new LatLng(latitude, longitude)).title(addressLineDrop).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_on));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 17.0f);
            mMap.moveCamera(cameraUpdate);
            mMap.addMarker(markerOptions).showInfoWindow();
        } catch (Exception ex) {
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class getAddressForLocation extends AsyncTask<Void, Void, Void> {
        Double latitude, longitude;

        public getAddressForLocation(Double latitude, Double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(LocationActivity.this, Locale.getDefault());
                StringBuilder sb = new StringBuilder();

                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses != null && addresses.size() > 0) {

                    String address = addresses.get(0).getAddressLine(0);
                    if (address != null)
                        sb.append(address).append(" ");
                    city = addresses.get(0).getLocality();
                    if (city != null)
                        sb.append(city).append(" ");

                    state = addresses.get(0).getAdminArea();
                    if (state != null)
                        sb.append(state).append(" ");
                    country = addresses.get(0).getCountryName();
                    if (country != null)
                        sb.append(country).append(" ");

                    String postalCode = addresses.get(0).getPostalCode();
                    if (postalCode != null)
                        sb.append(postalCode).append(" ");
                    userAddress = sb.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
                showLocation((new LatLng(latitude, longitude)));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (userAddress == null || userAddress.equalsIgnoreCase("")) {
                showLocation((new LatLng(latitude, longitude)));
            } else {
                MarkerOptions markerOptions;
                try {
                    mMap.clear();
                    txtUserAddress.setText("" + userAddress);

                    LatLng latLng = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(latLng).title(userAddress));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
                } catch (Exception ex) {
                }
            }
        }
    }

    private void showLocation(final LatLng latLng1) {
        if (!MapUtility.isNetworkAvailable(this)) {

            this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(LocationActivity.this, "No internet connection available.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MapUtility.showProgress(LocationActivity.this);
                }
            });
            MapServices.MapInterface mapInterface = MapServices.getClient();
            Call<MapRequestResponse> call;
            String s = latLng1.latitude + "," + latLng1.longitude;

            call = mapInterface.MapData(s);
            call.enqueue(new Callback<MapRequestResponse>() {
                @Override
                public void onResponse(Call<MapRequestResponse> call, Response<MapRequestResponse> response) {
                    MapUtility.hideProgress();
                    if (response.isSuccessful()) {
                        MapRequestResponse result = response.body();
                        if (result.getStatus().equalsIgnoreCase("OK")) {
                            if (result.getResults().size() > 0) {
                                final MarkerOptions[] markerOptions = {null};
                                try {
                                    mMap.clear();

                                    userAddress = result.getResults().get(0).getFormatted_address();
                                    txtUserAddress.setText("" + userAddress);

                                    if (userAddress != null) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                markerOptions[0] = new MarkerOptions().position(latLng1).title((userAddress)).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_on));
                                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng1, 17.0f);
                                                mMap.moveCamera(cameraUpdate);
                                                mMap.addMarker(markerOptions[0]).showInfoWindow();
                                            }
                                        });
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace(); // getFromLocation() may sometimes fail
                                }
                            }
                        }
                    } else {
                        MapUtility.hideProgress();
                    }
                }

                @Override
                public void onFailure(Call<MapRequestResponse> call, Throwable t) {
                    t.printStackTrace();
                    MapUtility.hideProgress();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    locationProvider = new LocationProvider(LocationActivity.this, LocationActivity.this);
                    locationProvider.connect();
                }
            }
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @SuppressLint("MissingPermission")
    private void getMyLocation() {
        // Getting LocationManager object from System Service LOCATION_SERVICE
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED) {
                mMap.setMyLocationEnabled(true);
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, mLocationListener);
                mMap.setOnMyLocationChangeListener(myLocationChangeListener);

            } else {
                requestPermissions(new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                }, 100);
            }
        }
    }

}