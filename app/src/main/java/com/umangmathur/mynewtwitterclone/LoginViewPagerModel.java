package com.umangmathur.mynewtwitterclone;

import android.os.Parcel;
import android.os.Parcelable;

public class LoginViewPagerModel implements Parcelable {
    int resourceImage;
    int backgroundColor;
    String displayText;

    public LoginViewPagerModel(int image, int backgroundColor, String displayText) {
        this.resourceImage = image;
        this.backgroundColor = backgroundColor;
        this.displayText = displayText;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getResourceImage() {
        return resourceImage;
    }

    public String getDisplayText() {
        return displayText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.resourceImage);
        dest.writeInt(this.backgroundColor);
        dest.writeString(this.displayText);
    }

    protected LoginViewPagerModel(Parcel in) {
        this.resourceImage = in.readInt();
        this.backgroundColor = in.readInt();
        this.displayText = in.readString();
    }

    public static final Creator<LoginViewPagerModel> CREATOR = new Creator<LoginViewPagerModel>() {
        public LoginViewPagerModel createFromParcel(Parcel source) {
            return new LoginViewPagerModel(source);
        }

        public LoginViewPagerModel[] newArray(int size) {
            return new LoginViewPagerModel[size];
        }
    };
}
