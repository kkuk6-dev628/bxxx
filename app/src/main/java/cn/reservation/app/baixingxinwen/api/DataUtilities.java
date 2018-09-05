package cn.reservation.app.baixingxinwen.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by kkuk6 on 7/2/2017.
 */

public class DataUtilities {

    public final static String DateFormatString = "yyyy-MM-dd'T'HH:mm:ss.sss'Z'";
    public final static String validemail = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+";

    public static Date createDateFromISOString(String dateString) {
        DateFormat format = new SimpleDateFormat(DataUtilities.DateFormatString, Locale.ENGLISH);
        try {
            return format.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static String getISODateString(Date date) {
        final DateFormat dateFormat = new SimpleDateFormat(DataUtilities.DateFormatString, Locale.ENGLISH);
        return dateFormat.format(date);
    }

    public static String getMediumDateString(Date date){
        final DateFormat dateFormat =DateFormat.getDateInstance(DateFormat.MEDIUM);
        return dateFormat.format(date);
    }

    public static String getMonthString(Date date) {
        final DateFormat dateFormat = new SimpleDateFormat("MMM", Locale.ENGLISH);
        return dateFormat.format(date);
    }

    public static byte[] getByteArrayFromBitmap(Bitmap bitmapPicture) {
        final int COMPRESSION_QUALITY = 100;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.JPEG, COMPRESSION_QUALITY,
                byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
//        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return b;
    }

    public static byte[] toByteArray(List<Integer> in) {
        try {
            final int n = in.size();
            byte ret[] = new byte[n];
            for (int i = 0; i < ret.length; i++) {
                ret[i] = (byte)in.get(i).intValue();
            }
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getBitmapFromString(byte[] bytePicture) {
//        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        try {
            BitmapFactory.Options options=new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
            options.inPurgeable = true; // inPurgeable is used to free up memory while required
            Bitmap decodedByte = BitmapFactory.decodeByteArray(bytePicture, 0, bytePicture.length, options);
            return decodedByte;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    public static byte[] getFileDataFromDrawable(Context context, int id) {
        Drawable drawable = ContextCompat.getDrawable(context, id);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] getFileDataFromPath(String filePath) {
        File file = new File(filePath);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * Turn drawable into byte array.
     *
     * @param drawable data
     * @return byte array
     */
    public static byte[] getFileDataFromDrawable(Context context, Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}
