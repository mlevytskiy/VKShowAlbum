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
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPhotoAlbum;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import java.util.List;

import vkshowalbum.mlevytskiy.com.vkshowalbum.adapter.AlbumsAdapter;
import vkshowalbum.mlevytskiy.com.vkshowalbum.businessObject.CustomAlbum;
import vkshowalbum.mlevytskiy.com.vkshowalbum.imageLoader.ImageLoader;
import vkshowalbum.mlevytskiy.com.vkshowalbum.imageLoader.ImageLoadingState;
import vkshowalbum.mlevytskiy.com.vkshowalbum.support.AlbumsLoadHelper;
import vkshowalbum.mlevytskiy.com.vkshowalbum.support.ToastFactory;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    private AlbumsAdapter albumsAdapter;
    private int userId;

    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = ((GridView) findViewById(R.id.grid_view));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.this.onItemClick(view, position);
            }
        });
        if (!VKSdk.isLoggedIn()) {
            VKSdk.login(this, App.VK_SCOPE);
        } else {
            loadingAlbums();
        }
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
        VKRequest request = VKApi.users().get();
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                userId = ((VKApiUserFull) ((VKList) response.parsedModel).get(0)).getId();
                new AlbumsLoadHelper().load(userId, new AlbumsLoadHelper.Callback() {
                    @Override
                    public void onSuccess(List<CustomAlbum> customAlbums, VKList<VKApiPhotoAlbum> userAlbums) {
                        albumsAdapter = new AlbumsAdapter(getBaseContext(), userAlbums, customAlbums);
                        gridView.setEmptyView(findViewById(android.R.id.empty));
                        gridView.setAdapter(albumsAdapter);
                        superActivityToast.dismiss();
                    }

                    @Override
                    public void onError(VKError error) {
                        MainActivity.this.onError(superActivityToast);
                    }
                });
                super.onComplete(response);
            }

            public void onError(VKError error) {
                MainActivity.this.onError(superActivityToast);
            }
        });

    }

    private void onError(SuperActivityToast progress) {
        progress.dismiss();
        new ToastFactory().createErrorToast(this, R.string.error).show();
    }

}
