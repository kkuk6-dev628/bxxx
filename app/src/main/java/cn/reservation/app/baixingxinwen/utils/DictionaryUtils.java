package cn.reservation.app.baixingxinwen.utils;

import org.json.JSONObject;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Random;

/**
 * Created by LiYin on 3/14/2017.
 */
public class DictionaryUtils {
    private Dictionary fid;
    private Dictionary typeid;
    private Dictionary appfldname;

    public DictionaryUtils() {
        this.fid = new Hashtable();
        this.typeid = new Hashtable();
        this.appfldname = new Hashtable();
    }
    public void setProperty(JSONObject item){
        appfldname.put("txt_property1","");
        appfldname.put("txt_property2","");
        appfldname.put("txt_property3","");
        appfldname.put("txt_home_favor_price","");
        appfldname.put("txt_type","");
        appfldname.put("txt_post_time",item.optString("dateline"));
        appfldname.put("default_image_name", "");
        appfldname.put("has_img_property1", "0");
        appfldname.put("has_img_property2", "0");
        appfldname.put("has_img_property3", "0");
        appfldname.put("is_no_image", "0");
        appfldname.put("phone_number", "");
        appfldname.put("fid", item.optString("fid"));
        appfldname.put("sortid", item.optString("sortid"));

        if(item.optJSONObject("fields")==null)
            return;

        String proVal1 = "";
        String proVal2 = "";
        String proVal3 = "";
        String priceVal = "";
        String typeVal = "";

        JSONObject fieldsJsonObject = item.optJSONObject("fields");
        String sortid = item.optString("sortid");

        switch(sortid){
            case "1"://房产信
            case "4"://房产信息
                proVal1 = fieldsJsonObject.optString("region");
                if(proVal1 != null && !proVal1.equals(""))
                    proVal1 = proVal1.trim();

                String floors = fieldsJsonObject.optString("floors");
                if(floors != null && !floors.equals(""))
                    floors = floors + "楼";
                else
                    floors = "";

                String square = fieldsJsonObject.optString("square");
                if(square != null && !square.equals(""))
                    square = square + "㎡";
                else
                    square = "";

                proVal2 = floors + " " + square;
                proVal3 = fieldsJsonObject.optString("house_number");
                priceVal = fieldsJsonObject.optString("price_label");

                appfldname.put("has_img_property1", "1");
                break;
            case "2"://车辆交易
            case "58"://车辆交易
                proVal1 = fieldsJsonObject.optString("car_type");
                proVal1 = proVal1.trim();
                proVal2 = fieldsJsonObject.optString("years_estimate_label");
                proVal3 = fieldsJsonObject.optString("car_speed_label");
                priceVal = fieldsJsonObject.optString("price_label");
                break;
            case "3":// 二手买卖-new
            case "29":
                proVal1 = fieldsJsonObject.optString("region");
                if(proVal1 != null && !proVal1.equals(""))
                    proVal1 = proVal1.trim();

                proVal2 = fieldsJsonObject.optString("type");
                if(proVal2 != null && !proVal2.equals(""))
                    proVal2 = proVal2.trim();
                proVal3 = fieldsJsonObject.optString("level");
                if(proVal3 != null && !proVal3.equals(""))
                    proVal3 = proVal3.trim();
                priceVal = fieldsJsonObject.optString("price_label");

                appfldname.put("has_img_property1", "1");
                break;
            case "7":
            case "8":
            case "28": //求职简历-new
            case "53": //招聘信息-new
                proVal1 = fieldsJsonObject.optString("region");
                if(proVal1 != null && !proVal1.equals(""))
                    proVal1 = proVal1.trim();

                proVal2 = fieldsJsonObject.optString("level");
                if(proVal2 != null && !proVal2.equals(""))
                    proVal2 = proVal2.trim();
                proVal3 = fieldsJsonObject.optString("experience");
                if(proVal3 != null && !proVal3.equals(""))
                    proVal3 = "经验:" + proVal3.trim();
                priceVal = fieldsJsonObject.optString("salary_range");

                String s = fieldsJsonObject.optString("salary_range_label");
                String[] s_array = s.split(":");
                if(s_array.length > 1)
                    typeVal = s_array[0];

                appfldname.put("has_img_property1", "1");
                appfldname.put("has_img_property2", "1");
                appfldname.put("has_img_property3", "1");

                appfldname.put("is_no_image", "1");
                break;
            case "13"://便民服务
            case "34"://便民服务
                proVal1 = fieldsJsonObject.optString("region");
                if(proVal1 != null && !proVal1.equals(""))
                    proVal1 = proVal1.trim();

                proVal2 = fieldsJsonObject.optString("type");
                if(proVal2 != null && !proVal2.equals(""))
                    proVal2 = proVal2.trim();
                appfldname.put("has_img_property1", "1");
                appfldname.put("phone_number", fieldsJsonObject.optString("phone"));
                appfldname.put("default_image_name", getDefaultImageName(7, "bianmin"));
                break;
            case "15"://婚姻交友
            case "16"://婚姻交友
                proVal1 = fieldsJsonObject.optString("region");
                if(proVal1 != null && !proVal1.equals(""))
                    proVal1 = proVal1.trim();

                proVal2 = fieldsJsonObject.optString("type");
                if(proVal2 != null && !proVal2.equals(""))
                    proVal2 = proVal2.trim();
                proVal3 = fieldsJsonObject.optString("sex");
                if(proVal3 != null && !proVal3.equals(""))
                    proVal3 = "经验:" + proVal3.trim();
                priceVal = fieldsJsonObject.optString("age");
                if(priceVal != null && !priceVal.equals(""))
                    priceVal = priceVal + "岁";

                appfldname.put("has_img_property1", "1");

                String sex = fieldsJsonObject.optString("sex");
                if(sex.trim().equals("男"))
                    appfldname.put("default_image_name", "man");
                else if (sex.trim().equals("女"))
                    appfldname.put("default_image_name", "women");

                break;
            case "21"://教育培训
                proVal1 = fieldsJsonObject.optString("region");
                if(proVal1 != null && !proVal1.equals(""))
                    proVal1 = proVal1.trim();
                appfldname.put("has_img_property1", "1");

                proVal2 = fieldsJsonObject.optString("type");
                if(proVal2 != null && !proVal2.equals(""))
                    proVal2 = proVal2.trim();
                proVal3 = fieldsJsonObject.optString("period");
                if(proVal3 != null && !proVal3.equals(""))
                    proVal3 = proVal3.trim();
                priceVal = fieldsJsonObject.optString("price");

                String s1 = fieldsJsonObject.optString("price_label");
                String[] s_array1 = s1.split(":");
                if(s_array1.length > 1)
                    typeVal = s_array1[0];

                appfldname.put("default_image_name", getDefaultImageName(12, "jiaoyunpeixun"));

                break;
            case "22"://宠物天地
                proVal1 = fieldsJsonObject.optString("region");
                if(proVal1 != null && !proVal1.equals(""))
                    proVal1 = proVal1.trim();
                appfldname.put("has_img_property1", "1");

                proVal2 = fieldsJsonObject.optString("type");
                if(proVal2 != null && !proVal2.equals(""))
                    proVal2 = proVal2.trim();
                proVal3 = fieldsJsonObject.optString("source");
                if(proVal3 != null && !proVal3.equals(""))
                    proVal3 = proVal3.trim();
                priceVal = fieldsJsonObject.optString("price_label");
                break;
            case "24"://出国资讯
                proVal1 = fieldsJsonObject.optString("region");
                if(proVal1 != null && !proVal1.equals(""))
                    proVal1 = proVal1.trim();
                appfldname.put("has_img_property1", "1");

                proVal2 = fieldsJsonObject.optString("type");
                if(proVal2 != null && !proVal2.equals(""))
                    proVal2 = proVal2.trim();
                appfldname.put("phone_number", fieldsJsonObject.optString("phone"));
                appfldname.put("default_image_name", getDefaultImageName(12, "chuguozixun"));
                break;
            case "91"://同城换物
                if(item.optJSONObject("fields")!=null){
                    appfldname.put("txt_property1","");
                    appfldname.put("txt_property2",item.optJSONObject("fields").optString("region_label"));
                    appfldname.put("txt_property3",item.optJSONObject("fields").optString("type_label"));
                    appfldname.put("txt_home_favor_price","");
                }
                break;
            case "108"://家教专栏
                if(item.optJSONObject("fields")!=null){
                    appfldname.put("txt_property1",item.optJSONObject("fields").optString("region_label"));
                    appfldname.put("txt_property2",item.optJSONObject("fields").optString("type_label"));
                    appfldname.put("txt_property3",item.optJSONObject("fields").optString("education_label"));
                    appfldname.put("txt_home_favor_price","");
                }
                break;
            case "33"://出兑求兑
                proVal1 = fieldsJsonObject.optString("region");
                if(proVal1 != null && !proVal1.equals(""))
                    proVal1 = proVal1.trim();
                appfldname.put("has_img_property1", "1");

                String square1 = fieldsJsonObject.optString("square");
                if(square1 == null || square1.equals(""))
                    square1 = "";
                else
                    square1 = square1 + "㎡";

                proVal2 = fieldsJsonObject.optString("type");
                proVal3 = square1;
                priceVal = fieldsJsonObject.optString("price_label");

                appfldname.put("has_img_property1", "1");
                break;
            case "40"://电话号码
            case "41"://电话号码
                proVal1 = fieldsJsonObject.optString("region");
                if(proVal1 != null && !proVal1.equals(""))
                    proVal1 = proVal1.trim();
                appfldname.put("has_img_property1", "1");

                proVal2 = fieldsJsonObject.optString("company");
                if(proVal2 != null && !proVal2.equals(""))
                    proVal2 = proVal2.trim();
                proVal3 = fieldsJsonObject.optString("type");
                if(proVal3 != null && !proVal3.equals(""))
                    proVal3 = proVal3.trim();
                priceVal = fieldsJsonObject.optString("price_label");
                appfldname.put("default_image_name", getDefaultImageName(12, "dianhuahaoma"));
                break;
            case "60"://招商加盟
                proVal1 = fieldsJsonObject.optString("region");
                if(proVal1 != null && !proVal1.equals(""))
                    proVal1 = proVal1.trim();
                appfldname.put("has_img_property1", "1");

                proVal2 = fieldsJsonObject.optString("type");
                if(proVal2 != null && !proVal2.equals(""))
                    proVal2 = proVal2.trim();
                proVal3 = fieldsJsonObject.optString("group");
                if(proVal3 != null && !proVal3.equals(""))
                    proVal3 = proVal3.trim();
                priceVal = fieldsJsonObject.optString("price_label");
                appfldname.put("default_image_name", getDefaultImageName(7, "zhaoshang"));
                break;
            case "61"://旅游专栏
                proVal1 = fieldsJsonObject.optString("region");
                if(proVal1 != null && !proVal1.equals(""))
                    proVal1 = proVal1.trim();
                appfldname.put("has_img_property1", "1");

                proVal2 = fieldsJsonObject.optString("type");
                if(proVal2 != null && !proVal2.equals(""))
                    proVal2 = proVal2.trim();
                proVal3 = fieldsJsonObject.optString("order");
                if(proVal3 != null && !proVal3.equals(""))
                    proVal3 = proVal3.trim();
                break;
            case "54":// 打折促销
                proVal1 = fieldsJsonObject.optString("region");
                if(proVal1 != null && !proVal1.equals(""))
                    proVal1 = proVal1.trim();
                appfldname.put("has_img_property1", "1");

                proVal2 = fieldsJsonObject.optString("type");
                if(proVal2 != null && !proVal2.equals(""))
                    proVal2 = proVal2.trim();
                break;
        }

        appfldname.put("txt_property1",proVal1);
        appfldname.put("txt_property2",proVal2);
        appfldname.put("txt_property3",proVal3);
        appfldname.put("txt_home_favor_price",priceVal);
        appfldname.put("txt_type",typeVal);
    }
    public String getProperty(String property){
        return appfldname.get(property).toString();
    }

    public void setFid(Object key, Object val) {
        this.fid.put(key,val);
    }

    public Object getFid(Object key) {
        return this.fid.get(key);
    }

    private String getDefaultImageName(int mxVal, String basicName){
        Random r = new Random();
        int i1 = r.nextInt(mxVal) + 1;
        return basicName + Integer.toString(i1);
    }

}
