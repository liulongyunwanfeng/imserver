package com.eplat.im.service;

import com.eplat.core.BizService;
import com.eplat.core.EntityCauseBean;
import com.eplat.core.ValueObject;
import com.eplat.im.dao.GroupsendDao;
import com.eplat.im.domain.GroupsendBean;
import com.eplat.utils.BeanUtil;
import com.eplat.utils.LoggerUtils;
import com.eplat.utils.ParsePkParam;
import com.eplat.web.ajax.AjaxResult;
import net.sf.ezmorph.bean.MorphDynaBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @项目名称：eplat
 * @类名称：GroupsendService
 * @类描述：群组发送
 * @创建人：系统生成
 * @version
 */
public class GroupsendService extends BizService {
	private Class beanClass = GroupsendBean.class;
	private GroupsendDao groupsendDao;

	public GroupsendDao getGroupsendDao() {
		return groupsendDao;
	}

	public void setGroupsendDao(GroupsendDao groupsendDao) {
		this.groupsendDao = groupsendDao;
	}
		/**
	 * insert
	 * 
	 * @描述：新增数据
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public AjaxResult insert(ValueObject valueObject) throws Exception {
		AjaxResult result = new AjaxResult();
		try {
			groupsendDao.setDBConnection(this.getDBConnection());
			Object obj = BeanUtil.mapToBean(valueObject.getValueMap(),beanClass);
			GroupsendBean bean = (GroupsendBean) obj;			
						groupsendDao.insert(bean);
			result.setStatusCode(0);
			result.setStatusMessage("新增群组发送成功");
		} catch (Exception e) {
			LoggerUtils.error("群组发送","新增群组发送失败："+e.getMessage());
			result.setStatusCode(-1);
			result.setResult(e);
			result.setStatusMessage("新增群组发送失败:"+e.getMessage());
		}
		return result;
	}
	/**
	 * update
	 * 
	 * @描述：修改数据
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public AjaxResult update(ValueObject valueObject) throws Exception {
		AjaxResult result = new AjaxResult();
		try {
			groupsendDao.setDBConnection(this.getDBConnection());
			Object obj = BeanUtil.mapToBean(valueObject.getValueMap(),beanClass);
						groupsendDao.update(obj);			
			result.setStatusCode(0);
			result.setStatusMessage("修改群组发送成功");
		} catch (Exception e) {
			LoggerUtils.error("群组发送","修改群组发送失败："+e.getMessage());
			result.setStatusCode(-1);
			result.setResult(e);
			result.setStatusMessage("修改群组发送数据失败："+e.getMessage());
		}
		return result;
	}
	/**
	 * 删除单条数据
	 * @param valueObject
	 * @return
	 * @throws Exception
	 */
	public AjaxResult deleteOne(ValueObject valueObject) throws Exception {
		AjaxResult result = new AjaxResult();
		try {
			Object obj = BeanUtil.mapToBean(valueObject.getValueMap(),
					beanClass);
			GroupsendBean bean = (GroupsendBean) obj;	
			groupsendDao.setDBConnection(this.getDBConnection());
						groupsendDao.delete(bean);
			result.setStatusCode(0);
			result.setStatusMessage("删除群组发送成功");
		} catch (Exception e) {
			LoggerUtils.error("群组发送","删除群组发送失败："+e.getMessage());
			result.setStatusCode(-1);
			result.setResult(e);
			result.setStatusMessage("删除群组发送失败："+e.getMessage());
		}
		return result;
	}
	/**
	 * 删除单条数据
	 * @param valueObject
	 * @return
	 * @throws Exception
	 */
	public AjaxResult delete(ValueObject valueObject) throws Exception {
		AjaxResult result = new AjaxResult();
		try {
			List list=(List)valueObject.getFormValue("paramspk");
			if (list==null||list.size()==0){
				result.setStatusCode(-1);
				result.setStatusMessage("请先选中要删除的数据然后才能继续！");
				return result;
			}
						List<GroupsendBean> delList=new ArrayList<GroupsendBean>();
			for (int i=0;i<list.size();i++){
				if (list.get(i) instanceof String){
					delList.add((GroupsendBean)BeanUtil.mapToBean(ParsePkParam.parseParam(String.valueOf(list.get(i))), GroupsendBean.class));
				} else {
					MorphDynaBean bean=(MorphDynaBean)list.get(i);
					delList.add((GroupsendBean)BeanUtil.mapToBean(bean, GroupsendBean.class));
				}
			}
			groupsendDao.setDBConnection(this.getDBConnection());
						groupsendDao.deleteBatch(delList);
			result.setStatusCode(0);
			result.setStatusMessage("删除群组发送成功");
		} catch (Exception e) {
			LoggerUtils.error("群组发送","删除群组发送失败："+e.getMessage());
			result.setStatusCode(-1);
			result.setResult(e);
			result.setStatusMessage("删除群组发送失败："+e.getMessage());
		}
		return result;
	}
	/**
	 * querypk
	 * 
	 * @描述：根据主键查找数据
	 * @param name
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 */
	public AjaxResult queryPk(ValueObject valueObject) throws Exception {
		AjaxResult result = new AjaxResult();
		try {
			groupsendDao.setDBConnection(this.getDBConnection());
			Object obj = BeanUtil.mapToBean(valueObject.getValueMap(),beanClass);
						obj = groupsendDao.queryPK(obj);
			result.setStatusCode(0);
			result.setResult(obj);
			result.setStatusMessage("查询群组发送成功");
		} catch (Exception e) {
			LoggerUtils.error("群组发送","查询群组发送失败:"+e.getMessage());
			result.setStatusCode(-1);
			result.setResult(e);
			result.setStatusMessage("查询群组发送失败："+e.getMessage());
		}
		return result;
	}
	
	/**
	 * 查询方法
	 * @param valueObject
	 * @return
	 * @throws Exception
	 */
	public AjaxResult query(ValueObject valueObject) throws Exception {
		AjaxResult result = new AjaxResult();
		try {
			groupsendDao.setDBConnection(this.getDBConnection());
			Object obj = BeanUtil.mapToBean(valueObject.getValueMap(),beanClass);
			GroupsendBean bean =(GroupsendBean)obj;
			EntityCauseBean cause=this.getWhereCause(bean, valueObject);			
			if (cause.getParams()!=null&&cause.getParams().length>0){
				result.setResult(groupsendDao.queryByCause(cause.getWhereCause(), cause.getParams()));
			} else {
				result.setResult(groupsendDao.query());
			}
			result.setStatusCode(0);			
			result.setStatusMessage("查询群组发送成功");
		} catch (Exception e) {
			LoggerUtils.error("群组发送","查询群组发送失败:"+e.getMessage());
			result.setStatusCode(-1);
			result.setResult(e);
			result.setStatusMessage("查询群组发送失败："+e.getMessage());
		}
		return result;
	}
}

