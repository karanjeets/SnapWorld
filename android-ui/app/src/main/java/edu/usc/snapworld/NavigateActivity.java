package edu.usc.snapworld;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
}
