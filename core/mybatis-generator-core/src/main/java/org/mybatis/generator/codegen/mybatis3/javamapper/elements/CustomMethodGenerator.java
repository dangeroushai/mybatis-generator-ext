/**
 *    Copyright ${license.git.copyrightYears} the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.codegen.mybatis3.javamapper.elements;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.codegen.mybatis3.custom.CustomClassGenerator;
import org.mybatis.generator.codegen.mybatis3.custom.util.MethodUtil;

/**
 * 为接口增加常用的方法
 * 
 * @author LeiHaijun
 * 
 */
public class CustomMethodGenerator extends AbstractJavaMapperMethodGenerator {

	/**
	 * 自定义方法名
	 * 
	 * @author leihaijun
	 * @date 2018年2月6日 下午5:07:09
	 */
	public static class CUSTOM_METHOD_NAME {
		public static final String INSERT_BATCH = "insertBatch";
		public static final String UPDATE_SELECTIVE_BATCH = "updateSelectiveBatch";
		public static final String SELECT_BATCH = "selectBatch";
		public static final String SELECT_AMOUNT = "selectAmount";
		public static final String DELETE_LOGICAL = "deleteLogical";
		public static final String DELETE_LOGICAL_BATCH = "deleteLogicalBatch";
	}

	/**
	 * 自定义方法参数名
	 * 
	 * @author leihaijun
	 * @date 2018年2月6日 下午5:07:09
	 */
	public static class CUSTOM_METHOD_PARAM_NAME {
		public static final String ID = "id";
		public static final String IDS = "ids";
		public static final String EXAMPLE = "example";
		public static final String OPERATOR_ID = "operatorId";
	}

	public static class CUSTOM_PROPERTIES_NAME {
		public static final String MAPPER = "mapper";
	}

	public CustomMethodGenerator(boolean isLogicalDelete) {
		super();
	}

	@Override
	public void addInterfaceElements(Interface interfaze) {
		this.generateInsertBathchMethod(interfaze);
		this.generateUpdateSelectiveBatchMethod(interfaze);
		this.generateSelectBathchMethod(interfaze);
		this.generateSelectAmountMethod(interfaze);

		MethodUtil.addExceptionClaimForAllMethods(interfaze);
	}
	
	private void generateSelectAmountMethod(Interface interfaze) {
		Method method = new Method();
		Map<String, String> commentMap = new LinkedHashMap<String, String>();
		commentMap.put(MethodUtil.METHOD_DESC, "查询记录数");

		method.setReturnType(FullyQualifiedJavaType.getIntInstance());
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setName(CUSTOM_METHOD_NAME.SELECT_AMOUNT);

		FullyQualifiedJavaType modelType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
		FullyQualifiedJavaType paramType = new FullyQualifiedJavaType(CustomClassGenerator.getQueryFullyQualifiedName(modelType.getShortName()));

		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
		importedTypes.add(paramType);
		Parameter parameter = new Parameter(paramType,
				CUSTOM_METHOD_PARAM_NAME.EXAMPLE);
		commentMap.put(parameter.getName(), "查询样本");
		method.addParameter(parameter); //$NON-NLS-1$

		MethodUtil.addCustomMethodComment(method, commentMap);

		interfaze.addImportedTypes(importedTypes);
		interfaze.addMethod(method);
	}

	private void generateUpdateSelectiveBatchMethod(Interface interfaze) {
		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
		Method method = new Method();
		Map<String, String> commentMap = new LinkedHashMap<String, String>();
		commentMap.put(MethodUtil.METHOD_DESC, "批量修改（部分字段）");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(FullyQualifiedJavaType.getIntInstance());
		method.setName(CUSTOM_METHOD_NAME.UPDATE_SELECTIVE_BATCH);

		FullyQualifiedJavaType exampleType = new FullyQualifiedJavaType(
				introspectedTable.getBaseRecordType());
		FullyQualifiedJavaType keyType = introspectedTable
				.getPrimaryKeyColumns().get(0).getFullyQualifiedJavaType();

		FullyQualifiedJavaType keysType = FullyQualifiedJavaType
				.getNewListInstance();
		keysType.addTypeArgument(keyType);

		importedTypes.add(keyType);
		importedTypes.add(keysType);
		importedTypes.add(new FullyQualifiedJavaType(
				"org.apache.ibatis.annotations.Param"));

		Parameter exampleParameter = new Parameter(exampleType,
				CUSTOM_METHOD_PARAM_NAME.EXAMPLE);
		Parameter idsParameter = new Parameter(keysType,
				CUSTOM_METHOD_PARAM_NAME.IDS);

		StringBuilder sb = new StringBuilder();
		sb.setLength(0);
		sb.append("@Param(\""); //$NON-NLS-1$
		sb.append(exampleParameter.getName());
		sb.append("\")"); //$NON-NLS-1$
		exampleParameter.addAnnotation(sb.toString());
		commentMap.put(exampleParameter.getName(), "修改样本（只含修改字段）");

		sb.setLength(0);
		sb.append("@Param(\""); //$NON-NLS-1$
		sb.append(idsParameter.getName());
		sb.append("\")"); //$NON-NLS-1$
		idsParameter.addAnnotation(sb.toString());
		commentMap.put(idsParameter.getName(), "目标记录ID列表");

		method.addParameter(exampleParameter);
		method.addParameter(idsParameter);

		MethodUtil.addCustomMethodComment(method, commentMap);

		interfaze.addImportedTypes(importedTypes);
		interfaze.addMethod(method);
	}

	private void generateSelectBathchMethod(Interface interfaze) {
		Method method = new Method();
		Map<String, String> commentMap = new LinkedHashMap<String, String>();
		commentMap.put(MethodUtil.METHOD_DESC, "批量查询");

		FullyQualifiedJavaType modelType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
		FullyQualifiedJavaType paramType = new FullyQualifiedJavaType(CustomClassGenerator.getQueryFullyQualifiedName(modelType.getShortName()));
		FullyQualifiedJavaType returnType = FullyQualifiedJavaType
				.getNewListInstance();
		returnType.addTypeArgument(modelType);

		method.setReturnType(returnType);
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setName(CUSTOM_METHOD_NAME.SELECT_BATCH);

		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
		importedTypes.add(returnType);
		importedTypes.add(paramType);
		Parameter parameter = new Parameter(paramType,
				CUSTOM_METHOD_PARAM_NAME.EXAMPLE);
		commentMap.put(parameter.getName(), "查询样本");
		method.addParameter(parameter);

		MethodUtil.addCustomMethodComment(method, commentMap);

		interfaze.addImportedTypes(importedTypes);
		interfaze.addMethod(method);
	}

	/**
	 * 生成批量插入方法
	 * 
	 * @param interfaze
	 */
	private void generateInsertBathchMethod(Interface interfaze) {
		Method method = new Method();
		Map<String, String> commentMap = new LinkedHashMap<String, String>();
		commentMap.put(MethodUtil.METHOD_DESC, "批量插入");

		method.setReturnType(FullyQualifiedJavaType.getIntInstance());
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setName(CUSTOM_METHOD_NAME.INSERT_BATCH);

		FullyQualifiedJavaType elementType = new FullyQualifiedJavaType(
				introspectedTable.getBaseRecordType());
		FullyQualifiedJavaType parameterType = FullyQualifiedJavaType
				.getNewListInstance();
		parameterType.addTypeArgument(elementType);

		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
		importedTypes.add(parameterType);
		importedTypes.add(elementType);

		Parameter parameter = new Parameter(parameterType, "records");
		commentMap.put(parameter.getName(), "模型列表");
		method.addParameter(parameter);

		MethodUtil.addCustomMethodComment(method, commentMap);

		interfaze.addImportedTypes(importedTypes);
		interfaze.addMethod(method);
	}

}
