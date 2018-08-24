package cn.reservation.app.baixingxinwen.utils;

import java.util.Dictionary;
import java.util.Hashtable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
    public void setProperty(JSONObject item, String fid_val){
        appfldname.put("txt_property1","");
        appfldname.put("txt_property2","");
        appfldname.put("txt_property3","");
        appfldname.put("txt_home_favor_price","");
        switch(fid_val){
            case "2"://房产信息
                if(item.optJSONObject("fields")!=null){
                    appfldname.put("txt_home_favor_price",item.optJSONObject("fields").optString("price_label"));
                    appfldname.put("txt_property1",item.optJSONObject("fields").optString("region_label"));
                    appfldname.put("txt_property2",item.optJSONObject("fields").optString("floors_label"));
                    appfldname.put("txt_property3",item.optJSONObject("fields").optString("house_label"));
                }
                break;
            case "39"://车辆交易
                if(item.optJSONObject("fields")!=null){
                    appfldname.put("txt_property1",item.optJSONObject("fields").optString("region_label"));
                    appfldname.put("txt_property2",item.optJSONObject("fields").optString("car_type_label"));
                    appfldname.put("txt_property3",item.optJSONObject("fields").optString("abaility_estimate_label"));
                    if(item.optJSONObject("fields").optString("price_label")==""){
                        appfldname.put("txt_home_favor_price",item.optJSONObject("fields").optString("car_price_label"));
                    }else {
                        appfldname.put("txt_home_favor_price",item.optJSONObject("fields").optString("price_label"));
                    }
                }
                break;
            case "40"://物品买卖
                if(item.optJSONObject("fields")!=null){
                    appfldname.put("txt_property1",item.optJSONObject("fields").optString("region_label"));
                    appfldname.put("txt_property2",item.optJSONObject("fields").optString("type_label"));
                    appfldname.put("txt_property3",item.optJSONObject("fields").optString("level_label"));
                    if(item.optJSONObject("fields").optString("price_label")==""){
                        appfldname.put("txt_home_favor_price",item.optJSONObject("fields").optString("car_price_label"));
                    }else {
                        appfldname.put("txt_home_favor_price",item.optJSONObject("fields").optString("price_label"));
                    }
                }
                break;
            case "38"://招聘求职
                if(item.optJSONObject("fields")!=null){
                    appfldname.put("txt_property1",item.optJSONObject("fields").optString("region_label"));
                    appfldname.put("txt_property2",item.optJSONObject("fields").optString("experience_label"));
                    appfldname.put("txt_property3",item.optJSONObject("fields").optString("education_label"));
                    appfldname.put("txt_home_favor_price",item.optJSONObject("fields").optString("salary_range_label"));
                }
                break;
            case "42"://便民服务
                if(item.optJSONObject("fields")!=null){
                    appfldname.put("txt_property1",item.optJSONObject("fields").optString("region_label"));
                    appfldname.put("txt_property2",item.optJSONObject("fields").optString("type_label"));
                    appfldname.put("txt_property3","");
                    appfldname.put("txt_home_favor_price","");
                }
                break;
            case "48"://婚姻交友
                if(item.optJSONObject("fields")!=null){
                    appfldname.put("txt_property1",item.optJSONObject("fields").optString("region_label"));
                    appfldname.put("txt_property2",item.optJSONObject("fields").optString("type_label"));
                    appfldname.put("txt_property3",item.optJSONObject("fields").optString("sex_label"));
                    appfldname.put("txt_home_favor_price",item.optJSONObject("fields").optString("age_label"));
                }
                break;
            case "74"://教育培训
                if(item.optJSONObject("fields")!=null){
                    appfldname.put("txt_property1",item.optJSONObject("fields").optString("region_label"));
                    appfldname.put("txt_property2",item.optJSONObject("fields").optString("education_label"));
                    appfldname.put("txt_property3",item.optJSONObject("fields").optString("period_label"));
                    appfldname.put("txt_home_favor_price",item.optJSONObject("fields").optString("education_price_label"));
                }
                break;
            case "92"://宠物天地
                if(item.optJSONObject("fields")!=null){
                    appfldname.put("txt_property1",item.optJSONObject("fields").optString("region_label"));
                    appfldname.put("txt_property2",item.optJSONObject("fields").optString("type_label"));
                    appfldname.put("txt_property3",item.optJSONObject("fields").optString("source_label"));
                    appfldname.put("txt_home_favor_price",item.optJSONObject("fields").optString("price_label"));
                }
                break;
            case "83"://出国资讯
                if(item.optJSONObject("fields")!=null){
                    appfldname.put("txt_property1","");
                    appfldname.put("txt_property2",item.optJSONObject("fields").optString("region_label"));
                    appfldname.put("txt_property3",item.optJSONObject("fields").optString("type_label"));
                    appfldname.put("txt_home_favor_price","");
                }
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
            case "93"://出兑求兑
                if(item.optJSONObject("fields")!=null){
                    appfldname.put("txt_property1",item.optJSONObject("fields").optString("region_label"));
                    appfldname.put("txt_property2",item.optJSONObject("fields").optString("type_label"));
                    appfldname.put("txt_property3",item.optJSONObject("fields").optString("floor_label"));
                    appfldname.put("txt_home_favor_price",item.optJSONObject("fields").optString("price_label"));
                }
                break;
            case "94"://电话号码
                if(item.optJSONObject("fields")!=null){
                    appfldname.put("txt_property1",item.optJSONObject("fields").optString("region_label"));
                    appfldname.put("txt_property2",item.optJSONObject("fields").optString("type_label"));
                    appfldname.put("txt_property3",item.optJSONObject("fields").optString("company_label"));
                    appfldname.put("txt_home_favor_price",item.optJSONObject("fields").optString("price_label"));
                }
                break;
            case "44"://招商加盟
                if(item.optJSONObject("fields")!=null){
                    appfldname.put("txt_property1",item.optJSONObject("fields").optString("region_label"));
                    appfldname.put("txt_property2",item.optJSONObject("fields").optString("type_label"));
                    appfldname.put("txt_property3",item.optJSONObject("fields").optString("group_label"));
                    appfldname.put("txt_home_favor_price",item.optJSONObject("fields").optString("price_label"));
                }
                break;
            case "50"://游山玩水
                if(item.optJSONObject("fields")!=null){
                    appfldname.put("txt_property1",item.optJSONObject("fields").optString("region_label"));
                    appfldname.put("txt_property2",item.optJSONObject("fields").optString("type_label"));
                    appfldname.put("txt_property3",item.optJSONObject("fields").optString("order_label"));
                    appfldname.put("txt_home_favor_price",item.optJSONObject("fields").optString("price_label"));
                }
                break;
            case "107":
                if(item.optJSONObject("fields")!=null){
                    appfldname.put("txt_property1",item.optJSONObject("fields").optString("region_label"));
                    appfldname.put("txt_property2",item.optJSONObject("fields").optString("type_label"));
                    appfldname.put("txt_property3",item.optJSONObject("fields").optString("group_label"));
                    appfldname.put("txt_home_favor_price","");
                }
                break;
        }
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

}
