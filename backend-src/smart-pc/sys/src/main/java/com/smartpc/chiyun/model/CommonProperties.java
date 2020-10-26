package com.smartpc.chiyun.model;

import com.smartpc.chiyun.model.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class CommonProperties implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @CreatedDate
    private Date dateCreated;
    @LastModifiedDate
    private Date dateModified;
    private String createdBy;
    private String modifiedBy;
    private String createdByName;
    private String modifiedByName;
    @Column(name = "org_id")
    private Long orgId;


    @PrePersist
    public void onPrePersist() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!(auth  instanceof UsernamePasswordAuthenticationToken)){
            return;
        }
        if(!auth.isAuthenticated()){
            return;
        }
        User user=(User)auth.getDetails();
        if(user==null){
            return;
        }
        this.createdBy=user.getUsername();
        this.createdByName=user.getRealName();
        this.modifiedBy=user.getUsername();
        this.modifiedByName=user.getRealName();
        //在保存时自动添加上orgId，如果需要自己分配orgId，则需要先保存记录，然后修改orgId，再保存
        this.orgId = user.getOrgId();

    }
    @PreUpdate
    public void onPreUpdate() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!(auth  instanceof UsernamePasswordAuthenticationToken)){
            return;
        }
        if(!auth.isAuthenticated()){
            return;
        }
        User user=(User)auth.getDetails();
        if(user==null){
            return;
        }
        this.modifiedBy=user.getUsername();
        this.modifiedByName=user.getRealName();
    }
}
