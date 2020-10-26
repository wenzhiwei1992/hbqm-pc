package com.smartpc.chiyun.model.user.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 用于树结构拖动
 */
@Getter
@Setter
public class ProductTypeHandleVO {
    /**
     * 被拖拽节点
     */
    private Long draggingNodeId;
    /**
     * 拖拽最后进入的节点
     */
    private Long dropNodeId;
    /**
     *被拖拽节点的放置位置（before、after、inner）
     */
    private String dropType;


}
