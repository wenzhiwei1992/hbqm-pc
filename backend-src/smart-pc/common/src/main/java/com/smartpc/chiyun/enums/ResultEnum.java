package com.smartpc.chiyun.enums;

import lombok.Getter;

/**
 * @author zihao
 * 通用返回结果值枚举
 */
@Getter
public enum ResultEnum {
    /**
     * 构件明细明细行为空
     */
    DETAILSISEMPTY("failed", "明细行为空，请检查"),
    PLATENOTEMPTY("failed", "有模台计划当前处于非空闲状态，不能执行该操作"),
    PRODUCTSTATUSERROR("failed", "构件明细状态已经不是待生产，不允许撤销"),
    NUMBERMUSTBENOTNULL("failed", "请检查数量是否填写正确！"),
    DATASIZEISZORE("failed", "未找到相关数据"),
    CREATEPLATEERR("failed", "创建模台失败！"),
    SYSEXCEPTION("failed"),
    CONSTRAINT("failed", "该信息已有其他引用,不能删除"),
    UKNOCANNOTSAME("failed", "编号不能重复"),
    UKNAMECANNOTSAME("failed", "名称不能重复"),
    FAILED("failed", "系统错误,请联系管理员"),
    SUCCESS("success", "success"),
    PAGEINFODATAERR("failed", "failed"),
    UPDATEMUSTHAVEID("failed", "修改必须传ID值"),
    GETONEFAILED("failed", "根据ID获取值失败"),
    CATEGORYCLASSIFICATIONISNULL("failed", "请填写物料分类"),
    CATEGORYNAMEISNULL("failed", "请填写物料类型名称"),
    CATEGORYNOISNULL("failed", "请填写编码"),
    OPTION("failed", "计量单位名称重复"),
    UNITISNULL("failed", "请填写计量单位"),
    NAME_DUPPLICATED("failed", "已有该名称"),
    READ_EXCEL_ERROR("failed", "读取EXCEL失败"),
    REPEAT_INVOICE("failed", "发货单下数据重复"),
    FILE_NOT("failed", "保存路径未配置"),
    SELECTBUILDANDFLOOR("failed", "请检查选中的楼栋楼层"),
    /**
     * 原料
     */
    CHECKMATERIALNUM("fail", "请检查添加原料申请量"),
    QUANTITYGREATERTHANNUM("fail", "申请量大于库存量"),
    QUANTITYGREATEHAVEQUANTITY("fail", "申请量大于库存量"),
    ADDMATERIAL("fail", "请添加原料"),
    /**
     * 构件类型
     */
    PRODUCTTYPENOTEDITORDEL("fail", "该构件类型下有层级信息"),
    PRODUCT_STATUS_DYJ_ERROR("failed", "构件状态已经不是待隐检，不允许撤销"),
    NOT_PRODUCT_TYPE("fail", "请查看是否存在对应的构件类型"),
    CHECK_PRODUCT_TYPE_NAME("fail", "检查构件类型名称"),
    NOTNULL_PRODUCT_TYPE("fail", "构件类型不能为空"),
    /**
     * 原料类型
     */
    CATEGORYNOTEDITORDEL("fail", "该原料类型下有层级信息"),
    CATEGORYHAVEINFO("fail", "该原料类型下存在对应信息"),
    /**
     * 模具
     */
    CHECK_MATERIAL("fail", "请查看所选项目下是否存在对应的模具"),
    /**
     * 项目
     */
    SELECT_PROJECT("failed", "请先选择项目"),
    SELECT_PROJECT_TRUE("fail", "请选择对应项目"),
    SELECT_PROJECT_NO("fail", "请检查选择项目的项目编号"),
    /**
     * 构件
     */
    PRODUCT_NO_ERROR("fail", "构件编码异常,请查看编码规则是否规范————————————>"),
    /**
     * 楼栋楼层
     */
    HAVE_BUILD("failed", "楼栋已存在"),
    /**
     * 目标未选择
     */
    TARGET_NOT_SELECT("failed", "目标未选择"),
    CODE_EXISTS("fail", "该编号已经存在"),

    /**
     * 企业微信相关枚举
     */
    ACCESS_TOKEN_ERROR("failed", " 未获取到 ACCESS TOKEN "),

    /**
     * 用户相关
     */
    USER_NOT_FOUND_ERROR("failed", "获取不到用户信息"),
    EPWX_URL_ERROR("failed", "企业微信URL配置错误，无法直接登录，请手动登录"),
    EPDD_URL_ERROR("success", "该用户已存在于当前企业下，自动订正企业id"),
    /**
     * 发货
     */
    INVOICE_NULL("failed", "发货单不存在"),
    CONFIRMATION_DATE_NULL("failed", "确认日期为空"),
    /**
     * 手机端添加发货单
     */
    NOT_PLAN("failed", "此构件不在发货计划内，请扫描正确的构件"),
    NOT_OK_CHECK("failed", "此构件为不良品，请通知质检人员进行返修操作或请联系管理员"),
    NOT_PUT_PRODUCT("failed", "此构件未入库,请联系管理员,当前状态:"),
    NOT_EXIST_PD("failed", "构件明细不存在，请联系管理员，重新打印二维码"),
    ;

    private String status;

    private String msg;

    ResultEnum(String status) {
        this.status = status;
    }

    ResultEnum(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }
}
