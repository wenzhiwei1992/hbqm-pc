package com.smartpc.chiyun.model.syscode;

import com.smartpc.chiyun.model.CommonProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * @author zihao
 */
@Entity
@Table(name = "sys_dict")
@Data
public class CodeExplain  extends CommonProperties {

    /**
     * 系统代码
     */
    @Column(name = "code_no")
    private String  codeNo;
    /**
     * 系统名称
     */
    @Column(name = "code_name")
    private String codeName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 所属模块,根据情况填写
     */
    @Column(name = "code_belong")
    private String codeBelong;

    @Transient
    private List<CodeExplainContent> codeContents;

}
