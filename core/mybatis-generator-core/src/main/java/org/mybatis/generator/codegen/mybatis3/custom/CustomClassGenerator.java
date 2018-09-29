package org.mybatis.generator.codegen.mybatis3.custom;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.mybatis3.custom.util.MethodUtil;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.CustomMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.CustomMethodGenerator.CUSTOM_METHOD_NAME;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.CustomMethodGenerator.CUSTOM_METHOD_PARAM_NAME;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.CustomMethodGenerator.CUSTOM_PROPERTIES_NAME;

/**
 * 自定义的modelQuery生成器
 * @author leihaijun
 * @date 2018年2月12日 下午2:40:02
 */
public class CustomClassGenerator extends AbstractJavaGenerator{
	
	//创建BaseQueryCondition
	static {
		
	}

	/**
	 * 包基础路径
	 */
	public static String basePackage;
	public static String mapperPackage;
	public static String extMapperPackage;
	private String mapperType;
	private String extMapperType;
	
	public CustomClassGenerator( List<String> warnings,
            ProgressCallback progressCallback, IntrospectedTable introspectedTable) {
		super();

        super.setContext(context);
        super.setIntrospectedTable(introspectedTable);
        super.setProgressCallback(progressCallback);
        super.setWarnings(warnings);
        
        mapperType = introspectedTable.getMyBatis3JavaMapperType();
        mapperPackage = mapperType.substring(0, mapperType.lastIndexOf("."));
        extMapperPackage = mapperPackage + ".ext";
        extMapperType = extMapperPackage + mapperType.substring(mapperType.lastIndexOf(".")).replace("Mapper", "ExtMapper");
        basePackage = mapperPackage.substring(0, mapperPackage.lastIndexOf("."));
    }
	

    @Override
    public List<CompilationUnit> getCompilationUnits() {
    	List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
    	
    	answer.add(generateQueryClass());
    	answer.add(generateServicelInterface());
    	answer.add(generateServiceImplClass());
    	// 生成扩展Mapper文件
    	answer.add(generateExtMapperInterface());
    	
    	return answer;      
    }
    
    private CompilationUnit generateQueryClass(){
    	
    	FullyQualifiedJavaType modelType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
    	FullyQualifiedJavaType queyType = new FullyQualifiedJavaType(getQueryFullyQualifiedName(modelType.getShortName() ));
    	TopLevelClass topLevelClass = new TopLevelClass(queyType);
    	topLevelClass.setSuperClass(modelType.getFullyQualifiedName());
    	
    	topLevelClass.setVisibility(JavaVisibility.PUBLIC);
    	
    	//添加公共查询字段
    	FullyQualifiedJavaType queryConditionField = new FullyQualifiedJavaType(getQueryFullyQualifiedName("CommonCondition"));
    	
    	Field field = new Field();
    	field.setVisibility(JavaVisibility.PRIVATE);
    	field.setType(queryConditionField);
    	field.setName("queryCondition");
    	
    	field.addJavaDocLine("/**");
    	field.addJavaDocLine(" * 查询条件");
    	field.addJavaDocLine(" */");
    	topLevelClass.addField(field);
    	
    	Method getter = new Method();
    	getter.setVisibility(JavaVisibility.PUBLIC);
    	getter.setReturnType(queryConditionField);
    	getter.setName("getQueryCondition");
    	getter.addBodyLine("return this." + field.getName() + ";");
    	
    	Method setter = new Method();
    	setter.setVisibility(JavaVisibility.PUBLIC);
    	setter.setName("setQueryCondition");
    	
    	Parameter parameter = new Parameter(queryConditionField, field.getName());
    	setter.addParameter(parameter);
    	
    	setter.addBodyLine("this." + field.getName() + "=" + parameter.getName() + ";");
    	
    	topLevelClass.addMethod(getter);
    	topLevelClass.addMethod(setter);
    	
    	topLevelClass.addImportedType(queryConditionField);

    	return topLevelClass;
    }
    
    private CompilationUnit generateExtMapperInterface(){
    	
    	FullyQualifiedJavaType modelType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
    	FullyQualifiedJavaType queyType = new FullyQualifiedJavaType(getExtMapperFullyQualifiedName(modelType.getShortName() ));
    	Interface topLevelClass = new Interface(queyType);
    	
    	topLevelClass.addSuperInterface(new FullyQualifiedJavaType(getMapperFullyQualifiedName(modelType.getShortName())));
    	
    	topLevelClass.setVisibility(JavaVisibility.PUBLIC);
    	topLevelClass.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Repository"));
    	topLevelClass.addAnnotation("@Repository");
    	
    	return topLevelClass;
    }
    
    
    private CompilationUnit generateServicelInterface(){
    	
    	FullyQualifiedJavaType modelType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
    	FullyQualifiedJavaType queyType = new FullyQualifiedJavaType(getServiceFullyQualifiedName(modelType.getShortName() ));
    	Interface topLevelClass = new Interface(queyType);
    	
    	topLevelClass.setVisibility(JavaVisibility.PUBLIC);
    	
    	return topLevelClass;
    }
    
    private CompilationUnit generateServiceImplClass(){
    	
    	FullyQualifiedJavaType modelType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
    	FullyQualifiedJavaType serviceImplType = new FullyQualifiedJavaType(getServiceImplFullyQualifiedName(modelType.getShortName() ));
    	TopLevelClass topLevelClass = new TopLevelClass(serviceImplType);
    	topLevelClass.addSuperInterface(new FullyQualifiedJavaType(getServiceFullyQualifiedName(modelType.getShortName())));
    	topLevelClass.setVisibility(JavaVisibility.PUBLIC);

    	topLevelClass.addImportedType(new FullyQualifiedJavaType("java.util.Date"));
    	topLevelClass.addImportedType(new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired"));
    	topLevelClass.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Service"));
    	topLevelClass.addImportedType(new FullyQualifiedJavaType("org.springframework.transaction.annotation.Transactional"));
    	topLevelClass.addImportedType(new FullyQualifiedJavaType(modelType.getFullyQualifiedName()));
    	topLevelClass.addImportedType(new FullyQualifiedJavaType(basePackage + ".example." + modelType.getShortName() + "Example"));
    	//topLevelClass.addImportedType(new FullyQualifiedJavaType(getExtMapperFullyQualifiedName(modelType.getShortName())));
    	
    	topLevelClass.addAnnotation("@Service");
    	topLevelClass.addAnnotation("@Transactional(rollbackFor = Exception.class)");
    	
    	Field mapperField = new Field();
    	mapperField.setName(CUSTOM_PROPERTIES_NAME.MAPPER);
    	mapperField.setVisibility(JavaVisibility.PRIVATE);
    	//mapperField.setType(new FullyQualifiedJavaType(mapperType));
    	mapperField.setType(new FullyQualifiedJavaType(extMapperType));
    	mapperField.addAnnotation("@Autowired");
    	
		topLevelClass.addField(mapperField);
		topLevelClass.addMethod(generateDeleteLogicalMethod());
		topLevelClass.addMethod(generateDeleteLogicalBatchMethod());
    	
    	
    	return topLevelClass;
    }
    
    private Method generateDeleteLogicalMethod() {
		Method method = new Method();
		Map<String, String> commentMap = new LinkedHashMap<String, String>();
		commentMap.put(MethodUtil.METHOD_DESC, "逻辑删除");

		method.setReturnType(FullyQualifiedJavaType.getIntInstance());
		method.setVisibility(JavaVisibility.PROTECTED);
		method.setName(CUSTOM_METHOD_NAME.DELETE_LOGICAL);
		method.addException(new FullyQualifiedJavaType("java.lang.Exception"));

		FullyQualifiedJavaType intType = FullyQualifiedJavaType
				.getIntInstance();

		Parameter idParameter = new Parameter(intType,
				CUSTOM_METHOD_PARAM_NAME.ID);
		commentMap.put(idParameter.getName(), "目标记录ID");
		method.addParameter(idParameter);
		Parameter operatorIdParameter = new Parameter(intType,
				CUSTOM_METHOD_PARAM_NAME.OPERATOR_ID);
		commentMap.put(operatorIdParameter.getName(), "操作人ID");
		method.addParameter(operatorIdParameter);

		MethodUtil.addCustomMethodComment(method, commentMap);

		FullyQualifiedJavaType recordType = new FullyQualifiedJavaType(
				introspectedTable.getBaseRecordType());
		String recordTypeShortName = recordType.getShortName();

		String recordName = "record";
		StringBuilder methodBodySB = new StringBuilder();
		methodBodySB.append(recordTypeShortName).append(" ").append(recordName)
				.append(" = ").append("new ").append(recordTypeShortName)
				.append("();");
		methodBodySB.append("\t\r\n");
		methodBodySB.append(recordName).append(".setId(")
				.append(idParameter.getName()).append(");");
		methodBodySB.append("\t\r\n");
		methodBodySB.append(recordName).append(".setStatus(0);");
		methodBodySB.append("\t\r\n");
		methodBodySB.append(recordName).append(".setUpdateTime(new Date());");
		methodBodySB.append("\t\r\n");
		methodBodySB.append(recordName).append(".setUpdateUser(")
				.append(operatorIdParameter.getName()).append(");");
		methodBodySB.append("\t\r\n");
		methodBodySB.append("return ").append(CUSTOM_PROPERTIES_NAME.MAPPER)
				.append(".updateByPrimaryKeySelective(").append(recordName)
				.append(");");

		method.addBodyLine(methodBodySB.toString());

		return method;

	}

	private Method generateDeleteLogicalBatchMethod() {
		Method method = new Method();
		Map<String, String> commentMap = new LinkedHashMap<String, String>();
		commentMap.put(MethodUtil.METHOD_DESC, "批量逻辑删除");

		method.setReturnType(FullyQualifiedJavaType.getIntInstance());
		method.setVisibility(JavaVisibility.PROTECTED);
		method.setName(CUSTOM_METHOD_NAME.DELETE_LOGICAL_BATCH);
		method.addException(new FullyQualifiedJavaType("java.lang.Exception"));

		FullyQualifiedJavaType intType = FullyQualifiedJavaType
				.getIntInstance();

		FullyQualifiedJavaType elementType = new FullyQualifiedJavaType(
				"java.lang.Integer");
		FullyQualifiedJavaType idsParameterType = FullyQualifiedJavaType
				.getNewListInstance();
		idsParameterType.addTypeArgument(elementType);
		Parameter idsParameter = new Parameter(idsParameterType,
				CUSTOM_METHOD_PARAM_NAME.IDS);
		commentMap.put(idsParameter.getName(), "目标记录ID列表");
		method.addParameter(idsParameter);
		Parameter operatorIdParameter = new Parameter(intType,
				CUSTOM_METHOD_PARAM_NAME.OPERATOR_ID);
		commentMap.put(operatorIdParameter.getName(), "操作人ID");
		method.addParameter(operatorIdParameter);

		MethodUtil.addCustomMethodComment(method, commentMap);

		FullyQualifiedJavaType recordType = new FullyQualifiedJavaType(
				introspectedTable.getBaseRecordType());
		String recordTypeShortName = recordType.getShortName();

		String recordName = "record";
		StringBuilder methodBodySB = new StringBuilder();
		methodBodySB.append("if (").append(idsParameter.getName())
				.append(" == null || ").append(idsParameter.getName())
				.append(".size() == 0) {return 0;}");
		methodBodySB.append("\t\r\n");
		methodBodySB.append(recordTypeShortName).append(" ").append(recordName)
				.append(" = ").append("new ").append(recordTypeShortName)
				.append("();");
		methodBodySB.append("\t\r\n");
		methodBodySB.append(recordName).append(".setStatus(0);");
		methodBodySB.append("\t\r\n");
		methodBodySB.append(recordName).append(".setUpdateTime(new Date());");
		methodBodySB.append("\t\r\n");
		methodBodySB.append(recordName).append(".setUpdateUser(")
				.append(operatorIdParameter.getName()).append(");");
		methodBodySB.append("\t\r\n");
		/*methodBodySB.append("return ").append(CUSTOM_PROPERTIES_NAME.MAPPER)
				.append(".updateSelectiveBatch(").append(recordName)
				.append(", ").append(idsParameter.getName()).append(");");*/

		methodBodySB.append(recordTypeShortName).append("Example " + CustomMethodGenerator.CUSTOM_METHOD_PARAM_NAME.EXAMPLE + " = new ").append(recordTypeShortName).append("Example();");
		methodBodySB.append("\t\r\n");
		methodBodySB.append(CustomMethodGenerator.CUSTOM_METHOD_PARAM_NAME.EXAMPLE + ".createCriteria().andIdIn(").append(idsParameter.getName()).append(").andStatusEqualTo(1);");
		methodBodySB.append("\t\r\n");
		methodBodySB.append("\t\r\n");
		methodBodySB.append("return ").append(CUSTOM_PROPERTIES_NAME.MAPPER).append(".updateByExampleSelective(").append(recordName)
		.append(", ").append(CustomMethodGenerator.CUSTOM_METHOD_PARAM_NAME.EXAMPLE).append(");");;
		
		method.addBodyLine(methodBodySB.toString());

		return method;

	}
	
	 /**
     * 根据模型名获取Mapper的全限定名
     * @param modelTypeShortName
     * @return
     */
    private String getMapperFullyQualifiedName(String modelTypeShortName){
    	return basePackage + ".mapper." + modelTypeShortName + "Mapper";
    }

    /**
     * 根据模型名获取扩展Mapper的全限定名
     * @param modelTypeShortName
     * @return
     */
    private String getExtMapperFullyQualifiedName(String modelTypeShortName){
    	return basePackage + ".mapper.ext." + modelTypeShortName + "ExtMapper";
    }
    
    /**
     * 根据模型名获取Query的全限定名
     * @param modelTypeShortName
     * @return
     */
    public static String getQueryFullyQualifiedName(String modelTypeShortName){
        return basePackage + ".query." + modelTypeShortName + "Query";
    }
    /**
     * 根据模型名获取service的全限定名
     * @param modelTypeShortName
     * @return
     */
    private String getServiceFullyQualifiedName(String modelTypeShortName){
    	return basePackage + ".service." + modelTypeShortName + "Service";
    }
    /**
     * 根据模型名获取serviceImpl的全限定名
     * @param modelTypeShortName
     * @return
     */
    private String getServiceImplFullyQualifiedName(String modelTypeShortName){
    	return basePackage + ".service.impl." + modelTypeShortName + "ServiceImpl";
    }

}
