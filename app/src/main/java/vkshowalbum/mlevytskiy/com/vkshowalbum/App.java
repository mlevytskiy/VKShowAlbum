package vkshowalbum.mlevytskiy.com.vkshowalbum;

import android.app.Application;

import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;

/**
 * Created by max on 10.09.15.
 */
public class App extends Application {

    public static final String[] VK_SCOPE = new String[]{VKScope.PHOTOS};

    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);
    }

}
