package vkshowalbum.mlevytskiy.com.vkshowalbum.adapter.albumsAdapterImpl;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vk.sdk.api.model.VKApiPhotoAlbum;

import vkshowalbum.mlevytskiy.com.vkshowalbum.R;
import vkshowalbum.mlevytskiy.com.vkshowalbum.businessObject.CustomAlbum;
import vkshowalbum.mlevytskiy.com.vkshowalbum.businessObject.Album;
import vkshowalbum.mlevytskiy.com.vkshowalbum.imageLoader.ImageLoader;

/**
 * Created by max on 12.09.15.
 */
public class ViewHolder {

    private ImageView icon;
    private TextView title;
    private ImageLoader imageLoader;

    public ViewHolder(View view, ImageLoader imageLoader) {
        icon = (ImageView) view.findViewById(R.id.image_view);
        title = (TextView) view.findViewById(R.id.title);
        this.imageLoader = imageLoader;
    }

    public void fill(Album item) {
        if (item.isCustomAlbum()) {
            fill(item.getCustomAlbum());
        } else {
            fill(item.getVkApiPhotoAlbum());
        }
    }

    private void fill(CustomAlbum customAlbum) {
        imageLoader.loadImage(icon, customAlbum.iconUrl);
        title.setText(customAlbum.getTitleId());
    }

    private void fill(VKApiPhotoAlbum album) {
        imageLoader.loadImage(icon, album.thumb_src);
        title.setText(album.title);
    }

}
