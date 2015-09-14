package vkshowalbum.mlevytskiy.com.vkshowalbum.adapter;

import android.content.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKList;

import java.util.List;

import vkshowalbum.mlevytskiy.com.vkshowalbum.R;
import vkshowalbum.mlevytskiy.com.vkshowalbum.imageLoader.ImageLoader;

/**
 * Created by max on 12.09.15.
 */
public class AlbumAdapter extends BaseAdapter {

    private List<VKApiPhoto> photos;
    private LayoutInflater layoutInflater;
    private ImageLoader imageLoader;

    public AlbumAdapter(Context context, VKList<VKApiPhoto> photos) {
        layoutInflater = LayoutInflater.from(context);
        imageLoader = new ImageLoader(context);
        this.photos = photos;
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public VKApiPhoto getItem(int position) {
        return photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_photo, parent, false);
        }
        imageLoader.loadImage((ImageView) convertView, photos.get(position).photo_130);
        return convertView;
    }

    public void loadIcon(ImageView imageView, int position) {
        VKApiPhoto item = getItem(position);
        imageLoader.loadImage(imageView, item.photo_130);
    }

}
