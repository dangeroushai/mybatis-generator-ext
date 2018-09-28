package org.mybatis.generator.codegen.mybatis3.custom.util;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;

public class MethodUtil {
	
	/**
	 * 方法说明标识
	 */
	public static final String METHOD_DESC = "methodDesc";
	
	/**
	 * 
	 * @param method
	 * @param commentMap
	 *            注释<参数名，参数说明>
	 */
	public static void addCustomMethodComment(Method method,
			Map<String, String> commentMap) {
		method.addJavaDocLine("/**");
		for (Entry<String, String> entry : commentMap.entrySet()) {
			if (entry.getKey().equals(METHOD_DESC)) {
				method.addJavaDocLine(" * " + entry.getValue());
			} else {
				method.addJavaDocLine(" * @param " + entry.getKey() + " "
						+ entry.getValue());
			}
		}

		method.addJavaDocLine(" */");
	}
	

	/**
	 * 为所有方法添加异常声明
	 * 
	 * @param interfaze
	 */
	public static void addExceptionClaimForAllMethods(Interface interfaze) {
		FullyQualifiedJavaType exceptionType = new FullyQualifiedJavaType(
				"java.lang.Exception");
		if (interfaze != null && interfaze.getMethods() != null) {
			for (Method method : interfaze.getMethods()) {
				method.addException(exceptionType);
			}
		}

		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
		importedTypes.add(exceptionType);
		interfaze.addImportedTypes(importedTypes);
	}
}
