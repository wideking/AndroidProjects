package hr.plavatvornica.zadatak;

import android.content.Context;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

/**
 * Custom Adapter class for generating ListView
 * <p/>
 * This adapter is filling custom ListView with information from Hotel object. It uses recycled view to save memory, and make app runs smoother.
 * Created by widek on 30.6.2015..
 */
public class lvHotelAdapter extends BaseAdapter {
    Context context;
    ArrayList<Hotel> listHotels;

    /**
     * This is adapters constructor.
     *
     * @param context    This is context parameter.
     * @param listHotels This is ArrayList type parameter that contains Hotel objects. Objects inside ArrayList are used for filling ListView
     */
    lvHotelAdapter(Context context, ArrayList<Hotel> listHotels) {

        this.context = context;
        this.listHotels = listHotels;


    }

    /**
     * Method that returns size of displayed list.
     *
     * @return returns size of displayed list
     */
    @Override
    public int getCount() {
        return listHotels.size();
    }

    /**
     * Method that returns Hotel item on specified position.
     *
     * @param position Integer value that represents place of object inside ArrayList
     * @return returns Hotel item on specified position.
     */
    @Override
    public Hotel getItem(int position) {
        return listHotels.get(position);
    }

    /**
     * Method that returns place of the object inside the list
     *
     * @param position Integer value that represents place of object inside ArrayList
     * @return Hotel item on specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Method that sets objects inside of item_layout.xml and returns recycled view with new information
     * <p/>
     * Method gets current Hotel object from ArrayList, ImageView and TextView's from View parameter.
     * ImageView and TextView's information are replaced with information from current Hotel object. Method returns view that contains new information.
     *
     * @param position    Integer value that represents place of object inside ArrayList
     * @param convertView View objects that is represents current view.
     * @param parent      Group that view belongs. Set as null.
     * @return returns recycled view that is filled with new object values.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_layout, null);
        }
        Hotel current = getItem(position);

        ImageView ivProfilePicture = (ImageView) convertView.findViewById(R.id.iv_profile_picture);
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tvStreet = (TextView) convertView.findViewById(R.id.tv_street_name);
        TextView tvCity = (TextView) convertView.findViewById(R.id.tv_city);

        int mainPictureID = convertView.getResources().getIdentifier(current.getPictureName(), "drawable", context.getPackageName());
        ivProfilePicture.setImageResource(mainPictureID);



        tvName.setText(current.getName());
        tvStreet.setText(current.getStreet());
        tvCity.setText(current.getCity());
        return convertView;
    }

}
