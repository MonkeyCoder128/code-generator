package com.jfyf.codegenerator.generator;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.FileType;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;

import java.io.File;
import java.util.*;

public abstract class AbstractTemplateEngineLocal extends AbstractTemplateEngine {

    private void fitAddEntityRequest(List<TableField> tableFields, TableInfo tableInfo) {
        for(Iterator<TableField> it = tableFields.iterator(); it.hasNext();){
            TableField  tableField =it.next();
            if(tableField.isKeyIdentityFlag() || "ADDTIME".equals(tableField.getName()) || "EDITTIME".equals(tableField.getName())) {
                it.remove();
            }
        }

        Set<String> importPacks = tableInfo.getImportPackages();
        for(Iterator<String> it = importPacks.iterator(); it.hasNext();){
            String importPack = it.next();
            if(importPack.indexOf("baomidou") > 0){
                it.remove();
            }
        }

    }

    private void fillEntityProp(Map<String, Object> objectMap) {
        TableInfo tableInfo = (TableInfo) objectMap.get("table");
        int addFieldFillPacCount = 0;
        for(TableField tableField : tableInfo.getFields()) {
            tableField.setConvert(true);
            if("ADDTIME".equals(tableField.getName())) {
                tableField.setFill("INSERT");
                if(addFieldFillPacCount == 0) {
                    tableInfo.getImportPackages().add("com.baomidou.mybatisplus.annotation.FieldFill");
                    addFieldFillPacCount++;
                }
            }
            if("UPDATETIME".equals(tableField.getName())) {
                tableField.setFill("INSERT_UPDATE");
            }
        }

    }

    /**
     * list深度拷贝
     * @param list
     * @return
     */
    public List copyList(List list) {
        //list深度拷贝
        List<Integer> newList = new ArrayList<>();
        CollUtil.addAll(newList, new Object[list.size()]);
        Collections.copy(newList, list);
        return newList;
    }

    /**
     * 输出 java xml 文件
     */
    @Override
    public AbstractTemplateEngine batchOutput() {
        try {
            List<TableInfo> tableInfoList = getConfigBuilder().getTableInfoList();
            for (TableInfo tableInfo : tableInfoList) {
                Map<String, Object> objectMap = getObjectMap(tableInfo);
                Map<String, String> pathInfo = getConfigBuilder().getPathInfo();
                TemplateConfig template = getConfigBuilder().getTemplate();
                // 自定义内容
                InjectionConfig injectionConfig = getConfigBuilder().getInjectionConfig();
                if (null != injectionConfig) {
                    injectionConfig.initTableMap(tableInfo);
                    objectMap.put("cfg", injectionConfig.getMap());
                    List<FileOutConfig> focList = injectionConfig.getFileOutConfigList();
                    if (CollectionUtils.isNotEmpty(focList)) {
                        for (FileOutConfig foc : focList) {
                            if (isCreate(FileType.OTHER, foc.outputFile(tableInfo))) {
                                writer(objectMap, foc.getTemplatePath(), foc.outputFile(tableInfo));
                            }
                        }
                    }
                }
                // Mp.java
                String entityName = tableInfo.getEntityName();
                List<TableField> originalTableFieldList = tableInfo.getFields();
                if (null != entityName && null != pathInfo.get(ConstVal.ENTITY_PATH)) {
                    fillEntityProp(objectMap);
                    String entityFile = String.format((pathInfo.get(ConstVal.ENTITY_PATH) + File.separator + "%s" + "DO" + suffixJavaOrKt()), entityName) ;
                    if (isCreate(FileType.ENTITY, entityFile)) {
                        writer(objectMap, templateFilePath(template.getEntity(getConfigBuilder().getGlobalConfig().isKotlin())), entityFile);
                    }

                    List<TableField> originalTableFields = tableInfo.getFields();
                    List<TableField> addEntityTableFieldList = copyList(tableInfo.getFields());
                    fitAddEntityRequest(addEntityTableFieldList, tableInfo);
                    tableInfo.setFields(addEntityTableFieldList);

                    //向ObjectMap中添加首字母小写的实体对象名
                    String entity = (String) objectMap.get("entity");
                    objectMap.put("lowerEntity", entity.toLowerCase());

                    //AddEntityRequest.java
                    fitAddEntityRequest(tableInfo.getFields(), tableInfo);
                    String addEntityRequestFile = String.format((pathInfo.get(ConstVal.ENTITY_PATH) + File.separator + "%s" + suffixJavaOrKt()), "Add" + entityName + "Request");
                    if (isCreate(FileType.ENTITY, addEntityRequestFile)) {
                        writer(objectMap, "templates/AddEntityRequest.java.ftl", addEntityRequestFile);
                    }

                    //UpdateEntityRequest.java
                    fitAddEntityRequest(tableInfo.getFields(), tableInfo);
                    String updateEntityRequestFile = String.format((pathInfo.get(ConstVal.ENTITY_PATH) + File.separator + "%s" + suffixJavaOrKt()), "Update" + entityName + "Request");
                    if (isCreate(FileType.ENTITY, updateEntityRequestFile)) {
                        writer(objectMap, "templates/UpdateEntityRequest.java.ftl", updateEntityRequestFile);
                    }


                    //EntitySearchRequest.java
                    String entitySearchRequestFile = String.format((pathInfo.get(ConstVal.ENTITY_PATH) + File.separator + "%s" + suffixJavaOrKt()), entityName + "SearchRequest");
                    if (isCreate(FileType.ENTITY, entitySearchRequestFile)) {
                        writer(objectMap, "templates/EntitySearchRequest.java.ftl", entitySearchRequestFile);
                    }

                    tableInfo.setFields(originalTableFields);
                    //EntityListVO.java
                    String entityListVOFile = String.format((pathInfo.get(ConstVal.ENTITY_PATH) + File.separator + "%s" + suffixJavaOrKt()), entityName + "ListVO");
                    if (isCreate(FileType.ENTITY, entityListVOFile)) {
                        writer(objectMap, "templates/EntityListVO.java.ftl", entityListVOFile);
                    }

                    //EntityDetailVO.java
                    String entityDetailVOFile = String.format((pathInfo.get(ConstVal.ENTITY_PATH) + File.separator + "%s" + suffixJavaOrKt()), entityName + "DetailVO");
                    if (isCreate(FileType.ENTITY, entityDetailVOFile)) {
                        writer(objectMap, "templates/EntityDetailVO.java.ftl", entityDetailVOFile);
                    }
                }

                //converter.java
                String entityConverterFile = String.format((pathInfo.get(ConstVal.ENTITY_PATH) + File.separator + "%s" + suffixJavaOrKt()), entityName + "Converter");
                if (isCreate(FileType.ENTITY, entityConverterFile)) {
                    writer(objectMap, "templates/entityConverter.java.ftl", entityConverterFile);
                }

                // MpMapper.java
                if (null != tableInfo.getMapperName() && null != pathInfo.get(ConstVal.MAPPER_PATH)) {
                    String mapperFile = String.format((pathInfo.get(ConstVal.MAPPER_PATH) + File.separator + tableInfo.getMapperName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.MAPPER, mapperFile)) {
                        writer(objectMap, templateFilePath(template.getMapper()), mapperFile);
                    }
                }
                // MpMapper.xml
                if (null != tableInfo.getXmlName() && null != pathInfo.get(ConstVal.XML_PATH)) {
                    String xmlFile = String.format((pathInfo.get(ConstVal.XML_PATH) + File.separator + tableInfo.getXmlName() + ConstVal.XML_SUFFIX), entityName);
                    if (isCreate(FileType.XML, xmlFile)) {
                        writer(objectMap, templateFilePath(template.getXml()), xmlFile);
                    }
                }

                // IMpService.java
                if (null != tableInfo.getServiceName() && null != pathInfo.get(ConstVal.SERVICE_PATH)) {
                    String serviceFile = String.format((pathInfo.get(ConstVal.SERVICE_PATH) + File.separator + tableInfo.getServiceName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.SERVICE, serviceFile)) {
                        writer(objectMap, templateFilePath(template.getService()), serviceFile);
                    }
                }
                // MpServiceImpl.java
                if (null != tableInfo.getServiceImplName() && null != pathInfo.get(ConstVal.SERVICE_IMPL_PATH)) {
                    String implFile = String.format((pathInfo.get(ConstVal.SERVICE_IMPL_PATH) + File.separator + tableInfo.getServiceImplName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.SERVICE_IMPL, implFile)) {
                        writer(objectMap, templateFilePath(template.getServiceImpl()), implFile);
                    }
                }
                // MpController.java
                if (null != tableInfo.getControllerName() && null != pathInfo.get(ConstVal.CONTROLLER_PATH)) {
                    String controllerFile = String.format((pathInfo.get(ConstVal.CONTROLLER_PATH) + File.separator + tableInfo.getControllerName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.CONTROLLER, controllerFile)) {
                        writer(objectMap, templateFilePath(template.getController()), controllerFile);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("无法创建文件，请检查配置信息！", e);
        }
        return this;
    }

}
