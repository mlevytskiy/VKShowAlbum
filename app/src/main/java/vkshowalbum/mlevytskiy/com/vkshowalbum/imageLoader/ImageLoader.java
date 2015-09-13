package vkshowalbum.mlevytskiy.com.vkshowalbum.imageLoader;

import android.content.*;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import vkshowalbum.mlevytskiy.com.vkshowalbum.R;

/**
 * Created by max on 11.09.15.
 */
public class ImageLoader {

    public static final int IMAGE_LOADER_TAG_KEY = R.id.image_loader;

    private Picasso picasso;

    public ImageLoader(Context context) {
        picasso = Picasso.with(context);
    }

    public void loadImage(ImageView imageView, String url) {
        loadImage(imageView, url, null);
    }

    public void loadImage(final ImageView imageView, String url, final Callback callback) {
        if (TextUtils.isEmpty(url)) {
            imageView.setImageDrawable(null);
            return;
        }

        final ImageView.ScaleType defaultScaleType = imageView.getScaleType();
        imageView.setTag(IMAGE_LOADER_TAG_KEY, ImageLoadingState.loading);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        picasso.load(url)
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_try_again)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        imageView.setScaleType(defaultScaleType);
                        imageView.setTag(IMAGE_LOADER_TAG_KEY, ImageLoadingState.success);
                        if (callback != null) {
                            callback.onSuccess();
                        }
                    }

                    @Override
                    public void onError() {
                        imageView.setTag(IMAGE_LOADER_TAG_KEY, ImageLoadingState.error);
                        if (callback != null) {
                            callback.onError();
                        }
                    }
                });
    }



}
