package us.xingkong.retrofitrealmpicload.presenter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import us.xingkong.retrofitrealmpicload.R;

/**
 * Created by Arcobaleno on 2017/7/8.
 */

public class GlideLoadPic {

    private Context context;
    private ImageView targetImageView;
    private String picResource;
    private int picResourceId;
    private int resourceType; //0则说明资源数据类型是String，1则是int

    public GlideLoadPic(Context context, ImageView targetImageView, String picResource, int resourceType) {
        this.context = context;
        this.targetImageView = targetImageView;
        this.picResource = picResource;
        this.resourceType = resourceType;
    }

    public GlideLoadPic(Context context, ImageView targetImageView, int picResourceId, int resourceType) {
        this.context = context;
        this.targetImageView = targetImageView;
        this.picResourceId = picResourceId;
        this.resourceType = resourceType;
    }

    public void LoadPic() {
        if (resourceType == 0) {
            Glide
                    .with(context)
                    .load(picResource)
                    .error(R.mipmap.ic_launcher)
                    .into(targetImageView);
        } else if (resourceType == 1) {
            Glide
                    .with(context)
                    .load(picResourceId)
                    .error(R.mipmap.ic_launcher)
                    .into(targetImageView);
        }
    }

}
