package edu.usc.snapworld;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v13.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResultsFragment extends Fragment {
    private ListView listView;
    private ListViewAdapter listViewAdapter;
    ArrayList<ListItemWrapper> imageItems = new ArrayList<>();
    private Bitmap bitmap;
    public Criteria criteria;
    public String bestProvider;
    public String latitude;
    public String longitude;
    private LocationManager locationManager;
    private LocationListener locationListener;
    JSONObject jsonListata = null;
    //JSONObject jsondata=null;
    //static JSONArray jsonarray= null;


    public ResultsFragment() {
        // Required empty public constructor
    }
    public static ResultsFragment newInstance() {
        return new ResultsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //setHasOptionsMenu(true);
        setHasOptionsMenu(true);

        View view =  inflater.inflate(R.layout.fragment_results, container, false);
        listView = (ListView) view.findViewById(R.id.imageList);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onLocationChanged(Location location) {

                if (ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
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

        String jsonListUrl = "http://104.197.77.81:8080/snapworld/data/getdata/"+latitude+"/"+longitude;

        jsonList.requestType = Constants.RequestType.GET;
        jsonList.url=jsonListUrl;

        System.out.println(jsonList.url);
        //JSONArray dataJsonArr = null;

        jsonList.execute();

        imageItems = new ArrayList<>();
        listViewAdapter = new ListViewAdapter(getActivity(), R.layout.list_item_layout, imageItems);
        listView.setAdapter(listViewAdapter);
        makeData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ListItemWrapper item = (ListItemWrapper) parent.getItemAtPosition(position);
                Toast.makeText(getActivity(),"You clicked "+item.getDescription(),Toast.LENGTH_SHORT).show();
            }});

        return view;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       /*MenuItem filter = menu.add("Category");
        filter.setIcon(R.drawable.correct); */
       // System.out.println("OptionsMenu");
        inflater.inflate(R.menu.results_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    private void makeData() {
        String imgUrl;

        for (int i = 0; i < Constants.jsonListArray.length(); i++) {
            try {
              final  JSONObject jsonobject = Constants.jsonListArray.getJSONObject(i);
                DownloadImageTask imgTask = new DownloadImageTask(new DownloadImageTask.ImgAsyncResponse() {

                    @Override
                    public void processFinish(Bitmap output) {
                        bitmap = output;
                        System.out.println("Inside Process Finish");
                        try {
                            ListViewAdapter adapter = (ListViewAdapter) listView.getAdapter();
                            adapter.getData().add(new ListItemWrapper(bitmap, jsonobject.getString("description"), jsonobject.getString("category"), "0.3 miles"));
                            adapter.notifyDataSetChanged();

                            //imageItems.add(new ListItemWrapper(bitmap, jsonobject.getString("description"), jsonobject.getString("category"), "0.3 miles"));
                            System.out.println("inside Thread " + jsonobject.getString("description"));

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });


                //URL url = new URL("http://104.197.77.81/snapdata/" + jsonobject.getString("imgpath"));

                //imgTask.requestType = Constants.RequestType.GET;
                imgUrl = "http://104.197.77.81/snapdata/" + jsonobject.getString("imgpath");
                imgTask.urlString = imgUrl;

                System.out.println(imgTask.urlString);
                //JSONArray dataJsonArr = null;

                imgTask.execute();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }


        }

        System.out.println("Imageitems"+imageItems);


    }

    private ArrayList<ListItemWrapper> getData() {


       /* try {

            for (int i = 0; i < Constants.jsonListArray.length(); i++) {
               final JSONObject jsonobject = Constants.jsonListArray.getJSONObject(i);
               // Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.correct);
               // Bitmap bitmap= null;


                           URL url = new URL("http://104.197.77.81/snapdata/" + jsonobject.getString("imgpath"));


                        *//*try {
                            URL url = new URL("http://104.197.77.81/snapdata/"+jsonobject.getString("imgpath"));
                            System.out.println(url);
                             bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            //Constants.temp = bitmap;
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*//*
                imageItems.add(new ListItemWrapper(bitmap, jsonobject.getString("description"), jsonobject.getString("category"), "0.3 miles"));




        /*for (int i = 0; i < 20; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.correct);
            imageItems.add(new ListItemWrapper(bitmap, "Image#" + i, "General", "0.5 miles"));
        }*/
        return imageItems;
    }



}
