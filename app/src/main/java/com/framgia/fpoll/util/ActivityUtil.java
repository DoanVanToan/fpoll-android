package com.framgia.fpoll.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;
import java.util.List;
import java.util.Random;

import static com.framgia.fpoll.util.Constant.DataConstant.DATA_PLASH;
import static com.framgia.fpoll.util.Constant.DataConstant.NUMBER_SPACE;

/**
 * Created by tuanbg on 2/9/17.
 */
public class ActivityUtil {
    private final static int NUMBER_RANDOM = 4;
    private final static String TEXT_TEMPLATE = "abcdefghijklmnopqrstuvwxyz0123456789";

    public static void addFragment(@NonNull FragmentManager fragmentManager,
            @NonNull Fragment fragment, int frameId) {
        fragmentManager.beginTransaction().replace(frameId, fragment).commit();
    }

    public static String byString(List<String> data) {
        StringBuilder message = new StringBuilder();
        for (String msg : data) {
            message.append(msg).append("\n");
        }
        return message.toString();
    }

    public static void showToast(Context context, int res) {
        if (context == null) return;
        Toast.makeText(context, res, Toast.LENGTH_LONG).show();
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static String subLinkPoll(String pollLink) {
        return pollLink.substring(pollLink.lastIndexOf(DATA_PLASH) + NUMBER_SPACE);
    }

    public static String randomString() {
        StringBuilder textResult = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < NUMBER_RANDOM; i++) {
            textResult.append(TEXT_TEMPLATE.charAt(random.nextInt(TEXT_TEMPLATE.length())));
        }
        return textResult.toString();
    }

    public static String getPathFromUri(Context context, Uri uri) {
        if (context == null || uri == null) return null;
        String result = null;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            result = uri.getPath();
        } else {
            if (cursor.moveToFirst()) {
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
                cursor.close();
            }
        }
        return result;
    }
}
