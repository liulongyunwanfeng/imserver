package com.eplat.db.sql;

import com.eplat.utils.StringUtils;

/**
 * 
 * @项目名称：eplat
 * @类名称：OldSqlServerQueryType
 * @类描述：SQL2000及以下版本的分页排序SQL处理类，SQL2000和SQL7的分页处理SQL语句 对SQL语句有个特殊要求
 *                                                     针对主查询的select必须大写 from
 *                                                     必须大写 where 必须大写 order by
 *                                                     必须大写
 * @创建人：高洋
 * @创建时间：2010-2-9 下午02:55:57
 * @修改人：高洋
 * @修改时间：2010-2-9 下午02:55:57
 * @修改备注：
 * @version
 */
public class OldSqlServerQueryType {
	/**
	 * 查询SQL语句的FROM部分
	 */
	public String fromPart = "";

	/**
	 * 查询SQL语句的WHERE部分
	 */
	public String wherePart = "";

	/**
	 * 查询SQL语句的SELECT部分
	 */
	public String selectPart = "";

	/**
	 * 查询SQL语句的其他部分，如ORDER BY
	 */
	public String otherPart = "";
	/**
	 * 查询SQL语句的Group By部分
	 */
	public String groupPart = "";

	// 每页显示的记录行数
	public int pageNumber = 20;
	// 当前页
	public int currentPage = 1;

	/**
	 * 处理类型
	 */

	public boolean isDistinct;
	public String sqlStatement = "";

	public OldSqlServerQueryType(String statement) {
		sqlStatement = statement;
		this.decomposeStatement(statement);
		if (selectPart.length() < 8) {
			isDistinct = false;
		} else {
			if (selectPart.toLowerCase().trim().substring(0, 8).equals(
					"distinct")) {
				selectPart = selectPart.trim().substring(9,
						selectPart.trim().length());
				isDistinct = true;
			}
		}
	}

	public String getSQL(int currentPage, int pageNumber) throws Exception {
		this.currentPage = currentPage;
		this.pageNumber = pageNumber;
		return getProcessPageSQL();

	}

	/**
	 * 取记录总数的SQL语句
	 */
	public String getTotalSQL() throws Exception {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select count(1) from ");
		buffer.append(this.fromPart);
		buffer.append(" where ");
		buffer.append(this.wherePart);
		return buffer.toString();
	}

	/**
	 * 把传入的SQL语句按SELECT、FROM、WHERE、ORDER BY 分解成几部分
	 * 
	 * @param statement
	 *            - 传入的SQL语句
	 * 
	 */
	public void decomposeStatement(String statement) {
		int selectPos = 0, fromPos = 0, wherePos = 0, otherPos = 0;
		int groupPos = 0;
		selectPos = statement.indexOf("SELECT", 0);
		fromPos = statement.indexOf("FROM", 0);
		wherePos = statement.indexOf("WHERE", 0);
		otherPos = statement.indexOf("ORDER BY", 0);
		groupPos = statement.indexOf("GROUP BY", 0);
		selectPart = statement.substring(selectPos + 6, fromPos);
		if (wherePos == -1) { // 没有where部分
			if (groupPos == -1) { // 没有group部分
				if (otherPos == -1) {// 没有orderby 部分
					fromPart = statement.substring(fromPos + 4);
				} else {
					fromPart = statement.substring(fromPos + 4, otherPos);
				}
			} else { // 存在group 部分
				fromPart = statement.substring(fromPos + 4, groupPos);
			}
		} else {// 取where 部分
			fromPart = statement.substring(fromPos + 4, wherePos);
			if (groupPos == -1) { // 没有group部分
				if (otherPos == -1) {// 没有orderby 部分
					wherePart = statement.substring(wherePos + 5);
				} else {
					wherePart = statement.substring(wherePos + 5, otherPos);
				}
			} else { // 存在group 部分
				wherePart = statement.substring(wherePos + 5, groupPos);
			}
		}
		if (groupPos != -1) {
			if (otherPos == -1) {
				groupPart = statement.substring(groupPos + 8);
			} else {
				groupPart = statement.substring(groupPos + 8, otherPos);
			}
		}
		if (otherPos > 0) {
			otherPart = statement.substring(otherPos + 8);
		}

	}

	/**
	 * 取分页查询第一页数据
	 * 
	 * @return
	 */
	public String getSQLEx() {
		String sqlStatement;
		if (isDistinct) {
			sqlStatement = "select distinct top " + pageNumber + " "
					+ selectPart + " from " + fromPart;
		} else {
			sqlStatement = "select top " + pageNumber + " " + selectPart
					+ " from " + fromPart;
		}
		if (!wherePart.trim().equals("")) { // 有查询条件
			sqlStatement = sqlStatement + " where " + wherePart;
		}
		if (!groupPart.trim().equals("")) { // 分组
			sqlStatement = sqlStatement + " GROUP BY " + groupPart;
		}
		if (!otherPart.trim().equals("")) {
			sqlStatement = sqlStatement + " ORDER BY " + otherPart;
		}
		return sqlStatement;

	}

	/**
	 * 
	 * generateTableName
	 * 
	 * @描述：生成临时表
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	private String generateTableName() {
		String tableName = StringUtils.generateID();
		tableName = StringUtils.replace(tableName, "-", "");
		return "#TEMP_" + tableName;
	}

	/**
	 * 取分页SQL
	 * 
	 * @param conditionList
	 *            List
	 * @return String
	 */
	public String getProcessPageSQL() {
		String tableName = this.generateTableName();
		StringBuffer buffer = new StringBuffer();
		if (this.currentPage > 1) { // 大于第一页的数据分页处理
			String tempsql=this.sqlStatement.trim();
			String selectsql = tempsql.substring(6);
			buffer.append("SELECT identity(int,1,1) TEMP_ROW_NUM,TT.* into ");
			buffer.append(tableName);
			buffer.append(" FROM (SELECT top ");
			buffer.append(String.valueOf(this.currentPage * this.pageNumber)+" ");
			buffer.append(selectsql);
			buffer.append(") TT");
			buffer.append("\r\n");
			buffer.append(" SELECT ");
			//buffer.append(splitPrefix(this.selectPart));
			buffer.append(" * ");
			buffer.append(" FROM ");
			buffer.append(tableName);
			buffer.append(" WHERE ");
			buffer.append(" TEMP_ROW_NUM>"
					+ String.valueOf((this.currentPage - 1) * this.pageNumber));
			buffer.append(" AND TEMP_ROW_NUM<="
					+ String.valueOf(this.currentPage * this.pageNumber));
			buffer.append(" ORDER BY TEMP_ROW_NUM");
			buffer.append("\r\n");
			buffer.append("DROP TABLE " + tableName);
			
		} else { // 第二页的数据
			return this.getSQLEx();
		}
		return buffer.toString();
	}

	public String splitPrefix(String sqls) {
		if (sqls.trim().length() > 1) {
			String[] sqlArray = sqls.split(",");
			if ((sqlArray != null) && (sqlArray.length > 0)) {
				String rtn = "";
				for (int i = 0; i < sqlArray.length; i++) {
					int index = sqlArray[i].indexOf(".");
					if (index > 0) {
						if (rtn.equals("")) {
							rtn = sqlArray[i].substring(index + 1);
						} else {
							rtn = rtn + "," + sqlArray[i].substring(index + 1);
						}
					} else {
						if (rtn.equals("")) {
							rtn = sqlArray[i];
						} else {
							rtn = rtn + "," + sqlArray[i];
						}
					}

				}
				return rtn;
			}
			return sqls;
		} else {
			return sqls;
		}
	}

	/**
	 * 
	 * getSortSQL
	 * 
	 * @描述：获取排序的SQL语句
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public String getSortSQL(String sortExp) throws Exception {
		if (StringUtils.hasLength(sortExp)) {
			this.otherPart = sortExp;
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT ");
		if (this.isDistinct) {
			buffer.append(" DISTINCT ");
		}
		buffer.append(this.selectPart);
		buffer.append(" FROM ");
		buffer.append(this.fromPart);
		buffer.append(" WHERE ");
		buffer.append(this.wherePart + "");
		if (!this.groupPart.trim().equalsIgnoreCase("")) {
			buffer.append(" GROUP BY " + this.groupPart);
		}
		if (StringUtils.hasLength(sortExp)) {
			buffer.append(" ORDER BY ");
			buffer.append(this.otherPart);
		}
		return buffer.toString();
	}

	public String getSQL() throws Exception {
		return sqlStatement;
	}

	public String getSortSQL(int currentPage, int pageNumber, String sortExp)
			throws Exception {
		this.otherPart = sortExp;
		this.currentPage = currentPage;
		this.pageNumber = pageNumber;
		return getProcessPageSQL();
	}

	public static void main(String[] args) {
		try {
			String statement = "SELECT  a.sp_tjz,a.sp_pj,a.sp_xm,b.mc  FROM sp_history a,car_bm b "
					+ "WHERE a.cxbh=b.bm and b.lx='5' GROUP BY  a.sp_tjz,a.sp_pj,a.sp_xm,b.mc ORDER BY a.sp_pj desc,a.SP_XM";
			OldSqlServerQueryType type = new OldSqlServerQueryType(statement);
			System.out.println("数据分页：");
			System.out.println(type.getSQL(10, 10));
			System.out.println("排序SQL语句:");
			System.out.println(type.getSortSQL("a.sp_tjz"));

		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
}
