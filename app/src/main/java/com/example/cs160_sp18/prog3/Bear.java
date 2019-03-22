package com.example.cs160_sp18.prog3;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class Bear implements Parcelable {
    public int imgId;
    public String bearName;
    public double lat;
    public double lon;

    Bear(int imgId, String bearName, double lat, double lon) {
        this.imgId = imgId;
        this.bearName = bearName;
        this.lat = lat;
        this.lon = lon;
    }

    protected Bear(Parcel in) {
        imgId = in.readInt();
        bearName = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
    }

    public static final Creator<Bear> CREATOR = new Creator<Bear>() {
        @Override
        public Bear createFromParcel(Parcel in) {
            return new Bear(in);
        }

        @Override
        public Bear[] newArray(int size) {
            return new Bear[size];
        }
    };

    protected String getDistance() {
        Location targetLocation = new Location("");//provider name is unnecessary
        targetLocation.setLatitude(lat);//your coords of course
        targetLocation.setLongitude(lon);

        float dist = MainActivity.currLoc.distanceTo(targetLocation);
        return Float.toString(dist);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(imgId);
        parcel.writeString(bearName);
        parcel.writeDouble(lat);
        parcel.writeDouble(lon);
    }
}
