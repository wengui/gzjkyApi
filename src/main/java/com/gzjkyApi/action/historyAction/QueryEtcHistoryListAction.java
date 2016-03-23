package com.gzjkyApi.action.historyAction;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.gzjky.base.util.VaildateUtils;
import com.gzjky.bean.extend.ElectrocardioInputBean;
import com.gzjky.bean.extend.EtcOutputBean;
import com.gzjky.dao.constant.CodeConstant;
import com.gzjky.dao.readdao.ElectrocardioHistoryReadMapper;
import com.gzjkyApi.action.ApiBaseAction;

import net.sf.json.JSONObject;

/**
 * 血压查询Action
 * 
 * @author yuting
 *
 */
public class QueryEtcHistoryListAction extends ApiBaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5540564677635944167L;

	private List<EtcOutputBean> list;

	@Autowired
	private ElectrocardioHistoryReadMapper electrocardioHistoryReadMapper;

	private JSONObject result;

	public String doExec(){

		// 处理跨域请求问题
		super.sethttp(ServletActionContext.getResponse());
		
		// 页面参数取得
		HttpServletRequest request = ServletActionContext.getRequest();

		// 查询参数设定
		ElectrocardioInputBean input = new ElectrocardioInputBean();
		input.setStartDate(request.getParameter("startDate")); // 开始时间
		input.setEndDate(request.getParameter("endDate"));// 结束时间
		String heartType = request.getParameter("heartType");
		if (CodeConstant.WARN_TYPE.equals(heartType)) {
			// 告警的场合
			input.setHeartType(CodeConstant.WARN_TYPE_STATUS);
		}
		int pointerStart = NumberUtils.toInt(request.getParameter("pointerStart"));
		int pageSize = NumberUtils.toInt(request.getParameter("pageSize"));
		String patientId = request.getParameter("patientId");

		input.setPageMax((pointerStart + pageSize));
		input.setPageMin(pointerStart+1);

		// 患者id取得，最终是要从session里面取得一个可变的值
		input.setPatientId(patientId);

		// 从数据库中取得需要的对象
		list = electrocardioHistoryReadMapper.selectEtcByCondition(input);

		ModelMap modelMap = new ModelMap();

		// 取得结果不为空的场合
		if (!VaildateUtils.isEmptyList(list)) {

			modelMap.setOutBeanList(list);

			modelMap.setRecordTotal(list.get(0).getTotal());
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
