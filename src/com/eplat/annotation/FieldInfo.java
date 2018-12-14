package com.eplat.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 字段的信息标注
 * 
 * @author Administrator
 *
 */
@Retention(RetentionPolicy.RUNTIME)
// 注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Documented
// 说明该注解将被包含在javadoc中
public @interface FieldInfo {
	// 字段名称
	String fieldid() default "";

	// 验证类型
	String checktype() default "";

	// 字段中文名字
	String name() default "";

	// 是否写入日志
	boolean logflag() default true;

	// 属性ID
	String propid() default "";

	// 字段类型
	String fieldtype() default "";

	boolean iskey() default false;

	// 默认值
	String defaultvalue() default "";

	// 后台验证类型
	String vaildtype() default "";

	// 验证消息
	String vaildmsg() default "";

	// 字段長度
	int fieldlen() default 0;

	// 转换成json时使用
	String jsonkey() default "";

}
