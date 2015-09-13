package vkshowalbum.mlevytskiy.com.vkshowalbum.businessObject;

import vkshowalbum.mlevytskiy.com.vkshowalbum.R;

/**
 * Created by max on 12.09.15.
 */
public enum CustomAlbumType {

    PhotosWithMe {

        public int getTitleId() {
            return R.string.photos_with_me;
        }

    };

    abstract public int getTitleId();
}
