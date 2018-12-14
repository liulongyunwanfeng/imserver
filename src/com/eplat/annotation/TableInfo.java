package com.eplat.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * 表名称信息标注
 * @author Administrator
 *
 */
@Retention(RetentionPolicy.RUNTIME) 
@Documented
public @interface TableInfo {
	//数据表名称
	String tablename() default "";
	//表描述
	String tabledesc() default "";
	//记录日志类型
	String logtype() default "";

}
