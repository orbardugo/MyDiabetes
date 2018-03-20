package com.example.amir.mydiabetes;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubmitFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener {

    TextView headtxt;
    Button smsBtn;
    View view;
    String name;
    String phoneNum;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mCurrentLocation;
    Snackbar smsSnackbar;

    public SubmitFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_submit, container, false);
        TextView result;
        result = view.findViewById(R.id.sugarLvlTxt);
        headtxt = view.findViewById(R.id.txtHead);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(view.getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        name = prefs.getString("txt_name", "");
        phoneNum = prefs.getString("edit_text_emergency", "");
        smsBtn = view.findViewById(R.id.smsBtn);
        smsBtn.setOnClickListener(this);
        String smsSend  = "emergency sms was sent";
        smsSnackbar = Snackbar.make(view, smsSend,  Snackbar.LENGTH_SHORT);

        int gluc = getArguments().getInt("glucose");
        String res = null;
        // low sugar
        if (gluc <= 70) {
            headtxt.setTextColor(Color.parseColor("#FF0000"));
            headtxt.setText("LOW SUGAR LEVEL");
            res = "Your Blood Sugar Level " + gluc + " is low!\n You should eat or drink something to raise it. \n" +
                    "We'll recommend one of the following:\n" +
                    "* 3 – 4 glucose tablets or gels\n" +
                    "* 4 ounces of fruit juice\n" +
                    "* 6 ounces of regular soda\n" +
                    "* 1 tablespoon of honey\n" +
                    "* 1 tablespoon of table sugar"+
                    "\n" +
                    "\n" +
                    "Click down to send emergency sms with your location:";
            smsBtn.setVisibility(View.VISIBLE);
        }
        else if (gluc >= 250) { //high sugar
            headtxt.setTextColor(Color.parseColor("#FF0000"));
            headtxt.setText("HIGH SUGAR LEVEL");
            res = "Your Blood Sugar Level " + gluc + " is high!\n" +
                    "We'll recommend to take insulin injection and  Drink lots of water\n" +
                    "If your glucose level does not go down, contact your doctor and consider a hospital evacuation." +
                    "\n" +
                    "\n" +
                    "Click down to send emergency sms with your location:";
            smsBtn.setVisibility(View.VISIBLE);

        }
        else {  // fine sugar
            headtxt.setTextColor(Color.parseColor("#7CFC00"));
            headtxt.setText("SUGAR LEVEL IS GOOD");
            res = "Your Blood Sugar Level " + gluc + " is good!";
        }
        result.setText(res);
        return view;
    }

    @Override
    public void onClick(View v) {
        SmsManager smsManager;
        smsManager = SmsManager.getDefault();
        Double lat, lng;
        createLocationRequest();
        if (ActivityCompat.checkSelfPermission(view.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(view.getContext()    , android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lat = mCurrentLocation.getLatitude();
        lng = mCurrentLocation.getLongitude();
        smsManager.sendTextMessage(phoneNum, null, name+"'s blood sugar is very high! glucose:" + getArguments().getInt("glucose") + " \nhis location is at: " + "http://maps.google.com/?q="+lat+","+lng , null, null);
        smsSnackbar.show();
    }

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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(view.getContext(),  android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } else {
            Toast.makeText(view.getContext(), "No permissions", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(6000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
}
