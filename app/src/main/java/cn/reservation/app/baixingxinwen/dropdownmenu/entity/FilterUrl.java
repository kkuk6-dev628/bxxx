package cn.reservation.app.baixingxinwen.dropdownmenu.entity;

import android.text.TextUtils;

import java.util.HashMap;

/**
 * 描述：
 */
public class FilterUrl {
    private volatile static FilterUrl filterUrl;

    private FilterUrl() {
    }

    public static FilterUrl instance() {
        if (filterUrl == null) {
            synchronized (FilterUrl.class) {
                if (filterUrl == null) {
                    filterUrl = new FilterUrl();
                }
            }
        }
        return filterUrl;
    }

    public String singleListPosition;
    public String doubleListLeft;
    public String doubleListRight;
    public String singleGridPosition;
    public String doubleGridTop;
    public String doubleGridBottom;

    public HashMap<String, Object> filterParams;

    public int columnPosition = -1;
    public int rowPosition = -1;
    public int itemPosition = -1;
    public String positionTitle;

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();

        if (!TextUtils.isEmpty(singleListPosition)) {
            buffer.append("singleListPosition=");
            buffer.append(singleListPosition);
            buffer.append("\n");
        }


        if (!TextUtils.isEmpty(doubleListLeft)) {
            buffer.append("doubleListLeft=");
            buffer.append(doubleListLeft);
            buffer.append("\n");
        }

        if (!TextUtils.isEmpty(doubleListRight)) {
            buffer.append("doubleListRight=");
            buffer.append(doubleListRight);
            buffer.append("\n");
        }

        if (!TextUtils.isEmpty(singleGridPosition)) {
            buffer.append("singleGridPosition=");
            buffer.append(singleGridPosition);
            buffer.append("\n");
        }

        if (!TextUtils.isEmpty(doubleGridTop)) {
            buffer.append("doubleGridTop=");
            buffer.append(doubleGridTop);
            buffer.append("\n");
        }

        if (!TextUtils.isEmpty(doubleGridBottom)) {
            buffer.append("doubleGridBottom=");
            buffer.append(doubleGridBottom);
            buffer.append("\n");
        }

        return buffer.toString();
    }

    public void clear() {
        filterUrl = null;
    }


}