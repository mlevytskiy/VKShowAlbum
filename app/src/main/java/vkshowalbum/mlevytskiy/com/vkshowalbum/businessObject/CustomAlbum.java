package vkshowalbum.mlevytskiy.com.vkshowalbum.businessObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKList;

/**
 * Created by max on 12.09.15.
 */
public class CustomAlbum implements Parcelable {

    public final CustomAlbumType type;
    public final VKList<VKApiPhoto> photos;
    public final String iconUrl;

    public CustomAlbum(CustomAlbumType type, VKList<VKApiPhoto> photos) {
        this.type = type;
        this.photos = photos;
        if (photos == null || photos.isEmpty()) {
            iconUrl = null;
        } else {
            int lastItemIndex = photos.size() - 1;
            iconUrl = photos.get(lastItemIndex).photo_130;
        }
    }

    public int getTitleId() {
        return type.getTitleId();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        photos.writeToParcel(dest, flags);
        dest.writeString(this.iconUrl);
    }

    private CustomAlbum(Parcel in) {
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : CustomAlbumType.values()[tmpType];
        this.photos = VKList.CREATOR.createFromParcel(in);
        this.iconUrl = in.readString();
    }

    public static final Parcelable.Creator<CustomAlbum> CREATOR = new Parcelable.Creator<CustomAlbum>() {
        public CustomAlbum createFromParcel(Parcel source) {
            return new CustomAlbum(source);
        }

        public CustomAlbum[] newArray(int size) {
            return new CustomAlbum[size];
        }
    };
}
