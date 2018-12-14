package com.eplat.annotation;

import java.lang.reflect.Field;
@TableInfo(tablename="SU_STAFF",tabledesc="用户表",logtype="default")
public class TestAnno {
	@FieldInfo(fieldid="STAFF_ID",checktype="NOTNULL",name="用户ID",logflag=true,propid="staffid",fieldtype="VARCHAR")
	private String staffid;
	@FieldInfo(fieldid="STAFF_NAME",checktype="NOTNULL",name="用户名称",logflag=true,propid="staffname",fieldtype="VARCHAR")
	
	private String staffName;

	public String getStaffid() {
		return staffid;
	}

	public void setStaffid(String staffid) {
		this.staffid = staffid;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public static void main(String[] args) {
		try {
	
			TestAnno data=new TestAnno();
			System.out.println(data.getClass().getSimpleName());
			data.setStaffid("用户ID");
			data.setStaffName("用户名称");
			TableInfo tableInfo=data.getClass().getAnnotation(TableInfo.class);
			System.out.println(tableInfo.tablename()+":"+tableInfo.tabledesc()+":"+tableInfo.logtype());
			Field[] fields =  TestAnno.class.getDeclaredFields();
			for (int i=0;i<fields.length;i++){
				System.out.println("字段名称："+fields[i].getName());
				Object obj=fields[i].get(data);
				System.out.println("字段值："+String.valueOf(obj));
				FieldInfo fieldInfo=fields[i].getAnnotation(FieldInfo.class);
				if (fieldInfo!=null){
					System.out.println(fieldInfo.fieldid()+":"+fieldInfo.fieldtype()+":"+fieldInfo.name());
				}
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
