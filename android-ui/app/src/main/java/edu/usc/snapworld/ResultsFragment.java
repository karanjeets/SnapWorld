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
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v13.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
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
    public String categorySelected = "general";
    private LocationManager locationManager;
    private LocationListener locationListener;
    JSONObject jsonListata = null;
    private  static SeekBar seek_bar;
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
        FloatingActionButton filters = (FloatingActionButton) view.findViewById(R.id.fab);
       final ImageButton general = (ImageButton) view.findViewById(R.id.fab_general);
        final ImageButton food = (ImageButton) view.findViewById(R.id.fab_food);
        final ImageButton nature = (ImageButton) view.findViewById(R.id.fab_nature);
        final ImageButton his = (ImageButton) view.findViewById(R.id.fab_his);
        final ImageButton education = (ImageButton) view.findViewById(R.id.fab_education);
        final ImageButton sports = (ImageButton) view.findViewById(R.id.fab_sports);
        seek_bar = (SeekBar) view.findViewById(R.id.seekBar);

        filters.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (general.getVisibility() == View.VISIBLE) {
                    general.setVisibility(View.GONE);
                    food.setVisibility(View.GONE);
                    sports.setVisibility(View.GONE);
                    his.setVisibility(View.GONE);
                    education.setVisibility(View.GONE);
                    nature.setVisibility(View.GONE);
                } else {
                    general.setVisibility(View.VISIBLE);
                    food.setVisibility(View.VISIBLE);
                    sports.setVisibility(View.VISIBLE);
                    his.setVisibility(View.VISIBLE);
                    education.setVisibility(View.VISIBLE);
                    nature.setVisibility(View.VISIBLE);
                }
                //Toast.makeText(getActivity(),"you clicked filters",Toast.LENGTH_SHORT).show();
            }
        });

        general.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                categorySelected = "general";
                refreshData();
                //Toast.makeText(getActivity(),"you clicked food",Toast.LENGTH_SHORT).show();
            }
        });

        food.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                categorySelected = "food";
                refreshData();
                //Toast.makeText(getActivity(),"you clicked food",Toast.LENGTH_SHORT).show();
            }
        });

        sports.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                categorySelected = "sports";
                refreshData();
                //Toast.makeText(getActivity(),"you clicked food",Toast.LENGTH_SHORT).show();
            }
        });

        nature.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                categorySelected = "nature";
                refreshData();
                //Toast.makeText(getActivity(),"you clicked food",Toast.LENGTH_SHORT).show();
            }
        });

        education.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                categorySelected = "education";
                refreshData();
                //Toast.makeText(getActivity(),"you clicked food",Toast.LENGTH_SHORT).show();
            }
        });

        his.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                categorySelected = "History & Culture";
                refreshData();
                //Toast.makeText(getActivity(),"you clicked food",Toast.LENGTH_SHORT).show();
            }
        });

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

        String jsonListUrl = "http://104.198.60.184:8080/snapworld/data/getdata/"+latitude+"/"+longitude;

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

        seekbar(); //calling seekbar
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_general:
                categorySelected = "general";
                refreshData();
                return true;

            case R.id.menu_food:
                categorySelected = "food";
                refreshData();
                return true;

            case R.id.menu_nature:
                categorySelected = "nature";
                refreshData();
                return true;

            case R.id.menu_sports:
                categorySelected = "sports";
                refreshData();
                return true;

            case R.id.menu_edu:
                categorySelected = "education";
                refreshData();
                return true;

            case R.id.menu_his:
                categorySelected = "History & Culture";
                refreshData();
                return true;
        }
        return true;
    }

    private void makeData() {
        String imgUrl;

        for (int i = 0; i < Constants.jsonListArray.length(); i++) {

                try {

                    final JSONObject jsonobject = Constants.jsonListArray.getJSONObject(i);
                    //if ((categorySelected == "general") || (categorySelected.toLowerCase() == jsonobject.getString("category_name").toLowerCase())) {

                        DownloadImageTask imgTask = new DownloadImageTask(new DownloadImageTask.ImgAsyncResponse() {

                            @Override
                            public void processFinish(Bitmap output) {
                                bitmap = output;
                                System.out.println("Inside Process Finish");
                                try {
                                    ListViewAdapter adapter = (ListViewAdapter) listView.getAdapter();
                                    adapter.getData().add(new ListItemWrapper(bitmap, jsonobject.getString("description"), jsonobject.getString("category_name"),jsonobject.getString("road_distance").substring(0,4)+" miles"));
                                    adapter.notifyDataSetChanged();

                                    //imageItems.add(new ListItemWrapper(bitmap, jsonobject.getString("description"), jsonobject.getString("category"), "0.3 miles"));
                                    System.out.println("inside Thread " + jsonobject.getString("description"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });


                        //URL url = new URL("http://104.197.77.81/snapdata/" + jsonobject.getString("imgpath"));

                        //imgTask.requestType = Constants.RequestType.GET;
                        imgUrl = "http://104.198.60.184/snapdata/" + jsonobject.getString("imgpath");
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


            System.out.println("Imageitems" + imageItems);


    }

    private void refreshData() {
        String imgUrl;
        ListViewAdapter adapter = (ListViewAdapter) listView.getAdapter();
        adapter.clear();

        for (int i = 0; i < Constants.jsonListArray.length(); i++) {

            try {

                final JSONObject jsonobject = Constants.jsonListArray.getJSONObject(i);
               // System.out.println();
                System.out.println(categorySelected);
                System.out.println(jsonobject.getString("category_name"));
                if ((categorySelected == "general") || (categorySelected.equalsIgnoreCase(jsonobject.getString("category_name")))) {

                    DownloadImageTask imgTask = new DownloadImageTask(new DownloadImageTask.ImgAsyncResponse() {

                        @Override
                        public void processFinish(Bitmap output) {
                            bitmap = output;
                            System.out.println("Inside Process Finish");
                            try {
                                ListViewAdapter adapter = (ListViewAdapter) listView.getAdapter();
                                adapter.getData().add(new ListItemWrapper(bitmap, jsonobject.getString("description"), jsonobject.getString("category_name"), jsonobject.getString("road_distance").substring(0,4)+" miles"));
                                adapter.notifyDataSetChanged();

                                //imageItems.add(new ListItemWrapper(bitmap, jsonobject.getString("description"), jsonobject.getString("category"), "0.3 miles"));
                                System.out.println("inside Thread " + jsonobject.getString("description"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });


                    //URL url = new URL("http://104.197.77.81/snapdata/" + jsonobject.getString("imgpath"));

                    //imgTask.requestType = Constants.RequestType.GET;
                    imgUrl = "http://104.198.60.184/snapdata/" + jsonobject.getString("imgpath");
                    imgTask.urlString = imgUrl;

                    System.out.println(imgTask.urlString);
                    //JSONArray dataJsonArr = null;

                    imgTask.execute();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }


        System.out.println("Imageitems" + imageItems);


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

    public void seekbar()
    {

       // text_view = (TextView)findViewById(R.id.textView);
        //text_view.setText("Covered : "+seek_bar.getProgress() + "/" + seek_bar.getMax());
        //final
        seek_bar.setOnSeekBarChangeListener(

                new SeekBar.OnSeekBarChangeListener(){
                    int progress_value;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress_value = progress;
                        System.out.println(progress_value);
                       // text_view.setText("Covered : "+progress + "/" + seek_bar.getMax());
                        //Toast.makeText(getActivity(),progress,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                      //  text_view.setText("Covered : "+progress_value + "/" + seek_bar.getMax());
                         System.out.println(progress_value);
                        //Toast.makeText(getActivity(),progress_value,Toast.LENGTH_LONG).show();
                    }

                }
        );
    }



}
