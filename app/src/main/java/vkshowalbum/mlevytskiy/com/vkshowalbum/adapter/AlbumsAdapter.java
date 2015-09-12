package vkshowalbum.mlevytskiy.com.vkshowalbum.adapter;

import android.content.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.vk.sdk.api.model.VKApiPhotoAlbum;
import com.vk.sdk.api.model.VKList;

import java.util.ArrayList;
import java.util.List;

import vkshowalbum.mlevytskiy.com.vkshowalbum.R;
import vkshowalbum.mlevytskiy.com.vkshowalbum.adapter.albumsAdapterImpl.CustomAlbum;
import vkshowalbum.mlevytskiy.com.vkshowalbum.adapter.albumsAdapterImpl.Item;
import vkshowalbum.mlevytskiy.com.vkshowalbum.adapter.albumsAdapterImpl.ViewHolder;
import vkshowalbum.mlevytskiy.com.vkshowalbum.imageLoader.ImageLoader;

/**
 * Created by max on 11.09.15.
 */
public class AlbumsAdapter extends BaseAdapter {

    private List<Item> items;
    private LayoutInflater layoutInflater;
    private ImageLoader imageLoader;


    public AlbumsAdapter(Context context, VKList<VKApiPhotoAlbum> vkList, List<CustomAlbum> customAlbums) {
        layoutInflater = LayoutInflater.from(context);
        imageLoader = new ImageLoader(context);

        items = new ArrayList<>();
        for (CustomAlbum customAlbum : customAlbums) {
            items.add(new Item(customAlbum));
        }
        for (VKApiPhotoAlbum vkApiPhotoAlbum : vkList) {
            items.add(new Item(vkApiPhotoAlbum));
        }
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Item getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item, parent, false);
            viewHolder = new ViewHolder(convertView, imageLoader);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.fill(getItem(position));
        return convertView;
    }

    public void loadIcon(ImageView imageView, int position) {
        Item item = getItem(position);
        imageLoader.loadImage(imageView, item.isCustomAlbum() ? item.getCustomAlbum().iconUrl :
                                                                item.getVkApiPhotoAlbum().thumb_src);
    }
}
