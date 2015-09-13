package vkshowalbum.mlevytskiy.com.vkshowalbum.support;

import android.text.TextUtils;

import com.vk.sdk.api.model.VKApiPhoto;

/**
 * Created by max on 13.09.15.
 */
public class PhotoUtils {

    public static String getMaxPhotoSize(VKApiPhoto photo) {
        if (!TextUtils.isEmpty(photo.photo_2560)) {
            return photo.photo_2560;
        } else if (!TextUtils.isEmpty(photo.photo_1280)) {
            return photo.photo_1280;
        } else if (!TextUtils.isEmpty(photo.photo_807)) {
            return photo.photo_807;
        } else if (!TextUtils.isEmpty(photo.photo_604)) {
            return photo.photo_604;
        } else if (!TextUtils.isEmpty(photo.photo_130)) {
            return photo.photo_130;
        } else if (!TextUtils.isEmpty(photo.photo_75)) {
            return photo.photo_75;
        }
        return "";
    }

}
