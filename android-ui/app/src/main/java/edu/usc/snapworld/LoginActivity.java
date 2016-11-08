package edu.usc.snapworld;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;
import static edu.usc.snapworld.Constants.jsonListArray;

public class LoginActivity extends AppCompatActivity {
    JSONObject jsondata=null;
    JSONObject jsonListata = null;
    String username = "";
    Intent i;
    public Criteria criteria;
    public String bestProvider;
    public String latitude;
    public String longitude;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final ImageView backgroundOne = (ImageView) findViewById(R.id.background_one);
        final ImageView backgroundTwo = (ImageView) findViewById(R.id.background_two);

        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(25000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = backgroundOne.getWidth();
                final float translationX = width * progress;
                backgroundOne.setTranslationX(translationX);
                backgroundTwo.setTranslationX(translationX - width + 1);
            }
        });
        animator.start();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
                return;
            }
        }


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onLocationChanged(Location location) {

                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    locationManager.removeUpdates(this);

                //open the map:
                latitude = Double.toString(location.getLatitude());
                longitude = Double.toString( location.getLongitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }

        };
        criteria = new Criteria();
        bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();


        try {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location !=null)
            {
                latitude = Double.toString(location.getLatitude());
                longitude = Double.toString(location.getLongitude());
            }

            else
            {
                locationManager.requestLocationUpdates(bestProvider,1000,0,locationListener);
            }
        }
        catch(SecurityException e)
        {
            e.printStackTrace();
        }
    }

    public void loginClick(View v)
    {
        if(v.getId() == R.id.Login)
        {
            EditText b = (EditText) findViewById(R.id.password);
            String password = b.getText().toString();
            if(password.equals("snapworld")) {
                EditText a = (EditText) findViewById(R.id.username);
                username = a.getText().toString();
                i = new Intent(LoginActivity.this, MainActivity.class);

                AsyncTaskParseJson jsonList = new AsyncTaskParseJson(new AsyncTaskParseJson.AsyncResponse() {


                    @Override

                    public void processFinish(JSONObject output) {
                        try {

                            jsonListata=output;
                            // System.out.println("From MainActivity");
                            //System.out.println(jsondata.getString("latitude"));
                            Constants.jsonListArray = jsonListata.getJSONArray("Snapdata");



                       }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                String jsonListUrl = "http://104.198.60.184:8080/snapworld/data/getdata/"+latitude+"/"+longitude;

                jsonList.requestType = Constants.RequestType.GET;
                jsonList.url=jsonListUrl;

                System.out.println(jsonList.url);
                //JSONArray dataJsonArr = null;

                jsonList.execute();

                AsyncTaskParseJson json = new AsyncTaskParseJson(new AsyncTaskParseJson.AsyncResponse() {


                    @Override

                    public void processFinish(JSONObject output) {
                        try {

                            jsondata=output;
                           // System.out.println("From MainActivity");
                            //System.out.println(jsondata.getString("latitude"));
                            JSONArray jsonarray = jsondata.getJSONArray("Categories");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String name = jsonobject.getString("name");
                                String id = jsonobject.getString("id");
                                Constants.categoryMap.put(name,id);

                                //System.out.println(name);
                            }
                            i.putExtra("username", username);
                            //i.putExtra("categoryMap",categoryMap);
                            startActivity(i);


                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                String url = "http://104.198.60.184:8080/snapworld/data/getcategory";

                json.requestType = Constants.RequestType.GET;
                json.url=url;

                System.out.println(json.url);
                //JSONArray dataJsonArr = null;

                json.execute();




            }
            else
            {
                //Toa.makeText(getActivity(), "this is my Toast message!!! =)",
                //      Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),"Password Incorrect!!! ",Toast.LENGTH_LONG).show();
            }
        }
    }

}
