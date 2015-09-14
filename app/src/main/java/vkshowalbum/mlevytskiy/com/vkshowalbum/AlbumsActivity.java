package vkshowalbum.mlevytskiy.com.vkshowalbum;

import android.content.*;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.model.VKApiPhotoAlbum;
import com.vk.sdk.api.model.VKList;

import java.util.ArrayList;
import java.util.List;

import vkshowalbum.mlevytskiy.com.vkshowalbum.adapter.AlbumsAdapter;
import vkshowalbum.mlevytskiy.com.vkshowalbum.businessObject.CustomAlbum;
import vkshowalbum.mlevytskiy.com.vkshowalbum.imageLoader.ImageLoader;
import vkshowalbum.mlevytskiy.com.vkshowalbum.imageLoader.ImageLoadingState;
import vkshowalbum.mlevytskiy.com.vkshowalbum.support.AlbumsLoadHelper;
import vkshowalbum.mlevytskiy.com.vkshowalbum.support.GetUserIdCallback;
import vkshowalbum.mlevytskiy.com.vkshowalbum.support.ToastFactory;

public class AlbumsActivity extends AppCompatActivity {

    private static final String USER_ALBUM = "userAlbum";
    private static final String CUSTOM_ALBUMS = "customAlbums";
    private static final String USER_ID = "userID";

    private GridView gridView;
    private AlbumsAdapter albumsAdapter;
    private int userId;

    private VKList<VKApiPhotoAlbum> userAlbums;
    private ArrayList<CustomAlbum> customAlbums;

    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = ((GridView) findViewById(R.id.grid_view));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlbumsActivity.this.onItemClick(view, position);
            }
        });
        if (savedInstanceState != null) {
            //do nothing
        } else {
            if (!VKSdk.isLoggedIn()) {
                VKSdk.login(this, App.VK_SCOPE);
            } else {
                loadingAlbums();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                loadingAlbums();
            }
            @Override
            public void onError(VKError error) {
                new ToastFactory().createErrorToast(AlbumsActivity.this, R.string.error).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @CallSuper
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        userAlbums = savedInstanceState.getParcelable(USER_ALBUM);
        customAlbums = savedInstanceState.getParcelableArrayList(CUSTOM_ALBUMS);
        userId = savedInstanceState.getInt(USER_ID);

        if (customAlbums != null || userAlbums != null) {
            albumsAdapter = new AlbumsAdapter(getBaseContext(), userAlbums, customAlbums);
            gridView.setAdapter(albumsAdapter);
        }
    }

    @CallSuper
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(USER_ALBUM, userAlbums);
        savedInstanceState.putParcelableArrayList(CUSTOM_ALBUMS, customAlbums);
        savedInstanceState.putInt(USER_ID, userId);
    }

    private void onItemClick(View view, int position) {
        ImageView imageView = (ImageView) view.findViewById(R.id.image_view);

        ImageLoadingState imageLoadingState = (ImageLoadingState) imageView.getTag(ImageLoader.IMAGE_LOADER_TAG_KEY);
        if (imageLoadingState == ImageLoadingState.error) {
            albumsAdapter.loadIcon(imageView, position);
        } else {
            Intent intent = new Intent(this, ConcreteAlbumActivity.class);
            intent.putExtra(ConcreteAlbumActivity.ALBUM, albumsAdapter.getItem(position));
            intent.putExtra(ConcreteAlbumActivity.USER_ID, userId);
            startActivity(intent);
        }
    }

    public void onClickRefresh(View view) {
        gridView.setEmptyView(null);
        gridView.setAdapter(null);
        loadingAlbums();
    }

    private void loadingAlbums() {
        final SuperActivityToast superActivityToast = new ToastFactory().createProgressToast(this, R.string.load_albums);
        superActivityToast.show();
        AlbumsLoadHelper albumsLoadHelper = new AlbumsLoadHelper();
        AlbumsLoadHelper.Callback callback = new AlbumsLoadHelper.Callback() {

            @Override
            public void onSuccess(List<CustomAlbum> customAlbums, VKList<VKApiPhotoAlbum> userAlbums) {
                AlbumsActivity.this.userAlbums = userAlbums;
                AlbumsActivity.this.customAlbums = new ArrayList<CustomAlbum>(customAlbums);
                albumsAdapter = new AlbumsAdapter(getBaseContext(), userAlbums, customAlbums);
                gridView.setEmptyView(findViewById(android.R.id.empty));
                gridView.setAdapter(albumsAdapter);
                superActivityToast.dismiss();
            }

            @Override
            public void onError(VKError error) {
                AlbumsActivity.this.onError(superActivityToast);
            }
        };

        if (userId == 0) {
            albumsLoadHelper.loadWithoutUserId(callback, new GetUserIdCallback() {
                @Override
                public void onSuccess(int userId) {
                    AlbumsActivity.this.userId = userId;
                }
            });
        } else {
            albumsLoadHelper.load(userId, callback);
        }

    }

    private void onError(SuperActivityToast progress) {
        progress.dismiss();
        new ToastFactory().createErrorToast(this, R.string.error).show();
    }

}
