package com.example.pritika.stormy.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pritika.stormy.R;
import com.example.pritika.stormy.weather.Current;
import com.example.pritika.stormy.weather.Day;
import com.example.pritika.stormy.weather.Forecast;
import com.example.pritika.stormy.weather.Hour;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String DAILY_FORECAST = "DAILY_FORECAST";
    public static final String HOURLY_FORECAST = "HOURLY_FORECAST";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private Location mLastLocation;
    private LocationListener mLocationListener;
    private LocationRequest mLocationRequest;
    TextView mLatitudeText, mLongitudeText;
    protected TextView mLastUpdateTimeTextView;
    protected Button mStartUpdatesButton;
    protected Button mStopUpdatesButton;
    GoogleApiClient mGoogleApiClient;
    protected String mLatitudeLabel;
    protected String mLongitudeLabel;
    protected String mLastUpdateTimeLabel;

    int REQUEST_CHECK_SETTINGS;
    protected String mLastUpdateTime;

    protected Location mCurrentLocation;

    protected Boolean mRequestingLocationUpdates;
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";

    private Forecast mForecast;
    @Bind(R.id.timeLabel)
    TextView mTimeLabel;
    @Bind(R.id.temperatureLabel)
    TextView mTemperatureLabel;
    @Bind(R.id.precipValue)
    TextView mPrecipValue;
    @Bind(R.id.summaryLabel)
    TextView mSummaryLabel;
    @Bind(R.id.IconimageView)
    ImageView mIconImageView;
    @Bind(R.id.humidityValue)
    TextView mHumidityValue;
    @Bind(R.id.refreshImageview)
    ImageView mrefreshImageview;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
       // mLatitudeText = (TextView) findViewById(R.id.latitudeText);
       // mLongitudeText = (TextView) findViewById(R.id.longitude_text);
       // mLastUpdateTimeTextView = (TextView) findViewById(R.id.last_update_time_text);
       // mStartUpdatesButton = (Button) findViewById(R.id.start_updates_button);
        //mStopUpdatesButton = (Button) findViewById(R.id.stop_updates_button);
        //SET LABELS
       // mLatitudeLabel = getResources().getString(R.string.latitude_label);
        //mLongitudeLabel = getResources().getString(R.string.longitude_label);
        //mLastUpdateTimeLabel = getResources().getString(R.string.last_update_time_label);

       // mRequestingLocationUpdates = false;
        //mLastUpdateTime = "";
        updateValuesFromBundle(savedInstanceState);


        if (mGoogleApiClient == null) {
//            mGoogleApiClient = new GoogleApiClient.Builder(this)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .addApi(LocationServices.API)
//                    .build();

        }
        ButterKnife.bind(this);
        mProgressBar.setVisibility(View.INVISIBLE);
        final double latitude = 37.826;
        final double longitude = -122.423;
        mrefreshImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(latitude, longitude);
            }
        });

        getForecast(latitude, longitude);
        Log.d(TAG, "Main UI code is running");
    }


    protected void onStart() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void getForecast(double latitude, double longitude) {
        String apiKey = "5cf94d4a971b1003911ae4af5f070984";

        String forecastUrl = "https://api.forecast.io/forecast/" + apiKey + "/" + latitude + "," + longitude;
        if (isNetworkAvailable()) {
            toggleRefresh();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastUrl)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    try {
                        String jsonData = response.body().string();
                        //execute synchronous method many codes running on same thread
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mForecast = parseForecastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });

                            //log whole response body
                            // synchronous get error network on main thread exception fix this with concurrency change code to asynchronous ui code for main thread
                            //thread is path of execution for a code, code running at the background thread move back to main thread then execute ui code(highest priority)

                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught:", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught:", e);
                    }


                }
            });
        } else {
            Toast.makeText(this, getString(R.string.network_unavailable_message),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void toggleRefresh() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mrefreshImageview.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mrefreshImageview.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {
        Current current = mForecast.getCurrent();
        mTemperatureLabel.setText(current.getTemperature() + ""); // added to make mCurrent a string
        mTimeLabel.setText("At "   +  current.getFormattedTime()  +  "it will be");
        mHumidityValue.setText(current.getHumidity() + "");
        mPrecipValue.setText(current.getPrecipChance() + "%");
        mSummaryLabel.setText(current.getSummary());
        Drawable drawable = getResources().getDrawable(current.getIconId());
        mIconImageView.setImageDrawable(drawable);
    }

    private Forecast parseForecastDetails(String jsonData) throws JSONException {
        Forecast forecast = new Forecast();
        forecast.setCurrent(getCurrentDetails(jsonData));
        Log.d("JSON Data", jsonData.toString());
        forecast.setHourlyForecast(getHourlyForecast(jsonData));
        forecast.setDailyForecast(getDailyForecast(jsonData));
        return forecast;
    }

    private Day[] getDailyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");
        Day[] days = new Day[data.length()];
        for (int i = 0; i < data.length(); i++) {
            JSONObject jsonDay = data.getJSONObject(i);
            Day day = new Day();
            day.setSummary(jsonDay.getString("summary"));
            day.setIcon(jsonDay.getString("icon"));
            day.setTemperatureMax(jsonDay.getDouble("temperatureMax"));
            day.setTime(jsonDay.getLong("time"));
            day.setTimezone(timezone);
            days[i] = day;


        }
        return days;
    }

    private Hour[] getHourlyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");
        Hour[] hours = new Hour[data.length()];
        for (int i = 0; i < data.length(); i++) {
            JSONObject jsonHour = data.getJSONObject(i);
            Hour hour = new Hour();
            hour.setSummary(jsonHour.getString("summary"));
            hour.setTemperature(jsonHour.getDouble("temperature"));
            hour.setIcon(jsonHour.getString("icon"));
            hour.setTime(jsonHour.getLong("time"));
            hour.setTimezone(timezone);
            hours[i] = hour;

        }
        return hours;
    }

    private Current getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData); //exception
        String timezone = forecast.getString("timezone");
        Log.i(TAG, "FROM JSON: " + timezone);
        JSONObject currently = forecast.getJSONObject("currently");
        Current current = new Current();
        current.setHumidity(currently.getDouble("humidity"));
        current.setTime(currently.getLong("time"));
        current.setIcon(currently.getString("icon"));
        current.setPrecipChance(currently.getDouble("precipProbability"));
        current.setSummary(currently.getString("summary"));
        current.setTemperature(currently.getDouble("temperature"));
        current.setTimeZone(timezone); //
        Log.d(TAG, current.getFormattedTime());
        return current;
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE); //CAST it to a connectivity manager
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();// need permission
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    //dialog tells user to do something
    private void alertUserAboutError() {

        AlertDialogFragment dialog = new AlertDialogFragment();
        //show some activity
        dialog.show(getFragmentManager(), "Error_dialog");
    }

    @OnClick(R.id.dailyButton)
    public void startDailyActivity(View view) {
        Intent intent = new Intent(this, DailyForecastActivity.class);
        intent.putExtra(DAILY_FORECAST, mForecast.getDailyForecast());
        startActivity(intent);
    }

    @OnClick(R.id.hourlyButton)
    public void startHourlyActivity(View view) {
        Intent intent = new Intent(this, HourlyForecastActivity.class);
        intent.putExtra(HOURLY_FORECAST, mForecast.getHourlyForecast());
        startActivity(intent);


    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Google client connected");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        Log.d(TAG , mLastLocation.getLatitude()+"");
        Log.d(TAG , mLastLocation.getLongitude()+"");

//
//        if (mLastLocation != null) {
//            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
//            Log.i(TAG, mLastLocation.getLatitude() + "");
//            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
//
//
//        }else {
//            Log.i(TAG, "Location is null" );
//        }
//        if (mRequestingLocationUpdates) {
//           // startLocationUpdates();
//
//        }

    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, connectionResult.getErrorMessage());
    }


    @Override
    protected void onPause() {
        super.onPause();
        //stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
            //startLocationUpdates();
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY,
                mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    private void updateUI() {
        mLatitudeText.setText(String.valueOf(mCurrentLocation.getLatitude()));
        mLongitudeText.setText(String.valueOf(mCurrentLocation.getLongitude()));
        mLastUpdateTimeTextView.setText(mLastUpdateTime);
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and
            // make sure that the Start Updates and Stop Updates buttons are
            // correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);
               // setButtonsEnabledState();
                // Update the value of mCurrentLocation from the Bundle and update the
                // UI to show the correct latitude and longitude.
                if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                    // Since LOCATION_KEY was found in the Bundle, we can be sure that
                    // mCurrentLocationis not null.
                    mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
                }
                // Update the value of mLastUpdateTime from the Bundle and update the UI.
                if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                    mLastUpdateTime = savedInstanceState.getString(
                            LAST_UPDATED_TIME_STRING_KEY);
                }
                updateUI();

            }
        }
    }



}

