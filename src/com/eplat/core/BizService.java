package com.eplat.core;

import com.eplat.annotation.EntityBean;
import com.eplat.annotation.FieldInfo;
import com.eplat.db.DBConnection;
import com.eplat.utils.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 业务类的父类
 * 
 * @author Administrator
 * 
 */
public class BizService implements IBizService {
	// 数据库连接对象
	protected DBConnection dbConnection;
	protected String datasourceId;

	public String getDatasourceId() {
		return this.datasourceId;
	}

	public void setDatasourceId(String datasourceId) {
		this.datasourceId = datasourceId;
	}

	public void setDBConnection(DBConnection dbConnection) throws Exception {
		this.dbConnection = dbConnection;

	}

	public DBConnection getDBConnection() throws Exception {
		return this.dbConnection;
	}

	public void beginTrans() throws Exception {
		this.dbConnection.beginTrans();

	}

	public void commit() throws Exception {
		this.dbConnection.commit();

	}

	public void rollback() throws Exception {
		this.dbConnection.rollback();

	}

	@Override
	public EntityCauseBean getWhereCause(EntityBean entity, ValueObject vo)
			throws Exception {
		EntityCauseBean cause=new EntityCauseBean();
		String whereCause="";
		List<Object> params=new ArrayList<Object>();
		Field[] fields=entity.getClass().getDeclaredFields();
		for(int i=0;i<fields.length;i++){
			Field field=fields[i];	
			FieldInfo fieldInfo=field.getAnnotation(FieldInfo.class);
			if (fieldInfo!=null){//说明是数据库字段
				if (StringUtils.hasLength(vo.getValue(field.getName()))){//该输入条件有内容
					whereCause=whereCause+" AND "+fieldInfo.fieldid()+"=? ";
					field.setAccessible(true);
					params.add(field.get(entity));
				}
			};
		}		
		cause.setParams(params.toArray());
		cause.setWhereCause(whereCause);
		return cause;
	}


}
