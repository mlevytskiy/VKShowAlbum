package vkshowalbum.mlevytskiy.com.vkshowalbum.businessObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.vk.sdk.api.model.VKApiPhotoAlbum;

/**
 * Created by max on 12.09.15.
 */
public class Album implements Parcelable {

    private VKApiPhotoAlbum vkApiPhotoAlbum;
    private CustomAlbum customAlbum;
    private boolean isCustomAlbum = false;

    public Album(VKApiPhotoAlbum vkApiPhotoAlbum) {
        this.vkApiPhotoAlbum = vkApiPhotoAlbum;
        isCustomAlbum = false;
    }

    public Album(CustomAlbum customAlbum) {
        this.customAlbum = customAlbum;
        isCustomAlbum = true;
    }

    public VKApiPhotoAlbum getVkApiPhotoAlbum() {
        return vkApiPhotoAlbum;
    }

    public CustomAlbum getCustomAlbum() {
        return customAlbum;
    }

    public boolean isCustomAlbum() {
        return isCustomAlbum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.vkApiPhotoAlbum, 0);
        dest.writeParcelable(this.customAlbum, 0);
        dest.writeByte(isCustomAlbum ? (byte) 1 : (byte) 0);
    }

    private Album(Parcel in) {
        this.vkApiPhotoAlbum = in.readParcelable(VKApiPhotoAlbum.class.getClassLoader());
        this.customAlbum = in.readParcelable(CustomAlbum.class.getClassLoader());
        this.isCustomAlbum = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Album> CREATOR = new Parcelable.Creator<Album>() {
        public Album createFromParcel(Parcel source) {
            return new Album(source);
        }

        public Album[] newArray(int size) {
            return new Album[size];
        }
    };
}
