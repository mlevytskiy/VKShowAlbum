package vkshowalbum.mlevytskiy.com.vkshowalbum.support;

import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;

import java.util.concurrent.TimeUnit;

/**
 * Created by max on 12.09.15.
 */
public class ToastFactory {

    public SuperActivityToast createProgressToast(AppCompatActivity activity, @StringRes int stringId) {
        final SuperActivityToast superActivityToast = new SuperActivityToast(activity, SuperToast.Type.PROGRESS);
        superActivityToast.setText(activity.getString(stringId));
        int VERY_LONG_DURATION = (int) TimeUnit.MINUTES.toMillis(2);
        superActivityToast.setDuration(VERY_LONG_DURATION);
        return superActivityToast;
    }

    public SuperActivityToast createErrorToast(AppCompatActivity activity, @StringRes int stringId) {
        SuperActivityToast superActivityToastError = new SuperActivityToast(activity, SuperToast.Type.STANDARD);
        superActivityToastError.setBackground(SuperToast.Background.RED);
        superActivityToastError.setDuration(SuperToast.Duration.MEDIUM);
        superActivityToastError.setText(activity.getString(stringId));
        return superActivityToastError;
    }

}
