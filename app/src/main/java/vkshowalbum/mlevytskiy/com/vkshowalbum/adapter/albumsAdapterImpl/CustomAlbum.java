package vkshowalbum.mlevytskiy.com.vkshowalbum.adapter.albumsAdapterImpl;

import com.vk.sdk.api.model.VKApiPhoto;
import java.util.List;

/**
 * Created by max on 12.09.15.
 */
public class CustomAlbum {

    public final CustomAlbumType type;
    public final List<VKApiPhoto> photos;
    public final String iconUrl;

    public CustomAlbum(CustomAlbumType type, List<VKApiPhoto> photos) {
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
}
