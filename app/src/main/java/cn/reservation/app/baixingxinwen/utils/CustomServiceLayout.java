package cn.reservation.app.baixingxinwen.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.activity.EnterpriseListActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by LiYin on 3/20/2017.
 */
public class CustomServiceLayout extends LinearLayout implements View.OnClickListener {

    private TextView mTxtTitle;
    private Resources res;
    private HashMap<String, JSONObject> serviceJsonObjectMap;
    private ArrayList<LinearLayout> linearLayouts = new ArrayList<LinearLayout>();
    private ArrayList<ImageView> customImageViews = new ArrayList<ImageView>();
//    private ArrayList<CustomServiceItem> customCategoryItems = new ArrayList<CustomServiceItem>();

    private int LinearCount = 0;
    private int ServiceItemCount = 0;
    private Context context;
    public AnimatedActivity pActivity;

    private LayoutParams linearVerticalLayoutParams;
    private LayoutParams linearHorizontalLayoutParams;
    private LayoutParams imageViewParams;

//    public CustomCategoryLayout(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }

    public CustomServiceLayout(Context context, JSONArray list, AnimatedActivity pActivity1) throws JSONException {
        super(context);

        this.context = context;
        pActivity = pActivity1;

        //params들을 먼저 정의한다.
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        android:layout_margin="0.5dp"
//        android:layout_weight="1"
//        android:background="@color/colorBackground"
//        android:clickable="true"
//        android:orientation="vertical"
//        android:paddingTop="0dp">
        linearVerticalLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.FILL_PARENT);
        int px = pxFromDp((float) 0.5);
        linearVerticalLayoutParams.setMargins(px,px,px,px);
        linearVerticalLayoutParams.height = LayoutParams.WRAP_CONTENT;
        linearVerticalLayoutParams.width = LayoutParams.MATCH_PARENT;
        linearVerticalLayoutParams.weight = 1;
//        android:layout_width="match_parent"
//        android:layout_height="80dp"
//        android:layout_margin="0.5dp"
//        android:layout_weight="1"
//        android:background="@color/colorBackground"
//        android:clickable="true"
//        android:orientation="horizontal"
//        android:paddingTop="10dp">
        linearHorizontalLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.FILL_PARENT);
        px = pxFromDp((float) 0.5);
        linearHorizontalLayoutParams.setMargins(px,px,px,px);
        linearHorizontalLayoutParams.height = pxFromDp(80);
        linearHorizontalLayoutParams.width = LayoutParams.MATCH_PARENT;
        linearHorizontalLayoutParams.weight = 1;
//        android:layout_width="wrap_content"
//        android:layout_height="80dp"
//        android:layout_alignParentBottom="true"
//        android:layout_alignParentRight="true"
//        android:layout_centerHorizontal="true"
//        android:layout_marginLeft="5dp"
//        android:layout_marginRight="5dp"
//        android:layout_weight="1"
//        android:adjustViewBounds="true"
//        android:background="@android:color/transparent"
//        android:scaleType="fitCenter"
//        android:src="@drawable/home_middle_banner3" />
        imageViewParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.FILL_PARENT);
        px = pxFromDp((float) 5);
        imageViewParams.setMargins(px,px,px,px);
        imageViewParams.height = pxFromDp(80);
        imageViewParams.width = LayoutParams.MATCH_PARENT;
        imageViewParams.weight = 1;



        this.setOrientation(LinearLayout.VERTICAL);
        this.setLayoutParams(linearVerticalLayoutParams);
        serviceJsonObjectMap =  new HashMap<String,JSONObject>();

        SharedPreferences prefs = context.getSharedPreferences("bxxx", MODE_PRIVATE);
        String service1 = prefs.getString("service1", null);

        // category1에 대한 자료가 없으면 서버로부터 자료를 하나도 다운하지 못한 상태이므로
        // linear는 6, item은 18개이다.
        if(service1 == null){
            LinearCount = 1;
            ServiceItemCount = 3;

//            customCategoryItems = new CustomServiceItem[ServiceItemCount];
//            linearLayouts = new LinearLayout[LinearCount];

            for(int i = 0; i < LinearCount; i++){
                LinearLayout l = new LinearLayout(this.getContext());
                l.setOrientation(LinearLayout.HORIZONTAL);
                l.setPadding(0, pxFromDp(5), 0, 0);
//                l.setWeightSum((float) 1.5);
                l.setLayoutParams(linearHorizontalLayoutParams);

                linearLayouts.add(l);
                this.addView(l);

                for(int j=0; j<3; j++){
                    ImageView imgView = new ImageView(this.getContext());
                    imgView.setLayoutParams(imageViewParams);
                    imgView.setClickable(true);
                    imgView.setImageResource(R.drawable.home_middle_banner2);
                    l.addView(imgView);
                    customImageViews.add(imgView);
                    imgView.setOnClickListener(this);
                }
            }
        }
        else{
            //만일 보관된자료가 있다면 그자료를 읽는다.
            LinearLayout l;
            l = new LinearLayout(this.getContext());
            while (true){
                String serviceData = prefs.getString("service" + Integer.toString(ServiceItemCount + 1), null);
                if(serviceData == null)
                    break;

                JSONObject serviceJsonObject = new JSONObject(serviceData);

                if(ServiceItemCount % 3 == 0){
                    l = new LinearLayout(this.getContext());
                    l.setOrientation(LinearLayout.HORIZONTAL);
                    l.setPadding(0, pxFromDp(5), 0, 0);
//                    l.setWeightSum((float) 1.5);
                    l.setLayoutParams(linearHorizontalLayoutParams);

                    this.addView(l);
                    linearLayouts.add(l);
                    LinearCount++;
                }

                String localImgUrl = serviceJsonObject.optString("imageLocalFilePath");

                ImageView imgView = new ImageView(this.getContext());
                imgView.setClickable(true);
                imgView.setLayoutParams(imageViewParams);
                Bitmap myBitmap = BitmapFactory.decodeFile(localImgUrl);
                imgView.setImageBitmap(myBitmap);

//                CustomServiceItem c = new CustomServiceItem(l.getContext(), catJsonObject, pActivity);
//                LayoutParams params = (LayoutParams) c.getLayoutParams();
//                params.width = 100;
                l.addView(imgView);
                customImageViews.add(imgView);
                ServiceItemCount++;

                imgView.setId(Integer.parseInt(serviceJsonObject.optString("id")));
                serviceJsonObjectMap.put(serviceJsonObject.optString("id"), serviceJsonObject);
                imgView.setOnClickListener(this);
            }
        }


//        res = context.getResources();



        if (list != null) {
            for(int i=0; i<list.length(); i++) {
                JSONObject item = list.getJSONObject(i);

                String item_page = item.optString("item_page");
                if(!item_page.equals("service"))
                    continue;

                String key = "ServiceKey" + item.optString("item_order");

                int order = Integer.parseInt(item.optString("item_order"));

                ImageView selectedImgView;

                //만일 order값이 item의 개수보다 더 크면 새로 추가해야 한다.
                if(order > ServiceItemCount){
                    String localImgUrl = item.optString("imageLocalFilePath");

                    selectedImgView = new ImageView(this.getContext());
                    selectedImgView.setLayoutParams(imageViewParams);
                    selectedImgView.setClickable(true);
                    Bitmap myBitmap = BitmapFactory.decodeFile(localImgUrl);
                    selectedImgView.setImageBitmap(myBitmap);
                    customImageViews.add(selectedImgView);
                    ServiceItemCount++;

                    selectedImgView.setId(Integer.parseInt(item.optString("id")));
                    serviceJsonObjectMap.put(item.optString("id"), item);
                    selectedImgView.setOnClickListener(this);

                    LinearLayout selectedLinear;
                    if(order > LinearCount * 3){
                        selectedLinear = new LinearLayout(this.getContext());
                        selectedLinear.setLayoutParams(linearHorizontalLayoutParams);
                        this.addView(selectedLinear);
                        linearLayouts.add(selectedLinear);
                        LinearCount++;
                    }
                    else{
                        int selectedLinearIndex = (order - 1) / 3;
                        selectedLinear = linearLayouts.get(linearLayouts.size() - 1);
                    }

                    selectedLinear.addView(selectedImgView);
                }
                else{
                    int selectedCategoryItemIndex = order - 1;
                    selectedImgView = customImageViews.get(selectedCategoryItemIndex);
                }



            }


        }
    }



    public void setCategoryItemData(int order, String imgUrl, JSONObject item){
        ImageView selectedImgView = customImageViews.get(order - 1);
        String localImgUrl = item.optString("imageLocalFilePath");
        Bitmap myBitmap = BitmapFactory.decodeFile(localImgUrl);
        selectedImgView.setImageBitmap(myBitmap);

        selectedImgView.setId(Integer.parseInt(item.optString("id")));
        serviceJsonObjectMap.put(item.optString("id"), item);
    }

    public int pxFromDp(final float dp) {
        return (int)(dp * context.getResources().getDisplayMetrics().density);
    }

    @Override
    public void onClick(View view) {
//        String title = (String) this.mTxtTitle.getText();
        ImageView imageView = (ImageView) view;

        String imageViewID = Integer.toString(imageView.getId());
        JSONObject jsonData = serviceJsonObjectMap.get(imageViewID);
//
        if(jsonData == null)
            return;

        String item_name = jsonData.optString("item_name");
        if(item_name == null || item_name.equals(""))
            return;
        Intent intent;
        switch (item_name){
            case "service_phone3"://便民电话-房屋维修
                intent = new Intent(this.context, EnterpriseListActivity.class);
                intent.putExtra("enterprise", "房屋维修");
                intent.putExtra("order", "3");
                pActivity.startChildActivity("activity_enterprise", intent);
                break;
            case "service_phone2"://便民电话-代驾
                intent = new Intent(this.context, EnterpriseListActivity.class);
                intent.putExtra("enterprise", "代驾");
                intent.putExtra("order", "2");
                pActivity.startChildActivity("activity_enterprise", intent);
                break;
            case "service_phone1"://便民电话-跑腿
                intent = new Intent(this.context, EnterpriseListActivity.class);
                intent.putExtra("enterprise", "跑腿");
                intent.putExtra("order", "1");
                pActivity.startChildActivity("activity_enterprise", intent);
                break;
            default:
                break;
        }
    }
}
