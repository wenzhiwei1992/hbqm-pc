package com.smartpc.chiyun.model.user;

import com.smartpc.chiyun.model.CommonProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "pc_dept")
public class Dept extends CommonProperties {
    @Column(name = "dept_name")
    private String deptName;
}
