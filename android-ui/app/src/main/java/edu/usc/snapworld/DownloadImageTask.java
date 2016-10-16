package edu.usc.snapworld;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Monica on 10/14/2016.
 */

public class DownloadImageTask extends AsyncTask<String,Bitmap,Bitmap> {
    String urlString = null;
    Bitmap bitmap;
    URL url;

    public interface ImgAsyncResponse {
        void processFinish(Bitmap output);
    }

    public DownloadImageTask.ImgAsyncResponse asynch = null;

    public DownloadImageTask(DownloadImageTask.ImgAsyncResponse asyncResponse) {
        asynch = asyncResponse;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        try {
            System.out.println("DownloadImageTask in Background");
            url = new URL(urlString);
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
       // mImageView.setImageBitmap(result);
        super.onPostExecute(result);
        asynch.processFinish(result);
    }
}
