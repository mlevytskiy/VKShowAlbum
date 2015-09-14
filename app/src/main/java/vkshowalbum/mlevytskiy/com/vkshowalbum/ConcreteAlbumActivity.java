package vkshowalbum.mlevytskiy.com.vkshowalbum;

import android.content.*;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import vkshowalbum.mlevytskiy.com.vkshowalbum.support.PhotoUtils;
import vkshowalbum.mlevytskiy.com.vkshowalbum.support.ToastFactory;

/**
 * Created by max on 12.09.15.
 */
public class ConcreteAlbumActivity extends AppCompatActivity {

    public static final String ALBUM = "album";
    public static final String USER_ID = "userId";
    private static final String PHOTOS = "photos";
    private static final String TITLE = "title";

    private GridView gridView;
    private Album album;
    private int userId;
    private int albumId;
    private AlbumAdapter adapter;

    private VKList<VKApiPhoto> photos;

    @CallSuper
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_concrete_album);
        gridView = (GridView) findViewById(R.id.grid_view);
        setListenerOnGridView();
        if (bundle != null) {
            //do nothing
        } else {
            Intent intent = getIntent();
            if (intent != null) {
                album = intent.getParcelableExtra(ALBUM);
                if (album.isCustomAlbum()) {
                    adapter = new AlbumAdapter(getBaseContext(), album.getCustomAlbum().photos);
                    setTitle(album.getCustomAlbum().getTitleId());
                    gridView.setAdapter(adapter);
                } else {
                    userId = intent.getIntExtra(USER_ID, -1);
                    albumId = album.getVkApiPhotoAlbum().getId();
                    loadPhotos(albumId, userId);
                    setTitle(album.getVkApiPhotoAlbum().title);
                }

            }
        }
    }

    private void setListenerOnGridView() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ConcreteAlbumActivity.this, PhotoActivity.class);
                VKApiPhoto vkApiPhoto = adapter.getItem(position);
                String url = PhotoUtils.getMaxPhotoSize(vkApiPhoto);
                intent.putExtra(PhotoActivity.TITLE, vkApiPhoto.text);
                intent.putExtra(PhotoActivity.URL, url);
                startActivity(intent);
            }
        });
    }

    @CallSuper
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        album = savedInstanceState.getParcelable(ALBUM);
        userId = savedInstanceState.getInt(USER_ID);
        albumId = album.isCustomAlbum() ? 0 : album.getVkApiPhotoAlbum().getId();
        photos = savedInstanceState.getParcelable(PHOTOS);

        adapter = album.isCustomAlbum() ? new AlbumAdapter(getBaseContext(), album.getCustomAlbum().photos) :
                                          new AlbumAdapter(getBaseContext(), photos);
        gridView.setAdapter(adapter);

        setTitle(savedInstanceState.getString(TITLE));
    }

    @CallSuper
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(ALBUM, album);
        savedInstanceState.putInt(USER_ID, userId);
        savedInstanceState.putParcelable(PHOTOS, photos);
        savedInstanceState.putString(TITLE, getTitle().toString());
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
                photos = new VKList<VKApiPhoto>(response.json, VKApiPhoto.class);
                adapter = new AlbumAdapter(getBaseContext(), photos);
                gridView.setAdapter(adapter);
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
