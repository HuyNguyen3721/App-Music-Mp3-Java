package com.example.appplaymusicmp3.customdatabinding;

import android.widget.ImageView;
import android.widget.TextView;
import androidx.databinding.BindingAdapter;
import com.bumptech.glide.Glide;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class Untils {
    private static  Untils instance;
    static {
        try{
            instance =  new Untils();
        }catch (Exception e){

        }
    }
    public static Untils getInstance() {
        return instance;
    }

    @BindingAdapter("setImage")
    public static void setImage(ImageView iv,String uri ){
        Glide.with(iv.getContext()).load(uri).into(iv);
    }
    @BindingAdapter("setText")
    public static void setText(TextView tv , String content ){
        tv.setText(content);
    }
    @BindingAdapter("setCrlImage")
    public static void  setCrlImage(CircleImageView iv  ,  String uri ){
        Glide.with(iv.getContext())
                .load(uri)
                .circleCrop()
                .into(iv);
    }
    @BindingAdapter("setbg_image_blur")
    public static void setbg_image_blur(  ImageView iv  ,  String uri ){
        Glide.with(iv.getContext()).load(uri).transform(new BlurTransformation(35,3))
                .into(iv);
    }


}
