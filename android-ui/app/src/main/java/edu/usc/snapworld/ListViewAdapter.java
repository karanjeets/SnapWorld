package edu.usc.snapworld;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Monica on 10/1/2016.
 */

public class ListViewAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private List<ListItemWrapper> data = Collections.synchronizedList(new ArrayList<ListItemWrapper>());

    public ListViewAdapter(Context context, int layoutResourceId, ArrayList data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View eachRow = convertView;
        ListViewHolder holder = null;

        if (eachRow == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            eachRow = inflater.inflate(layoutResourceId, parent, false);
            holder = new ListViewHolder();

                //eachRow.setBackgroundColor(Color.parseColor("#feeaef"));

                //eachRow.setBackgroundColor(Color.parseColor("#FFFFFF"));

            holder.imageDesc = (TextView) eachRow.findViewById(R.id.gridDesc);
            holder.imageCategory = (TextView) eachRow.findViewById(R.id.gridCategory);
            holder.imageDistance = (TextView) eachRow.findViewById(R.id.gridDistance);
            holder.image = (ImageView) eachRow.findViewById(R.id.gridImage);
            eachRow.setTag(holder);
        } else {
            holder = (ListViewHolder) eachRow.getTag();
        }

        ListItemWrapper item = (ListItemWrapper)data.get(position);

        holder.imageCategory.setText(item.getCategory());
        holder.imageDistance.setText(item.getDistance());
        holder.imageDesc.setText(item.getDescription());
        holder.image.setImageBitmap(item.getImage());
        return eachRow;
    }

    public List<ListItemWrapper> getData() {
        return data;
    }

    static class ListViewHolder {
        TextView imageDesc;
        TextView imageCategory;
        TextView imageDistance;
        ImageView image;
    }
}
