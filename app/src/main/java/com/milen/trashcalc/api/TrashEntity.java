package com.milen.trashcalc.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.milen.trashcalc.constants.Preferences;

import java.text.DecimalFormat;
import java.util.Map;

public class TrashEntity implements Parcelable {
    private Object mDate;
    private String mTruckId;
    private double mTrashWeight;
    private double mTruckWeight;

    public TrashEntity(Map<String, String> date, String truckId, double fullTruckValue, double emptyTruckValue) {
        this.mDate = date;
        this.mTruckId = truckId;
        //todo make better rounding
        DecimalFormat df = new DecimalFormat(Preferences.DECIMAL_FORMAT_STRING);
        this.mTrashWeight = Double.parseDouble(df.format(fullTruckValue - emptyTruckValue));
        this.mTruckWeight = emptyTruckValue;
    }

    public TrashEntity() {

    }

    public Object getDate() {
        return mDate;
    }

    @Exclude
    public Long getDateAsLong() {
        return (Long) mDate;
    }

    public void setDate(Object date) {
        this.mDate = date;
    }

    public String getTruckId() {
        return mTruckId;
    }

    public void setTruckId(String truckId) {
        this.mTruckId = truckId;
    }

    public double getTrashWeight() {
        return mTrashWeight;
    }

    public void setTrashWeight(double trashWeight) {
        this.mTrashWeight = trashWeight;
    }

    public double getTruckWeight() {
        return mTruckWeight;
    }

    public void setTruckWeight(double truckWeight) {
        this.mTruckWeight = truckWeight;
    }

    protected TrashEntity(Parcel in) {
        mDate = (Object) in.readValue(Object.class.getClassLoader());
        mTruckId = in.readString();
        mTrashWeight = in.readDouble();
        mTruckWeight = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mDate);
        dest.writeString(mTruckId);
        dest.writeDouble(mTrashWeight);
        dest.writeDouble(mTruckWeight);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TrashEntity> CREATOR = new Parcelable.Creator<TrashEntity>() {
        @Override
        public TrashEntity createFromParcel(Parcel in) {
            return new TrashEntity(in);
        }

        @Override
        public TrashEntity[] newArray(int size) {
            return new TrashEntity[size];
        }
    };
}