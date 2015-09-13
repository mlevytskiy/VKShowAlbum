package vkshowalbum.mlevytskiy.com.vkshowalbum;

import android.content.*;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;

import uk.co.senab.photoview.PhotoViewAttacher;
import vkshowalbum.mlevytskiy.com.vkshowalbum.imageLoader.ImageLoader;

/**
 * Created by max on 13.09.15.
 */
public class PhotoActivity extends AppCompatActivity {

    public static final String URL = "url";
    public static final String TITLE = "title";

    @CallSuper
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_photo);
        final ImageView photo = (ImageView) findViewById(R.id.photo);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        photo.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        if (intent != null) {
            setTitle(intent.getStringExtra(TITLE));
            String url = intent.getStringExtra(URL);
            new ImageLoader(this).loadImage(photo, url, new Callback() {
                @Override
                public void onSuccess() {
                    new PhotoViewAttacher(photo);
                    photo.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError() {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
