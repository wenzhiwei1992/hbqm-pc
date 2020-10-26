package com.smartpc.chiyun.model.dept;

import com.smartpc.chiyun.model.CommonProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "ma_department")
public class Department extends CommonProperties {
    public static final String BEFORE = "before";
    public static final String INNER = "inner";
    public static final String AFTER = "after";
    @Column(name = "dept_name")
    private String deptName;

    private Integer state = 1;
    /**
     * 部门代码
     */
    @Column(name = "dept_code")
    private String deptCode;
    /**
     * 上级ID
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 所属等级
     */
    @Column(name = "code_level")
    private Integer codeLevel;

    /**
     * 负责人dept_hod
     */
    @Column(name = "dept_hod")
    private String deptHod;

    /**
     * 部门属性
     */
    @Column(name = "dept_prop")
    private String deptProp;

    /**
     * 部门类型
     */
    @Column(name = "dept_type")
    private String deptType;

    /**
     * 电话
     */
    @Column(name = "dept_phone")
    private String deptPhone;

    /**
     * 传真
     */
    @Column(name = "dept_fax")
    private String deptFax;

    /**
     * 邮编
     */
    @Column(name = "dept_postcode")
    private String deptPostCode;


    /**
     * 备注
     */
    @Column(name = "dept_memo")
    private String deptMemo;


    /**
     * 是否末级节点
     */
    @Column(name = "ifLastNode")
    private Integer ifLastNode;


    /**
     * 是否同步 默认为0 0未同步 1已同步 2同步失败
     */
    @Column(name = "ifSync")
    private Integer ifSync = 0;


}
