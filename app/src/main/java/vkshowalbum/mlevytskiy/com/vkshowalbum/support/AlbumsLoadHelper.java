package vkshowalbum.mlevytskiy.com.vkshowalbum.support;

import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKBatchRequest;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKApiPhotoAlbum;
import com.vk.sdk.api.model.VKList;

import java.util.ArrayList;
import java.util.List;

import vkshowalbum.mlevytskiy.com.vkshowalbum.businessObject.CustomAlbum;
import vkshowalbum.mlevytskiy.com.vkshowalbum.businessObject.CustomAlbumType;

/**
 * Created by max on 12.09.15.
 */
public class AlbumsLoadHelper {

    public void load(final int userId, final Callback callback) {
        VKRequest userAlbumsRequest = new VKRequest("photos.getAlbums", VKParameters.from(VKApiConst.OWNER_ID, userId, "need_covers", 1, "need_system", 1));
        VKRequest photoWitMeRequest = new VKRequest("photos.getUserPhotos", VKParameters.from(VKApiConst.OWNER_ID, userId));

        VKBatchRequest vkBatchRequest = new VKBatchRequest(userAlbumsRequest, photoWitMeRequest);

        vkBatchRequest.executeWithListener(new VKBatchRequest.VKBatchRequestListener() {
            @Override
            public void onComplete(VKResponse[] responses) {
                super.onComplete(responses);
                List<CustomAlbum> albums = new ArrayList<>();
                addAlbum(responses[1], albums, CustomAlbumType.PhotosWithMe);
                callback.onSuccess(albums, new VKList<VKApiPhotoAlbum>(responses[0].json, VKApiPhotoAlbum.class));
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                callback.onError(error);
            }
        });
    }

    private void addAlbum(VKResponse vkResponse, List<CustomAlbum> albums, CustomAlbumType type) {
        VKList<VKApiPhoto> photos = new VKList<VKApiPhoto>(vkResponse.json, VKApiPhoto.class);
        if (!photos.isEmpty()) {
            CustomAlbum album = new CustomAlbum(type, photos);
            albums.add(album);
        }
    }

    public interface Callback {
        void onSuccess(List<CustomAlbum> customAlbums, VKList<VKApiPhotoAlbum> userAlbums);
        void onError(VKError error);
    }

}
