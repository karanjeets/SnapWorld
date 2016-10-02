package edu.usc.snapworld;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class DetailsActivity extends AppCompatActivity {
    static Image img;
    static byte[] bytes;
    public Criteria criteria;
    public String bestProvider;
    public String latitude;
    public String longitude;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        System.out.println("Hello Kaaka");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
                return;
            }
        }


        System.out.println("ByteBuffer nikal kaaka");
        //ByteBuffer buffer = img.getPlanes()[0].getBuffer();
        System.out.println("Bytes array initialize kar kaaka");
        //byte[] bytes = new byte[buffer.remaining()];
        System.out.println("Bytes nikal kaaka");
        //buffer.get(bytes);

        System.out.println("Bytes nikal gyi kaaka: " + new String(bytes));


        ImageView imgView = (ImageView) findViewById(R.id.imageView);
        System.out.println("Bitmap banne jaa rhi hai....");
        //Log.i("YOOOO", "Bitmap banne jaa rhi hai....");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        System.out.println("Bitmap Ban gaya");
        //Log.i("YOOOO", "Bitmap Ban gaya");
        imgView.setImageBitmap(bitmap);


        //imgView.setImageBitmap(BitmapFactory.decodeFile(getExternalFilesDir(null).getAbsolutePath() + File.separator + "pic.jpg"));
/*
        int mWidth = imgView.getWidth();
        int mHeight = imgView.getHeight();
        //DisplayMetrics imgViewMetrics = new DisplayMetrics();
        //imgView.getDisplay().getMetrics(imgViewMetrics);

        final Image.Plane[] planes = img.getPlanes();
        //final Buffer buffer = planes[0].getBuffer().rewind();
          ByteBuffer buffer = planes[0].getBuffer();
        int offset = 0;
        //int mWidth = img.getWidth();
        //int mHeight = img.getHeight();
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
// create bitmap
        int rowPadding = rowStride - pixelStride * img.getWidth();
        byte[] bitmapImage = new byte[img.getWidth()*img.getHeight()*4];

        // int offset = 0;
        Bitmap bmp = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.RGB_565);
*/
        //Log.i("DetailsActivity", img.getWidth() + ", " + img.getHeight());
/*
        for (int i = 0; i < img.getHeight(); ++i)
        {
            for (int j = 0; j < img.getWidth(); ++j)
            {
                Log.i("Loop Number", i + "," + j);
                int pixel = 0;
                pixel |= (buffer.get(offset) & 0xff) << 16;     // R
                pixel |= (buffer.get(offset + 1) & 0xff) << 8;  // G
                pixel |= (buffer.get(offset + 2) & 0xff);       // B
                pixel |= (buffer.get(offset + 3) & 0xff) << 24; // A
                bmp.setPixel(j, i, pixel);
                offset += pixelStride;
            }
            offset += rowPadding;
        }
*/

       /* Log.i("INFO", "Bitmap size = " + bmp.getByteCount());
        Log.i("INFO", "Buffer size = " + buffer.capacity());*/
  //      buffer.rewind();
    //    bmp.copyPixelsFromBuffer(buffer);
//        img.close();

        // ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

        // byte[] byteArray = stream.toByteArray();

      //   imgView.setImageBitmap(bmp);

        //ImageView imgView = (ImageView) findViewById(R.id.imageView);
        //imgView.setImageBitmap(bmp);
    }

    public void correct_function(View view)
    {
        EditText editText = (EditText) findViewById(R.id.textView);
        String description = editText.getText().toString();
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String category = spinner.getSelectedItem().toString();

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

        Toast.makeText(DetailsActivity.this, latitude + longitude + description + category,Toast.LENGTH_LONG).show();
        finish();
    }


}
