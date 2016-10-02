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

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResultsFragment extends Fragment {
    private ListView listView;
    private ListViewAdapter listViewAdapter;


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
        listViewAdapter = new ListViewAdapter(getActivity(), R.layout.list_item_layout, getData());
        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ListItemWrapper item = (ListItemWrapper) parent.getItemAtPosition(position);
                Toast.makeText(getActivity(),"You clicked "+item.getDescription(),Toast.LENGTH_SHORT).show();
            }});

        return view;


    }

    private ArrayList<ListItemWrapper> getData() {
        final ArrayList<ListItemWrapper> imageItems = new ArrayList<>();
       // TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < 20; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.correct);
            imageItems.add(new ListItemWrapper(bitmap, "Image#" + i, "General", "0.5 miles"));
        }
        return imageItems;
    }



}
