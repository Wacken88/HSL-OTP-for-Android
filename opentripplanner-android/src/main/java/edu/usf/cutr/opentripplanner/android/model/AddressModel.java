package edu.usf.cutr.opentripplanner.android.model;

import java.util.Locale;

import edu.usf.cutr.opentripplanner.android.util.CustomAddress;

/**
 * Created by Radek on 3/21/15.
 */
public class AddressModel {

    private int id;
    private String address;
    private double latitude = 0;
    private double longitude = 0;
    private boolean isLatitude = false;
    private boolean isLongitude = false;

    public AddressModel() {
    }

    public AddressModel(String address) {
        this.address = address;
    }

    public AddressModel(String address, double latitude, double longitude, boolean isLatitude, boolean isLongitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isLatitude = isLatitude;
        this.isLongitude = isLongitude;
    }

    public AddressModel(int id, String address, double latitude, double longitude, boolean isLatitude, boolean isLongitude) {
        this.id = id;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isLatitude = isLatitude;
        this.isLongitude = isLongitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public boolean hasLat() {
        return isLatitude;
    }

    public boolean hasLong() {
        return isLongitude;
    }


    public void setLatitudeBool(boolean isLatitude) {
        this.isLatitude = isLatitude;
    }

    public void setLongitudeBool(boolean isLongitude) {
        this.isLongitude = isLongitude;
    }

    public CustomAddress getAsCustomAddress(Locale l){
        CustomAddress res = new CustomAddress(l);

        if(isLatitude)
            res.setLatitude(latitude);
        if(isLongitude)
            res.setLongitude(longitude);

        String[] tokens = address.split(", ");
        int i = 0;
        for(String t:tokens){
            res.setAddressLine(i, t);
            i++;
        }
        return res;
    }
}
