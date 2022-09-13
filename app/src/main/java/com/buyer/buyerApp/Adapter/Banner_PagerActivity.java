package com.buyer.buyerApp.Adapter;

import android.content.Context;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.buyer.buyerApp.ModelResult.BannerResult;
import com.buyer.buyerApp.R;


import java.util.List;


public class Banner_PagerActivity extends PagerAdapter {
    //private ArrayList<String> IMAGES;
    private static Integer[] IMAGES = {};
    private final LayoutInflater inflater;
    private final List<BannerResult> bannerRasponceList ;
    Context context;


    public Banner_PagerActivity(Context context, List<BannerResult> bannerResponcelist) {
        this.context = context;
        this.bannerRasponceList = bannerResponcelist;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return bannerRasponceList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.baner_pager, view, false);

        assert imageLayout != null;
        final ImageView imageView = imageLayout.findViewById(R.id.imgDisplay);

         String strImg = bannerRasponceList.get(position).getImage();
        Log.e("ATHUSKS",strImg);
//        Toast.makeText(context, strImg+"", Toast.LENGTH_SHORT).show();

        Glide.with(context).load(strImg)
                .thumbnail(0.5f)
                .into(imageView);

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}