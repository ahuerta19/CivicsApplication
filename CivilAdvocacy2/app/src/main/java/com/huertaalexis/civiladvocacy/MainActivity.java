package com.huertaalexis.civiladvocacy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    private final static String repURL = "https://www.googleapis.com/civicinfo/v2/representatives";

    //Following is IIT's address. Done for testing before implementing the locator service.
    //public static String defLoc = 10 W 35th St, Chicago, IL 60616;
    
    public static String location;
    private static final String yourAPIKey = "AIzaSyAIK0n_86mc1lP5XvA4SsENUp4zGTPVIdw";

    private final ArrayList<Official> officialList  = new ArrayList<>();
    private RecyclerView recyclerView;
    private OfficialAdapter mAdapter;

    private final String TAG = getClass().getSimpleName();
    private RequestQueue queue;
    private long start;

    private TextView locationV;

    private FusedLocationProviderClient mFusedLocationClient;
    private static final int LOCATION_REQUEST = 111;

    private static String locationString = "Unspecified Location";
    TextView data;
    TextView data2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        mAdapter = new OfficialAdapter(officialList, this);

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        queue = Volley.newRequestQueue(this);

        locationV = findViewById(R.id.locationView);
        data = findViewById(R.id.noInternet);
        data2 = findViewById(R.id.noInternet2);
        mFusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);
        if(hasNetworkConnection()) {
            data.setVisibility(TextView.INVISIBLE);
            data2.setVisibility(TextView.INVISIBLE);
            determineLocation();
        }else{
            locationV.setText("No Data For Location");
            data.setVisibility(TextView.VISIBLE);
            data2.setVisibility(TextView.VISIBLE);
        }
    }

    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.home_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(hasNetworkConnection()){
            if (item.getItemId() == R.id.aboutButton){
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                intent.putExtra("OPENING CLASS", MainActivity.class.getSimpleName());
                startActivity(intent);
                return true;
            }else if (item.getItemId() == R.id.locationButton){
                LayoutInflater inflater = LayoutInflater.from(this);
                @SuppressLint("InflateParams") final View view2 = inflater.inflate(R.layout.loc_layout, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Enter address");

                builder.setView(view2);

                builder.setPositiveButton("Ok", (dialog, id) -> {
                    EditText input  = view2.findViewById(R.id.locationInput);
                    location = input.getText().toString();
                    officialList.clear();
                    doDownload();
                });
                builder.setNegativeButton("Cancel", (dialog, id) -> {

                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }else{
                return super.onOptionsItemSelected(item);
            }
        }else{
            Toast.makeText(this, "No Internet Connection! Can not use this function", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void determineLocation() {
        // Check perm - if not then start the  request and return
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    // Got last known location. In some situations this can be null.
                    if (location != null) {
                        locationString = getPlace(location);
                        locationV.setText(locationString);
                    }
                })
                .addOnFailureListener(this, e ->
                        Toast.makeText(MainActivity.this,
                                e.getMessage(), Toast.LENGTH_LONG).show());
    }
    public static String getLocation(){
        return location;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    determineLocation();
                } else {
                    locationV.setText("Denied");
                }
            }
        }
    }
    private String getPlace(Location loc) {

        StringBuilder sb = new StringBuilder();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            location = addresses.get(0).getAddressLine(0);
            doDownload();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Official c = officialList.get(pos);
        Intent intent = new Intent(MainActivity.this, OfficialActivity.class);
        intent.putExtra(Official.class.getName(), c);
        startActivity(intent);
    }


    @Override
    public boolean onLongClick(View v) {
        Toast.makeText(this, "Nothing Interesting Happens\nHere on Long Click", Toast.LENGTH_SHORT).show();
        return true;
    }

    public void updateData(ArrayList<Official> cList) {
        mAdapter.notifyItemRangeChanged(0, cList.size());
    }
    private int getIcon(String iV){
        int iconID =
                this.getResources().getIdentifier(iV, "drawable", this.getPackageName());
        if (iconID == 0) {
            Log.d(TAG, "parseCurrentRecord: CANNOT FIND ICON " + iV);
        }
        return iconID;
    }


    private void doDownload() {
        if (hasNetworkConnection()) {
            //Build URL
            data.setVisibility(TextView.INVISIBLE);
            data2.setVisibility(TextView.INVISIBLE);
            Uri.Builder buildURL = Uri.parse(repURL).buildUpon();
            buildURL.appendQueryParameter("key", yourAPIKey);
            buildURL.appendQueryParameter("address", location);
            String urlUsed = buildURL.build().toString();

            //Used to debug URL
            //Log.d(TAG, "doDownload: " + urlUsed);

            start = System.currentTimeMillis();


            Response.Listener<JSONObject> listener =
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                setTitle("Civil Advocacy");
                                officialList.clear();
                                JSONObject normIn = response.getJSONObject("normalizedInput");

                                String line = normIn.getString("line1");
                                String city = normIn.getString("city");
                                String state = normIn.getString("state");
                                String zip = normIn.getString("zip");

                                locationV.setText(line + " " + city + " " + state + " " + zip);

                                JSONArray offices = response.getJSONArray("offices");
                                JSONArray officials = response.getJSONArray("officials");

                                for (int i = 0; i < offices.length(); i++) {
                                    JSONObject off2 = offices.getJSONObject(i);
                                    String officeTitle = off2.getString("name");

                                    //We want to retrieve the index into the official JSONArray, since there can be
                                    //More than one, we need JSONArray
                                    JSONArray offIndex = off2.getJSONArray("officialIndices");
                                    int lengthOff = offIndex.length();
                                    for (int j = 0; j < lengthOff; j++) {
                                        int offPos = Integer.parseInt(offIndex.getString(j));
                                        JSONObject officials2 = officials.getJSONObject(offPos);

                                        String name = officials2.getString("name");
                                        String party = officials2.getString("party");
                                        Log.d(TAG, "doDownload: " + "issue?");

                                        String FB = null;
                                        String TW = null;
                                        String YT = null;
                                        if (officials2.has("channels")) {
                                            JSONArray channels = officials2.getJSONArray("channels");

                                            for (int k = 0; k < channels.length(); k++) {
                                                JSONObject chanIndex = channels.getJSONObject(k);
                                                Log.d(TAG, "doDownload: " + "third for loop");
                                                if (chanIndex.getString("type").equals("Facebook")) {
                                                    FB = chanIndex.getString("id");
                                                }
                                                if (chanIndex.getString("type").equals("Twitter")) {
                                                    TW = chanIndex.getString("id");
                                                }
                                                if (chanIndex.getString("type").equals("YouTube")) {
                                                    YT = chanIndex.getString("id");
                                                }
                                            }
                                        }

                                        String address = "";
                                        if (officials2.has("address")) {
                                            JSONArray add = officials2.getJSONArray("address");
                                            JSONObject add2 = add.getJSONObject(0);

                                            String lineOne = null;
                                            String lineTwo = null;
                                            String lineThree = null;
                                            String city1 = null;
                                            String state1 = null;
                                            String zip1 = null;
                                            if (add2.has("line1")) {
                                                lineOne = add2.getString("line1");
                                            } else {
                                                lineOne = "";
                                            }
                                            if (add2.has("line2")) {
                                                lineTwo = add2.getString("line2");
                                            } else {
                                                lineTwo = "";
                                            }
                                            if (add2.has("line3")) {
                                                lineThree = add2.getString("line3");
                                            } else {
                                                lineThree = "";
                                            }
                                            if (add2.has("city")) {
                                                city1 = add2.getString("city");
                                            } else {
                                                city1 = "";
                                            }
                                            if (add2.has("state")) {
                                                state1 = add2.getString("state");
                                            } else {
                                                state1 = "";
                                            }
                                            if (add2.has("zip")) {
                                                zip1 = add2.getString("zip");
                                            } else {
                                                zip1 = "";
                                            }
                                            address = lineOne + " " + lineTwo + " " + lineThree + " " + city1 + " " + state1 + " " + zip1;
                                        }
                                        String phone = null;
                                        if (officials2.has("phones")) {
                                            JSONArray phoneArr = officials2.getJSONArray("phones");
                                            phone = phoneArr.getString(0);
                                        }
                                        String email = null;
                                        if (officials2.has("emails")) {
                                            email = officials2.getJSONArray("emails").getString(0);
                                        }
                                        String url = null;
                                        if (officials2.has("urls")) {
                                            JSONArray urlArr = officials2.getJSONArray("urls");
                                            url = urlArr.getString(0);
                                        }
                                        String photo = null;
                                        String updatedPhoto = null;
                                        if (officials2.has("photoUrl")) {
                                            photo = officials2.getString("photoUrl");
                                            updatedPhoto = photo.replace("http://", "https://");
                                            Log.d(TAG, "url: " + updatedPhoto);
                                        } else {
                                            updatedPhoto = null;
                                        }

                                        Log.d(TAG, "test: " + email);
                                        officialList.add(new Official(name, officeTitle, party, FB, TW, YT, address, phone, email, url, updatedPhoto));
                                    }
                                    updateData(officialList);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    };
            Response.ErrorListener error = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                        setTitle("Duration: " + (System.currentTimeMillis() - start));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            JsonObjectRequest jsonObjectRequest =
                    new JsonObjectRequest(Request.Method.GET, urlUsed,
                            null, listener, error);
            // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest);
        }else{
            Toast.makeText(this, "No Internet Connection! Can not use this function", Toast.LENGTH_SHORT).show();
        }
    }
}