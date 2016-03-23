package com.gzjkyApi.action.historyAction;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.gzjky.base.util.VaildateUtils;
import com.gzjky.bean.extend.QueryBloodPressureInputBean;
import com.gzjky.bean.extend.QueryBloodPressureOutputBean;
import com.gzjky.dao.constant.CodeConstant;
import com.gzjky.dao.readdao.BloodPressureHistoryReadMapper;
import com.gzjkyApi.action.ApiBaseAction;
import com.opensymphony.xwork2.ActionContext;

import net.sf.json.JSONObject;

/**
 * 血压查询Action
 * 
 * @author yuting
 *
 */
public class QueryBloodPressureListAction extends ApiBaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5540564677635944167L;

	private List<QueryBloodPressureOutputBean> bloodPressureList;

	private JSONObject result;

	@Autowired
	private BloodPressureHistoryReadMapper bloodPressureHistoryReadMapper;

	public String doExec(){

		// 处理跨域请求问题
		super.sethttp(ServletActionContext.getResponse());
				
		// 页面参数取得
		HttpServletRequest request = ServletActionContext.getRequest();
		

		// 查询参数设定
		QueryBloodPressureInputBean queryBloodPressureInputBean = new QueryBloodPressureInputBean();
		queryBloodPressureInputBean.setStartDate(request.getParameter("startDate")); // 开始时间
		queryBloodPressureInputBean.setEndDate(request.getParameter("endDate"));// 结束时间
		String bloodType = request.getParameter("bloodType");
		if (CodeConstant.WARN_TYPE.equals(bloodType)) {
			// 告警的场合
			queryBloodPressureInputBean.setBloodType(CodeConstant.WARN_TYPE_STATUS);
		}
		int pointerStart = NumberUtils.toInt(request.getParameter("pointerStart"));
		int pageSize = NumberUtils.toInt(request.getParameter("pageSize"));
		String patientId = request.getParameter("patientId");

		queryBloodPressureInputBean.setPageMax((pointerStart + pageSize));
		queryBloodPressureInputBean.setPageMin(pointerStart + 1);

		// 患者id取得，最终是要从session里面取得一个可变的值
		queryBloodPressureInputBean.setPatientId(patientId);

		// 从数据库中取得需要的对象
		bloodPressureList = bloodPressureHistoryReadMapper.selectBloodPressureByCondition(queryBloodPressureInputBean);

		ModelMap modelMap = new ModelMap();

		// 取得结果不为空的场合
		if (!VaildateUtils.isEmptyList(bloodPressureList)) {

			modelMap.setOutBeanList(bloodPressureList);

			modelMap.setRecordTotal(bloodPressureList.get(0).getTotal());
		}

		// 将java对象转成json对象
		result = JSONObject.fromObject(modelMap);// 转换为json

		return SUCCESS;
	}

	public JSONObject getResult() {
		return result;
	}

	public void setResult(JSONObject result) {
		this.result = result;
	}

}
