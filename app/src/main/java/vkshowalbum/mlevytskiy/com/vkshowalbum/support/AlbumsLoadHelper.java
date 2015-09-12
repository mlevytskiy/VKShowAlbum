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

import vkshowalbum.mlevytskiy.com.vkshowalbum.adapter.albumsAdapterImpl.CustomAlbum;
import vkshowalbum.mlevytskiy.com.vkshowalbum.adapter.albumsAdapterImpl.CustomAlbumType;

/**
 * Created by max on 12.09.15.
 */
public class AlbumsLoadHelper {

    public void load(final int userId, final Callback callback) {
        VKRequest userAlbumsRequest = new VKRequest("photos.getAlbums", VKParameters.from(VKApiConst.OWNER_ID, userId, "need_covers", 1));
        VKRequest photoWitMyRequest = new VKRequest("photos.getUserPhotos", VKParameters.from(VKApiConst.OWNER_ID, userId));
        VKRequest profilePhotoRequest = new VKRequest("photos.get", VKParameters.from(VKApiConst.OWNER_ID,
                userId, VKApiConst.ALBUM_ID, "profile"));
        VKRequest savedPhotoRequest = new VKRequest("photos.get", VKParameters.from(VKApiConst.OWNER_ID,
                userId, VKApiConst.ALBUM_ID, "saved"));

        VKRequest wallPhotosRequest = new VKRequest("photos.get", VKParameters.from(VKApiConst.OWNER_ID,
                userId, VKApiConst.ALBUM_ID, "wall"));

        VKBatchRequest vkBatchRequest = new VKBatchRequest(userAlbumsRequest, photoWitMyRequest, profilePhotoRequest, savedPhotoRequest, wallPhotosRequest);

        vkBatchRequest.executeWithListener(new VKBatchRequest.VKBatchRequestListener() {
            @Override
            public void onComplete(VKResponse[] responses) {
                super.onComplete(responses);
                List<CustomAlbum> albums = new ArrayList<>();
                addAlbum(responses[1], albums, CustomAlbumType.PhotosWithMe);
                addAlbum(responses[2], albums, CustomAlbumType.PhotosFromMyProfile);
                addAlbum(responses[3], albums, CustomAlbumType.SavedPhotos);
                addAlbum(responses[4], albums, CustomAlbumType.WallPhotos);
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
