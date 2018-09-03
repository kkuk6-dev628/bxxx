package cn.reservation.app.baixingxinwen.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.baiiu.filter.adapter.MenuAdapter;
import com.baiiu.filter.adapter.SimpleTextAdapter;
import com.baiiu.filter.interfaces.OnFilterDoneListener;
import com.baiiu.filter.interfaces.OnFilterItemClickListener;
import com.baiiu.filter.typeview.DoubleListView;
import com.baiiu.filter.typeview.SingleGridView;
import com.baiiu.filter.util.CommonUtil;
import com.baiiu.filter.util.UIUtil;
import com.baiiu.filter.view.FilterCheckedTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.dropdownmenu.entity.FilterType;
import cn.reservation.app.baixingxinwen.dropdownmenu.entity.FilterUrl;
//import com.baiiu.dropdownmenu.entity.FilterType;
//import com.baiiu.dropdownmenu.entity.FilterUrl;

/**
 * author: baiiu
 * date: on 16/1/17 21:14
 * description:
 */
public class DropMenuAdapter implements MenuAdapter {
    private final Context mContext;
    private OnFilterDoneListener onFilterDoneListener;
    private String[] titles;
    private JSONArray basicDataJsonArray;
    private int selectedColumnIndex = -1;

    public DropMenuAdapter(Context context, JSONArray list, OnFilterDoneListener onFilterDoneListener) {
        this.mContext = context;

        this.basicDataJsonArray = list;
        this.titles = new String[list.length() + 1];

        this.titles[0] = "重置";

        for(int i = 0; i < list.length(); i++){
            try {
                JSONObject item = list.getJSONObject(i);
                String title = item.optString("title");
                this.titles[i + 1] = title;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        this.onFilterDoneListener = onFilterDoneListener;
    }

    @Override
    public int getMenuCount() {
        return titles.length;
    }

    @Override
    public String getMenuTitle(int position) {
        return titles[position];
    }

    @Override
    public int getBottomMargin(int position) {
//        if (position == 3) {
//            return 0;
//        }

        return UIUtil.dp(mContext, 140);
    }

    @Override
    public View getView(int position, FrameLayout parentContainer) {
        View view = parentContainer.getChildAt(position);

        JSONObject item = null;
        try {
            if(position == 0){
                view = createDoubleListView(position);
            }
            else{
                item = this.basicDataJsonArray.getJSONObject(position-1);
                String type = item.optString("type");
                if(type.contains("region") || type.contains("more") || type.contains("number") || type.contains("text")){
                    view = createDoubleListView(position);
                }
                else{
                    view = createSingleGridView(position);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }



//        switch (position) {
//            case 0:
//                view = createSingleListView();
//                break;
//            case 1:
//                view = createDoubleListView();
//                break;
//            case 2:
//                view = createSingleGridView();
//                break;
//            case 3:
//                // view = createDoubleGrid();
//                view = createBetterDoubleGrid();
//                break;
//        }


        return view;
    }

    @Override
    public boolean hasChildInMainMenu(int position) {
        JSONObject item = null;
        if(position == 0){
            return false;
        }
        try {
            item = basicDataJsonArray.getJSONObject(position-1);
            String type = item.optString("type");
            if(type.contains("number") || type.contains("text")){
                return false;
            }
            else if(!type.contains("region") && !type.contains("more")){
                return item.has("choice");
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

//    private View createSingleListView() {
//        SingleListView<String> singleListView = new SingleListView<String>(mContext)
//                .adapter(new SimpleTextAdapter<String>(null, mContext) {
//                    @Override
//                    public String provideText(String string) {
//                        return string;
//                    }
//
//                    @Override
//                    protected void initCheckedTextView(FilterCheckedTextView checkedTextView) {
//                        int dp = UIUtil.dp(mContext, 15);
//                        checkedTextView.setPadding(dp, dp, 0, dp);
//                    }
//                })
//                .onItemClick(new OnFilterItemClickListener<String>() {
//                    @Override
//                    public void onItemClick(String item) {
//                        FilterUrl.instance().singleListPosition = item;
//
//                        FilterUrl.instance().position = 0;
//                        FilterUrl.instance().positionTitle = item;
//
//                        onFilterDone();
//                    }
//                });
//
//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < 10; ++i) {
//            list.add("" + i);
//        }
//        singleListView.setList(list, -1);
//
//        return singleListView;
//    }


    private View createDoubleListView(final int position) {
        DoubleListView<FilterType, String> comTypeDoubleListView = new DoubleListView<FilterType, String>(mContext)
                .leftAdapter(new SimpleTextAdapter<FilterType>(null, mContext) {
                    @Override
                    public String provideText(FilterType filterType) {
                        return filterType.desc;
                    }

                    @Override
                    protected void initCheckedTextView(FilterCheckedTextView checkedTextView) {
                        checkedTextView.setPadding(UIUtil.dp(mContext, 44), UIUtil.dp(mContext, 15), 0, UIUtil.dp(mContext, 15));
                    }
                })
                .rightAdapter(new SimpleTextAdapter<String>(null, mContext) {
                    @Override
                    public String provideText(String s) {
                        return s;
                    }

                    @Override
                    protected void initCheckedTextView(FilterCheckedTextView checkedTextView) {
                        checkedTextView.setPadding(UIUtil.dp(mContext, 30), UIUtil.dp(mContext, 15), 0, UIUtil.dp(mContext, 15));
                        checkedTextView.setBackgroundResource(android.R.color.white);
                    }
                })
                .onLeftItemClickListener(new DoubleListView.OnLeftItemClickListener<FilterType, String>() {
                    @Override
                    public List<String> provideRightList(FilterType item, int rowPosition) {
                        List<String> child = item.child;
                        if (CommonUtil.isEmpty(child)) {
                            FilterUrl.instance().doubleListLeft = item.desc;
                            FilterUrl.instance().doubleListRight = "";

                            FilterUrl.instance().columnPosition = position;
                            FilterUrl.instance().rowPosition = rowPosition;
                            FilterUrl.instance().positionTitle = item.desc;
                            FilterUrl.instance().itemPosition = -1;

                            onFilterDone(FilterUrl.instance().columnPosition, rowPosition, -1);
                        }

                        return child;
                    }
                })
                .onRightItemClickListener(new DoubleListView.OnRightItemClickListener<FilterType, String>() {
                    @Override
                    public void onRightItemClick(FilterType item, String string) {
                        FilterUrl.instance().doubleListLeft = item.desc;
                        FilterUrl.instance().doubleListRight = string;

                        FilterUrl.instance().columnPosition = position;
                        FilterUrl.instance().itemPosition = item.child.indexOf(string);
                        FilterUrl.instance().positionTitle = string;

                        // 이경우는 사용자가 두번째 메뉴를 누르지않고 직접 3번째 메뉴를 누르는 경우이다.
                        JSONObject item1 = null;
                        try {
                            item1 = basicDataJsonArray.getJSONObject(position - 1);

                            JSONObject choiceItem;
                            int rowIndex = -1;

                            String type = item1.optString("type");
                            if(type.contains("region")){
                                choiceItem = item1.getJSONObject("choice").getJSONObject("main");
                                for (int i = 0; i < choiceItem.length(); i++) {
                                    String name = choiceItem.optString(Integer.toString(i + 1));
                                    if (name.equals(item.desc)) {
                                        rowIndex = i;
                                        break;
                                    }
                                }
                            }
                            else if(type.contains("more")) {
                                JSONArray infoItem = item1.getJSONArray("info");
                                for(int i = 0; i < infoItem.length(); i++){
                                    String title = infoItem.getJSONObject(i).optString("title");
                                    if (title.equals(item.desc)) {
                                        rowIndex = i;
                                        break;
                                    }
                                }
                            }
                            else{
                                choiceItem = item1.getJSONObject("choice");

                                for (int i = 0; i < choiceItem.length(); i++) {
                                    String name = choiceItem.optString(Integer.toString(i + 1));
                                    if (name.equals(item.desc)) {
                                        rowIndex = i;
                                        break;
                                    }
                                }
                            }

                            FilterUrl.instance().rowPosition = rowIndex;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        onFilterDone(FilterUrl.instance().columnPosition, FilterUrl.instance().rowPosition, FilterUrl.instance().itemPosition);
                    }
                });


        List<FilterType> list = new ArrayList<>();

        // 만일 position 이 0이면 검색키를 초기화하는 단추이므로 child 가 없다.
        if(position > 0){
            try {
                JSONObject item = this.basicDataJsonArray.getJSONObject(position-1);

                String type = item.optString("type");
                if(type.contains("region")){
                    JSONObject choiceItem = item.getJSONObject("choice");
                    JSONObject mainItem = choiceItem.getJSONObject("main");
                    int mainAreaCount = mainItem.length();
                    for(int i = 0; i < mainAreaCount; i++){
                        //第一项
                        FilterType filterType = new FilterType();
                        String firstTitle = mainItem.optString(Integer.toString(i + 1));
                        filterType.desc = firstTitle;

                        // 만일 주메뉴이름이 연길이면 구체적인 지역을 현시해야 하므로
                        // 검사를 하고 연길이면 구체적인 지역을 child 로 매달아야 한다.
                        if(firstTitle.contains("延吉")){
                            //第二项
                            JSONObject yanjiItem = choiceItem.getJSONObject("yanji");
                            int yanjiAreaCount = yanjiItem.length();

                            if(yanjiAreaCount > 0){
                                List<String> childList = new ArrayList<>();
                                for (int j = 0; j < yanjiAreaCount; ++j) {
                                    childList.add(yanjiItem.optString(Integer.toString(j + 1)));
                                }
                                filterType.child = childList;
                            }

                        }
                        list.add(filterType);
                    }

                }
                else if(type.contains("more")){
                    JSONArray infoArray = item.getJSONArray("info");
                    int infoItemCount = infoArray.length();
                    for(int i = 0; i < infoItemCount; i++){
                        //第一项
                        JSONObject infoItem = infoArray.getJSONObject(i);
                        FilterType filterType = new FilterType();
                        filterType.desc = infoItem.optString("title");

                        String typeOfInfoItem = infoItem.optString("type");
                        // 만일 type 이 number 혹은 text 이면 검색하는 형식이 다르므로
                        if(!typeOfInfoItem.contains("number") && !typeOfInfoItem.contains("number")){
                            //第二项
                            JSONObject choiceItem = infoItem.getJSONObject("choice");
                            int choiceItemCount = choiceItem.length();

                            List<String> childList = new ArrayList<>();
                            for (int j = 0; j < choiceItemCount; ++j) {
                                childList.add(choiceItem.optString(Integer.toString(j + 1)));
                            }
                            filterType.child = childList;
                        }
                        list.add(filterType);
                    }

                }
                else if(!type.contains("number") && !type.contains("text") && !type.contains("click")){

                    JSONObject choiceItem = item.getJSONObject("choice");
                    int choiceItemCount = choiceItem.length();
                    for(int i = 0; i < choiceItemCount; i++) {
                        //第一项
                        FilterType filterType = new FilterType();
                        filterType.desc = choiceItem.optString(Integer.toString(i + 1));

                        list.add(filterType);
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            //初始化选中.
            if(list.size() > 0){
                comTypeDoubleListView.setLeftList(list, 0);
                comTypeDoubleListView.setRightList(list.get(0).child, -1);
            }

            comTypeDoubleListView.getLeftListView().setBackgroundColor(mContext.getResources().getColor(R.color.b_c_fafafa));

        }

        return comTypeDoubleListView;
//        return null;
    }


    private View createSingleGridView(final int position) {
        SingleGridView<String> singleGridView = new SingleGridView<String>(mContext)
                .adapter(new SimpleTextAdapter<String>(null, mContext) {
                    @Override
                    public String provideText(String s) {
                        return s;
                    }

                    @Override
                    protected void initCheckedTextView(FilterCheckedTextView checkedTextView) {
                        checkedTextView.setPadding(0, UIUtil.dp(context, 3), 0, UIUtil.dp(context, 3));
                        checkedTextView.setGravity(Gravity.CENTER);
                        checkedTextView.setBackgroundResource(R.drawable.selector_filter_grid);
                    }
                })
                .onItemClick(new OnFilterItemClickListener<String>() {
                    @Override
                    public void onItemClick(String item) {
                        FilterUrl.instance().singleGridPosition = item;
                        FilterUrl.instance().columnPosition = position;

                        try {
                            JSONObject item1 = basicDataJsonArray.getJSONObject(position-1);
                            JSONObject choiceItem = item1.getJSONObject("choice");

                            int rowIndex = -1;
                            for(int i = 0; i< choiceItem.length(); i++){
                                String name = choiceItem.optString(Integer.toString(i + 1));
                                if(name.equals(item)){
                                    rowIndex = i;
                                }
                            }
                            FilterUrl.instance().rowPosition = rowIndex;
                            FilterUrl.instance().positionTitle = item;
                            FilterUrl.instance().itemPosition = -1;

                            onFilterDone(FilterUrl.instance().columnPosition, FilterUrl.instance().rowPosition, -1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }





                    }
                });

        if(position > 0){

            try {
                JSONObject item = basicDataJsonArray.getJSONObject(position-1);
                List<String> list = new ArrayList<>();

                JSONObject choiceItem = item.getJSONObject("choice");
                int choiceItemCount = choiceItem.length();
                for(int i = 0; i < choiceItemCount; i++) {
                    //第一项
//                    FilterType filterType = new FilterType();
//                    filterType.desc = choiceItem.optString(Integer.toString(i + 1));

                    list.add(choiceItem.optString(Integer.toString(i + 1)));
                }

//                for (int i = 20; i < 39; ++i) {
//                    list.add(String.valueOf(i));
//                }
                singleGridView.setList(list, -1);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }




        return singleGridView;
    }


//    private View createBetterDoubleGrid() {
//
//        List<String> phases = new ArrayList<>();
//        for (int i = 0; i < 10; ++i) {
//            phases.add("3top" + i);
//        }
//        List<String> areas = new ArrayList<>();
//        for (int i = 0; i < 10; ++i) {
//            areas.add("3bottom" + i);
//        }
//
//
//        return new BetterDoubleGridView(mContext)
//                .setmTopGridData(phases)
//                .setmBottomGridList(areas)
//                .setOnFilterDoneListener(onFilterDoneListener)
//                .build();
//    }


//    private View createDoubleGrid() {
//        DoubleGridView doubleGridView = new DoubleGridView(mContext);
//        doubleGridView.setOnFilterDoneListener(onFilterDoneListener);
//
//
//        List<String> phases = new ArrayList<>();
//        for (int i = 0; i < 10; ++i) {
//            phases.add("3top" + i);
//        }
//        doubleGridView.setTopGridData(phases);
//
//        List<String> areas = new ArrayList<>();
//        for (int i = 0; i < 10; ++i) {
//            areas.add("3bottom" + i);
//        }
//        doubleGridView.setBottomGridList(areas);
//
//        return doubleGridView;
//    }

    @Override
    public void onFilterDone(int columnPosition, int rowPosition, int itemPosition) {

        if(rowPosition == -1){
            // 이 경우는 기본메뉴가 child 를 하나도 가지고 있지 않는 경우이다.
            FilterUrl.instance().columnPosition = columnPosition;
            FilterUrl.instance().positionTitle = titles[columnPosition];

            if(!hasChildInMainMenu(columnPosition)){
                onFilterDoneListener.onFilterDoneReturnPosition(columnPosition, -1, -1);
                return;
            }


//            if(columnPosition == 0){
//                onFilterDoneListener.onFilterDoneReturnPosition(0, -1, -1);
//            }
//            else{
//                if(!hasChildInMainMenu(columnPosition)){
//                    onFilterDoneListener.onFilterDoneReturnPosition(columnPosition, -1, -1);
//                    return;
//                }
//            }
            return;
        }

        onFilterDoneListener.onFilterDoneReturnPosition(FilterUrl.instance().columnPosition, FilterUrl.instance().rowPosition, FilterUrl.instance().itemPosition);

        if (onFilterDoneListener != null && FilterUrl.instance().columnPosition > -1) {
            onFilterDoneListener.onFilterDone(FilterUrl.instance().columnPosition, "", "");
        }
    }

}
