package com.smartpc.chiyun.utils;

import java.util.ArrayList;
import java.util.List;

public class IdUtil {

    /**
     * 删除操作传入的id集合是逗号分隔，切割之后返回Long类型的数组
     * @param ids
     * @return
     */
    public static List<Long> splitIdsToIdList(String ids){
        String[] split = ids.split(",");
        List<Long> list = new ArrayList<>();
        for (String id : split) {
            list.add(Long.parseLong(id));
        }
        return list;
    }
}
