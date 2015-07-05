package hr.plavatvornica.zadatak;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class that describes Hotel
 * Created by widek on 30.6.2015..
 */
public class Hotel implements Parcelable{
    private String name;
    private String street;
    private String city;
    private String pictureName;
    private String description;
    private int rating;

    /**
     * Parametarised Class constructor
     *
     * @param name        type String parameter that represents name of hotel
     * @param street      type String parameter that represents street where is  hotel located
     * @param city        type String parameter that represents city where is  hotel located
     * @param pictureName type String parameter that represents name of the profile picture of hotel that is used in ListView
     * @param description type String parameter that represents description of Hotel
     * @param rating      type int parameter that represents Quality/rating of Hotel
     */
    Hotel(String name, String street, String city, String pictureName, String description,int rating) {
        this.name = name;
        this.street = street;
        this.city = city;
        this.pictureName = pictureName;
        this.description = description;
        this.rating=rating;
    }

    /**
     * Class constructor that creates default Hotel object
     */
    Hotel() {
        this.name = "name";
        this.street = "street";
        this.city = "city";
        this.pictureName = "pictureName";
        this.description = "description";
        this.rating=1;


    }

    /**
     * Constructor for creating object from parcel
     *
     * @param in Parcel from which object will be created
     */
    Hotel(Parcel in){
        readFromParcel(in);
    }

    /**
     * Method for reading data from parcel
     *
     * @param in Parcel from which object will be created
     */
    private void readFromParcel(Parcel in) {
        name=in.readString();
        street=in.readString();
        city=in.readString();
        pictureName=in.readString();
        description=in.readString();
        rating=in.readInt();

    }


    /**
     * Method that returns description of Hotel
     *
     * @return returns description of Hotel
     */
    public String getDescription() {
        return description;
    }

    /**
     * Method sets Description of hotel
     *
     * @param description type String parameter that represents description of Hotel
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Method that returns name of hotel
     *
     * @return returns name of hotel
     */
    public String getName() {
        return name;
    }

    /**
     * Method for setting name of Hotel object
     *
     * @param name type String parameter that represents name of hotel
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method that returns street where hotel is located
     *
     * @return returns street where hotel is located
     */

    public String getStreet() {
        return street;
    }

    /**
     * Method for setting street of Hotel object
     *
     * @param street type String parameter that represents street where is  hotel located
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Method that returns city where hotel is located
     *
     * @return returns city where hotel is located
     */

    public String getCity() {
        return city;
    }

    /**
     * Method for setting city of Hotel object
     *
     * @param city type String parameter that represents city where is  hotel located
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Method that returns name of the profile picture
     *
     * @return returns name of the profile picture
     */
    public String getPictureName() {
        return pictureName;
    }

    /**
     * Method for setting pictureName of Hotel object
     *
     * @param pictureName type String parameter that represents name of the profile picture of hotel that is used in ListView
     */
    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Method for geting value of Rating
     * @return  returns int value of rating.
     */
    public int getRating() {
        return rating;
    }
    /**
     * Method for setting value of Rating
     *
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Method for writing Object data to parcel
     * @param dest Parcel that is used for sending data
     * @param flags Additional flags about how the object should be written
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(street);
        dest.writeString(city);
        dest.writeString(pictureName);
        dest.writeString(description);
        dest.writeInt(rating);


    }
    public static final Parcelable.Creator CREATOR=new Parcelable.Creator(){
        @Override
        public Object createFromParcel(Parcel source) {
            return new Hotel(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new Hotel[size];
        }
    };
}
