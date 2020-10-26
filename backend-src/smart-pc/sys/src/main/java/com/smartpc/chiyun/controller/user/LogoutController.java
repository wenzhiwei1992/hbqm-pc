package com.smartpc.chiyun.controller.user;

import com.smartpc.chiyun.model.sys.SR;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class LogoutController {

    //@Log("用户退出")
    @RequestMapping(value = "/user/logout", method = RequestMethod.POST)
    public SR logout(HttpServletRequest request, HttpServletResponse response){
        SR sr=new SR();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        sr.setStatus(SR.SUCCESS);
        return sr;
    }
}
