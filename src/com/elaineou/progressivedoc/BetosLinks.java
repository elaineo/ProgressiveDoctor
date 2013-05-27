package com.elaineou.progressivedoc;

import android.os.Parcel;
import android.os.Parcelable;

public class BetosLinks implements Parcelable {
	private String cc;
	private String icd;
	
	public BetosLinks(String c, String i) {
		cc = c;
		icd = i;
	}
	public String getCC() {
		return cc;
	}
	public String getICD() {
		return icd;
	}
    /* everything below here is for implementing Parcelable */

    public int describeContents() {
        return 0;
    }
    // write your object's data to the passed-in Parcel
    public void writeToParcel(Parcel out, int flags) {
    	out.writeStringArray(new String[] {this.cc,this.icd});
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<BetosLinks> CREATOR = new Parcelable.Creator<BetosLinks>() {
        public BetosLinks createFromParcel(Parcel in) {
            return new BetosLinks(in);
        }

        public BetosLinks[] newArray(int size) {
            return new BetosLinks[size];
        }
    };

    //takes a Parcel and craps out BetosLinks
    private BetosLinks(Parcel in) {
        String[] data = new String[2];
        in.readStringArray(data);
        this.cc = data[0];
        this.icd = data[1];
    }	
}
