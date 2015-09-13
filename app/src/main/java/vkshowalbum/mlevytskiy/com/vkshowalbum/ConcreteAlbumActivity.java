package vkshowalbum.mlevytskiy.com.vkshowalbum;

import android.content.*;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKList;

import vkshowalbum.mlevytskiy.com.vkshowalbum.adapter.AlbumAdapter;
import vkshowalbum.mlevytskiy.com.vkshowalbum.businessObject.Album;
import vkshowalbum.mlevytskiy.com.vkshowalbum.support.ToastFactory;

/**
 * Created by max on 12.09.15.
 */
public class ConcreteAlbumActivity extends AppCompatActivity {

    public static final String ALBUM = "album";
    public static final String USER_ID = "userId";
    private GridView gridView;
    private Album album;
    private int userId;
    private int albumId;

    @CallSuper
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_main);
        gridView = (GridView) findViewById(R.id.grid_view);
        Intent intent = getIntent();
        if (intent != null) {
            album = intent.getParcelableExtra(ALBUM);
            if (album.isCustomAlbum()) {
                gridView.setAdapter(new AlbumAdapter(getBaseContext(), album.getCustomAlbum().photos));
            } else {
                userId = intent.getIntExtra(USER_ID, -1);
                albumId = album.getVkApiPhotoAlbum().getId();
                loadPhotos(albumId, userId);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadPhotos(int albumId, int userId) {
        final SuperActivityToast progress = new ToastFactory().createProgressToast(this, R.string.load_photos);
        progress.show();
        VKRequest photoRequest = new VKRequest("photos.get", VKParameters.from(VKApiConst.OWNER_ID,
                userId, VKApiConst.ALBUM_ID, albumId));
        photoRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                gridView.setEmptyView(findViewById(android.R.id.empty));
                gridView.setAdapter(new AlbumAdapter(getBaseContext(), new VKList<VKApiPhoto>(response.json, VKApiPhoto.class)));
                progress.dismiss();
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                progress.dismiss();
                new ToastFactory().createErrorToast(ConcreteAlbumActivity.this, R.string.error).show();
            }
        });
    }

    public void onClickRefresh(View view) {
        if (album.isCustomAlbum()) {
            //do nothing
        } else {
            gridView.setEmptyView(null);
            gridView.setAdapter(null);
            loadPhotos(albumId, userId);
        }
    }

}
