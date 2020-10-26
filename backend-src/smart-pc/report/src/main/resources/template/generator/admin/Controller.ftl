package ${package}.rest;

import Log;
import com.smartpc.chiyun.common.enums.ResultEnum;
import ${package}.domain.${className};
import ${package}.service.${className}Service;
import ${package}.service.dto.${className}QueryCriteria;
import com.smartpc.chiyun.common.vo.ResultVO;
import com.smartpc.chiyun.common.voutils.ResultVOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
* @author ${author}
* @date ${date}
*/
@RestController
public class ${className}Controller {

    @Autowired
    private ${className}Service ${changeClassName}Service;

    @Log("查询${className}")
    @GetMapping(value = "/${changeClassName}")
    @PreAuthorize("hasAnyRole('ADMIN','${upperCaseClassName}_SELECT')")
    public ResultVO get${className}s(${className}QueryCriteria criteria, Pageable pageable){
        return ResultVOUtils.success(${changeClassName}Service.queryAll(criteria,pageable));
    }

    @Log("新增${className}")
    @PostMapping(value = "/${changeClassName}/add")
    @PreAuthorize("hasAnyRole('ADMIN','${upperCaseClassName}_CREATE')")
    public ResultVO create(@Validated @RequestBody ${className} resources){
        return ResultVOUtils.success(${changeClassName}Service.create(resources));
    }

    @Log("修改${className}")
    @PutMapping(value = "/${changeClassName}/update")
    @PreAuthorize("hasAnyRole('ADMIN','${upperCaseClassName}_ALL','${upperCaseClassName}_EDIT')")
    public ResultVO update(@Validated @RequestBody ${className} resources){
        ${changeClassName}Service.update(resources);
        return ResultVOUtils.success();
    }

    @Log("删除${className}")
    @DeleteMapping(value = "/${changeClassName}/{${pkChangeColName}}")
    @PreAuthorize("hasAnyRole('ADMIN','${upperCaseClassName}_DELETE')")
    public ResultVO delete(@PathVariable ${pkColumnType} ${pkChangeColName}){
        ${changeClassName}Service.delete(${pkChangeColName});
        return ResultVOUtils.success();
    }
}
