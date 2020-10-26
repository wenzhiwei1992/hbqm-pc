package com.smartpc.chiyun.config;

import com.smartpc.chiyun.exception.DataNotFoundException;
import com.smartpc.chiyun.utils.HttpUtil;
import com.smartpc.chiyun.model.sys.VTUserDX;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Data
public class WxUtils {

    private static final String GET_TOKEN_URL= "https://qyapi.weixin.qq.com/cgi-bin/gettoken";

    private static final String GET_DEPT_USERS_URL= "https://qyapi.weixin.qq.com/cgi-bin/user/list";



    /**
     * 获取token
     * @return
     */
    public static String getAccessToken(String corpid,String corpsecret){
        String url = GET_TOKEN_URL + "?corpid="+corpid + "&corpsecret="+corpsecret;
        String result = HttpUtil.doGet(url);
        try {
            JSONObject resultObj = new JSONObject(result);
            if (resultObj.getInt("errcode") == 0) {
                return resultObj.getString("access_token");
            } else {
                throw new DataNotFoundException("从微信服务器获取 AccessToken 失败 - " + result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new DataNotFoundException("从微信服务器获取 AccessToken 失败 - " + result);
        }
    }


    /**
     * 获取token
     * @return
     */
    public static List<VTUserDX> getDeptUsers(String accessToken,Long deptId){
        List<VTUserDX> users = new ArrayList<VTUserDX>();

        String url = GET_DEPT_USERS_URL + "?access_token="+accessToken
                + "&department_id="+deptId
                + "&fetch_child=1";
        String result = HttpUtil.doGet(url);
        setQywxUsers(users, result);
        return users;
    }


    private static void setQywxUsers(List<VTUserDX> users, String result) {
        try {
            JSONObject resultObj = new JSONObject(result);
            if(0 == resultObj.getInt("errcode")){
                JSONArray userlist = resultObj.getJSONArray("userlist");
                for (int i = 0; i < userlist.length(); i++) {
                    JSONObject userObj = userlist.getJSONObject(i);
                    VTUserDX user = new VTUserDX();
                    user.setUserid(userObj.has("userid") ? userObj.getString("userid") : "");
                    user.setName(userObj.has("name") ? userObj.getString("name") : "");
                    user.setDepartment(userObj.has("department") ? userObj.getJSONArray("department").toString() : "");
                    user.setOrder(userObj.has("order") ? userObj.getJSONArray("order").toString() : "");
                    user.setPosition(userObj.has("position") ? userObj.getString("position") : "");
                    user.setMobile(userObj.has("mobile") ? userObj.getString("mobile") : "");
                    user.setGender(userObj.has("gender") ? userObj.getString("gender") : "");
                    user.setEmail(userObj.has("email") ? userObj.getString("email") : "");
                    user.setIsLeader(userObj.has("isleader") ? String.valueOf(userObj.getInt("isleader")) : "");
                    user.setAvatar(userObj.has("avatar") ? userObj.getString("avatar") : "");
                    user.setTelephone(userObj.has("telephone") ? userObj.getString("telephone") : "");
                    user.setEnglishName(userObj.has("english_name") ? userObj.getString("english_name") : "");
                    user.setStatus(userObj.has("status") ? String.valueOf(userObj.getInt("status")) : "");
                    user.setExtattr(userObj.has("extattr") ? userObj.getJSONObject("extattr").toString() : "");
                    user.setEnable(userObj.has("enable") ? userObj.getString("enable") : "");
                    users.add(user);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
