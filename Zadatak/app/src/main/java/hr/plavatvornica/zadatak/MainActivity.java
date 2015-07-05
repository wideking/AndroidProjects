package hr.plavatvornica.zadatak;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

//TODO prepraviti profile picture da bude u ravnini s tekstom.
//TODO napraviti da ucitava slike iz foldera a ne iz res..

public class MainActivity extends ActionBarActivity {

    ArrayList<Hotel> listHotel;
    ArrayList<Integer> arrayPicturesIDs;
    ListView lvHoteli;
    lvHotelAdapter hotelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listHotel = createArrayList();
        int width_secondScreenPictures=(int)getResources().getDimension(R.dimen.second_activity_small_picture_width);
        Log.d("TAG_widthSecondScreen",Integer.toString(width_secondScreenPictures));
        createBitmaps(width_secondScreenPictures, width_secondScreenPictures);
        initialiseListView();

    }

    protected void initialiseListView() {
        lvHoteli = (ListView) findViewById(R.id.lvHoteli);
        hotelAdapter = new lvHotelAdapter(this, listHotel);
        lvHoteli.setAdapter(hotelAdapter);
        lvHoteli.setOnItemClickListener(hotelOnItemClickListener);
    }

    /**
     * Method that creates bitmaps from all drawables. Bitmaps are custom size.
     * @param height Height parameter of new image.
     * @param width Width parameter of new image.

     */
    private void createBitmaps(int height, int width) {
        if(arrayPicturesIDs==null){
            getDrawableIDs();
        }


        BitmapEditor bitmapEditor=new BitmapEditor(arrayPicturesIDs,width,height,this,getBaseContext());





    }

    /**
     * Method for transforming DP values of images to px. Reads display density , calculates needed px's and returns that value.
     * @param dp  Required density - value of ImageView
     * @return Returns needed value of pixels.
     */
    private int dpToPx(int dp) { //No need for this shit because getResources().getDimension converts units :(
        /*DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        */

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        Log.d("TAG_dp",Integer.toString(dp));
        int px = Math.round(dp * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));

        return px;
    }

    /**
     * Method that fills ArrayList with drawable ID's
     */

    private void getDrawableIDs() {
        int pictureID;
        String pictureName;
        arrayPicturesIDs= new ArrayList<>();
        for(int i=0;i<listHotel.size();i++){//Go through all hotels
            for(int j=1;j<5;j++){//Go through all 4 picture that hotels has

                pictureName=listHotel.get(i).getPictureName().replace("1", Integer.toString(j));

                pictureID=getResources().getIdentifier(pictureName, "drawable", getPackageName());

                arrayPicturesIDs.add(pictureID);
            }


        }
    }

    /**
     * Implementation of OnItemClickListener.
     */

    AdapterView.OnItemClickListener hotelOnItemClickListener = new AdapterView.OnItemClickListener() {
        /**
         * Method that registers click on the Item,  passes data and starts second activity.
         * @param parent The Adapter that has registered the click
         * @param view The view within the AdapterView that was clicked
         * @param position The position of the view in the adapter.
         * @param id The row id of the item that was clicked.
         */
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent secondActivity= new Intent(getBaseContext(),SecondActivity.class);
            Bundle bundleHotel=new Bundle();
            final String  bundleKey = getResources().getString(R.string.title_activity_second);
            bundleHotel.putParcelable(bundleKey,listHotel.get(position));
            secondActivity.putExtra(bundleKey,bundleHotel);
            startActivity(secondActivity);


        }
    };

    /**
     * Private Method that creates ArrayList from String Array resource.
     *
     * @return returns ArrayList with Hotel objects
     */
    private ArrayList<Hotel> createArrayList() {
        ArrayList<Hotel> listHotel = new ArrayList<Hotel>();
        TypedArray arrayHotels = getResources().obtainTypedArray(R.array.all_hotels); // String array should be read as TypedArray to have access to inside String Arrays
        String[][] array = new String[arrayHotels.length()][];
        for (int i = 0; i < arrayHotels.length(); i++) {//Get all inside StringArrays.

            int resourceID = arrayHotels.getResourceId(i, 0);
            if (resourceID > 0) {
                array[i] = getResources().getStringArray(resourceID);
            }


        }
        arrayHotels.recycle();//Release resource so they can be used later.


        for (int i = 0; i < array.length; i++) {//For loop that reads all String fields values and creates Hotel objects with those values. The Hotel object is added to ArrayList.
            Hotel newHotel = new Hotel();
            for (int j = 0; j < array[i].length; j++) {
                switch (j) {
                    case 0://Name of hotel
                        newHotel.setName(array[i][j]);
                        break;
                    case 1://street of hotel
                        newHotel.setStreet(array[i][j]);
                        break;
                    case 2://city of hotel
                        newHotel.setCity(array[i][j]);
                        break;
                    case 3://profile_picture of hotel
                        newHotel.setPictureName(array[i][j]);
                        break;
                    case 4://description of hotel
                        newHotel.setDescription(array[i][j]);
                        break;
                    case 5://rating of hotel
                        newHotel.setRating(Integer.parseInt(array[i][j]));
                        break;
                }


            }
            listHotel.add(newHotel);
        }

        return listHotel;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
