package vkshowalbum.mlevytskiy.com.vkshowalbum;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.vk.sdk.VKSdk;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!VKSdk.isLoggedIn()) {
            VKSdk.login(this, App.VK_SCOPE);
        }
    }

}
