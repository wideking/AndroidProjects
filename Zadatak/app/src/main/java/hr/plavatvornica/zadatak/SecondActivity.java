package hr.plavatvornica.zadatak;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.File;

//TODO write javadoc
//todo make image look nice
//todo make action bar
//todo make image open in galery

/**
 * Class for second activity. This class displays information about Hotel, like basic information and contains few images.
 * Class contains method for setting up the layout.
 * Class contains method for scanning files for picture. Method was taken from : http://stackoverflow.com/questions/6074270/built-in-gallery-in-specific-folder/8255674#8255674
 *
 */

public class SecondActivity extends ActionBarActivity implements MediaScannerConnection.MediaScannerConnectionClient {
    String picturePath;
    String allFiles[];
    File folder;
    private MediaScannerConnection mediaScannerConn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        final String  bundleKey = getResources().getString(R.string.title_activity_second);
        Bundle bundleHotel=getIntent().getBundleExtra(bundleKey);
        Hotel hotel=bundleHotel.getParcelable(bundleKey);

        setTitle(hotel.getName());
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_second);
        initialiseLayout(hotel);
        folder = new File(picturePath);
        allFiles = folder.list();
        Log.d("TAG_allFIles",allFiles.toString());
        //startScan();








    }
    private void startScan() {
        Log.d("Connected","success"+mediaScannerConn);
        if(mediaScannerConn!=null)
        {
            mediaScannerConn.disconnect();
        }
        mediaScannerConn = new MediaScannerConnection(this,this);
        mediaScannerConn.connect();
    }

    /**
     * This method initialise Layout with information from Hotel object. Main purpose of method is to locate picture thumbnails and load them into appropriate ImageView.
     * @param hotel represents Hotel object that contains information about hotel.
     */
    private void initialiseLayout(Hotel hotel) {
        TextView tvName=(TextView)findViewById(R.id.tv_second_activity_name);
        TextView tvStreet=(TextView)findViewById(R.id.tv_second_activity_street_name);
        TextView tvCity=(TextView)findViewById(R.id.tv_second_activity_city);
        TextView tvDescription=(TextView)findViewById(R.id.tv_second_activity_description);

        ImageView ivMainPicture=(ImageView)findViewById(R.id.iv_second_activity_main_image);
        ImageView ivSmallPicture2=(ImageView)findViewById(R.id.iv_second_activity_small_picture1);
        ImageView ivSmallPicture3=(ImageView)findViewById(R.id.iv_second_activity_small_picture2);
        ImageView ivSmallPicture4=(ImageView)findViewById(R.id.iv_second_activity_small_picture3);
        RatingBar rbHotelRating=(RatingBar)findViewById(R.id.rb_second_activity_ratingBar);
        rbHotelRating.setRating(hotel.getRating());


        tvName.setText(hotel.getName());
        tvStreet.setText(hotel.getStreet());
        tvCity.setText(hotel.getCity());
        tvDescription.setText(hotel.getDescription());

        int mainPictureID = getResources().getIdentifier(hotel.getPictureName(), "drawable", getPackageName());//No point of using bitmap when bitmap is the same size..
        ivMainPicture.setImageResource(mainPictureID);


        String pic_path = getBaseContext().getFilesDir() + "/Images";
        String titlePicture2 = hotel.getPictureName().replace("1","2");
        String titlePicture3 = hotel.getPictureName().replace("1","3");
        String titlePicture4 = hotel.getPictureName().replace("1","4");
        String pictureFolderTitle = titlePicture2.substring(0, titlePicture2.lastIndexOf("_"));//This should return String like "hotel_adriatic"

        int width=(int)getResources().getDimension(R.dimen.second_activity_small_picture_width);//DP size of profile pic.

        picturePath=pic_path + "/" + pictureFolderTitle+"/";
        File picture2=new File(pic_path + "/" + pictureFolderTitle + "/" + "thumbnails/"+titlePicture2 + "_thumbnail_"+width+".png");
        File picture3=new File(pic_path + "/" + pictureFolderTitle + "/" + "thumbnails/"+titlePicture3 + "_thumbnail_"+width+".png");
        File picture4=new File(pic_path + "/" + pictureFolderTitle + "/" + "thumbnails/"+titlePicture4 + "_thumbnail_"+width+".png");


        ivSmallPicture2.setImageURI(Uri.fromFile(picture2));
        ivSmallPicture3.setImageURI(Uri.fromFile(picture3));
        ivSmallPicture4.setImageURI(Uri.fromFile(picture4));


        /*ivMainPicture.setOnClickListener(clickListener); Disabled becouse opening gallery is not working. Probably because images are in private storage :/
        ivSmallPicture2.setOnClickListener(clickListener);
        ivSmallPicture3.setOnClickListener(clickListener);
        ivSmallPicture4.setOnClickListener(clickListener);
*/


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);
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
        if(id== android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    View.OnClickListener clickListener=new View.OnClickListener() {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
           Uri test= Uri.parse(picturePath);
            Log.d("TAG_URI",test.toString());
            test=Uri.fromFile(folder);
            Log.d("TAG_URIfromFile",test.toString());

            /*Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(picturePath), "image/*");
            startActivityForResult(intent, 0);
*/
            startScan();

        }
    };


    /**
     * Called to notify the client when a connection to the
     * MediaScanner service has been established.
     */
    @Override
    public void onMediaScannerConnected() {
        mediaScannerConn.scanFile(folder.getAbsolutePath(),"image/*");

    }

    /**
     * Called to notify the client when the media scanner has finished
     * scanning a file.
     *
     * @param path the path to the file that has been scanned.
     * @param uri  the Uri for the file if the scanning operation succeeded
     */
    @Override
    public void onScanCompleted(String path, Uri uri) {
        try {
            Log.d("onScanCompleted",uri + "success"+mediaScannerConn);
            if (uri != null)
            {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "image/*");
                startActivity(intent);
            }
        } finally
        {
            mediaScannerConn.disconnect();
            mediaScannerConn = null;
        }
    }

    }

