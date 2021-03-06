package cn.reservation.app.baixingxinwen.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import cn.reservation.app.baixingxinwen.dropdownmenu.entity.view.MultiGroupList.MultiGroupListView;
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
    private char[] childFlags;

    public DropMenuAdapter(Context context, JSONArray list, OnFilterDoneListener onFilterDoneListener) {
        this.mContext = context;

        this.basicDataJsonArray = list;
        this.titles = new String[list.length()];

        for (int i = 0; i < list.length(); i++) {
            try {
                JSONObject item = list.getJSONObject(i);
                String title = item.optString("title");
                this.titles[i] = title;
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

        return UIUtil.dp(mContext, 80);
    }

    @Override
    public View getView(int position, FrameLayout parentContainer) {
        View view = parentContainer.getChildAt(position);

        JSONObject item = null;
        try {
            item = this.basicDataJsonArray.getJSONObject(position);
            String type = item.optString("type");
            if (type.contains("region")) {
                view = createDoubleListView(position);
            } else if (type.contains("more")) {
                view = createMultiGroupListView(position);
            } else if (type.contains("number") || type.contains("text")) {
                String unit = item.optString("unit");

                final String option_id = item.optString("optionid");

                final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.dropdown_search, null);

                RelativeLayout rlt_price_search = (RelativeLayout) view.findViewById(R.id.rlt_price_search);
                ((EditText) view.findViewById(R.id.edit_min_price)).setHint("????????????" + unit + "???");
                ((EditText) view.findViewById(R.id.edit_max_price)).setHint("????????????" + unit + "???");

                final View finalView = view;
                rlt_price_search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view1) {
                        String min_price = ((EditText) finalView.findViewById(R.id.edit_min_price)).getText().toString();
                        String max_price = ((EditText) finalView.findViewById(R.id.edit_max_price)).getText().toString();
                        if (min_price.equals("")) {
                            Toast toast = Toast.makeText(mContext, "??????????????????", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 250);
                            ;
                            toast.show();
                            ((EditText) (finalView.findViewById(R.id.edit_min_price))).setFocusableInTouchMode(true);
                            ((EditText) (finalView.findViewById(R.id.edit_min_price))).requestFocus();
                            return;
                        }
                        if (max_price.equals("")) {
                            Toast toast = Toast.makeText(mContext, "??????????????????", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 250);
                            ;
                            toast.show();
                            ((EditText) (finalView.findViewById(R.id.edit_max_price))).setFocusableInTouchMode(true);
                            ((EditText) (finalView.findViewById(R.id.edit_max_price))).requestFocus();
                            return;
                        }
                        String price = min_price + "<->" + max_price;

                        onFilterDoneListener.onFilterDone(FilterUrl.instance().columnPosition, option_id, price);

                    }
                });
            } else {
                view = createSingleGridView(position);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    private View createMultiGroupListView(int position) {
        try {
            JSONObject data = this.basicDataJsonArray.getJSONObject(position);
            final MultiGroupListView view = new MultiGroupListView(mContext, data.getJSONArray("info"));
            view.setColumnPosition(position);
            view.setOnFilterDoneListener(new OnFilterDoneListener() {
                @Override
                public void onFilterDone(int position, String positionTitle, String urlValue) {
                    FilterUrl.instance().filterParams = view.getFilterParams();
                    DropMenuAdapter.this.onFilterDone(position, 0, 0);
                }

                @Override
                public void onFilterDoneReturnPosition(int columnPosition, int rowPosition, int itemPosition) {

                }
            });

            return view;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean hasChildInMainMenu(int position) {
        JSONObject item = null;
        try {
            item = basicDataJsonArray.getJSONObject(position);
            String type = item.optString("type");
            if (type.contains("number") || type.contains("text") || type.contains("click")) {
                return false;
            } else if (!type.contains("region") && !type.contains("more")) {
                return item.has("choice");
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

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

                        // ???????????? ???????????? ????????? ????????? ??????????????? ?????? 3?????? ????????? ????????? ????????????.
                        JSONObject item1 = null;
                        try {
                            item1 = basicDataJsonArray.getJSONObject(position);

                            JSONObject choiceItem;
                            int rowIndex = -1;

                            String type = item1.optString("type");
                            if (type.contains("region")) {
                                choiceItem = item1.getJSONObject("choice").getJSONObject("main");
                                for (int i = 0; i < choiceItem.length(); i++) {
                                    String name = choiceItem.optString(Integer.toString(i + 1));
                                    if (name.equals(item.desc)) {
                                        rowIndex = i;
                                        break;
                                    }
                                }
                            } else if (type.contains("more")) {
                                JSONArray infoItem = item1.getJSONArray("info");
                                for (int i = 0; i < infoItem.length(); i++) {
                                    String title = infoItem.getJSONObject(i).optString("title");
                                    if (title.equals(item.desc)) {
                                        rowIndex = i;
                                        break;
                                    }
                                }
                            } else {
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

        try {
            JSONObject item = this.basicDataJsonArray.getJSONObject(position);

            String type = item.optString("type");
            if (type.contains("region")) {
                JSONObject choiceItem = item.getJSONObject("choice");
                JSONObject mainItem = choiceItem.getJSONObject("main");
                int mainAreaCount = mainItem.length();
                for (int i = 0; i < mainAreaCount; i++) {
                    //?????????
                    FilterType filterType = new FilterType();
                    String firstTitle = mainItem.optString(Integer.toString(i + 1));
                    filterType.desc = firstTitle;

                    // ?????? ?????????????????? ???????????? ???????????? ????????? ???????????? ?????????
                    // ????????? ?????? ???????????? ???????????? ????????? child ??? ???????????? ??????.
                    if (firstTitle.contains("??????")) {
                        //?????????
                        JSONObject yanjiItem = choiceItem.getJSONObject("yanji");
                        int yanjiAreaCount = yanjiItem.length();

                        if (yanjiAreaCount > 0) {
                            List<String> childList = new ArrayList<>();
                            for (int j = 0; j < yanjiAreaCount; ++j) {
                                childList.add(yanjiItem.optString(Integer.toString(j + 1)));
                            }
                            filterType.child = childList;
                        }

                    }
                    list.add(filterType);
                }

            } else if (type.contains("more")) {
                JSONArray infoArray = item.getJSONArray("info");
                int infoItemCount = infoArray.length();
                for (int i = 0; i < infoItemCount; i++) {
                    //?????????
                    JSONObject infoItem = infoArray.getJSONObject(i);
                    FilterType filterType = new FilterType();
                    filterType.desc = infoItem.optString("title");

                    String typeOfInfoItem = infoItem.optString("type");
                    // ?????? type ??? number ?????? text ?????? ???????????? ????????? ????????????
                    if (!typeOfInfoItem.contains("number") && !typeOfInfoItem.contains("text")) {
                        //?????????
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

            } else if (!type.contains("number") && !type.contains("text") && !type.contains("click")) {

                JSONObject choiceItem = item.getJSONObject("choice");
                int choiceItemCount = choiceItem.length();
                for (int i = 0; i < choiceItemCount; i++) {
                    //?????????
                    FilterType filterType = new FilterType();
                    filterType.desc = choiceItem.optString(Integer.toString(i + 1));

                    list.add(filterType);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        //???????????????.
        if (list.size() > 0) {
            comTypeDoubleListView.setLeftList(list, 0);
            comTypeDoubleListView.setRightList(list.get(0).child, -1);
        }

        comTypeDoubleListView.getLeftListView().setBackgroundColor(mContext.getResources().getColor(R.color.b_c_fafafa));

        return comTypeDoubleListView;
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
                            JSONObject item1 = basicDataJsonArray.getJSONObject(position);
                            JSONObject choiceItem = item1.getJSONObject("choice");

                            int rowIndex = -1;
                            for (int i = 0; i < choiceItem.length(); i++) {
                                String name = choiceItem.optString(Integer.toString(i + 1));
                                if (name.equals(item)) {
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

        try {
            JSONObject item = basicDataJsonArray.getJSONObject(position);
            List<String> list = new ArrayList<>();

            JSONObject choiceItem = item.getJSONObject("choice");
            int choiceItemCount = choiceItem.length();
            for (int i = 0; i < choiceItemCount; i++) {
                //?????????
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


        return singleGridView;
    }


    @Override
    public void onFilterDone(int columnPosition, int rowPosition, int itemPosition) {

        if (rowPosition == -1) {

            // ??? ????????? ??????????????? child ??? ????????? ????????? ?????? ?????? ????????????.
            FilterUrl.instance().columnPosition = columnPosition;
            FilterUrl.instance().positionTitle = titles[columnPosition];

            if (!hasChildInMainMenu(columnPosition)) {
                onFilterDoneListener.onFilterDoneReturnPosition(columnPosition, -1, -1);
                return;
            }

            return;
        }

        onFilterDoneListener.onFilterDoneReturnPosition(FilterUrl.instance().columnPosition, rowPosition, itemPosition);

    }

}
