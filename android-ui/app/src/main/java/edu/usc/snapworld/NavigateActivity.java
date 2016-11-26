package edu.usc.snapworld;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class NavigateActivity extends AppCompatActivity {

    static byte[] bytes;
    static Bitmap img;
    private  ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);
        imageView = (ImageView)findViewById(R.id.fullImage);
        setImage();
    }

    public void setImage()
    {
        imageView.setImageBitmap(img);
    }

    public void navigateMap(View v)
    {
        String latitude_cur = "34.0310149";
        String longitude_cur = "-118.288292";
        String latitude_dest = "34.028566";
        String longitude_dest = "-118.284314";
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr="+latitude_cur+","+longitude_cur+"&daddr="+latitude_dest+","+longitude_dest));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_LAUNCHER );
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }

}
