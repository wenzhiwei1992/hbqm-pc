package com.smartpc.chiyun.service.sys.mapper;

import com.smartpc.chiyun.service.sys.dto.MenuDto;
import com.smartpc.chiyun.base.BaseMapper;
import com.smartpc.chiyun.model.sys.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @Author yue
 * @create 2020/3/12 9:42 下午
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuMapper extends BaseMapper<MenuDto, Menu> {
}
