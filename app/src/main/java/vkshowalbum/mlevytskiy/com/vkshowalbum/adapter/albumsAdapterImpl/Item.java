package vkshowalbum.mlevytskiy.com.vkshowalbum.adapter.albumsAdapterImpl;

import com.vk.sdk.api.model.VKApiPhotoAlbum;

/**
 * Created by max on 12.09.15.
 */
public class Item {

    private VKApiPhotoAlbum vkApiPhotoAlbum;
    private CustomAlbum customAlbum;
    private boolean isCustomAlbum = false;

    public Item(VKApiPhotoAlbum vkApiPhotoAlbum) {
        this.vkApiPhotoAlbum = vkApiPhotoAlbum;
        isCustomAlbum = false;
    }

    public Item(CustomAlbum customAlbum) {
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
}
