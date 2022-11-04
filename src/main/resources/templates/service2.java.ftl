package bdcqjdc.${package.ModuleName}.biz;

import com.bdcqjdc.entity.dataobject.${entity}DO;
import com.bdcqjdc.entity.dto.${lowerEntity}.dto.Add${entity}Request;
import com.bdcqjdc.entity.dto.${lowerEntity}.dto.${entity}DetailVO;
import com.bdcqjdc.entity.dto.${lowerEntity}.dto.Update${entity}Request;
import com.bdcqjdc.entity.dto.${lowerEntity}.dto.${entity}SearchRequest;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${superServiceClassPackage};

/**
 * <p>
    * ${table.comment!} 服务类
    * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}DO> {
     /**
     * 新增记录
     * @param request
     * @return
     */
    boolean insert${entity}(Add${entity}Request request);

    /**
     * 根据主键查询记录详情
     * @param id
     * @return
     */
    ${entity}DetailVO get${entity}DetailById(String id);

    /**
     * 修改单条记录
     * @param request
     * @return
     */
    boolean update${entity}(Update${entity}Request request);

    /**
     * 根据条件进行列表查询
     * @param request
     * @return
    */
    Page search${entity}List(${entity}SearchRequest request);
}
</#if>
