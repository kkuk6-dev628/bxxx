package cn.reservation.app.baixingxinwen.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.activity.AdverViewActivity;
import cn.reservation.app.baixingxinwen.activity.HomeActivity;
import cn.reservation.app.baixingxinwen.activity.SearchActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;

public class HomeSliderAdapter extends PagerAdapter{
    final Context context;
    int images[];
    LayoutInflater layoutInflater;
    String[] imageUrls;
    String[] linkUrls;
    ImageView[] imageViews;
    Bitmap[] imageBmps;

    public int selectedIndex = -1;


    public HomeSliderAdapter(Context context, String images[], String links[]) {
        this.context = context;
        this.imageUrls = images;
        linkUrls = links;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setImageUrls(String[] imgUrls){
        this.imageUrls = imgUrls;
    }

    public void setimages(int[] imgUrls){
        this.images = imgUrls;
    }

    public void setImagesBMP(Bitmap[] imgBmps){
        this.imageBmps = imgBmps;
    }

    @Override
    public int getCount() {
        if(this.imageUrls == null)
            return images.length;
        return imageUrls.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.slider_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.homeImageView);
        if(this.imageUrls==null || this.imageUrls.length == 0){
//            imageView.setImageResource(images[position]);
        }
        else{
//            Bitmap imgBmp = imageBmps[position];
//            if(imgBmp != null)
//                imageView.setImageBitmap(imgBmp);
            String imgUrl = imageUrls[position];
            Picasso
                    .with(this.context)
                    .load(imgUrl)
                    .centerCrop()
//                    .placeholder(R.drawable.default_img)
                    .resize(CommonUtils.getPixelValue(this.context, 256), CommonUtils.getPixelValue(this.context, 95))
                    .into(imageView);
            selectedIndex = position;
        }

        container.addView(itemView);

        //listening to image click
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == -1)
                    return;
//                Intent mainIntent = new Intent(HomeActivity.this, RoomDetailActivity.class);
//                HomeActivity.this.startActivity(mainIntent);
//                HomeActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                Intent intent = new Intent(HomeActivity.homeActivity, AdverViewActivity.class);
                intent.putExtra("adver_url", linkUrls[position]);
//                popUp.dismiss();
                HomeActivity.homeActivity.startActivityForResult(intent,501);
                //Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}