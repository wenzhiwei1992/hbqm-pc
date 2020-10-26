package com.smartpc.chiyun.controller.user;

import com.smartpc.chiyun.config.AppUtil;
import com.smartpc.chiyun.config.dingding.CorpUserDetail;
import com.smartpc.chiyun.controller.user.dto.SsoDTO;
import com.smartpc.chiyun.dao.user.OrgDao;
import com.smartpc.chiyun.dao.user.UserDao;
import com.smartpc.chiyun.dao.user.UserM2EpWxDao;
import com.smartpc.chiyun.enums.ResultEnum;
import com.smartpc.chiyun.service.DingDingConfigService;
import com.smartpc.chiyun.service.user.LoginService;
import com.smartpc.chiyun.utils.HttpUtil;
import com.smartpc.chiyun.utils.StringUtil;
import com.smartpc.chiyun.utils.WeChatConfigSingleton;
import com.smartpc.chiyun.vo.ResultVO;
import com.smartpc.chiyun.voutils.ResultVOUtils;

import com.smartpc.chiyun.model.user.User;
import com.smartpc.chiyun.model.user.UserM2EpWx;
import com.smartpc.chiyun.model.sys.SR;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class UserM2EpWxController {


    @Autowired
    private LoginService loginService;

    @Autowired
    OrgDao orgDao;

    @Autowired
    UserDao userDao;

    @Autowired
    private UserM2EpWxDao userM2EpWxDao;

    @Autowired
    DingDingConfigService dingDingConfigService;

    @PostMapping("/user/m2ep/list")
    public ResultVO list(@RequestBody UserM2EpWx m2){

        Pageable pageable = PageRequest.of(m2.getPageInfo().getCurrentPage()-1, m2.getPageInfo().getPageSize());
        Page<UserM2EpWx> page = userM2EpWxDao.findAll(pageable);
        for(UserM2EpWx userM2EpWx : page){
            User user = userDao.findByUserId(Long.parseLong(userM2EpWx.getUserId()));
            userM2EpWx.setUserName(user==null?"":user.getRealName());
        }

        return ResultVOUtils.success(page);
    }

    @PostMapping("/user/m2ep/add")
    public ResultVO add(@RequestBody UserM2EpWx m2){
        m2.setOrgId(AppUtil.getCurrentUserOrgId());
        try{
            if(m2.getType()==2){ //钉钉用户
                //钉钉用户在这里认为 userid为PCPM平台登录的用户名
                //而wxUserid认为是用户的手机号
                //新建时发起注册
                CorpUserDetail corpUserDetail = new CorpUserDetail();
                corpUserDetail.setMobile(m2.getPhone());
                corpUserDetail.setUserid(m2.getEpWxUserId());
                corpUserDetail.setName(m2.getUserId());
                List<Long> list = new ArrayList<>();
                list.add(1L);
                corpUserDetail.setDepartment(list);

                String useridDD = dingDingConfigService.createUser(corpUserDetail);
                if(StringUtil.isNotNullAndEmpty(useridDD) && useridDD.indexOf("error")!=-1){
                    return  ResultVOUtils.error("failed",useridDD);
                }
                if(!m2.getEpWxUserId().equals(useridDD)){//已存在的用户
                    m2.setEpWxUserId(useridDD);
                    userM2EpWxDao.save(m2);
                    return ResultVOUtils.success(ResultEnum.EPDD_URL_ERROR);
                }
            }
            userM2EpWxDao.save(m2);
        }catch(Exception e){
            e.printStackTrace();
            return  ResultVOUtils.error(ResultEnum.UKNAMECANNOTSAME);
        }

        return ResultVOUtils.success();
    }


    @PutMapping("/user/m2ep/edit")
    public ResultVO edit(@RequestBody UserM2EpWx m2){
        userM2EpWxDao.save(m2);
        return ResultVOUtils.success();
    }


    //???? 应该废除掉 但是不确定还有几个地方在使用
//    @PutMapping("/user/m2ep/{userId}")
//    public ResultVO findByUserId(@PathVariable String userId){
//        UserM2EpWx byId = userM2EpWxDao.findByUserIdAndType(userId,1);
//        if (byId!=null){
//            return ResultVOUtils.success(byId);
//        }
//        return ResultVOUtils.error(ResultEnum.USER_NOT_FOUND_ERROR);
//    }

    @PutMapping("/user/m2ep/findById/{id}")
    public ResultVO findById(@PathVariable Integer id){
        UserM2EpWx byId = userM2EpWxDao.findById(id).get();
        if (byId!=null){
            return ResultVOUtils.success(byId);
        }
        return ResultVOUtils.error(ResultEnum.USER_NOT_FOUND_ERROR);
    }

    @DeleteMapping("/user/m2ep/delete")
    public ResultVO delete(@RequestParam(name = "id") Integer id){
        userM2EpWxDao.deleteById(id);
        return ResultVOUtils.success();
    }

    /**
     * 企业微信用户点击应用，直接登录到手机端,not used,pending delete
     */
    @GetMapping("/user/m2ep/buildSsoUrl")
    public ResultVO buildSsoUrl(HttpServletRequest request , HttpServletResponse response ) throws IOException {

        String redirectUri = constructRedirectUri(request);
        return ResultVOUtils.success(redirectUri);
    }

    @PostMapping("/user/m2ep/sso")
    public ResultVO sso(@RequestBody SsoDTO ssoDTO){
        String code = ssoDTO.getCode();
        String corpId = ssoDTO.getCorpId();
        String secret = ssoDTO.getSecret();
        String tenantId = ssoDTO.getTenantId();

        log.info("ssoDTO = {}",ssoDTO);

        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(corpId)
            || StringUtils.isEmpty(secret) ){
            return ResultVOUtils.error(ResultEnum.USER_NOT_FOUND_ERROR);
        }
        String accessToken = WeChatConfigSingleton.getAccessToken(corpId, secret);
        log.info("accessToken={}",accessToken);
        if (StringUtils.isEmpty(accessToken)){
            return ResultVOUtils.error(ResultEnum.ACCESS_TOKEN_ERROR);
        }
        String qywxUserId = getQywxUserId(accessToken, code);
        log.info("qywxUserId = {}",qywxUserId);

        //处理登录事项
        List<UserM2EpWx> list = userM2EpWxDao.findByEpUserId(qywxUserId,1);
        if (list.size()>0){
            UserM2EpWx epWx = list.get(0);
            if (StringUtil.isNotEmpty(epWx)){
                User query = null;

                //这里这样写 是为了兼容可能出现的以前的错误数据
                //以前Userid存的是用户名  现在改为用户id  数2020年4月28日16:21:08  李强
                try {
                    Long userId = Long.parseLong(epWx.getUserId()+"");
                    query = userDao.findByUserId(userId);
                }catch (Exception e){
                    query = userDao.findByUsername(epWx.getUserId()+"");
                }
                query.setTenantId(tenantId);
                try {
                    query.setOrg(orgDao.findById(query.getOrgId()).get());
                }catch (Exception e){
                    e.printStackTrace();
                }
                SR<User> user = loginService.setLoginUser(query);
                log.info("testSSOSuccess----------------------------------------------------->",user.getEntity());
                return ResultVOUtils.success(user.getEntity());

            }
        }
        log.info("testSSOError----------------------------------------------------->","error");
        return ResultVOUtils.error(ResultEnum.USER_NOT_FOUND_ERROR);



    }


    private String getQywxUserId(String accessToken,String code){
        String url = SsoDTO.SSO_GET_USER_BY_CODE + "?access_token="+accessToken+"&code="+code;
        String json = HttpUtil.doGet(url);
        JSONObject userResultObj = null;
        try {
            userResultObj = new JSONObject(json);
            String errcode = userResultObj.getString("errcode");
            if ("0".equals(errcode)) {
                return userResultObj.getString("UserId");
            }else {
                String errmsg = userResultObj.getString("errmsg");
                log.error(errmsg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * OAuth2.0 : https://open.weixin.qq.com/connect/oauth2/authorize?appid=CORPID
     *          &redirect_uri=REDIRECT_URI
     *          &response_type=code
     *          &scope=SCOPE
     *          &state=STATE#wechat_redirect
     * appid	是	企业的CorpID
     * redirect_uri	是	授权后重定向的回调链接地址，请使用urlencode对链接进行处理
     * response_type	是	返回类型，此时固定为：code
     * scope	是	应用授权作用域，此时固定为：snsapi_base
     * state	否	重定向后会带上state参数，企业可以填写a-zA-Z0-9的参数值，长度不可超过128个字节
     * #wechat_redirect	是	微信终端使用此参数判断是否需要带上身份信息
     *
     *
     * 点击后，页面将跳转至 redirect_uri?code=CODE&state=STATE，企业可根据code参数获得员工的userid。
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    protected String constructRedirectUri(HttpServletRequest request) throws UnsupportedEncodingException {
        String redirectUrl = request.getParameter("redirectUrl");
        String corpId = request.getParameter("corpId");
        String secret = request.getParameter("secret");
        String tenantId  = request.getParameter("tenantId");
        redirectUrl = (redirectUrl != null) ? URLDecoder.decode(redirectUrl, "UTF-8") : HttpUtil.getBaseUrl(request);
        String redirect_uri = HttpUtil.getBaseUrl(request)
                + "?action=sso"
                + "&redirectUrl=" + URLEncoder.encode(redirectUrl, "UTF-8")
                + "&corpId="+corpId
                + "&appSecret="+secret
                + "&tenantId="+tenantId;
        String sso_url = "https://open.weixin.qq.com/connect/oauth2/authorize"
                + "?appid=" + corpId
                + "&redirect_uri=" + URLEncoder.encode(redirect_uri, "UTF-8")
                + "&response_type=code"
                + "&scope=snsapi_base"
                + "&state=STATE"
                + "#wechat_redirect";
        redirectUrl = sso_url;
        return redirectUrl;
    }

}
