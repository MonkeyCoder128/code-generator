package com.bdcqjdc.entity.dto.${lowerEntity}.converter;

import java.util.List;
import com.bdcqjdc.entity.dataobject.${entity}DO;
import com.bdcqjdc.entity.dto.${lowerEntity}.dto.Add${entity}Request;
import com.bdcqjdc.entity.dto.${lowerEntity}.dto.${entity}DetailVO;
import com.bdcqjdc.entity.dto.${lowerEntity}.dto.Update${entity}Request;
import com.bdcqjdc.entity.dto.${lowerEntity}.dto.${entity}ListVO;
import org.mapstruct.Mapper;

/**
 * @author ${author}
 * @since ${date}
 */
@Mapper(componentModel = "spring")
public interface ${entity}Converter{
    ${entity}DO addRequest2DO(Add${entity}Request request);

    ${entity}DetailVO do2DetailVO(${entity}DO ${lowerEntity}DO);

    ${entity}DO updateRequest2DO(Update${entity}Request request);

    ${entity}ListVO do2ListVO(${entity}DO ${lowerEntity}DO);

    List<${entity}ListVO> doList2ListVOList(List<${entity}DO> ${lowerEntity}DOList);
}