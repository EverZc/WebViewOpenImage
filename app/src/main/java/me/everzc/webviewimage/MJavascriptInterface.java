package me.everzc.webviewimage;

import android.content.Context;
import android.content.Intent;
import android.util.Log;



/**
 * 选择相册列表
 * Created by Zwj on 2019/09/06.
 */

public class MJavascriptInterface {
    private Context context;
    private String[] imageUrls;

    public MJavascriptInterface(Context context, String[] imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @android.webkit.JavascriptInterface
    public void openImage(String img) {
        for (int i = 0; i < imageUrls.length; i++) {
            Log.e("图片地址"+i,imageUrls[i].toString());
        }
        Intent intent = new Intent();
        intent.putExtra("imageUrls", imageUrls);
        intent.putExtra("curImageUrl", img);
        intent.setClass(context, PhotoBrowseActivity.class);
        context.startActivity(intent);

    }
}
