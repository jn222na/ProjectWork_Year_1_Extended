package com.example.DAL;

import android.os.Parcel;
import android.os.Parcelable;

public class GetSetters implements Parcelable {

    private long id;
    private String firstname;
    private String lastname;
    private String phonenumber;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }


    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }


    @Override
    public String toString() {

        return "" + this.firstname + "   " + this.lastname + "    " + this.phonenumber + "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Just cut and paste this for now
    public static final Parcelable.Creator<GetSetters> CREATOR = new Parcelable.Creator<GetSetters>() {
        public GetSetters createFromParcel(Parcel in) {
            return new GetSetters(in);
        }

        public GetSetters[] newArray(int size) {
            return new GetSetters[size];
        }
    };

    public void writeToParcel(Parcel out, int flags) {
        // Again this order must match the Question(Parcel) constructor
        out.writeString(firstname);
        out.writeString(lastname);
        out.writeString(phonenumber);
        // Again continue doing this for the rest of your member data
    }

    GetSetters() {
        firstname = getFirstname();
        lastname = getLastname();
        phonenumber = getPhonenumber();

    }
    GetSetters(Parcel in) {
        // This order must match the order in writeToParcel()
        firstname = in.readString();
        lastname = in.readString();
        phonenumber = in.readString();
        // Continue doing this for the rest of your member data
    }
}
