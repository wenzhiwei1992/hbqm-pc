package com.smartpc.chiyun.utils;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @Author yue
 * @create 2020/5/30 12:09 下午
 */
public class MapSortUtil {
    /**
     * 根据map的key排序(Key为String类型请先处理)
     *
     * @param map    map 待排序的map
     * @param isDesc isDesc 是否降序，true：降序，false：升序
     * @param <K>
     * @param <V>
     * @return 排序好的map
     */
    public static <K extends Comparable<? super K>, V> Map<K, V> sortByKey(Map<K, V> map, boolean isDesc) {
        Map<K, V> result = Maps.newLinkedHashMap();
        if (isDesc) {
            map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByKey().reversed())
                    .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        } else {
            map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByKey())
                    .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        }
        return result;
    }

    /**
     * 根据map的value排序
     *
     * @param map    map 待排序的map
     * @param isDesc isDesc 是否降序，true：降序，false：升序
     * @param <K>
     * @param <V>
     * @return 排序好的map
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, boolean isDesc) {
        Map<K, V> result = Maps.newLinkedHashMap();
        if (isDesc) {
            map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByValue().reversed())
                    .forEach(e -> result.put(e.getKey(), e.getValue()));
        } else {
            map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByValue())
                    .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        }
        return result;
    }
}
