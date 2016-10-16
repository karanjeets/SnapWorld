package edu.usc.snapworld;


import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
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
        View view =  inflater.inflate(R.layout.fragment_results, container, false);
        listView = (ListView) view.findViewById(R.id.imageList);
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
