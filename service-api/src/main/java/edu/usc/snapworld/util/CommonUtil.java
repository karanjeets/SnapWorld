package edu.usc.snapworld.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Created by karanjeetsingh on 10/1/16.
 */
public class CommonUtil {

    public static Properties configProperties = new Properties();

    /**
     * Runs at application startup.
     * Loads configuration into properties file
     */
    private static void loadConfig() {
        InputStream is = null;
        try {
            is = CommonUtil.class.getClassLoader().getResourceAsStream(Constants.CONFIG_FILENAME);
            configProperties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * Create directory recursively
     * @param dirPath
     * @return true: if created; false: if exception
     */
    public static boolean safeMkdir(String dirPath) {
        Path path = Paths.get(dirPath);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Saves the image to filesystem and return a path to it
     * @param image : byte array
     * @param username : string
     * @param timestamp : string in format {@value Constants#TIMESTAMP_FORMAT}
     * @return filePath : string : null if image not saved
     */
    public static String saveImage(byte[] image, String username, String timestamp) {
        String imageDir = configProperties.getProperty(Constants.IMAGE_REPOSITORY) + File.separator + username;
        if(!safeMkdir(imageDir))
            return null;
        String imagePath = imageDir + File.separator + timestamp + Constants.IMAGE_EXT;
        try {
            FileUtils.writeByteArrayToFile(new File(imagePath), image);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return imagePath;
    }

    /**
     * Returns a byte array of the image from filesystem
     * @param imagePath : string
     * @return image : byte array
     */
    public static byte[] getImage(String imagePath) {
        File imageFile = new File(imagePath);
        byte[] image;
        if (!imageFile.exists()) {
            System.out.println("File " + imagePath + " doesn't exist");
            return null;
        }
        try {
            image = FileUtils.readFileToByteArray(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return image;
    }

    /**
     * Convert Common Timestamp to SQL Timestamp
     * @param timestamp : string in format {@value Constants#TIMESTAMP_FORMAT}
     * @return timestamp : SQL format
     */
    public static Timestamp toSqlTimestamp(String timestamp) {
        Timestamp sqlTimestamp;
        Date date;
        final DateFormat dateFormater = new SimpleDateFormat(Constants.TIMESTAMP_FORMAT);
        try {
            date = dateFormater.parse(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        sqlTimestamp = new Timestamp(date.getTime());
        return sqlTimestamp;
    }

    /**
     * Initialize Application
     */
    static {
        loadConfig();
    }

    /**
     * Thanks to @yusufshakeel for this method.
     * Reference - https://github.com/yusufshakeel/Java-Image-Processing-Project
     */
    private static byte[] generateRandomImage() {
        //image dimension
        int width = 640;
        int height = 320;

        //create buffered image object img
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        //create random image pixel by pixel
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int a = (int)(Math.random()*256);   //alpha
                int r = (int)(Math.random()*256);   //red
                int g = (int)(Math.random()*256);   //green
                int b = (int)(Math.random()*256);   //blue

                int p = (a<<24) | (r<<16) | (g<<8) | b; //pixel

                img.setRGB(x, y, p);
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, Constants.IMAGE_EXT, baos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }

    public static void main(String[] args) {
        // Save random generated image
        //System.out.println(saveImage(generateRandomImage(), "karan", "2016-10-01-19-35-12"));

        // Get Image
        //System.out.println(new String(getImage(configProperties.getProperty(Constants.IMAGE_REPOSITORY) +
        //        File.separator + "karan" + File.separator + "2016-10-01-19-35-12.jpg")));

        // Convert to SQL Timestamp
        //System.out.println(toSqlTimestamp("2016-10-01-19-35-12"));
    }

}
