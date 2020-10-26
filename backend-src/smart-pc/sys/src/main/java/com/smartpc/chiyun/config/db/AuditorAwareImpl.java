//package com.vps.chiyun.config.db;
//
//import com.vps.chiyun.model.user.User;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.domain.AuditorAware;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//import java.util.Optional;
//@Configuration
//public class AuditorAwareImpl implements AuditorAware<String> {
//
//    @Override
//    public Optional<String> getCurrentAuditor() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if(!(auth  instanceof UsernamePasswordAuthenticationToken)){
//            return null;
//        }
//        if(!auth.isAuthenticated()){
//            return null;
//        }
//        User user=(User)auth.getDetails();
//        if(user==null){
//            return null;
//        }
//        return Optional.ofNullable(user.getUsername());
//
//    }
//
//}
