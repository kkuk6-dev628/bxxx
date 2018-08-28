package cn.reservation.app.baixingxinwen.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.activity.HomeActivity;
import cn.reservation.app.baixingxinwen.utils.CustomCategoryItem;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by LiYin on 3/20/2017.
 */
public class CustomCategoryLayout extends LinearLayout {

    private TextView mTxtTitle;
    private Resources res;
    private HashMap<String, JSONObject> categoryJsonObjectMap;
    private ArrayList<LinearLayout> linearLayouts = new ArrayList<LinearLayout>();
    private ArrayList<CustomCategoryItem> customCategoryItems = new ArrayList<CustomCategoryItem>();

    private int LinearCount = 0;
    private int CategoryItemCount = 0;
    private Context context;

//    public CustomCategoryLayout(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }

    public CustomCategoryLayout(Context context, JSONArray list, AnimatedActivity pActivity) throws JSONException {
        super(context);

        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_category_linear_layout, this, true);
        this.setOrientation(LinearLayout.VERTICAL);
        this.setPadding(0, 20, 0, 20);
//        this.setPadding(10,0,10,0);
//        LayoutParams par = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
//                LayoutParams.FILL_PARENT);
//        par.setMargins(10,10,10,10);
//        this.setLayoutParams(par);
//        customCategoryItems = new CustomCategoryItem[18];
//        linearLayouts = new LinearLayout[6];
        categoryJsonObjectMap =  new HashMap<String,JSONObject>();

        SharedPreferences prefs = context.getSharedPreferences("bxxx", MODE_PRIVATE);
        String category1 = prefs.getString("category1", null);

        // category1에 대한 자료가 없으면 서버로부터 자료를 하나도 다운하지 못한 상태이므로
        // linear는 6, item은 18개이다.
        if(category1 == null){
            LinearCount = 6;
            CategoryItemCount = 18;

//            customCategoryItems = new CustomCategoryItem[CategoryItemCount];
//            linearLayouts = new LinearLayout[LinearCount];

            for(int i = 0; i < LinearCount; i++){
                LinearLayout l = new LinearLayout(this.getContext());
                l.setOrientation(LinearLayout.HORIZONTAL);
                    l.setPadding(20, 0, 20, 0);
                l.setWeightSum((float) 1.5);
                LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.FILL_PARENT);
                params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                params.height = LinearLayout.LayoutParams.WRAP_CONTENT;


                l.setLayoutParams(params);
                linearLayouts.add(l);
                this.addView(l);

                for(int j=0; j<3; j++){
                    CustomCategoryItem c = new CustomCategoryItem(l.getContext(), null, pActivity);
                    l.addView(c);
                    customCategoryItems.add(c);
                }
            }
        }
        else{
            //만일 보관된자료가 있다면 그자료를 읽는다.
            LinearLayout l;
            l = new LinearLayout(this.getContext());
            while (true){
                String catData = prefs.getString("category" + Integer.toString(CategoryItemCount + 1), null);
                if(catData == null)
                    break;

                JSONObject catJsonObject = new JSONObject(catData);

                if(CategoryItemCount % 3 == 0){
                    l = new LinearLayout(this.getContext());
                    l.setOrientation(LinearLayout.HORIZONTAL);
                    l.setPadding(20, 0, 20, 0);
                    l.setWeightSum((float) 1.5);
                    LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                            LayoutParams.FILL_PARENT);
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    params.height = LinearLayout.LayoutParams.WRAP_CONTENT;


                    l.setLayoutParams(params);

                    this.addView(l);
                    linearLayouts.add(l);
                    LinearCount++;
                }

                CustomCategoryItem c = new CustomCategoryItem(l.getContext(), catJsonObject, pActivity);
//                LayoutParams params = (LayoutParams) c.getLayoutParams();
//                params.width = 100;
                l.addView(c);
                customCategoryItems.add(c);
                CategoryItemCount++;
            }
        }


//        res = context.getResources();



        if (list != null) {
            for(int i=0; i<list.length(); i++) {
                JSONObject item = list.getJSONObject(i);

                String item_page = item.optString("item_page");
                if(!item_page.equals("cat_button"))
                    continue;

                String key = "CatKey" + item.optString("item_order");
                categoryJsonObjectMap.put(key, item);

                int order = Integer.parseInt(item.optString("item_order"));

                CustomCategoryItem selectedCategoryItem;

                //만일 order값이 item의 개수보다 더 크면 새로 추가해야 한다.
                if(order > CategoryItemCount){
                    selectedCategoryItem = new CustomCategoryItem(context, item, pActivity);
                    customCategoryItems.add(selectedCategoryItem);
                    CategoryItemCount++;

                    LinearLayout selectedLinear;
                    if(order > LinearCount * 3){
                        selectedLinear = new LinearLayout(this.getContext());
                        this.addView(selectedLinear);
                        linearLayouts.add(selectedLinear);
                        LinearCount++;
                    }
                    else{
                        int selectedLinearIndex = (order - 1) / 3;
                        selectedLinear = linearLayouts.get(linearLayouts.size() - 1);
                    }

                    selectedLinear.addView(selectedCategoryItem);
                }
                else{
                    int selectedCategoryItemIndex = order - 1;
                    selectedCategoryItem = customCategoryItems.get(selectedCategoryItemIndex);
                }



            }


        }
    }



    public void setCategoryItemData(int order, String imgUrl, JSONObject item){
        CustomCategoryItem selectedCategoryItem = customCategoryItems.get(order - 1);
        selectedCategoryItem.setJsonData(item);

        ImageView imgView = selectedCategoryItem.getmImageView();

//        Picasso
//                .with(this.context)
//                .load(imgUrl)
//                .centerCrop()
////                    .placeholder(R.drawable.default_img)
//                .resize(CommonUtils.getPixelValue(this.context, 100), CommonUtils.getPixelValue(this.context, 95))
//                .into(imgView);
//
//        if(f.exists()){
//
//            Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
//            imgView.setImageBitmap(myBitmap);
//
//        }
        Bitmap myBitmap = BitmapFactory.decodeFile(imgUrl);
        imgView.setImageBitmap(myBitmap);

        TextView txtTitle = selectedCategoryItem.getmTxtTitle();
        String title = item.optString("item_label");
        txtTitle.setText(title);
    }

}
