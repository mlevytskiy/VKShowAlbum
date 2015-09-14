package vkshowalbum.mlevytskiy.com.vkshowalbum;

import android.app.Application;
import android.content.*;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;

/**
 * Created by max on 10.09.15.
 */
public class App extends Application {

    public static final String[] VK_SCOPE = new String[]{VKScope.PHOTOS};

    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken == null) {
                VKSdk.logout();
                Intent intent = new Intent(App.this, AlbumsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    };

    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);
        vkAccessTokenTracker.startTracking();
    }

}
