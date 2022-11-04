package bdcqjdc.${package.ModuleName}.impl;

import com.bdcqjdc.entity.dataobject.${entity}DO;
import com.bdcqjdc.entity.dto.${lowerEntity}.dto.Add${entity}Request;
import com.bdcqjdc.entity.dto.${lowerEntity}.dto.${entity}DetailVO;
import com.bdcqjdc.entity.dto.${lowerEntity}.dto.Update${entity}Request;
import com.bdcqjdc.entity.dto.${lowerEntity}.dto.${entity}SearchRequest;
import com.bdcqjdc.entity.dto.${lowerEntity}.dto.${entity}ListVO;
import com.bdcqjdc.entity.dto.${lowerEntity}.converter.${entity}Converter;
import com.bdcqjdc.entity.mapper.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * <p>
    * ${table.comment!} 服务实现类
    * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}DO> implements ${table.serviceName} {

    @Autowired
    private ${entity}Converter ${lowerEntity}Converter;

    @Autowired
    private ${entity}Mapper ${lowerEntity}Mapper;

    /**
     * 新增记录
     * @param request
     * @return
     */
    @Override
    public boolean insert${entity}(Add${entity}Request request) {
        ${entity}DO ${lowerEntity}DO = ${lowerEntity}Converter.addRequest2DO(request);
        return this.save(${lowerEntity}DO);
    }

    /**
     * 根据主键查询记录详情
     * @param id
     * @return
     */
    @Override
    public ${entity}DetailVO get${entity}DetailById(String id) {
        ${entity}DO ${lowerEntity}DO = this.getById(id);
        return ${lowerEntity}Converter.do2DetailVO(${lowerEntity}DO);
    }

    /**
     * 修改单条记录
     * @param request
     * @return
     */
    @Override
    public boolean update${entity}(Update${entity}Request request) {
        ${entity}DO ${lowerEntity}DO = ${lowerEntity}Converter.updateRequest2DO(request);
        return this.updateById(${lowerEntity}DO);
    }

    /**
     * 根据条件进行列表查询
     * @param request
     * @return
     */
    @Override
    public Page search${entity}List(${entity}SearchRequest request) {
        Page<${entity}DO> pageParam = new Page<${entity}DO>(request.getCurrentPage(), request.getPageSize());
        QueryWrapper<${entity}DO> wrapper = new QueryWrapper<>();
        //设置默认排序
        wrapper = "desc".equals(request.getSortOrder()) ? wrapper.orderByDesc(request.getSortField()) :  wrapper.orderByAsc(request.getSortField());

        //TODO 根据当前情况设置wrapper条件

        Page page = this.page(pageParam, wrapper);
        //将查询出来的DO List转为 ListVO List并重新设置到page对象中，并返回page对象
        return page.setRecords(${lowerEntity}Converter.doList2ListVOList(page.getRecords()));
    }

}
</#if>
