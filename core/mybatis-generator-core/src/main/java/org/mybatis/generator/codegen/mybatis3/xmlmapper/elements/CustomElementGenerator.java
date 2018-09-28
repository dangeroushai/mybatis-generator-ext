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
package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.CustomMethodGenerator.CUSTOM_METHOD_NAME;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.CustomMethodGenerator.CUSTOM_METHOD_PARAM_NAME;
import org.mybatis.generator.config.GeneratedKey;

/**
 * 
 * @author LeiHaijun
 * 
 */
public class CustomElementGenerator extends AbstractXmlElementGenerator {
	
	/**
	 * 基本查询SQL
	 */
	private XmlElement selectIncludeEle;
	/**
	 * 公共分页SQL
	 */
	private XmlElement paingIncludeEle;

	public CustomElementGenerator(boolean isLogicalDelete) {
		super();
		init();
	}

	public static final String BASE_PAGING_SQL = "BasePagingSql";
	public static final String BASE_SELECT_SQL = "BaseSelectSql";
	public static final String BASE_WHERE_SQL = "BaseWhereSql";
	private void init() {
		selectIncludeEle = new XmlElement("include");
		selectIncludeEle.addAttribute(new Attribute("refid",
				BASE_SELECT_SQL));
		paingIncludeEle = new XmlElement("include");
		paingIncludeEle.addAttribute(new Attribute("refid",
				BASE_PAGING_SQL));
	}

	@Override
	public void addElements(XmlElement parentElement) {
		this.addInsertBatchMethodElement(parentElement);
		this.addSelectBatchMethodElement(parentElement);
		this.addSelectAmountMethodElement(parentElement);

		this.addUpdateSelectiveBatchMethodElement(parentElement);

		this.addBaseSelectSqlElement(parentElement);
		this.addBaseWhereSqlElement(parentElement);
		this.addBasePagingSqlElement(parentElement);
	}

	/**
	 * 添加批量选择更新节点
	 * 
	 * @param parentElement
	 */
	private void addUpdateSelectiveBatchMethodElement(XmlElement parentElement) {
		XmlElement answer = new XmlElement("update"); //$NON-NLS-1$

		answer.addAttribute(new Attribute("id", CUSTOM_METHOD_NAME.UPDATE_SELECTIVE_BATCH)); //$NON-NLS-1$

		context.getCommentGenerator().addComment(answer);

		StringBuilder sb = new StringBuilder();

		sb.append("update "); //$NON-NLS-1$
		sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
		answer.addElement(new TextElement(sb.toString()));

		XmlElement dynamicElement = new XmlElement("set"); //$NON-NLS-1$
		answer.addElement(dynamicElement);

		for (IntrospectedColumn introspectedColumn : ListUtilities
				.removeGeneratedAlwaysColumns(introspectedTable
						.getNonPrimaryKeyColumns())) {
			sb.setLength(0);
			sb.append(CUSTOM_METHOD_PARAM_NAME.EXAMPLE + "." + introspectedColumn.getJavaProperty());
			sb.append(" != null"); //$NON-NLS-1$
			XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
			isNotNullElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$
			dynamicElement.addElement(isNotNullElement);

			sb.setLength(0);
			sb.append(MyBatis3FormattingUtilities
					.getEscapedColumnName(introspectedColumn));
			sb.append(" = "); //$NON-NLS-1$
			sb.append(MyBatis3FormattingUtilities
					.getParameterClause(introspectedColumn, CUSTOM_METHOD_PARAM_NAME.EXAMPLE + "."));
			sb.append(',');

			isNotNullElement.addElement(new TextElement(sb.toString()));
		}

		boolean and = false;
		for (IntrospectedColumn introspectedColumn : introspectedTable
				.getPrimaryKeyColumns()) {
			sb.setLength(0);
			if (and) {
				sb.append(" and "); //$NON-NLS-1$
			} else {
				sb.append(" where "); //$NON-NLS-1$
				and = true;
			}

			String key = MyBatis3FormattingUtilities
					.getEscapedColumnName(introspectedColumn);

			sb.append(key);
			sb.append(" in "); //$NON-NLS-1$

			XmlElement foreachEle = new XmlElement("foreach");
			foreachEle.addAttribute(new Attribute("collection", CUSTOM_METHOD_PARAM_NAME.IDS));
			foreachEle.addAttribute(new Attribute("item", key));
			foreachEle.addAttribute(new Attribute("open", "("));
			foreachEle.addAttribute(new Attribute("separator", ","));
			foreachEle.addAttribute(new Attribute("close", ")"));
			foreachEle.addElement(new TextElement(MyBatis3FormattingUtilities
					.getParameterClause(introspectedColumn)));

			answer.addElement(new TextElement(sb.toString()));
			answer.addElement(foreachEle);
		}
		parentElement.addElement(answer);

	}

	/**
	 * 添加基本查询Sql节点
	 * 
	 * @param parentElement
	 */
	private void addBaseSelectSqlElement(XmlElement parentElement) {
		XmlElement sqlEle = new XmlElement("sql");
		sqlEle.addAttribute(new Attribute("id",
				BASE_SELECT_SQL));

		XmlElement baseColIncludeEle = new XmlElement("include");
		baseColIncludeEle.addAttribute(new Attribute("refid",
				"Base_Column_List"));

		XmlElement baseWhereIncludeEle = new XmlElement("include");
		baseWhereIncludeEle.addAttribute(new Attribute("refid",
				BASE_WHERE_SQL));

		sqlEle.addElement(new TextElement("select "));
		sqlEle.addElement(baseColIncludeEle);
		sqlEle.addElement(new TextElement("from "
				+ introspectedTable.getFullyQualifiedTableNameAtRuntime()));
		sqlEle.addElement(baseWhereIncludeEle);

		parentElement.addElement(sqlEle);
	}
	
	/**
	 * 添加基本查询Sql节点
	 * 
	 * @param parentElement
	 */
	private void addBaseWhereSqlElement(XmlElement parentElement) {
		XmlElement sqlEle = new XmlElement("sql");
		sqlEle.addAttribute(new Attribute("id",
				BASE_WHERE_SQL));
		
		XmlElement whereEle = new XmlElement("where");
		StringBuilder sb = new StringBuilder();
		for (IntrospectedColumn introspectedColumn : ListUtilities
				.removeGeneratedAlwaysColumns(introspectedTable
						.getNonPrimaryKeyColumns())) {
			sb.setLength(0);
			sb.append(introspectedColumn.getJavaProperty());
			sb.append(" != null"); //$NON-NLS-1$
			XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
			isNotNullElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$
			whereEle.addElement(isNotNullElement);
			
			sb.setLength(0);
			sb.append(" and ");
			sb.append(MyBatis3FormattingUtilities
					.getEscapedColumnName(introspectedColumn));
			sb.append(" = ");
			sb.append(MyBatis3FormattingUtilities
					.getParameterClause(introspectedColumn));
			
			isNotNullElement.addElement(new TextElement(sb.toString()));
		}
		sqlEle.addElement(whereEle);
		
		parentElement.addElement(sqlEle);
	}

	/**
	 * 添加批量查询记录节点
	 * 
	 * @param parentElement
	 */
	private void addSelectBatchMethodElement(XmlElement parentElement) {
		XmlElement selectEle = new XmlElement("select");
		selectEle.addAttribute(new Attribute("id",
				CUSTOM_METHOD_NAME.SELECT_BATCH));
		selectEle.addAttribute(new Attribute("parameterType", "map"));
		selectEle.addAttribute(new Attribute("resultMap", "BaseResultMap"));

		selectEle.addElement(new TextElement("select * from ("));
		selectEle.addElement(selectIncludeEle);
		selectEle.addElement(paingIncludeEle);
		selectEle.addElement(new TextElement(") as t"));

		parentElement.addElement(selectEle);
	}

	/**
	 * 添加查询记录总数节点
	 * 
	 * @param parentElement
	 */
	private void addSelectAmountMethodElement(XmlElement parentElement) {
		XmlElement selectEle = new XmlElement("select");
		selectEle.addAttribute(new Attribute("id",
				CUSTOM_METHOD_NAME.SELECT_AMOUNT));
		selectEle.addAttribute(new Attribute("parameterType", "map"));
		selectEle
				.addAttribute(new Attribute("resultType", "java.lang.Integer"));

		selectEle.addElement(new TextElement("select count(1) from ("));
		selectEle.addElement(selectIncludeEle);
		selectEle.addElement(new TextElement(") as t"));

		parentElement.addElement(selectEle);
	}

	/**
	 * 添加公共分页Sql节点
	 */
	private void addBasePagingSqlElement(XmlElement parentElement) {
		XmlElement sqlEle = new XmlElement("sql");
		sqlEle.addAttribute(new Attribute("id",
				BASE_PAGING_SQL));

		XmlElement chooseEle = new XmlElement("choose");

		XmlElement orderBySqldWhenEle = new XmlElement("when");
		orderBySqldWhenEle
				.addAttribute(new Attribute(
						"test",
						"queryCondition != null and queryCondition.orderSql != null and queryCondition.orderSql !=''"));
		orderBySqldWhenEle.addElement(new TextElement(
				"order by ${queryCondition.orderSql}"));

		XmlElement orderByFileldwhenEle = new XmlElement("when");
		orderByFileldwhenEle
				.addAttribute(new Attribute(
						"test",
						"queryCondition != null and queryCondition.orderField != null and queryCondition.orderField !=''"));
		orderByFileldwhenEle.addElement(new TextElement(
				"order by ${queryCondition.orderField}"));

		XmlElement ifEle = new XmlElement("if");
		ifEle.addAttribute(new Attribute("test",
				"queryCondition.orderDirection != null and queryCondition.orderDirection != ''"));
		ifEle.addElement(new TextElement("${queryCondition.orderDirection}"));
		orderByFileldwhenEle.addElement(ifEle);

		XmlElement otherwiseEle = new XmlElement("otherwise");
		otherwiseEle.addElement(new TextElement("order by update_time DESC"));

		chooseEle.addElement(orderBySqldWhenEle);
		chooseEle.addElement(orderByFileldwhenEle);
		chooseEle.addElement(otherwiseEle);

		XmlElement pagingIfEle = new XmlElement("if");
		pagingIfEle.addAttribute(new Attribute("test",
				"queryCondition != null and queryCondition.startIndex != null and queryCondition.pageSize != null"));
		pagingIfEle.addElement(new TextElement("limit ${queryCondition.startIndex}, ${queryCondition.pageSize}"));
		
		sqlEle.addElement(chooseEle);
		sqlEle.addElement(pagingIfEle);

		parentElement.addElement(sqlEle);
	}

	/**
	 * 添加批量插入节点
	 * 
	 * @param parentElement
	 */
	private void addInsertBatchMethodElement(XmlElement parentElement) {
		XmlElement answer = new XmlElement("insert");

		answer.addAttribute(new Attribute("id", CUSTOM_METHOD_NAME.INSERT_BATCH));

		// context.getCommentGenerator().addComment(answer);

		GeneratedKey gk = introspectedTable.getGeneratedKey();
		if (gk != null) {
			IntrospectedColumn introspectedColumn = introspectedTable
					.getColumn(gk.getColumn());
			// if the column is null, then it's a configuration error. The
			// warning has already been reported
			if (introspectedColumn != null) {
				if (gk.isJdbcStandard()) {
					answer.addAttribute(new Attribute(
							"useGeneratedKeys", "true")); //$NON-NLS-1$ //$NON-NLS-2$
					answer.addAttribute(new Attribute(
							"keyProperty", introspectedColumn.getJavaProperty())); //$NON-NLS-1$
					answer.addAttribute(new Attribute(
							"keyColumn", introspectedColumn.getActualColumnName())); //$NON-NLS-1$
				} else {
					answer.addElement(getSelectKey(introspectedColumn, gk));
				}
			}
		}

		StringBuilder insertClause = new StringBuilder();

		insertClause.append("insert into "); //$NON-NLS-1$
		insertClause.append(introspectedTable
				.getFullyQualifiedTableNameAtRuntime());
		insertClause.append(" ("); //$NON-NLS-1$

		StringBuilder valuesClause = new StringBuilder();
		valuesClause.append("("); //$NON-NLS-1$

		XmlElement foreachEle = new XmlElement("foreach");
		String itemName = "item";
		foreachEle.addAttribute(new Attribute("collection", "list"));
		foreachEle.addAttribute(new Attribute("item", itemName));
		foreachEle.addAttribute(new Attribute("separator", ","));

		List<String> valuesClauses = new ArrayList<String>();
		List<IntrospectedColumn> columns = ListUtilities
				.removeIdentityAndGeneratedAlwaysColumns(introspectedTable
						.getAllColumns());
		for (int i = 0; i < columns.size(); i++) {
			IntrospectedColumn introspectedColumn = columns.get(i);

			insertClause.append(MyBatis3FormattingUtilities
					.getEscapedColumnName(introspectedColumn));
			valuesClause.append(MyBatis3FormattingUtilities.getParameterClause(
					introspectedColumn, itemName + "."));
			if (i + 1 < columns.size()) {
				insertClause.append(", "); //$NON-NLS-1$
				valuesClause.append(", "); //$NON-NLS-1$
			}

			if (valuesClause.length() > 80) {
				answer.addElement(new TextElement(insertClause.toString()));
				insertClause.setLength(0);
				OutputUtilities.xmlIndent(insertClause, 1);

				valuesClauses.add(valuesClause.toString());
				valuesClause.setLength(0);
				OutputUtilities.xmlIndent(valuesClause, 1);
			}
		}

		insertClause.append(") values ");
		answer.addElement(new TextElement(insertClause.toString()));

		valuesClause.append(')');
		valuesClauses.add(valuesClause.toString());

		for (String clause : valuesClauses) {
			foreachEle.addElement(new TextElement(clause));
		}

		answer.addElement(foreachEle);

		if (context.getPlugins().sqlMapInsertElementGenerated(answer,
				introspectedTable)) {
			parentElement.addElement(answer);
		}
	}
}
