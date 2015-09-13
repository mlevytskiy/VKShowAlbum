package vkshowalbum.mlevytskiy.com.vkshowalbum.adapter;

import android.content.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKList;

import java.util.ArrayList;
import java.util.List;

import vkshowalbum.mlevytskiy.com.vkshowalbum.R;
import vkshowalbum.mlevytskiy.com.vkshowalbum.imageLoader.ImageLoader;

/**
 * Created by max on 12.09.15.
 */
public class AlbumAdapter extends BaseAdapter {

    private List<String> photoUrls;
    private LayoutInflater layoutInflater;
    private ImageLoader imageLoader;

    public AlbumAdapter(Context context, VKList<VKApiPhoto> photos) {
        layoutInflater = LayoutInflater.from(context);
        imageLoader = new ImageLoader(context);
        photoUrls = getStrPhotos(photos);
    }
    
    private List<String> getStrPhotos(List<VKApiPhoto> photos) {
        List<String> array = new ArrayList<>();
        for (VKApiPhoto vkApiPhoto : photos) {
            array.add(vkApiPhoto.photo_130);
        }
        return array;
    }

    @Override
    public int getCount() {
        return photoUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return photoUrls.get(position);
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
        imageLoader.loadImage((ImageView) convertView, photoUrls.get(position));
        return convertView;
    }

}
