package bdcqjdc.${package.ModuleName}.service;


import com.bdcqjdc.entity.dto.${lowerEntity}.dto.Add${entity}Request;
import com.bdcqjdc.entity.dto.${lowerEntity}.dto.Update${entity}Request;
import com.bdcqjdc.entity.dto.${lowerEntity}.dto.${entity}SearchRequest;
import org.springframework.web.bind.annotation.RestController;
import com.bdcqjdc.common.result.Result;
import bdcqjdc.${package.ModuleName}.biz.${entity}Biz;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import bdcqjdc.service.core.api.vo.Result;
import com.bdcqjdc.entity.dto.page.PageInfo;
import java.util.List;

/**
 * <p>
    * ${table.comment!} 前端控制器
    * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@RestController
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>/")
@Api(tags = "${table.comment}接口")
public class ${table.controllerName} {
    @Autowired
    private ${entity}Biz ${lowerEntity}Biz;

    @PostMapping("insert${entity}")
    @ApiOperation("新增${table.comment}")
    public Result insert${entity}(@RequestBody Add${entity}Request request){
        if(${lowerEntity}Biz.insert${entity}(request)){
            return Result.ok();
        }
        return Result.error("新增失败");
    }

    @DeleteMapping("delete${entity}ByIds")
    @ApiOperation(value = "批量删除${table.comment}")
    public Result delete${entity}ByIds(@ApiParam("${table.comment}ID列表") @RequestParam(value = "idList") List<String> idList) {
        if(${lowerEntity}Biz.removeByIds(idList)) {
            return Result.ok("删除成功");
        }
        return Result.error("删除失败");
    }

    @PutMapping("update${entity}")
    @ApiOperation("修改${table.comment}")
    public Result update${entity}(@RequestBody Update${entity}Request request){
        if(${lowerEntity}Biz.update${entity}(request)) {
            return Result.ok("修改成功");
        }
        return Result.error("修改失败");
    }

    @GetMapping("get${entity}DetailById")
    @ApiOperation(value = "读取明细")
    public Result get${entity}DetailById(@ApiParam("${table.comment}ID") @RequestParam String id){
        return Result.ok(${lowerEntity}Biz.get${entity}DetailById(id));
    }

    @PostMapping("search")
    @ApiOperation(value = "根据条件进行列表查询")
    public Result search${entity}List(${entity}SearchRequest request) {
        //TODO 默认排序条件设置
        request.defaultFillPageProp("","");
        return Result.ok(${lowerEntity}Biz.search${entity}List(request));
    }
}
