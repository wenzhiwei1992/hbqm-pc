package com.smartpc.chiyun.service.sys.service;

import com.smartpc.chiyun.model.sys.ServerConfig;
import com.smartpc.chiyun.model.sys.WxInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WxService {
	@Autowired
	ServerConfig serverConfig;
	//初始化
	public WxInfo initInfo(){
		WxInfo info=new WxInfo();
		info.setApiUrl("https://qyapi.weixin.qq.com");
		info.setCorpid(serverConfig.getQywxCorpid());
		info.setSecret(serverConfig.getQywxSecret());

		return info;
	}


	//第二步：通过code获取并设置access_token
//	public SR setAccessTokenByCode(ThirdLoginInfo info){
//		SR sr=new SR(AppCons.SR_FAIL,"微信登录获取token出错");
//		String url=info.getApiUrl()+"/sns/oauth2/access_token"
//		                             +"?appid="+info.getAppid()
//		                             + "&secret="+info.getSecret()
//		                             + "&code="+info.getCode()
//		                             + "&grant_type=authorization_code";
//		try{
//			String result = HttpUtil.get(url);
//			JSONObject json= new JSONObject(result);
//			if(json.isNull("access_token")||
//					json.isNull("expires_in")||
//					json.isNull("refresh_token")||
//					json.isNull("openid")||
//					json.isNull("unionid")){
//				return sr;
//			}
//			info.setAccessToken(json.getString("access_token"));
//			info.setExpiresIn(json.getString("expires_in"));
//			info.setRefreshToken(json.getString("refresh_token"));
//			info.setOpenId(json.getString("openid"));
//			info.setUnionId(json.getString("unionid"));
//		}catch(Exception ex){
//			logService.systemError(AppCons.SYSTEM_ERROR_SERVICE, "微信登录获取token出错", ex);
//			return sr;
//		}
//		sr.setStatus(AppCons.SR_SUCCESS);
//		return sr;
//	}
//
//	//第三步：通过token获取并设置userinfo
//	public SR setUserInfo(ThirdLoginInfo info){
//		SR sr=new SR(AppCons.SR_FAIL,"微信登录获取个人信息出错");
//		String url=info.getApiUrl()+"/sns/userinfo"
//		                             +"?access_token="+info.getAccessToken()
//		                             + "&openid="+info.getOpenId();
//		try{
//			String result = HttpUtil.get(url);
//			JSONObject json= new JSONObject(result);
//			if(json.isNull("nickname")||
//					json.isNull("headimgurl")){
//				return sr;
//			}
//			ThirdLoginUserinfo userinfo=new ThirdLoginUserinfo();
//			userinfo.setType(info.getType());
//			userinfo.setNickname(json.getString("nickname"));
//			userinfo.setHeadimgurl(json.getString("headimgurl"));
//			info.setUserinfo(userinfo);
//		}catch(Exception ex){
//			logService.systemError(AppCons.SYSTEM_ERROR_SERVICE, "微信登录获取个人信息出错", ex);
//			return sr;
//		}
//		sr.setStatus(AppCons.SR_SUCCESS);
//		return sr;
//	}
//
//	public User getUser(String unionid){
//		UserExample e=new UserExample();
//		Criteria c = e.createCriteria();
//		c.andWxUnionidEqualTo(unionid);
//		List<User> users = userMapper.selectByExample(e);
//		if(users!=null&&users.size()==1){
//			return users.get(0);
//		}
//		return null;
//	}
//
//
//	public SR thirdLoginInfoBind(User user,ThirdLoginInfo info){
//		SR sr=new SR(AppCons.SR_FAIL,"微信信息绑定出错");
//		String openId = info.getOpenId();
//		String unionId=info.getUnionId();
//		if(StringUtils.isEmpty(openId)||
//				StringUtils.isEmpty(unionId)){
//			sr.setMsg("信息不完整");
//			return sr;
//		}
//		user.setWxmpOpenid(openId);//这里要设置为wxmp的openid，表示从公众号来的
//		user.setWxUnionid(unionId);
//
//		user.setWxAccessToken(info.getAccessToken());
//		user.setWxExpiresIn(info.getExpiresIn());
//		user.setWxRefreshToken(info.getRefreshToken());
//		user.setWxNickname(info.getUserinfo().getNickname());
//		user.setWxHeadimgurl(info.getUserinfo().getHeadimgurl());
//
//		userMapper.updateByPrimaryKeySelective(user);
//		sr.setStatus(AppCons.SR_SUCCESS);
//		return sr;
//	}
//
//	public SR thirdLoginInfoUnbind(User user){
//		SR sr=new SR(AppCons.SR_FAIL,"微信登录信息解除绑定出错");
//		user.setWxOpenid("");
//		user.setWxUnionid("");
//		user.setWxAccessToken("");
//		user.setWxExpiresIn("");
//		user.setWxRefreshToken("");
//		user.setWxNickname("");
//		user.setWxHeadimgurl("");
//
//		userMapper.updateByPrimaryKeySelective(user);
//		sr.setStatus(AppCons.SR_SUCCESS);
//		return sr;
//	}
}
