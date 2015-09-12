package vkshowalbum.mlevytskiy.com.vkshowalbum.adapter.albumsAdapterImpl;

import vkshowalbum.mlevytskiy.com.vkshowalbum.R;

/**
 * Created by max on 12.09.15.
 */
public enum CustomAlbumType {
    PhotosFromMyProfile {

        public int getTitleId() {
            return R.string.photos_from_my_profile;
        }
    },
    PhotosWithMe {

        public int getTitleId() {
            return R.string.photos_with_me;
        }

    },
    SavedPhotos {

        public int getTitleId() {
            return R.string.saved_photos;
        }

    },
    WallPhotos {

        public int getTitleId() {
            return R.string.wall_photos;
        }

    };

    abstract public int getTitleId();
}
