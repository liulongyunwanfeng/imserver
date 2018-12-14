package com.eplat.im.service;

import com.eplat.core.BizService;
import com.eplat.core.EntityCauseBean;
import com.eplat.core.ValueObject;
import com.eplat.im.dao.MessageacceptDao;
import com.eplat.im.domain.MessagesendBean;
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
 * @类名称：MessageacceptService
 * @类描述：消息接收
 * @创建人：系统生成
 * @version
 */
public class MessageacceptService extends BizService {
	private Class beanClass = MessagesendService.class;
	private MessageacceptDao messageacceptDao;

	public MessageacceptDao getMessageacceptDao() {
		return messageacceptDao;
	}

	public void setMessageacceptDao(MessageacceptDao messageacceptDao) {
		this.messageacceptDao = messageacceptDao;
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
			messageacceptDao.setDBConnection(this.getDBConnection());
			Object obj = BeanUtil.mapToBean(valueObject.getValueMap(),
					beanClass);
			MessagesendBean bean = (MessagesendBean) obj;
			messageacceptDao.insert(bean);
			result.setStatusCode(0);
			result.setStatusMessage("新增消息接收成功");
		} catch (Exception e) {
			LoggerUtils.error("消息接收", "新增消息接收失败：" + e.getMessage());
			result.setStatusCode(-1);
			result.setResult(e);
			result.setStatusMessage("新增消息接收失败:" + e.getMessage());
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
			messageacceptDao.setDBConnection(this.getDBConnection());
			Object obj = BeanUtil.mapToBean(valueObject.getValueMap(),
					beanClass);
			messageacceptDao.update(obj);
			result.setStatusCode(0);
			result.setStatusMessage("修改消息接收成功");
		} catch (Exception e) {
			LoggerUtils.error("消息接收", "修改消息接收失败：" + e.getMessage());
			result.setStatusCode(-1);
			result.setResult(e);
			result.setStatusMessage("修改消息接收数据失败：" + e.getMessage());
		}
		return result;
	}

	/**
	 * 删除单条数据
	 * 
	 * @param valueObject
	 * @return
	 * @throws Exception
	 */
	public AjaxResult deleteOne(ValueObject valueObject) throws Exception {
		AjaxResult result = new AjaxResult();
		try {
			Object obj = BeanUtil.mapToBean(valueObject.getValueMap(),
					beanClass);
			MessagesendBean bean = (MessagesendBean) obj;
			messageacceptDao.setDBConnection(this.getDBConnection());
			messageacceptDao.delete(bean);
			result.setStatusCode(0);
			result.setStatusMessage("删除消息接收成功");
		} catch (Exception e) {
			LoggerUtils.error("消息接收", "删除消息接收失败：" + e.getMessage());
			result.setStatusCode(-1);
			result.setResult(e);
			result.setStatusMessage("删除消息接收失败：" + e.getMessage());
		}
		return result;
	}

	/**
	 * 删除单条数据
	 * 
	 * @param valueObject
	 * @return
	 * @throws Exception
	 */
	public AjaxResult delete(ValueObject valueObject) throws Exception {
		AjaxResult result = new AjaxResult();
		try {
			List list = (List) valueObject.getFormValue("paramspk");
			if (list == null || list.size() == 0) {
				result.setStatusCode(-1);
				result.setStatusMessage("请先选中要删除的数据然后才能继续！");
				return result;
			}
			List<MessagesendBean> delList = new ArrayList<MessagesendBean>();
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) instanceof String) {
					delList.add((MessagesendBean) BeanUtil.mapToBean(
							ParsePkParam
									.parseParam(String.valueOf(list.get(i))),
							MessagesendBean.class));
				} else {
					MorphDynaBean bean = (MorphDynaBean) list.get(i);
					delList.add((MessagesendBean) BeanUtil.mapToBean(bean,
							MessagesendBean.class));
				}
			}
			messageacceptDao.setDBConnection(this.getDBConnection());
			messageacceptDao.deleteBatch(delList);
			result.setStatusCode(0);
			result.setStatusMessage("删除消息接收成功");
		} catch (Exception e) {
			LoggerUtils.error("消息接收", "删除消息接收失败：" + e.getMessage());
			result.setStatusCode(-1);
			result.setResult(e);
			result.setStatusMessage("删除消息接收失败：" + e.getMessage());
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
			messageacceptDao.setDBConnection(this.getDBConnection());
			Object obj = BeanUtil.mapToBean(valueObject.getValueMap(),
					beanClass);
			obj = messageacceptDao.queryPK(obj);
			result.setStatusCode(0);
			result.setResult(obj);
			result.setStatusMessage("查询消息接收成功");
		} catch (Exception e) {
			LoggerUtils.error("消息接收", "查询消息接收失败:" + e.getMessage());
			result.setStatusCode(-1);
			result.setResult(e);
			result.setStatusMessage("查询消息接收失败：" + e.getMessage());
		}
		return result;
	}

	/**
	 * 查询方法
	 * 
	 * @param valueObject
	 * @return
	 * @throws Exception
	 */
	public AjaxResult query(ValueObject valueObject) throws Exception {
		AjaxResult result = new AjaxResult();
		try {
			messageacceptDao.setDBConnection(this.getDBConnection());
			Object obj = BeanUtil.mapToBean(valueObject.getValueMap(),
					beanClass);
			MessagesendBean bean = (MessagesendBean) obj;
			EntityCauseBean cause = this.getWhereCause(bean, valueObject);
			if (cause.getParams() != null && cause.getParams().length > 0) {
				result.setResult(messageacceptDao.queryByCause(
						cause.getWhereCause(), cause.getParams()));
			} else {
				result.setResult(messageacceptDao.query());
			}
			result.setStatusCode(0);
			result.setStatusMessage("查询消息接收成功");
		} catch (Exception e) {
			LoggerUtils.error("消息接收", "查询消息接收失败:" + e.getMessage());
			result.setStatusCode(-1);
			result.setResult(e);
			result.setStatusMessage("查询消息接收失败：" + e.getMessage());
		}
		return result;
	}
}
