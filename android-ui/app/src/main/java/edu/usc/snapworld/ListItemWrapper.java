package edu.usc.snapworld;

import android.graphics.Bitmap;
import android.os.StrictMode;

/**
 * Created by Monica on 10/1/2016.
 */

public class ListItemWrapper {

    private Bitmap image;
    private String description;
    private String distance;
    private String category;

    public ListItemWrapper(Bitmap image, String description, String category, String distance)
    {
        super();
        this.image =image;
        this.description = description;
        this.distance = distance;
        this.category = category;
    }

    public Bitmap getImage()
    {
        return image;
    }
    public String getDescription()
    {
        return description;
    }
    public String getDistance()
    {
        return distance;
    }
    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
