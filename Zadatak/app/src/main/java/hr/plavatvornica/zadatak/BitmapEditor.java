package hr.plavatvornica.zadatak;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Class that creates bitmap images from drawables. It uses AsyncTask for creating images and saving them on device internal memory.
 * Created by widek on 4.7.2015..
 */
public class BitmapEditor {
    private ArrayList<Integer> arrayPicturesIDs;
    private int newWidth;
    private int newHeight;
    private Context ctx;
    private MainActivity activity;
    private AsyncTask task;

    public BitmapEditor(ArrayList<Integer> arrayPicturesIDs, int newWidth, int newHeight,MainActivity activity,Context ctx) {
        this.arrayPicturesIDs = arrayPicturesIDs;
        this.newWidth = newWidth;
        this.newHeight = newHeight;
        this.ctx=ctx;
        this.activity=activity;
        task= new ThumbnailCreator().execute(arrayPicturesIDs);




    }

    public AsyncTask.Status getTaskStatus() {


        return task.getStatus();

    }

    private class ThumbnailCreator extends AsyncTask<ArrayList<Integer>,Integer,Integer> {

        private void savePicture(Bitmap thumbnailBitmap, String pictureTitle, File picture_thumbnail) {
            FileOutputStream fOutStream;
            try {
                fOutStream = new FileOutputStream(picture_thumbnail);
                thumbnailBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOutStream);
                thumbnailBitmap.recycle();
                fOutStream.flush();
                fOutStream.close();
            } catch (FileNotFoundException e) {
                Log.d("TAG_Exception", "File" + picture_thumbnail.getAbsolutePath() + " is invalid");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("TAG_Exception", "Cant write file" + pictureTitle);
                e.printStackTrace();
            }
        }


        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Integer doInBackground(ArrayList<Integer>... params) {

            Bitmap originalBitmap;
            Bitmap thumbnailBitmap;
            String pic_path = ctx.getApplicationContext().getFilesDir() + "/Images";
            String drawableTitle;
            String pictureFolderTitle;



            for (int i=0;i<params[0].size();i++) {//For every picture get it's bitmap , create new bitmap with custom size and save it in "pictures" folder.Also it will save original bitmap for use with Galley

                drawableTitle = ctx.getResources().getResourceEntryName(params[0].get(i));


                pictureFolderTitle = drawableTitle.substring(0, drawableTitle.lastIndexOf("_"));//This should return String like "hotel_adriatic"
                File picture_thumbnail_dir=new File(pic_path + "/" + pictureFolderTitle + "/" + "thumbnails");
                picture_thumbnail_dir.mkdirs();//Create needed directories
                File picture_thumbnail = new File(picture_thumbnail_dir.getPath(), drawableTitle + "_thumbnail_"+newWidth+".png");

                File picture = new File(pic_path + "/" + pictureFolderTitle, drawableTitle + ".png");//This directory should be created by picture_thumbnail file.
                if(picture_thumbnail.exists()){//if picture exist do nothing
                    Log.d("TAG_picture_thumbnail","picture is already created");
                }
                else{//if picture doesn't exist create it
                    BitmapFactory.Options o = new BitmapFactory.Options();//get bounds of image for measures
                    o.inJustDecodeBounds = true;
                    try {
                        AssetManager assets = ctx.getAssets();



                        BufferedInputStream buffer = new BufferedInputStream(assets.open(drawableTitle+".9.png"));
                        BitmapFactory.decodeStream(buffer, null, o);


                        // Find the correct scale value. It should be the power of 2.
                        int scale = 1;
                        Log.d("TAG_ScaleSize",Integer.toString(o.outHeight));
                        Log.d("TAG_ScaleHeight",Integer.toString(newHeight));
                        if (o.outHeight > newHeight || o.outWidth > newWidth) {
                        Log.d("TAG_ScaleHeight",Integer.toString(newHeight));
                            final int halfHeight = o.outHeight / 2;
                            final int halfWidth = o.outWidth / 2;

                            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                            // height and width larger than the requested height and width.
                            while ((halfHeight / scale) > newHeight
                                    && (halfWidth / scale) > newWidth) {
                                scale *= 2;
                            }
                        }

                        Log.d("TAG_scale",Integer.toString(scale));

                        // Decode with inSampleSize
                        BitmapFactory.Options o2 = new BitmapFactory.Options();
                        o2.inSampleSize = scale;
                        buffer.close();
                         buffer = new BufferedInputStream(assets.open(drawableTitle+".9.png"));
                        originalBitmap = BitmapFactory.decodeStream(buffer, null, o2);
                        buffer.close();

                        thumbnailBitmap = ThumbnailUtils.extractThumbnail(originalBitmap, newWidth, newHeight);

                        //buffer.close();
                        savePicture(thumbnailBitmap, drawableTitle, picture_thumbnail);
                        savePicture(originalBitmap, drawableTitle, picture);
                        thumbnailBitmap.recycle();
                        originalBitmap.recycle();


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

            }
            return 0;

        }
        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            activity.hotelAdapter.notifyDataSetChanged();

        }
    }
}