package com.smartpc.chiyun.config;

import com.smartpc.chiyun.dao.sys.SetupDao;
import com.smartpc.chiyun.dao.sys.SysCounterDao;
import com.smartpc.chiyun.dao.syscode.CodeExplainContentDao;
import com.smartpc.chiyun.model.syscode.CodeExplainContent;
import com.smartpc.chiyun.model.sys.Setup;
import com.smartpc.chiyun.model.sys.SysCounter;
import com.smartpc.chiyun.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AppUtil {

    @Autowired
    SetupDao setupDao;

    @Autowired
    SysCounterDao sysCounterDao;

    @Autowired
    CodeExplainContentDao dictDao;


    /**
     * 获取构件下一个状态
     * 传入的 currentStatus = first 时,返回第一个状态
     *
     * @param productDetailId 构件明细ID
     */
    public String getProductNextStatus(Long productDetailId) {
        List<String> dicts = dictDao.findNextDictByDetailId(productDetailId);
        String nextStatus = null;
        if (dicts.size() > 0) {
            nextStatus = dicts.get(0);
        }
        return nextStatus;
    }

    public List<String> getStatusList(String currentVal, String dictCodeNo) {
        String status = getNextStatus(currentVal, dictCodeNo);
        String[] split = status.split(",");
        return Arrays.asList(split);
    }

    /**
     * 通用获取下一个状态
     * 传入的 currentVal = first 时,返回第一个状态
     *
     * @param currentVal 当前状态值
     * @param dictCodeNo 当前字典类型, 枚举 DictEnum
     * @return 当返回值为空字符, 则需要提示错误
     */
    public String getNextStatus(String currentVal, String dictCodeNo) {
        //已经排好序
        List<CodeExplainContent> dicts = dictDao.findAllByCodeNoOrderBySort(dictCodeNo);
        //dicts.sort(Comparator.comparing(CodeExplainContent::getSort));
        if (dicts.size() == 0) {
            return "";
        }

        if (currentVal.equalsIgnoreCase("first")) {
            return dicts.get(0).getCodeName();
        }

        String nextStatus = "";
        int res = -1;
        //排序后,值相等的下一个元素就是下个状态
        for (int i = 0; i < dicts.size(); i++) {
            CodeExplainContent content = dicts.get(i);
            if (content.getCodeName().equals(currentVal)) {
                // res 为当前下标的下一个
                res = i + 1;
                break;
            }
        }
        if (res != -1) {
            CodeExplainContent content = dicts.get(res);
            nextStatus = content.getCodeName();
        }
        return nextStatus;
    }


    /**
     * 通用获取上一个状态
     * 传入的 currentVal = first 时,返回第一个状态
     *
     * @param currentVal 当前状态值
     * @param dictCodeNo 当前字典类型, 枚举 DictEnum
     * @return 当返回值为空字符, 则需要提示错误
     */
    public String getPreviousStatus(String currentVal, String dictCodeNo) {
        //已经排好序
        List<CodeExplainContent> dicts = dictDao.findAllByCodeNoOrderBySort(dictCodeNo);
        //dicts.sort(Comparator.comparing(CodeExplainContent::getSort));
        if (dicts.size() == 0) {
            return "";
        }

        if (currentVal.equalsIgnoreCase("first")) {
            return dicts.get(0).getCodeName();
        }

        String nextStatus = "";
        int res = -1;
        //排序后,值相等的下一个元素就是下个状态
        for (int i = 0; i < dicts.size(); i++) {
            CodeExplainContent content = dicts.get(i);
            if (content.getCodeName().equals(currentVal)) {
                // res 为当前下标的下一个
                res = i - 1;
                break;
            }
        }
        if (res != -1) {
            CodeExplainContent content = dicts.get(res);
            nextStatus = content.getCodeName();
        }
        return nextStatus;
    }

    /**
     * 获取指定类型最大值
     *
     * @param type
     * @return
     */
    public String getCurrentNo(String type, String prefix) {

        //确保记录唯一
        SysCounter sysCounter = sysCounterDao.findByType(type, getCurrentUserOrgId());
        if (sysCounter == null) {
            sysCounter = new SysCounter();
            //已经是第一次获取，所以下次获取从 2 开始
            sysCounter.setCurrentValue(2L);
            sysCounter.setType(type);
            sysCounterDao.save(sysCounter);
            return prefix + String.format("%03d", 1);
        }
        Long currentValue = sysCounter.getCurrentValue();
        String number = String.format("%03d", currentValue);
        //数字 +1
        Long nextValue = currentValue + 1;
        sysCounter.setCurrentValue(nextValue);
        sysCounterDao.save(sysCounter);

        return prefix + number;
    }

    /**
     * 如果返回null，则没有获取到user
     *
     * @return
     */
    public static User getCurrentUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof UsernamePasswordAuthenticationToken)) {

            return null;
        }
        if (!auth.isAuthenticated()) {
            return null;
        }
        return (User) auth.getDetails();
    }

    /**
     * 如果返回null，则没有获取到user
     * 获取当前用户的所有orgIds
     *
     * @return
     */
    public static List<Long> getCurrentUserOrgIds() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof UsernamePasswordAuthenticationToken)) {

            return null;
        }
        if (!auth.isAuthenticated()) {
            return null;
        }
        User currentUser = (User) auth.getDetails();
        List<String> ordIds = new ArrayList<>();
        if (currentUser != null && currentUser.getOrgIds() != null) {
            String[] split = currentUser.getOrgIds().split(";");
            ordIds = Arrays.asList(split);
        }
        return ordIds.stream().map(Long::valueOf).collect(Collectors.toList());
    }


    /**
     * 返回用户可查看组织的orgIds  用于sql in查询
     *
     * @return 1, 2
     */
    public static String getCurrentUserStringOrgIds() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof UsernamePasswordAuthenticationToken)) {
            return null;
        }
        if (!auth.isAuthenticated()) {
            return null;
        }
        User currentUser = (User) auth.getDetails();
        String orgIdsStr = "";
        if (currentUser != null && currentUser.getOrgIds() != null) {
            orgIdsStr = currentUser.getOrgIds();
            if (StringUtils.isEmpty(orgIdsStr)) {
                orgIdsStr = currentUser.getOrgId().toString();
            }
            orgIdsStr = orgIdsStr.substring(0, orgIdsStr.length() - 1);
            orgIdsStr = orgIdsStr.replaceAll(";", ",");

        }
        return orgIdsStr;
    }

    public Boolean isEnabledMultiOrg() {
        List<Setup> setupDaoAll = setupDao.findAll();
        if (setupDaoAll.size() > 0) {
            Setup setup = setupDaoAll.get(0);

            return setup.getEnabledMultiOrg() == null ? false : setup.getEnabledMultiOrg();
        }
        return false;
    }

    public static Long getCurrentUserOrgId() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof UsernamePasswordAuthenticationToken)) {

            return null;
        }
        if (!auth.isAuthenticated()) {
            return null;
        }
        User u = (User) auth.getDetails();
        return u.getOrgId();
    }

    public static String getCurrentUserLevel() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof UsernamePasswordAuthenticationToken)) {

            return null;
        }
        if (!auth.isAuthenticated()) {
            return null;
        }
        User u = (User) auth.getDetails();
        return u.getLevel();
    }

    public static String generateNo() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date_temp = sdf.format(date);

        return "NO." + date_temp;
    }


    /**
     * 生成三位流水码
     *
     * @param count 传入数字， 1，2，3...
     * @return
     */
    public static String generateNoFormat(String prefix, Integer count) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date_temp = sdf.format(date);
        String number = String.format("%03d", count);
        return prefix + date_temp + number;
    }

    /**
     * 获取指定类型最大值,多传 format
     *
     * @param type
     * @param format
     * @param prefix
     * @return
     */
    public String getCurrentNoCustomFormat(String type, String prefix, String format) {

        //确保记录唯一
        SysCounter sysCounter = sysCounterDao.findByType(type, getCurrentUserOrgId());
        if (sysCounter == null) {
            sysCounter = new SysCounter();
            //已经是第一次获取，所以下次获取从 2 开始
            sysCounter.setCurrentValue(2L);
            sysCounter.setType(type);
            sysCounterDao.save(sysCounter);
            return prefix + String.format(format, 1);
        }
        Long currentValue = sysCounter.getCurrentValue();
        String number = String.format(format, currentValue);
        //数字 +1
        Long nextValue = currentValue + 1;
        sysCounter.setCurrentValue(nextValue);
        sysCounterDao.save(sysCounter);

        return prefix + number;
    }

    /**
     * Test Java Method
     * @param args
     */
//    public static void main(String[] args) {
//        int size =10;
//        String number = String.format("%03d", size+1);
//        System.out.println("number : "+number);
//
//        String[] wwc = ProductDetail.PRODUCT_STATUS_WWC;
//        System.out.println(Arrays.toString(wwc));
//
//        String[] orderOver = ProductDetail.PRODUCT_STATUS_ORDER_OVER;
//        System.out.println(Arrays.toString(orderOver));
//        //产出量
//        String[] outPut = ProductDetail.PRODUCT_STATUS_OUT_PUT;
//        System.out.println(Arrays.toString(outPut));
//
//        String[] wsc = ProductDetail.PRODUCT_STATUS_WSC;
//        System.out.println(Arrays.toString(wsc));
//
//        String[] scz = ProductDetail.PRODUCT_STATUS_SCZ;
//        System.out.println(Arrays.toString(scz));
//    }
}
